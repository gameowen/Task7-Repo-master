package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

import databeans.Customer;
import databeans.Employee;

public class EmployeeDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();	
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;
	
	public EmployeeDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException{
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL = jdbcURL;
		this.tableName = tableName;
		
		if(!tableExists())
			createTable();
			
	}
	
	private synchronized Connection getConnection() throws MyDAOException{
		if(connectionPool.size() > 0){
			return connectionPool.remove(connectionPool.size() - 1);
		}
		try{
			Class.forName(jdbcDriver);
		}catch(ClassNotFoundException e){
			throw new MyDAOException(e);
		}
		try{
			return DriverManager.getConnection(jdbcURL);
		}catch(SQLException e){
			throw new MyDAOException(e);
		}
	}
	
	private synchronized void releaseConnection(Connection con){
		connectionPool.add(con);
	}
	public synchronized boolean create(Employee employee) throws MyDAOException{
		Connection con = null;
		try{
			con = getConnection();
			
			PreparedStatement pstmt;
			
			String sqlString = "SELECT * FROM " + tableName + " WHERE username=?";
            pstmt = con.prepareStatement(sqlString);
            pstmt.setString(1, employee.getUsername());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                rs.close();
                pstmt.close();
                return false;
            }
            rs.close();
            pstmt.close();
			
        	pstmt = con.prepareStatement("INSERT INTO " + tableName + " (username, password, firstname, lastname) VALUES (?,?,?,?)");
			pstmt.setString(1, employee.getUsername());
			pstmt.setString(2, employee.getPassword());
			pstmt.setString(3, employee.getFirstName());
			pstmt.setString(4, employee.getLastName());			
			int count = pstmt.executeUpdate();
			if(count != 1) throw new SQLException("Insert updated" + count + "rows");
			pstmt.close();
			releaseConnection(con);
		}catch(Exception e){
			try{
				if(con != null)
					con.close();
			}catch(SQLException e2){
				throw new MyDAOException(e);
			}
		}
		return true;
	}
	public Employee lookup(String username) throws MyDAOException{
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			Employee employee;
			if (!rs.next()) {
				employee = null;
			} else {
				employee = new Employee();				
				employee.setUsername(rs.getString("username"));
				employee.setPassword(rs.getString("password"));
				employee.setFirstName(rs.getString("firstname"));
				employee.setLastName(rs.getString("lastname"));
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return employee;

		} catch (SQLException e) {
            try { 
            	if (con != null) 
            		con.close(); 
            } 
            catch (SQLException e2) {
            	
            }
            throw new MyDAOException(e);
		}
		
		
	}
	
	public void setPassword(String username, String newPassword) throws MyDAOException{
		Connection con = null;		
    	try {
        	con = getConnection();
        	
        	
            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("UPDATE "  + tableName + " SET password=? WHERE username=?");
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);		
			pstmt.executeUpdate();
           
            stmt.close();
            releaseConnection(con);
            
        
    	} catch (SQLException e) {
            try { 
            	if (con != null) 
            		con.close(); 
            } 
            catch (SQLException e2) {
            	
            }
            throw new MyDAOException(e);
		}
		
		
	}
	public Employee[] getEmployees() throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " ");
        	
        	ResultSet rs = pstmt.executeQuery();
            
            List<Employee> list = new ArrayList<Employee>();
            while (rs.next()) {
            	Employee employee = new Employee();
            	employee.setUsername(rs.getString("username"));
				employee.setFirstName(rs.getString("firstname"));
				employee.setLastName(rs.getString("lastname"));
            	
            	
            	list.add(employee);
            }
            stmt.close();
            releaseConnection(con);
            
            return list.toArray(new Employee[list.size()]);
    	} catch (SQLException e) {
            try { 
            	if (con != null) 
            		con.close(); 
            } 
            catch (SQLException e2) {
            	
            }
            throw new MyDAOException(e);
		}
		
	}
	private boolean tableExists() throws MyDAOException{
		Connection con = null;
        try {
        	con = getConnection();
        	DatabaseMetaData metaData = con.getMetaData();
        	ResultSet rs = metaData.getTables(null, null, tableName, null);
        	
        	boolean answer = rs.next();
        	
        	rs.close();
        	releaseConnection(con);
        	
        	return answer;

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}
	private void createTable() throws MyDAOException {
		Connection con = null;
        try {
        	con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE " + tableName + " (username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, PRIMARY KEY(username))");           
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}
}
