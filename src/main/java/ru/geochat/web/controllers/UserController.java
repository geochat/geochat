package ru.geochat.web.controllers;


import java.sql.SQLException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import ru.geochat.ServerController;
import ru.geochat.model.dao.IUserManager;
import ru.geochat.web.forms.UserForm;

@Controller
@RequestMapping("/users")
public class UserController {
	



	private IUserManager userManager;
	private ServerController servController;

  @Required
  public void setUserManager(IUserManager userManager) {
    this.userManager = userManager;
  }
  @Required
	public ServerController getServController() {
		return servController;
	}

  @Required
	public void setServController(ServerController servController) {
		this.servController = servController;
	}

  @RequestMapping("/index")
  public String getIndex() {
    return "users_index";
  }
  
//  @RequestMapping("/view/{id}")
//  public ModelAndView getPerson(@PathVariable("id") String login) throws Exception {
//    ModelAndView mav = new ModelAndView("users_view");
//    mav.addObject("user", userManager.importGeo(10));
//    return mav;
//  }
  
  
  /*
  @RequestMapping("/new")
  public String getNewUser() {
    return "users_new";
  }*/
  
  @RequestMapping("/new")
  public ModelAndView getNewUser(@Valid @ModelAttribute("user") UserForm user, BindingResult result) throws SQLException {
	  ModelAndView mv = new ModelAndView();
	  String errorMessage = null;
	  if(!result.hasErrors()){
			if(userManager.registerUser(user.getLogin(), user.getPassword()) == true){
				mv.setViewName("redirect:login");
			}
			else {
				mv.setViewName("users_new");
				errorMessage = "This user already exists";
				mv.addObject("errorMessage", errorMessage);
			}
	  }
	  else {
		  mv.setViewName("users_new");
			//errorMessage = "Fields empty";
			mv.addObject("errorMessage", errorMessage);
		}
	    return mv;
  }
  
  @RequestMapping("/login")
  public String qwe() {
    return "users_login";
  }
  
  @RequestMapping(value="/users_login", method=RequestMethod.POST)
  public ModelAndView loginUser(@ModelAttribute("user") UserForm user, BindingResult result) throws Exception {
    ModelAndView mv = new ModelAndView();
    String md5login = userManager.loginUser(user.getLogin(), user.getPassword());
    if(md5login == null){
    	mv.setViewName("users_login");
    	mv.addObject("errorMessage", "Check login and pass");
    }
    else{ // add new user to serv controller
    	servController.addNewUserOnline(user.getLogin(), md5login);
    	mv.addObject("md5", md5login);
    	mv.setViewName("forward:/location/index");
    }
    return mv;
  }

  
  @ModelAttribute("user")
  public UserForm getUserForm() {
    return new UserForm();
  }

}
