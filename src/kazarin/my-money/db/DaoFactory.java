package kazarin.my_money.db;

import kazarin.my_money.model.Entry;

import java.util.Properties;

/*
 * Depends on:  db/Dao.java
 *              db/MySqlDao.java
 *              db/HHSqlDao.java
 *              model/Entry.java
 */
public class DaoFactory {

	public static Dao<Entry> getDao(Properties properties) {
		String type = properties.getProperty("DB type");
		switch (type) {

            case "MySQL":                
                return new MySqlDao(properties);
            
            case "HSQL":                
                return new HSqlDao(properties);

            default:
            	DBLogger.warning("Failed to DaoFactory.getDao...");
                throw new DaoException("Unknown DB type.");
        }
	}
}
