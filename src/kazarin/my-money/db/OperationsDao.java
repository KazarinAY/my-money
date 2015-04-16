/*
 * OperationsDao
 */
package kazarin.my_money.db;

import kazarin.my_money.model.Operation;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * Provides access to the operations table in database.
 */
public class OperationsDao extends AbstractDAO<Operation>{

	private String user = "guest";
	private String password = "12345678";	
	private String url = "jdbc:mysql://localhost/MYMONEY";
	private String driver = "com.mysql.jdbc.Driver";
	
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
	}	
 	
	@Override
	public List<Operation> getAll(){
		String sql = "SELECT * FROM operations;";
		List<Operation> list = new ArrayList<Operation>();
		ResultSet rs = null;
		try{
			
			connection = DriverManager.getConnection(url, user, password);			
			Statement stmt = connection.createStatement();			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Operation operation = new Operation();
				operation.setId(rs.getInt("op_id"));
				operation.setHowMuch(rs.getBigDecimal("op_how_much"));
				operation.setDate(rs.getDate("op_date"));
				operation.setDescription(rs.getString("op_description"));
				operation.setTags(rs.getString("op_date").split(","));
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
	/*	String sql = "INSERT INTO operations (number, faculty) " +
					 "VALUES (" + operation.getNumber() + ", '" + operation.getFaculty() + "');";
		getResultSet(sql);*/
	}
	
	@Override
	public void update(Operation oldOperation, Operation newGroup){
	/*	String sql = "UPDATE operations SET number='" + newGroup.getNumber() + "', faculty='" + newGroup.getFaculty() + 
					 "' WHERE number='" + oldOperation.getNumber() + "' AND faculty='" + oldOperation.getFaculty() + "';";
		getResultSet(sql);	*/			
	}
	
	@Override
	public void delete(Operation operation){
	/*	String sql = "DELETE FROM operations WHERE number='" + operation.getNumber() + "' AND faculty='" + group.getFaculty() + "';";
		getResultSet(sql);	*/
	}	

	
}
