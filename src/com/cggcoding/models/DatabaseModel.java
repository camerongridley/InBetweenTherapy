package com.cggcoding.models;

import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;

/**
 * To be used with any object representation of a database model.
 * @author cgrid_000
 *
 */
public interface DatabaseModel {
	
	/**Validates the data in the object and if ok, inserts new record in the database
	 * @return true if valid and insert successful, false if invalid and insert unsuccessful
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean validateAndSaveNewInDatabase() throws ValidationException, DatabaseException;
	
	/**Validates and if ok, updates data from database and sets the corresponding properties in the object
	 * @return true if successful loading the data, false if unsuccessful and throws Exception
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean validateAndUpdateInDatabase() throws ValidationException, DatabaseException;
	
	
	/**If the object has the id property set, can call this to retrieve the rest of it's data from the database
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean loadDataFromDatabase() throws ValidationException, DatabaseException;
	
	
	/**Deleted data from database and destroys the object
	 * @return true if successful deleting the data, false if unsuccessful and throws Exception
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	boolean deleteFromDatabase() throws ValidationException, DatabaseException;
	

	/**Copies the object the specified number of times
	 * @param o The class that models a database table
	 * @param numberOfCopies Number of copies desired of the model
	 * @return A list of the copied object
	 */
	List<Object> copy(Object o, int numberOfCopies);
}
