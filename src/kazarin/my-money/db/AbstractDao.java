package kazarin.my_money.db;

import kazarin.my_money.model.Entry;

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
/*
 * Depends on:  model/Entry.java
 * 				db/Dao.java
 */
public abstract class AbstractDao implements Dao<Entry> {

	private Properties propertiesDB;
	private Connection connection;
	private String url;
	private String driver;
	private String user;
	private String password;

	/**
	 * Constracts the AbstractDao.
 	 */
	public AbstractDao(Properties propertiesDB) {
		this.propertiesDB = propertiesDB;
		url = propertiesDB.getProperty("url");
		driver = propertiesDB.getProperty("driver");
		user = propertiesDB.getProperty("user");
		password = propertiesDB.getProperty("password");
		
		try {
			
			DBLogger.info("DRIVER: " + driver);
            Class.forName(driver);
            DBLogger.info("DRIVER... OK");

            connection = DriverManager.getConnection(url, propertiesDB);
			DBLogger.info("CONNECTION... OK");
        
        } catch (ClassNotFoundException e) {
            DBLogger.warning("Failed to find driver.", e);
            throw new DaoException("Failed to find driver.", e);
        } catch(SQLException e) {			
			DBLogger.warning("Failed to get connection.", e);
			DBLogger.warning("\turl=" + url
						+ "\n\tuser=" + user
						+ "\n\tpassword=" + password);
			throw new DaoException("Failed to get connection.", e);
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
	public List<Entry> getAll() {
		String sql = "SELECT * FROM operations;";
		DBLogger.info("SQL: " + sql);
		List<Entry> list = new ArrayList<Entry>();
		ResultSet rs = null;
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
			DBLogger.info("CONNECTION... OK");
			Statement stmt = connection.createStatement();
			DBLogger.info("Statement... OK");
			rs = stmt.executeQuery(sql);
			DBLogger.info("ResultSet... OK");
			
			while(rs.next()){
				Entry entry = new Entry();
				entry.setId(rs.getInt("op_id"));
				entry.setSum(rs.getBigDecimal("op_sum"));
				entry.setDate(rs.getDate("op_date"));
				entry.setDescription(rs.getString("op_description"));
				entry.setTags(rs.getString("op_tags").split(","));
				list.add(entry);
			}
		} catch(SQLException e){
			DBLogger.warning("Failed to get all.", e);
			throw new DaoException("Failed to get all.", e);
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

	protected void add(Entry entry, String sqlFormat) {
		String sum = entry.getSum().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(entry.getDate());
		String description = entry.getDescription();
		String tags = entry.getTagsStr();
		String sql = String.format(	sqlFormat,
									sum, date, description, tags);
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();			
			stmt.executeUpdate(sql);			

		} catch(SQLException e){
			DBLogger.warning("Failed to add entry.", e);
			throw new DaoException("Failed to add entry.");
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

	protected void update(Entry oldEntry, String sqlFormat){
		String idStr = String.valueOf(oldEntry.getId());
		String sum = oldEntry.getSum().toString();
		SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String date = dateFormat.format(oldEntry.getDate());
		String description = oldEntry.getDescription();
		String tags = oldEntry.getTagsStr();
		
		String sql = String.format(sqlFormat, 
									sum, date,
									description, tags,
									idStr);
		
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to update entry.", e);
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

	protected void delete(Entry entry, String sqlFormat){
		String id = String.valueOf(entry.getId());

		String sql = String.format(sqlFormat, id);
		DBLogger.info("SQL: " + sql);
		
		try{
			connection = DriverManager.getConnection(url, propertiesDB);
					
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			DBLogger.warning("Failed to delete entry.", e);
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

	public void createDB(String dbName, String sqlFormat,
								String createTable) {

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
			DBLogger.warning("Failed to create table.");			
			throw new DaoException(e.getMessage());
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
