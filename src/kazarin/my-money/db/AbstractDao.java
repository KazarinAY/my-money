package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

import java.util.logging.*;
import java.util.Properties;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Locale;

import kazarin.my_money.model.Environment;

public abstract class AbstractDao implements Dao<Operation> {

	private static Logger logger;

	static {
		try {
            logger = Logger.getLogger(AbstractDao.class.getName());
            FileHandler fh = new FileHandler("/tmp/AbstractDao.log");  
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


	protected Properties propertiesDB;
	protected Connection connection;
	protected String url;

	/**
	 * Constracts the AbstractDao.
 	 */
	public AbstractDao(Properties propertiesDB) {
		this.propertiesDB = propertiesDB;
		url = propertiesDB.getProperty("url");
		
		try {
			logger.info("DRIVER: " + propertiesDB.getProperty("driver"));
            Class.forName(propertiesDB.getProperty("driver"));
            logger.info("DRIVER... OK");
        }catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "ERROR: failed to find driver.", e);
            
        }
        
        try {	
			connection = DriverManager.getConnection(url, propertiesDB);
			logger.info("CONNECTION... OK");
		} catch(SQLException e) {			
			logger.log(Level.WARNING, "ERROR: failed to get connection.", e);
			logger.log(Level.WARNING, "\turl=" + url
								+ "\n\tuser=" + propertiesDB.getProperty("user")
								+ "\n\tpassword=" + propertiesDB.getProperty("password"));

		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.log(Level.WARNING, "ERROR: failed to close connection.", e);
				}				
			}
		}
		
	}

	@Override
	public List<Operation> getAll(){
		String sql = "SELECT * FROM operations;";
		logger.info("SQL: " + sql);
		List<Operation> list = new ArrayList<Operation>();
		ResultSet rs = null;
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
			logger.info("CONNECTION... OK");
			Statement stmt = connection.createStatement();
			logger.info("Statement... OK");
			rs = stmt.executeQuery(sql);
			logger.info("ResultSet... OK");
			
			while(rs.next()){
				Operation operation = new Operation();
				operation.setId(rs.getInt("op_id"));
				operation.setSum(rs.getBigDecimal("op_sum"));
				operation.setDate(rs.getDate("op_date"));
				operation.setDescription(rs.getString("op_description"));
				operation.setTags(rs.getString("op_tags").split(","));
				list.add(operation);
			}
		} catch(SQLException e){
			logger.warning("ERROR: failed to get all.");
			logger.log(Level.WARNING, "ERROR: failed to get resultSet.", e);			
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.log(Level.WARNING, "ERROR: failed to close connection.", e);
				}
			}
		}
		
		return list;
	}

	protected void add(Operation operation, String sqlFormat){
		String howMuch = operation.getSum().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(operation.getDate());
		String description = operation.getDescription();
		String tags = operation.getTagsStr();
		String sql = String.format(	sqlFormat,
									howMuch, date, description, tags);
		logger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();			
			stmt.executeUpdate(sql);			

		} catch(SQLException e){
			System.err.println("ERROR: failed to add operation.");
			System.err.println("ERROR: failed to get resultSet.");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println("ERROR: failed to close connection.");
				}				
			}
		}
	}

	protected void update(Operation oldOperation, String sqlFormat){
		String idStr = String.valueOf(oldOperation.getId());
		String howMuch = oldOperation.getSum().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(oldOperation.getDate());
		String description = oldOperation.getDescription();
		String tags = oldOperation.getTagsStr();
		
		String sql = String.format(sqlFormat, 
									howMuch, date,
									description, tags,
									idStr);
		
		logger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			System.err.println("ERROR: failed to update operation.");
			System.err.println("ERROR: failed to get resultSet.");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println("ERROR: failed to close connection.");
				}
			}
		}
	}

	protected void delete(Operation operation, String sqlFormat){
		String id = String.valueOf(operation.getId());

		String sql = String.format(sqlFormat, id);
		logger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			System.err.println("ERROR: failed to delete operation.");
			System.err.println("ERROR: failed to get resultSet.");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println("ERROR: failed to close connection.");
				}
			}
		}
	}

}
