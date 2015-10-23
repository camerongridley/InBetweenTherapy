package com.cggcoding.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class MySQLConnection implements DatabaseConnection {

	public MySQLConnection() {

	}

	@Override
	public Connection getConnection() {
		Connection cn = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/DoItRight");
			
			cn = ds.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		
		return cn;
	}

}
