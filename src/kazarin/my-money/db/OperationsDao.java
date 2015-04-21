/*
 * OperationsDao
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Operation;
import kazarin.my_money.model.Environment;

import java.util.logging.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.io.IOException;

/**
 * Provides access to the operations table in database.
 */
public class OperationsDao implements Dao<Operation>{

	private Logger logger;

	private Environment environment;

	private String url;
	
	Properties propertiesDB = new Properties();
	
	private Connection connection;

	private boolean ok; //true when environment is good
 
 	/**
	 * Constracts the OperationsDao.
 	 */
	public OperationsDao() {	
		super();
		try {
            logger = Logger.getLogger(OperationsDao.class.getName());
            FileHandler fh = new FileHandler("/tmp/OperationsDao.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            logger.setUseParentHandlers(false);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }

		environment = Environment.getInstance();
        propertiesDB = environment.getPropertiesDB();
		url = propertiesDB.getProperty("url");
		
		try {
			logger.info("DRIVER: " + propertiesDB.getProperty("driver"));
            Class.forName(propertiesDB.getProperty("driver"));
            logger.info("DRIVER... OK");
        }catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "ERROR: failed to find driver.", e);
            ok = false;            
        }
        
        try {	
			connection = DriverManager.getConnection(url, propertiesDB);
			logger.info("CONNECTION... OK");
		} catch(SQLException e) {			
			logger.log(Level.WARNING, "ERROR: failed to get connection.", e);
			logger.log(Level.WARNING, "\turl=" + url
								+ "\n\tuser=" + propertiesDB.getProperty("user")
								+ "\n\tpassword=" + propertiesDB.getProperty("password"));
			ok = false;			
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.log(Level.WARNING, "ERROR: failed to close connection.", e);
				}				
			}
		}
		ok = true;
	}

	public boolean isOk() {
		logger.info("isOk=" + String.valueOf(ok));
		return this.ok;
	}
 	
 	@Override
	public List<Operation> getAll(){
		String sql = "SELECT * FROM operations;";
		logger.info("SQL: " + sql);
		List<Operation> list = new ArrayList<Operation>();
		ResultSet rs = null;
		try{	
			connection = DriverManager.getConnection(url, propertiesDB);
			Statement stmt = connection.createStatement();			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Operation operation = new Operation();
				operation.setId(rs.getInt("op_id"));
				operation.setHowMuch(rs.getBigDecimal("op_how_much"));
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

	@Override
	public void add(Operation operation){
		String howMuch = operation.getHowMuch().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(operation.getDate());
		String description = operation.getDescription();
		String tags = operation.getTagsStr();
		String sql = String.format("INSERT INTO operations "
									+ "(op_how_much, op_date, op_description, op_tags) "
									+ "VALUES ('%s', '%s', \"%s\", \"%s\");",
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

	@Override
	public void update(Operation oldOperation){
		String idStr = String.valueOf(oldOperation.getId());
		String howMuch = oldOperation.getHowMuch().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(oldOperation.getDate());
		String description = oldOperation.getDescription();
		String tags = oldOperation.getTagsStr();
		
		String sql = String.format("UPDATE operations SET "
									+ "op_how_much='%s', op_date='%s', "
									+ "op_description=\"%s\", op_tags=\"%s\" "
									+ "WHERE op_id='%s';", 
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

	@Override
	public void delete(Operation operation){
		String id = String.valueOf(operation.getId());
		
		String sql = String.format("DELETE FROM operations WHERE op_id='%s';", id);
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
