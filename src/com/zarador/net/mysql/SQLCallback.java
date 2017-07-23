package com.zarador.net.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The callback class used for SQL queries
 * 
 * @author Nikki
 *
 */
public interface SQLCallback {

	/**
	 * Called when a query is complete
	 * 
	 * @param result
	 *            The result, or null if it was an UPDATE/DELETE.
	 * @throws SQLException
	 *             If an error occurs in the user implemented code.
	 */
	public void queryComplete(ResultSet result) throws SQLException;

	/**
	 * Called when a query fails.
	 * 
	 * @param e
	 *            The exception thrown
	 */
	public void queryError(SQLException e);

}
