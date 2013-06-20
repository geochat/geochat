package ru.geochat;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.log4j.Logger;

import ru.geochat.model.dao.FollowersManager;
import ru.geochat.model.dao.Geo;
import ru.geochat.model.dao.MessageTextManager;
import ru.geochat.model.dao.MessagesManager;
import ru.geochat.model.dao.UserManager;
import ru.geochat.model.dto.DataForUser;
import ru.geochat.model.dto.Message;
import ru.geochat.model.dto.User;
import com.grum.geocalc.*;

public class ServerController implements Runnable{
	private HashMap<String, User> usersOnline = new HashMap<String, User>();
	private ReentrantLock usersOnlineListLock = new ReentrantLock();
	private FollowersManager followersManager;
	private MessageTextManager messageTextManager;
	private UserManager userManager;
	private MessagesManager messagesManager;
	//server config
	private final Integer refreshPeriod = 5000; // milliseconds
	private final Integer userTimeout = 1000*60; // timeout for user in milliseconds
	private static final Logger logger = Logger.getLogger(ServerController.class);
	
	public Boolean submitLocation(Geo coordinate, String md5){
		User senderUser;
		usersOnlineListLock.lock();
		try
			{senderUser = usersOnline.get(md5);}		
		finally {usersOnlineListLock.unlock();}
		if (senderUser == null)
			return false;
		senderUser.setUserGeo(coordinate);
		try {
			userManager.exportGeo(senderUser.getUserId(), coordinate);
		}
		catch (Exception e) 
			{return false;}
		logger.info("[!] User "+senderUser.getUserLogin()+" submitted his geo: ("+ coordinate.getLatitude()+ ", "+coordinate.getLongitude()+")");
		return true;
	}
	
	public DataForUser generateDataForUser(String md5){
		User receivingUser; 
		usersOnlineListLock.lock();
		try{
			receivingUser = usersOnline.get(md5);
		}		
		finally {usersOnlineListLock.unlock();}
		
		DataForUser dataForUser = new DataForUser();
		dataForUser.user = receivingUser;
		dataForUser.usersInRadius = getUsersInRadius(receivingUser);
		try {	
			dataForUser.messagesList = this.messagesManager.importMessageForUser(receivingUser.getUserId());
		} catch (Exception e) {
			return null;
		}
		List <Message> messagesList = dataForUser.messagesList;
		for (Message message : messagesList) {
			message.sender.setUserLogin(this.userManager.importLogin(message.sender.getUserId()));
//			message.sender.setUserGeo(message.sender.);		//DO SMTH WITH IT!!!!
		}
		return dataForUser;
	}
	
	private double distanceBetweenGeoPoints(Geo point1, Geo point2)
	{
		// geo pos for point1
		Coordinate lat = new DegreeCoordinate(point1.getLatitude());
		Coordinate lng = new DegreeCoordinate(point1.getLongitude());
		Point p1 = new Point(lat, lng);
		// geo pos for point2
		lat = new DegreeCoordinate(point2.getLatitude());
		lng = new DegreeCoordinate(point2.getLongitude());
		Point p2 = new Point(lat, lng);
		return EarthCalc.getDistance(p1, p2); //in meters
	}
	
