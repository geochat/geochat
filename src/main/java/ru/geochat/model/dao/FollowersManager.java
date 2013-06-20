package ru.geochat.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class FollowersManager extends JdbcDaoSupport
{
	private String importFollowers;
	
	public void setImportFollowers(String importFollowers) {
		this.importFollowers = importFollowers;
	}
	public String getImportFollowers() {
		return importFollowers;
	}
	
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getInt("follower_user");
	}
	public List<Integer> importFollowers(Integer userId) throws Exception {
		List <Integer> followerIds = getJdbcTemplate().query(importFollowers, new Object[] {userId}, new RowMapper<Integer>(){
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("follower_user");
			}
		});
		return followerIds;
	}
}

