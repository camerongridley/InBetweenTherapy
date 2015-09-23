package com.cggcoding.utils;

import java.util.List;

public class SqlBuilders {
	
	/**Builds a SQL statement for a PreparedStatement and adds the number of ?s necessary for multiple int values - limited to where the only WHERE criteria is a list of ints
	 * @param baseSQLStatement - String that includes everything up until AFTER the first open parentheses for holding ?s for prepared statement parameters - e.g. "SELECT * FROM task WHERE task_id in ("
	 * @param intValues - needed for to know how many int parameters there are
	 * @return
	 */
	public static String includeMultipleIntParams(String baseSQLStatement, List<Integer> intValues){
    	StringBuilder sqlBuilder = new StringBuilder(baseSQLStatement);
    	
    	for(Integer value : intValues){
    		sqlBuilder.append("?,");
    	}	
    	
    	sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
    	sqlBuilder.append(")");
		String sql = sqlBuilder.toString();
		
		return sql;
	}
}
