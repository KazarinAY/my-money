/*
 * OperationsDao
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Operation;
import kazarin.my_money.MyLogger;

import java.util.logging.Level;

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

/**
 * Provides access to the operations table in database.
 */
public class OperationsDao implements Dao<Operation>{

	

	private String user = "guest";
	private String password = "12345678";	
	private String url = "jdbc:mysql://localhost/MYMONEY";
	private String driver = "com.mysql.jdbc.Driver";
	Properties properties=new Properties();
	
	private Connection connection;
 
 	/**
	 * Constracts the OperationsDao.
 	 */
	public OperationsDao(){	
		super();		

		try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            System.err.println("ERROR: failed to find driver.");
            e.printStackTrace();
        }
        properties.setProperty("user",user);
        properties.setProperty("password",password);
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");
	}
 	
 	@Override
	public List<Operation> getAll(){
		String sql = "SELECT * FROM operations;";
		MyLogger.log(Level.INFO, "SQL: " + sql);
		List<Operation> list = new ArrayList<Operation>();
		ResultSet rs = null;
		try{	
			connection = DriverManager.getConnection(url, properties);
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
			System.err.println("ERROR: failed to get all.");
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
		MyLogger.log(Level.INFO, "SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, properties);
					
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
		
		MyLogger.log(Level.INFO, "SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, properties);
					
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
		MyLogger.log(Level.INFO, "SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, properties);
					
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
