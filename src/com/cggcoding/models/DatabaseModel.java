package com.cggcoding.models;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;

/**
 * To be used with any object representation of a database model.
 * @author cgrid_000
 *
 */
public interface DatabaseModel {
	//TODO decide if save and load should be static factory methods 
	
	/**Validates the data in the object before inserting or updating in the database
	 * @return true if valid, false if invalid
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	//boolean validateForDatabase()  throws ValidationException, DatabaseException;
	

	//boolean saveNewInDatabase() throws ValidationException, DatabaseException;
	
	
	//boolean loadDataFromDatabase() throws ValidationException, DatabaseException;

	/**Updates data from database and sets the corresponding properties in the object
	 * @return true if successful loading the data, false if unsuccessful and throws Exception
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean updateInDatabase() throws ValidationException, DatabaseException;
	
	/**Deleted data from database and destroys the object
	 * @return true if successful deleting the data, false if unsuccessful and throws Exception
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean deleteFromDatabase() throws ValidationException, DatabaseException;
}
