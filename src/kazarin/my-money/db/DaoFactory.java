package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

import java.util.Properties;

public class DaoFactory {

	public static Dao<Operation> getDao(Properties properties) throws DaoException {
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
