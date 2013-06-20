package ru.geochat.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.geochat.model.dto.Message;

public class MessagesManager extends JdbcDaoSupport
{
	private String exportMessage;
	private String importMessage;
	
	public void exportMessage(Integer messageTo, Integer msgTextId) throws Exception {
		getJdbcTemplate().update(exportMessage, new Object[]{messageTo, msgTextId});
	}
	
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		Message msg = new Message();
		msg.sender.setUserId(rs.getInt("msg_from"));
		msg.text = rs.getString("msg_text");
		return msg;
	}
	
	public List<Message> importMessageForUser(Integer userId) throws SQLException {
		
		List<Message> messages = getJdbcTemplate().query(importMessage, new Object[] {userId}, new RowMapper<Message>() {
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				Message msg = new Message();
				msg.sender.setUserId(rs.getInt("msg_from"));
				msg.text = rs.getString("msg_text");
				return msg;
			}
		});
		
		return messages;
	}

	public String getExportMessage() {
		return exportMessage;
	}

	public void setExportMessage(String exportMessage) {
		this.exportMessage = exportMessage;
	}
	
	public String getImportMessage() {
		return importMessage;
	}

	public void setImportMessage(String importMessage) {
		this.importMessage = importMessage;
	}
}
