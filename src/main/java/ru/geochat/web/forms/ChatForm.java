package ru.geochat.web.forms;

public class ChatForm {
	private String outMessage;
	private String md5;
	private String submitButton;
	private String changeGeo;
	private String isPrivate;
	private String submitByCtrl;
	public String getOutMessage() {
		return outMessage;
	}
	public void setOutMessage(String outMessage) {
		this.outMessage = outMessage;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getSubmitButton() {
		return submitButton;
	}
	public void setSubmitButton(String submitButton) {
		this.submitButton = submitButton;
	}
	public String getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getChangeGeo() {
		return changeGeo;
	}
	public void setChangeGeo(String changeGeo) {
		this.changeGeo = changeGeo;
	}
	public String getSubmitByCtrl() {
		return submitByCtrl;
	}
	public void setSubmitByCtrl(String submitByCtrl) {
		this.submitByCtrl = submitByCtrl;
	}
}
