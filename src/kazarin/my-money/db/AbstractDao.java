package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

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

public abstract class AbstractDao implements Dao<Operation> {

	protected Properties propertiesDB;
	protected Connection connection;
	protected String url;

	/**
	 * Constracts the AbstractDao.
 	 */
	public AbstractDao(Properties propertiesDB) throws DaoException {
		this.propertiesDB = propertiesDB;
		url = propertiesDB.getProperty("url");
		
		try {
			DBLogger.info("DRIVER: " + propertiesDB.getProperty("driver"));
            Class.forName(propertiesDB.getProperty("driver"));
            DBLogger.info("DRIVER... OK");
        }catch (ClassNotFoundException e) {
            DBLogger.warning("Failed to find driver.", e);
            throw new DaoException("Failed to find driver.");
        }
        
        try {	
			connection = DriverManager.getConnection(url, propertiesDB);
			DBLogger.info("CONNECTION... OK");
		} catch(SQLException e) {			
			DBLogger.warning("Failed to get connection.", e);
			DBLogger.warning("\turl=" + url
						+ "\n\tuser=" + propertiesDB.getProperty("user")
						+ "\n\tpassword=" + propertiesDB.getProperty("password"));
			throw new DaoException("Failed to get connectio.");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
				}				
			}
		}
		
	}

	@Override
	public List<Operation> getAll(){
		String sql = "SELECT * FROM operations;";
		DBLogger.info("SQL: " + sql);
		List<Operation> list = new ArrayList<Operation>();
		ResultSet rs = null;
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
			DBLogger.info("CONNECTION... OK");
			Statement stmt = connection.createStatement();
			DBLogger.info("Statement... OK");
			rs = stmt.executeQuery(sql);
			DBLogger.info("ResultSet... OK");
			
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
			DBLogger.warning("Failed to get all.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
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
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();			
			stmt.executeUpdate(sql);			

		} catch(SQLException e){
			DBLogger.warning("RROR: Failed to add operation.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
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
		
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to update operation.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
				}
			}
		}
	}

	protected void delete(Operation operation, String sqlFormat){
		String id = String.valueOf(operation.getId());

		String sql = String.format(sqlFormat, id);
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to delete operation.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
				}
			}
		}
	}

	public void createDB(String dbName, String sqlFormat, String createTable) {
		String sql = String.format(sqlFormat, dbName);
		DBLogger.info("SQL: " + sql);
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to createDB.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
				}
			}
		}
		sql = createTable;
		DBLogger.info("SQL: " + sql);
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to create table.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBLogger.warning("Failed to close connection.", e);
				}
			}
		}
	}

}
