package ru.geochat.web.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.geochat.ServerController;
import ru.geochat.model.dao.Geo;
import ru.geochat.model.dto.DataForUser;
import ru.geochat.model.dto.Message;
import ru.geochat.model.dto.User;
import ru.geochat.web.forms.ChatForm;

@Controller
@RequestMapping(value="/chat")
public class ChatController {
	private ServerController servController;
	
	@RequestMapping(value="/index", method=RequestMethod.POST)
	public ModelAndView getIndex(@ModelAttribute("chatForm") ChatForm chatForm) {
		ModelAndView mv = new ModelAndView("chat");
		boolean md5Exist=false;
		if (chatForm.getMd5() != null)
			md5Exist=true;
		mv.addObject("md5Exist", md5Exist);
		if (chatForm.getMd5() != null) 
		{
			if (chatForm.getChangeGeo() != null)
			{	// if user wants to change his geo - redirect to map
				mv.addObject("md5",chatForm.getMd5());
				mv.setViewName("forward:/location/index");
				return mv;
			}
			boolean isPrivate=false;
			if (chatForm.getIsPrivate() != null)
				isPrivate=true;
			if (chatForm.getSubmitByCtrl() != null)
			{
				if ((chatForm.getSubmitByCtrl().compareTo("true") == 0)&&(!chatForm.getOutMessage().isEmpty()))
					servController.messageProccessor(chatForm.getMd5(), chatForm.getOutMessage(), isPrivate);
			}
			if (chatForm.getSubmitButton() != null) {
				
				if (!chatForm.getOutMessage().isEmpty())
					servController.messageProccessor(chatForm.getMd5(), chatForm.getOutMessage(), isPrivate);
			}
			User currentUser=servController.getUsersOnline().get(chatForm.getMd5());
			Geo userCoords=currentUser.getUserGeo();
			float userRadius=currentUser.getUserRadius();
			mv.addObject("userLat", userCoords.getLatitude());
			mv.addObject("userLng", userCoords.getLongitude());
			mv.addObject("userRadius", userRadius);
			servController.refreshUserLastRefresh(chatForm.getMd5());
			mv.addObject("messagesListString", this.getMessagesForUser(chatForm.getMd5()));
			ArrayList<User> usersInRadius = servController.getUsersInRadius(currentUser);
			mv.addObject("usersInRadius", usersInRadius);
			mv.addObject("nwBound", userCoords.getBoundsByRadius(1.5f*userRadius).get(0));
			mv.addObject("seBound", userCoords.getBoundsByRadius(1.5f*userRadius).get(1));
			mv.addObject("userLogin", currentUser.getUserLogin());
		}
		return mv;
	}

	public ServerController getServController() {
		return servController;
	}

	public void setServController(ServerController servController) {
		this.servController = servController;
	}
	
	private String getMessagesForUser(String md5) {
		String messagesListString = "";
		DataForUser data = servController.generateDataForUser(md5);
		if (data == null) {
			return "data is null";
		}
		for (Message message : data.messagesList) {
			messagesListString += message.sender.getUserLogin() + ": " + message.text + "\n";
		}
		return messagesListString;
	}
}
