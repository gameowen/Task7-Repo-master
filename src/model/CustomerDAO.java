package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import java.util.Random;

public class CustomerDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;

	public CustomerDAO(String jdbcDriver, String jdbcURL, String tableName)
			throws MyDAOException {
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL = jdbcURL;
		this.tableName = tableName;

		if (!tableExists())
			createTable();

	}

	private synchronized Connection getConnection() throws MyDAOException {
		if (connectionPool.size() > 0) {
			return connectionPool.remove(connectionPool.size() - 1);
		}
		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new MyDAOException(e);
		}
		try {
			return DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			throw new MyDAOException(e);
		}
	}

	private synchronized void releaseConnection(Connection con) {
		connectionPool.add(con);
	}

	public synchronized boolean create(Customer customer) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
	        PreparedStatement pstmt;
			String sqlString = "SELECT * from " + tableName + " where username=?";
			pstmt = con.prepareStatement(sqlString);
			pstmt.setString(1, customer.getUsername());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
			    rs.close();
			    pstmt.close();
			    return false;
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement("INSERT INTO " + tableName
							+ " (username, password, firstname, lastname, addr_line1, addr_line2, city, state, zip, cash, available_cash) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, customer.getUsername());
			pstmt.setString(2, customer.getPassword());
			pstmt.setString(3, customer.getFirstName());
			pstmt.setString(4, customer.getLastName());
			pstmt.setString(5, customer.getAddrL1());
			pstmt.setString(6, customer.getAddrL2());
			pstmt.setString(7, customer.getCity());
			pstmt.setString(8, customer.getState());
			pstmt.setString(9, customer.getZip() + "");
			pstmt.setLong(10, (long)(customer.getCash()*100));
			pstmt.setLong(11, (long)(customer.getAvailableCash()*100));
			int count = pstmt.executeUpdate();
			if (count != 1)
				throw new SQLException("Insert updated" + count + "rows");
			pstmt.close();
			releaseConnection(con);
		} catch (Exception e) {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) {
				throw new MyDAOException(e);
			}
		}
		return true;
	}

	public Customer lookup(String username) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			Customer customer;
			if (!rs.next()) {
				customer = null;
			} else {
				customer = new Customer();
				customer.setCustomerID(rs.getInt("customer_id"));
				customer.setPassword(rs.getString("password"));
				customer.setUsername(rs.getString("username"));
				customer.setFirstName(rs.getString("firstname"));
				customer.setLastName(rs.getString("lastname"));
				customer.setAddr1(rs.getString("addr_line1"));
				customer.setAddr2(rs.getString("addr_line2"));
				customer.setCity(rs.getString("city"));
				customer.setState(rs.getString("state"));
				customer.setZip(rs.getString("zip"));
				customer.setCash((double)rs.getLong("cash")*1.0/100.0);
				customer.setAvailableCash((double)rs.getLong("available_cash")*1.0/100.0);
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return customer;

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
	
	public Customer lookup(int customerId) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE customer_id=?");
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            Customer customer;
            if (!rs.next()) {
                customer = null;
            } else {
                customer = new Customer();
                customer.setCustomerID(rs.getInt("customer_id"));
                customer.setPassword(rs.getString("password"));
                customer.setUsername(rs.getString("username"));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setAddr1(rs.getString("addr_line1"));
                customer.setAddr2(rs.getString("addr_line2"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setZip(rs.getString("zip"));
                customer.setCash((double)rs.getLong("cash")*1.0/100.0);
                customer.setAvailableCash((double)rs.getLong("available_cash")*1.0/100.0);
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return customer;

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

	public ArrayList<Customer> search(String query) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + tableName + 
                         " WHERE username REGEXP ? " +
                         " OR firstname REGEXP ? " +
                         " OR lastname REGEXP ? ";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Customer> customers = new ArrayList<Customer>();
            Customer customer;
            while (rs.next()) {
                customer = new Customer();
                customer.setCustomerID(rs.getInt("customer_id"));
                customer.setUsername(rs.getString("username"));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));
                customer.setAddr1(rs.getString("addr_line1"));
                customer.setAddr2(rs.getString("addr_line2"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setZip(rs.getString("zip"));
                customer.setCash((double)rs.getLong("cash")*1.0/100.0);
                customer.setAvailableCash((double)rs.getLong("available_cash")*1.0/100.0);
                customers.add(customer);
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return customers;

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
	
	public void update(Customer customer) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            
            PreparedStatement pstmt = con.prepareStatement("UPDATE "  + tableName + " SET cash=?, available_cash=? WHERE username=?");
            pstmt.setLong(1, (long)(customer.getCash()*100));
            pstmt.setLong(2, (long)(customer.getAvailableCash()*100));
            pstmt.setString(3, customer.getUsername());
            pstmt.executeUpdate();
            
            pstmt.close();
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
	
	/* 
	 * Get the cash balance by providing the customer's id
	 * Author: Yuan Cao
	 */
	public double getAvailableCash(int customerId) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            
            String sql = "SELECT available_cash FROM "  + tableName + " WHERE customer_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            double cash = 0;
            if (rs.next()) {
                cash = rs.getLong("available_cash") * 1.0 / 100;
            } else {
                cash = 0;
            }
            rs.close();
            pstmt.close();
            releaseConnection(con);
            
            return cash;
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
	
	public void setPassword(String username, String password) throws MyDAOException {
		Connection con = null;		
    	try {
        	con = getConnection();
        	
        	
            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("UPDATE "  + tableName + " SET password=? WHERE username=?");
            pstmt.setString(1, password);
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

	public String resetPassword(String username) throws MyDAOException {
		Connection con = null;		
    	try {
        	con = getConnection();       	  	
            Statement stmt = con.createStatement();
            Customer customer = lookup(username);
            if(customer == null)
            	return null;
            Random random = new Random();
            String tempPassword = String.valueOf(random.nextInt(8192)+1);
            String password = hash(tempPassword);
            PreparedStatement pstmt = con.prepareStatement("UPDATE "  + tableName + " SET password=? WHERE username=?");
            pstmt.setString(1, password);
            pstmt.setString(2, username);		
			pstmt.executeUpdate();
           
            stmt.close();
            releaseConnection(con);
            return password;
        
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
	private String hash(String clearPassword) {
		

		MessageDigest md = null;
		try {
		  md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
		  throw new AssertionError("Can't find the SHA1 algorithm in the java.security package");
		}

		md.update(clearPassword.getBytes());
		byte[] digestBytes = md.digest();

		// Format the digest as a String
		StringBuffer digestSB = new StringBuffer();
		for (int i=0; i<digestBytes.length; i++) {
		  int lowNibble = digestBytes[i] & 0x0f;
		  int highNibble = (digestBytes[i]>>4) & 0x0f;
		  digestSB.append(Integer.toHexString(highNibble));
		  digestSB.append(Integer.toHexString(lowNibble));
		}
		String digestStr = digestSB.toString();

		return digestStr.substring(0, 8);
	}
	public Customer[] getCustomers() throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " ");
        	
        	ResultSet rs = pstmt.executeQuery();
            
            List<Customer> list = new ArrayList<Customer>();
            while (rs.next()) {
            	Customer customer = new Customer();
            	customer.setCustomerID(rs.getInt("customer_id"));
				customer.setUsername(rs.getString("username"));
				customer.setPassword(rs.getString("password"));
				customer.setFirstName(rs.getString("firstname"));
				customer.setLastName(rs.getString("lastname"));
				customer.setAddr1(rs.getString("addr_line1"));
				customer.setAddr2(rs.getString("addr_line2"));
				customer.setCity(rs.getString("city"));
				customer.setState(rs.getString("state"));
				customer.setZip(rs.getString("zip"));
				customer.setCash((double)rs.getLong("cash")*1.0/100.0);
                customer.setAvailableCash((double)rs.getLong("available_cash")*1.0/100.0);
            	
            	
            	list.add(customer);
            }
            stmt.close();
            releaseConnection(con);
            
            return list.toArray(new Customer[list.size()]);
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

	private boolean tableExists() throws MyDAOException {
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
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) { /* ignore */
			}
			throw new MyDAOException(e);
		}
	}

	private void createTable() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE TABLE "
					+ tableName
					+ " (customer_id INT NOT NULL AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, addr_line1 VARCHAR(255), addr_line2 VARCHAR(255), city VARCHAR(255), state VARCHAR(255), zip VARCHAR(255), cash BIGINT, available_cash BIGINT, PRIMARY KEY(customer_id))");
			stmt.close();

			releaseConnection(con);

		} catch (SQLException e) {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) { /* ignore */
			}
			throw new MyDAOException(e);
		}
	}
}
