package com.cggcoding.utils.database;

import java.sql.Connection;

import com.cggcoding.exceptions.DatabaseException;

public interface DatabaseConnection {
	Connection getConnection() throws DatabaseException;
}
