package ru.geochat.web.controllers;


import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.geochat.ServerController;
import ru.geochat.model.dao.Geo;
import ru.geochat.model.dto.User;
import ru.geochat.web.forms.LocationForm;


@Controller
@RequestMapping(value="/location")
public class LocationController {
	private ServerController servController;
	
	@RequestMapping(value="/index", method=RequestMethod.POST)
	public ModelAndView getIndex(@ModelAttribute("location") LocationForm location) {
		ModelAndView mv = new ModelAndView("map");
		boolean md5Exist=false;
		boolean geoExist=false;
		if (location.getMd5() != null)
			md5Exist=true;
		mv.addObject("md5Exist", md5Exist);
		if (location.getLatitude() != null && location.getLongitude() != null)
		{
			geoExist=true;
			mv.addObject("lat", location.getLatitude());
			mv.addObject("lng", location.getLongitude());
		}
		else
		{
			mv.addObject("lat", 0);
			mv.addObject("lng", 0);
		}
		mv.addObject("geoExist", geoExist);
		if (location.getMd5() != null)
		{
			mv.addObject("usersOnline", servController.getUsersInChat(location.getMd5()));
			servController.refreshUserLastRefresh(location.getMd5());
		}
		if (location.getEnterChatButton() != null)
		{
			
			if (location.getLatitude() == null || location.getLongitude() == null || location.getRadius() == null) 
			{	// if needed fields are empty - return to this form
				return mv;
			}
			if (location.getCanRedirect() != null)
				servController.submitCanRedirect(true, location.getMd5());
			else
				servController.submitCanRedirect(false, location.getMd5());
	    	servController.submitRadius(location.getRadius()*1000, location.getMd5());
			servController.submitLocation(new Geo(location.getLatitude(), location.getLongitude()), location.getMd5());
			mv.addObject("md5", location.getMd5());
			mv.setViewName("forward:/chat/index");
		}
		return mv;
	}
	public ServerController getServController() {
		return servController;
	}
	public void setServController(ServerController servController) {
		this.servController = servController;
	}
}
