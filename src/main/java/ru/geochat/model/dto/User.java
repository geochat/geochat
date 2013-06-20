package ru.geochat.model.dto;

import java.sql.Time;

import ru.geochat.model.dao.Geo;

public class User {
	private Integer userId;
	private String userLogin;
	private String userPassword;
	private Float userRadius;
	private Geo userGeo;
	private Boolean userIsOnline;
	private Boolean userCanRedirect;
	private Time userLastRefresh;
	
	//setters and getters
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public Float getUserRadius() {
		return userRadius;
	}
	public void setUserRadius(Float userRadius) {
		this.userRadius = userRadius;
	}
	public Boolean getUserIsOnline() {
		return userIsOnline;
	}
	public void setUserIsOnline(Boolean userIsOnline) {
		this.userIsOnline = userIsOnline;
	}
	public Boolean getUserCanRedirect() {
		return userCanRedirect;
	}
	public void setUserCanRedirect(Boolean userCanRedirect) {
		this.userCanRedirect = userCanRedirect;
	}
	public Time getUserLastRefresh() {
		return userLastRefresh;
	}
	public void setUserLastRefresh(Time userLastRefresh) {
		this.userLastRefresh = userLastRefresh;
	}
	public Geo getUserGeo() {
		return userGeo;
	}
	public void setUserGeo(Geo userGeo) {
		this.userGeo = userGeo;
	}
	
	public User(Integer userId, String userLogin, String userPassword,
			Float userRadius, Geo userGeo, Boolean userIsOnline,
			Boolean userCanRedirect, Time userLastRefresh) {
		this.userId = userId;
		this.userLogin = userLogin;
		this.userPassword = userPassword;
		this.userRadius = userRadius;
		this.userGeo = userGeo;
		this.userIsOnline = userIsOnline;
		this.userCanRedirect = userCanRedirect;
		this.userLastRefresh = userLastRefresh;
	}
	
	public User(Integer userId) {
		this.userId = userId;

	}
	
	public User() {
	}
	
	
	
}
