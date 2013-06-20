package ru.geochat.model.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;
import javax.sql.DataSource;

import org.eclipse.jetty.util.security.Credential.MD5;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import javax.swing.tree.TreePath;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.ResultSetExtractor;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import ru.geochat.model.dto.User;

public class UserManager extends JdbcDaoSupport implements IUserManager{
	
	private String searchByLogin;
	private String importIsOnline;
	private String importLogin;
	private String exportIsOnline;
	private String registerUser;
	private String loginUser;
	private String exportGeo;
	private String importGeo;
	private String importCanRedirect;
	private String exportCanRedirect;
	private String importLastRefresh;
	private String exportLastRefresh;
	private String exportRadius;
	private String importRadius;
	
	public Boolean registerUser(String login, String password) {
		if (this.searchByLogin(login) == null && password != "" && login != "") {
			getJdbcTemplate().update(registerUser, new Object[]{login,password});
			if (this.searchByLogin(login)!=null)
				return true;
			else
				return false;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public String loginUser(String login, String password) throws NoSuchAlgorithmException {
		if (this.searchByLogin(login) == null)
			return null;
		else {
			try{
				getJdbcTemplate().queryForInt(loginUser, new Object[]{login,password});
			}
			catch(Exception e){
				return null;
			}
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(login.getBytes(),0,login.length());
			return new BigInteger(1,m.digest()).toString(16);
		}
	}

	public void exportGeo(Integer userId, Geo geo) throws Exception {
		getJdbcTemplate().update(exportGeo, new Object[]{geo.getLatitude(),geo.getLongitude(),userId});
	}

	public Geo importGeo(Integer userId) throws Exception {
		return getJdbcTemplate().queryForObject(importGeo, new RowMapper<Geo>(){
			public Geo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Geo(rs.getDouble("user_geo_lat"),rs.getDouble("user_geo_lngt"));
			}
		},userId);
	}
	
	@SuppressWarnings("deprecation")
	public Integer searchByLogin(String login) {
		Integer userId = null;
		try{
			userId = getJdbcTemplate().queryForInt(searchByLogin,login);
		}
		catch(Exception e){
			return null;
		}
		return userId;
	} 
	
	public Boolean importCanRedirect(Integer userId) {
		Integer canRedirect = getJdbcTemplate().queryForInt(importCanRedirect,userId);
		if(canRedirect == 1)
			return true;
		else
			return false;
	}

	public void exportCanRedirect(Integer userId, Boolean canRedirect) {
		getJdbcTemplate().update(exportCanRedirect,new Object[]{canRedirect,userId});
	}

	@SuppressWarnings("deprecation")
	public Boolean importIsOnline(Integer userId) {
		Integer isOnline = getJdbcTemplate().queryForInt(importIsOnline,userId);
		if(isOnline==1)
			return true;
		else 
			return false;
	}

	public void exportIsOnline(Integer userId, Boolean isOnline) {
		getJdbcTemplate().update(exportIsOnline, new Object[]{isOnline,userId});
	}

	public Time importLastRefresh(Integer userId) {
		return getJdbcTemplate().queryForObject(importLastRefresh, new RowMapper<Time>() {
			public Time mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getTime("user_last_refresh");
			}
		},userId);
	}

	public void exportLastRefresh(Integer userId, Time time) {
		getJdbcTemplate().update(exportLastRefresh, new Object[]{time,userId});
	}
	
	public String importLogin(Integer userId)
	{
		return getJdbcTemplate().queryForObject (importLogin, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("user_login");
			}
		},userId);
	}
	

	public float importRadius(Integer userId)
	{
		return getJdbcTemplate().queryForObject (importRadius, new RowMapper<Float>() {
			public Float mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getFloat("user_radius");
			}
		},userId);
	}

	public void exportRadius(Integer userId, float radius)
	{
		getJdbcTemplate().update(exportRadius, new Object[]{radius,userId});
	}
	
	public String getImportLogin() {
		return importLogin;
	}
	public void setImportLogin(String importLogin) {
		this.importLogin = importLogin;
	}
	
	public String getImportIsOnline() {
		return importIsOnline;
	}
	public void setImportIsOnline(String importIsOnline) {
		this.importIsOnline = importIsOnline;
	}
	public String getRegisterUser() {
		return registerUser;
	}
	public void setRegisterUser(String registerUser) {
		this.registerUser = registerUser;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public String getExportGeo() {
		return exportGeo;
	}
	public void setExportGeo(String exportGeo) {
		this.exportGeo = exportGeo;
	}
	public String getImportGeo() {
		return importGeo;
	}
	public void setImportGeo(String importGeo) {
		this.importGeo = importGeo;
	}
	public String getImportCanRedirect() {
		return importCanRedirect;
	}
	public void setImportCanRedirect(String importCanRedirect) {
		this.importCanRedirect = importCanRedirect;
	}
	public String getExportCanRedirect() {
		return exportCanRedirect;
	}
	public void setExportCanRedirect(String exportCanRedirect) {
		this.exportCanRedirect = exportCanRedirect;
	}
	public String getImportLastRefresh() {
		return importLastRefresh;
	}
	public void setImportLastRefresh(String importLastRefresh) {
		this.importLastRefresh = importLastRefresh;
	}
	public String getExportLastRefresh() {
		return exportLastRefresh;
	}
	public void setExportLastRefresh(String exportLastRefresh) {
		this.exportLastRefresh = exportLastRefresh;
	}
	@Required
	public String getsearchByLogin() {
		return searchByLogin;
	}
	@Required
	public void setsearchByLogin(String searchByLogin) {
		this.searchByLogin = searchByLogin;
	}

	public String getExportIsOnline() {
		return exportIsOnline;
	}
	public void setExportIsOnline(String exportIsOnline) {
		this.exportIsOnline = exportIsOnline;
	}
	
	public String getExportRadius()
	{
		return exportRadius;
	}
	public void setExportRadius(String exportRadius)
	{
		this.exportRadius = exportRadius;
	}
	public String getImportRadius()
	{
		return importRadius;
	}
	public void setImportRadius(String importRadius)
	{
		this.importRadius = importRadius;
	}


}
