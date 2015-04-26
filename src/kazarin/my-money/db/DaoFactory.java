package kazarin.my_money.db;

import java.util.Properties;

public class DaoFactory {

	public static Dao getDao(Properties properties) throws DaoException {
		String type = properties.getProperty("DB type");
		switch (type) {

            case "MySQL":                
                return (Dao) new MySqlDao(properties);
            
            case "HSQL":                
                return (Dao) new HSqlDao(properties);

            case "TEST":
                return (Dao) new StubDao();

            default:
                throw new DaoException("Unknown DB type.");
        }
	}
}
