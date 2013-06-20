package ru.geochat.web.forms;


import org.hibernate.validator.constraints.NotEmpty;

public class UserForm {
	@NotEmpty(message="Enter login")
  private String login;
	@NotEmpty(message="Enter password")
  private String password;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
