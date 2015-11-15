package com.cggcoding.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class MySQLConnection implements DatabaseConnection {

	public MySQLConnection() {

	}

	@Override
	public Connection getConnection() {
		Connection cn = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			//The resource "jdbc/DoItRight" is set in /META-INF/context.xml - here all of the datasource connection properties are set
			DataSource ds = (DataSource) envCtx.lookup("jdbc/DoItRight");
			
			cn = ds.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		
		return cn;
	}

}
