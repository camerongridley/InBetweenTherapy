package com.cggcoding.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class TaskKeyword {
	private int taskKeywordID;
	private String keyword;
	private int userID;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	public TaskKeyword(){
	}
	
	public TaskKeyword(int taskKeywordID, String keyword, int userID) {
		super();
		this.taskKeywordID = taskKeywordID;
		this.keyword = keyword;
		this.userID = userID;
	}

	public TaskKeyword(String keyword, int userID) {
		super();
		this.keyword = keyword;
		this.userID = userID;
	}

	public int getTaskKeywordID() {
		return taskKeywordID;
	}

	public void setTaskKeywordID(int taskKeywordID) {
		this.taskKeywordID = taskKeywordID;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public TaskKeyword create(Connection cn) throws SQLException {
		
		dao.keywordCreate(cn, this);

		return this;
		
	}
	
	public static Map<String, TaskKeyword> loadCoreList() throws ValidationException, DatabaseException{
		Connection cn = null;
		Map<String, TaskKeyword> keywords = null;
		try{
			cn = dao.getConnection();
			
			keywords = dao.keywordCoreListLoad(cn);
		} catch (SQLException e){
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		}finally{
			DbUtils.closeQuietly(cn);
		}
		 
		
		return keywords;
	}
	
	public void update(Connection cn) throws SQLException, ValidationException{
		dao.keywordUpdate(cn, this);
	}
	
	public static void delete(int keywordID) throws DatabaseException{
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			
			dao.keywordDelete(cn, keywordID);
		} catch (SQLException e){
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		}finally{
			DbUtils.closeQuietly(cn);
		}
		
	}
	
	
	public boolean checkIfKeywordExists(String keyword, int forUserID){
		//TODO implement method
		return false;
	}
	
}
