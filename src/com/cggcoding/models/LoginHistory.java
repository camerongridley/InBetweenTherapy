package com.cggcoding.models;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class LoginHistory {
	private int loginHistoryID;
	private int userID;
	private LocalDateTime loginDateTime;
	
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	
	public LoginHistory() {
		
	}

	
	public LoginHistory(int userID, LocalDateTime loginDateTime) {
		this.userID = userID;
		this.loginDateTime = loginDateTime;
	}



	public LoginHistory(int loginHistoryID, int userID, LocalDateTime loginDateTime) {
		this.loginHistoryID = loginHistoryID;
		this.userID = userID;
		this.loginDateTime = loginDateTime;
	}


	public int getLoginHistoryID() {
		return loginHistoryID;
	}


	public void setLoginHistoryID(int loginHistoryID) {
		this.loginHistoryID = loginHistoryID;
	}


	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public LocalDateTime getLoginDateTime() {
		return loginDateTime;
	}


	public void setLoginDateTime(LocalDateTime loginDateTime) {
		this.loginDateTime = loginDateTime;
	}
	
	public void create(Connection cn) throws SQLException {
		dao.loginHistoryCreate(cn, this);
	}
	
	public void deleteOldEntries(Connection cn, long daysOfHistoryToKeep, LoginHistory lastLogin) throws SQLException{
		LocalDateTime ldt = lastLogin.getLoginDateTime();
		LocalDateTime deleteBeforeThisDate = ldt.minusDays(daysOfHistoryToKeep);
		
		dao.loginHistoryDeleteOldEntries(cn, getUserID(), deleteBeforeThisDate);
	}
	
	public List<LoginHistory> getAllLoginHistory(Connection cn) throws SQLException{
		return dao.getLoginHistory(cn, getUserID());
	}

}