	public ArrayList<User> getUsersInRadius(User selectedUser)
	{
		ArrayList<User> usersList = new ArrayList<User>();
		Geo selectedUserGeo = selectedUser.getUserGeo();
		usersOnlineListLock.lock();
		try
		{
			Iterator<Entry<String, User>> it = usersOnline.entrySet().iterator();
			Map.Entry<String,User> entry; //defines entry in map
			while (it.hasNext())
			{
				entry = (Map.Entry<String, User>) it.next();
				if (entry.getValue() == selectedUser)
					continue; // if parsing user is selected user (don't need to parse him)
				Geo parsingUserGeo = entry.getValue().getUserGeo(); // Geo pos for parsing user
				if (parsingUserGeo == null)
					continue; // if user had not commited his geopos yet but is online
				if (distanceBetweenGeoPoints(parsingUserGeo, selectedUserGeo) <= selectedUser.getUserRadius())
					usersList.add(entry.getValue()); // if user is in radius
			}
		}
		finally {usersOnlineListLock.unlock();}
		return usersList;	
	}
	
	
	public boolean messageProccessor(String md5, String messageText, Boolean isPrivate)
	{
		List<Integer> followersUserIds = null;
		User senderUser;
		ArrayList<User> usersInRadius = new ArrayList<User>();
		usersOnlineListLock.lock();
		try
			{senderUser = usersOnline.get(md5);}		
		finally {usersOnlineListLock.unlock();}
		if (senderUser == null)
			return false;
		if (isPrivate)
		{
			try
				{followersUserIds=followersManager.importFollowers(senderUser.getUserId());}
			catch (Exception e)
				{return false;}
		}
		ArrayList<User> parsingUsers = new ArrayList<User>(); // list for msg recievers
		parsingUsers.add(senderUser); // adding sender to recv list (sender must recv his msg)
		Integer parsingIdNum=0; // current parsing Id number in recievers list
		Integer msgTextId;
		try // add msg_text to DB
			{msgTextId=messageTextManager.exportMsgText(senderUser.getUserId(), messageText);}
		catch (Exception e)
			{return false;}	
		User currentParsingUser;
		do
		{	// usersBypass cycle
			currentParsingUser = parsingUsers.get(parsingIdNum);
			usersInRadius = this.getUsersInRadius(currentParsingUser);
			if ((currentParsingUser.getUserCanRedirect()) || // if user can redirect or
				(currentParsingUser == senderUser))			// user is sender of msg
			{
				for (User user : usersInRadius) 
				{	// add users in radius to parsing users list
					if (!parsingUsers.contains(user))
						parsingUsers.add(user);
				}
			}
			if ((isPrivate)&&(parsingIdNum != 0))
			{	// if msg is private
				if (followersUserIds.contains(currentParsingUser.getUserId())) // if parsing user is follower
					followersUserIds.remove(currentParsingUser.getUserId());
				else
					continue;
			}
			try 
				{messagesManager.exportMessage(currentParsingUser.getUserId(), msgTextId);}
			catch (Exception e)
				{continue;}
			if (isPrivate)
			{
				if (followersUserIds.isEmpty())
					break; // if followers list is empty (all followers parsed)	- stop!!!			
			}
		} while (++parsingIdNum != parsingUsers.size());
		return true;
	}
	
	
	public void run() 
	{
		while (true) 
		{
			usersOnlineListLock.lock();
			try
			{
				Iterator<Entry<String, User>> it = usersOnline.entrySet().iterator();
				//defines entry in map
				Map.Entry<String,User> entry;
				while (it.hasNext()) {
					
					entry = (Map.Entry<String, User>) it.next();
					Date systemDate = new Date();
					//if last refresh is longer than timeout then remove this entry
					if (systemDate.getTime() - entry.getValue().getUserLastRefresh().getTime() > userTimeout) 
					{
						userManager.exportIsOnline(entry.getValue().getUserId(), false); // export data to DB
						it.remove();
						logger.info("[-] User "+entry.getValue().getUserLogin()+" has gone offline by timeout");
					}
				}	
			}
			finally {usersOnlineListLock.unlock();}
			// wait for new refresh
			try {
				Thread.sleep(refreshPeriod);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addNewUserOnline(String userLogin, String md5)
	{	// adding new user online
    	User newUser = new User();
    	newUser.setUserLogin(userLogin); // set user login
    	newUser.setUserId(userManager.searchByLogin(userLogin)); // import user id from DB
    	newUser.setUserIsOnline(true); // set user online status 
    	userManager.exportIsOnline(newUser.getUserId(), true);
    	newUser.setUserCanRedirect(false); // user can't redirect messsages by default
    	userManager.exportCanRedirect(newUser.getUserId(), false);
		Time time = new Time((new Date()).getTime());
    	logger.info("[+] User "+newUser.getUserLogin()+" has logged in at: "+time.toString());
    	usersOnlineListLock.lock();
		try
			{usersOnline.put(md5, newUser);}		
		finally {usersOnlineListLock.unlock();}
		refreshUserLastRefresh(md5); 
		
		
	}
	
	public boolean submitRadius(float radius, String md5)
	{
		User senderUser;
		usersOnlineListLock.lock();
		try
			{senderUser = usersOnline.get(md5);}		
		finally {usersOnlineListLock.unlock();}
		if (senderUser == null)
			return false;
		senderUser.setUserRadius(radius);
		userManager.exportRadius(senderUser.getUserId(), radius);
		logger.info("[!] User "+senderUser.getUserLogin()+" submitted his radius: "+ radius);
		return true;
	}
	
	public boolean submitCanRedirect(boolean canRedirect, String md5)
	{
		User senderUser;
		usersOnlineListLock.lock();
		try
			{senderUser = usersOnline.get(md5);}		
		finally {usersOnlineListLock.unlock();}
		if (senderUser == null)
			return false;
		senderUser.setUserCanRedirect(canRedirect);
		userManager.exportCanRedirect(senderUser.getUserId(), canRedirect);
		logger.info("[!] User "+senderUser.getUserLogin()+" submitted his redirect status: "+ canRedirect);
		return true;
	}
	
	
	public void refreshUserLastRefresh(String md5)
	{	// refreshes data corresponding to user last refresh time
		Time time = new Time((new Date()).getTime());
		User senderUser;
		usersOnlineListLock.lock();
		try
			{senderUser = usersOnline.get(md5);}		
		finally {usersOnlineListLock.unlock();}
		senderUser.setUserLastRefresh(time);
		userManager.exportLastRefresh(senderUser.getUserId(), time); // export data to DB
		logger.info("[!] User "+senderUser.getUserLogin()+ " refreshed his page at: " +senderUser.getUserLastRefresh().toString());
	}
	
	public ArrayList<User> getUsersInChat(String userMd5)
	{
		ArrayList<User> usersInChat = new ArrayList<User>();
		usersOnlineListLock.lock();
		try
		{
			Iterator<Entry<String, User>> it = usersOnline.entrySet().iterator();
			Map.Entry<String,User> entry; //defines entry in map
			while (it.hasNext())
			{
				entry = (Map.Entry<String, User>) it.next();
				if (entry.getValue() == usersOnline.get(userMd5))
					continue;
				if (entry.getValue().getUserGeo() != null)
					usersInChat.add(entry.getValue());
			}
		}
		finally {usersOnlineListLock.unlock();}
		return usersInChat;
	}
	
	public HashMap<String, User> getUsersOnline() {
		return usersOnline;
	}

	public void setUsersOnline(HashMap<String, User> usersOnline) {
		this.usersOnline = usersOnline;
	}

	public ReentrantLock getUsersOnlineListLock() {
		return usersOnlineListLock;
	}

	public void setUsersOnlineListLock(ReentrantLock usersOnlineListLock) {
		this.usersOnlineListLock = usersOnlineListLock;
	}

	public FollowersManager getFollowersManager() {
		return followersManager;
	}

	public void setFollowersManager(FollowersManager followersManager) {
		this.followersManager = followersManager;
	}

	public MessageTextManager getMessageTextManager() {
		return messageTextManager;
	}

	public void setMessageTextManager(MessageTextManager messageTextManager) {
		this.messageTextManager = messageTextManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public MessagesManager getMessagesManager() {
		return messagesManager;
	}

	public void setMessagesManager(MessagesManager messagesManager) {
		this.messagesManager = messagesManager;
	}
	 	
}
