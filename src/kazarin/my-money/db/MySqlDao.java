/*
 * MySqlDao
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Entry;

import java.util.Properties;

/**
 * Provides access to the operations table in database.
 * Depends on:  model/Entry.java
 *				db/AbstractDao.java
 */
public class MySqlDao extends AbstractDao{
 
 	/**
	 * Constracts the MySqlDao.
 	 */
	MySqlDao(Properties properties) throws DaoException {	
		super(properties);
	}	
 	
 	@Override
	public void add(Entry entry) throws DaoException {		
		add(entry, "INSERT INTO operations "
								+ "(op_sum, op_date, op_description, op_tags) "
								+ "VALUES ('%s', '%s', \"%s\", \"%s\");");
		
	}

	@Override
	public void update(Entry oldEntry){			
		update(oldEntry, "UPDATE operations SET "
							+ "op_sum='%s', op_date='%s', "
							+ "op_description=\"%s\", op_tags=\"%s\" "
							+ "WHERE op_id='%s';");	
	}

	@Override
	public void delete(Entry entry){
		delete(entry, "DELETE FROM operations WHERE op_id='%s';");
	}

	@Override
	public void createDB(String dbName) throws DaoException {
		String createTable = 	"CREATE TABLE operations ("
								      + "op_id INT NOT NULL AUTO_INCREMENT, "
								      + "op_sum NUMERIC(10,2) NOT NULL, "
								      + "op_date DATE NOT NULL, "
								      + "op_description VARCHAR(255), "
								      + "op_tags VARCHAR(255), "
								      + "PRIMARY KEY (op_id)"
								+ ");";
		createDB(dbName, "CREATE DATABASE %s", createTable);

	}
}
