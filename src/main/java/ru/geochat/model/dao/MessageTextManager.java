package ru.geochat.model.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MessageTextManager extends JdbcDaoSupport
{
	private String exportMsgText;
	private String getLastMsgTextId;
	
	public String getGetLastMsgTextId() {
		return getLastMsgTextId;
	}

	public void setGetLastMsgTextId(String getLastMsgTextId) {
		this.getLastMsgTextId = getLastMsgTextId;
	}

	public String getExportMsgText() {
		return exportMsgText;
	}

	public void setExportMsgText(String exportMsgText) {
		this.exportMsgText = exportMsgText;
	}
	
	private Integer exportLastMsgtextId() throws Exception
	{
		return getJdbcTemplate().queryForInt(getLastMsgTextId);
	}
	
	public Integer exportMsgText(Integer msgFromId, String msgText) throws Exception {
		getJdbcTemplate().update(exportMsgText, new Object[]{msgFromId, msgText});
		return exportLastMsgtextId();
	}
	
}
