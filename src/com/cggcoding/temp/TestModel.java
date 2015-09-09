package com.cggcoding.temp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.utils.database.DatabaseConnection;
import com.cggcoding.utils.database.MySQLConnection;

import java.sql.Connection;


public class TestModel {
	private int userID;
	private String email;
	
	public TestModel(int userID){
		this.userID = userID;
	}
	
	public TestModel(int userID, String email) {
		this.userID = userID;
		this.email = email;
	}
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TestModel returnData() throws NamingException, SQLException{
		DatabaseConnection mysqlConn = new MySQLConnection();
	    
	    Connection cn = mysqlConn.getConnection();
	    PreparedStatement ps = null;
        ResultSet rs = null;
	    
        ps = cn.prepareStatement("SELECT * FROM user WHERE user_id=?");
        ps.setInt(1, userID);

        rs = ps.executeQuery();

        while (rs.next()){
        	this.userID = rs.getInt("user_id");
        	this.email = rs.getString("email");
        }
        
        return this;
	}

}
