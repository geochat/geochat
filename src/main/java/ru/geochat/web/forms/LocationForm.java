package ru.geochat.web.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class LocationForm {

//	@NotEmpty(message="latitude")
	private Double latitude;
//	@NotEmpty(message="longitude")
	private Double longitude;
	private String md5;
	private String enterChatButton;
	private Float radius;
	private String canRedirect;
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getEnterChatButton() {
		return enterChatButton;
	}
	public void setEnterChatButton(String enterChatButton) {
		this.enterChatButton = enterChatButton;
	}
	public Float getRadius()
	{
		return radius;
	}
	public void setRadius(Float radius)
	{
		this.radius = radius;
	}
	public String getCanRedirect()
	{
		return canRedirect;
	}
	public void setCanRedirect(String canRedirect)
	{
		this.canRedirect = canRedirect;
	}	
	

}
