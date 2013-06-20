package ru.geochat.model.dao;

import java.sql.SQLException;
import java.sql.Time;

import ru.geochat.model.dto.User;

public interface IUserManager {
	Boolean registerUser (String login, String password);
	String loginUser (String login, String password) throws Exception;			//���������� ��5 ��� ����������
	void exportGeo(Integer userId, Geo geo) throws Exception;					//���������� �� ������������
	Geo importGeo(Integer userId) throws Exception;								//������� �� ���� ���
	Integer searchByLogin(String login);										//returns user id
	Boolean importCanRedirect(Integer userId);									//returns ability of redirection
	void exportCanRedirect(Integer userId, Boolean canRedirect);
	Boolean importIsOnline(Integer userId);										//returns whether user is online
	void exportIsOnline(Integer userId, Boolean isOnline);
	Time importLastRefresh(Integer userId);										//returns user's last refresh
	void exportLastRefresh(Integer userId, Time time);
	float importRadius(Integer userId);	
	void exportRadius(Integer userId, float radius);
}
