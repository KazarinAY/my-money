/*
 * Dao
 */
package kazarin.my_money.db;

import java.util.List;

public interface Dao<T> {
	/**
	 * Gets the list of rows from the table.
	 *
	 * @return list of rows
	 */
	public List<T> getAll() throws DaoException;
	
	/**
	 * Adds the row to table.
	 *
	 * @param row to adding to table
	 */
	public void add(T object) throws DaoException;
	
	/**
	 * Updates the row in the table.
	 *
	 * @param the row to which you want to update
	 */
	public void update(T object);
	
	/**
	 * Deletes the row frome table.
	 *
	 * @param the row to delete
	 */
	public void delete(T object);

	/**
	 * Creates new DB.
	 *
	 * @param name of DB
	 */
	public void createDB(String dbName) throws DaoException;
}
