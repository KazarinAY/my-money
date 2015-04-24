/*
 * MySqlDao
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

import java.util.logging.*;

import java.io.IOException;

/**
 * Provides access to the operations table in database.
 */
public class MySqlDao extends AbstractDao{

	private static Logger logger;

	static {
		try {
            logger = Logger.getLogger(MySqlDao.class.getName());
            FileHandler fh = new FileHandler("/tmp/MySqlDao.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}
 
 	/**
	 * Constracts the MySqlDao.
 	 */
	public MySqlDao() {	
		super();
	}	
 	
 	@Override
	public void add(Operation operation){		
		add(operation, "INSERT INTO operations "
								+ "(op_sum, op_date, op_description, op_tags) "
								+ "VALUES ('%s', '%s', \"%s\", \"%s\");");
		
	}

	@Override
	public void update(Operation oldOperation){			
		update(oldOperation, "UPDATE operations SET "
							+ "op_sum='%s', op_date='%s', "
							+ "op_description=\"%s\", op_tags=\"%s\" "
							+ "WHERE op_id='%s';");	
	}

	@Override
	public void delete(Operation operation){
		delete(operation, "DELETE FROM operations WHERE op_id='%s';");
	}
}
