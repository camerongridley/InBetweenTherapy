package com.cggcoding.utils.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.utils.messaging.ErrorMessages;



public class MySQLConnection implements Serializable, DatabaseConnection {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MySQLConnection() {

	}

	@Override
	public Connection getConnection() throws DatabaseException {
		Connection cn = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			//The resource "jdbc/primaryDB" is set in /META-INF/context.xml - here all of the datasource connection properties are set
			DataSource ds = (DataSource) envCtx.lookup("jdbc/primarydb");
			
			cn = ds.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.DATABASE_CONNECTION_FAILURE);
		}
		
		return cn;
	}

}
