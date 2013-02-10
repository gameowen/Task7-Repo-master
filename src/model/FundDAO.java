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
import databeans.Fund;
public class FundDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();	
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;
	
	public FundDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException{
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
	public synchronized Fund create(Fund fund) throws MyDAOException{
	    Connection con = null;
		try{
			con = getConnection();
			con.setAutoCommit(false);
        	PreparedStatement pstmt;
        	pstmt = con.prepareStatement("SELECT * from " +tableName+ " WHERE name=? OR symbol=?");
        	pstmt.setString(1, fund.getName());
            pstmt.setString(2, fund.getSymbol());   
            if(pstmt.executeQuery().next()){
                return null;
            }
            pstmt.close();
        	
        	pstmt = con.prepareStatement("INSERT INTO " + tableName + " (name, symbol) VALUES (?,?)");
			pstmt.setString(1, fund.getName());
			pstmt.setString(2, fund.getSymbol());	
			int count = pstmt.executeUpdate();
			if(count != 1) throw new SQLException("Insert updated" + count + "rows");
			pstmt.close();
			con.commit();
			releaseConnection(con);			
		}catch(Exception e){
			try{
				if(con != null)
					con.close();
			}catch(SQLException e2){
				throw new MyDAOException(e);
			}
		}
        return fund;
	}
	
	public Fund lookup(int fund_id) throws MyDAOException{
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE fund_id=?");
			pstmt.setInt(1, fund_id);
			ResultSet rs = pstmt.executeQuery();

			Fund fund;
			if (!rs.next()) {
				fund = null;
			} else {
				fund = new Fund();
				fund.setId(rs.getInt("fund_id"));
				fund.setName(rs.getString("name"));
				fund.setSymbol(rs.getString("symbol"));
				
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return fund;

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
	
	public Fund lookup(String name, String symbol) throws MyDAOException{
        Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE name=? OR symbol=?");
            pstmt.setString(1, name);
            pstmt.setString(2, symbol);
            ResultSet rs = pstmt.executeQuery();

            Fund fund;
            if (!rs.next()) {
                fund = null;
            } else {
                fund = new Fund();
                fund.setId(rs.getInt("fund_id"));
                fund.setName(rs.getString("name"));
                fund.setSymbol(rs.getString("symbol"));
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return fund;

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
	
	public ArrayList<Fund> lookup(String query) throws MyDAOException{
	    Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT * FROM " + tableName + 
                    " WHERE fund_id REGEXP ? " +
                    " OR name REGEXP ? " + 
                    " OR symbol REGEXP ?");
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            pstmt.setString(3, query);
            
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Fund> funds = new ArrayList<Fund>();
            Fund fund = null;
            while (rs.next()) {
                fund = new Fund(); 
                fund.setId(rs.getInt("fund_id"));
                fund.setName(rs.getString("name"));
                fund.setSymbol(rs.getString("symbol"));
                funds.add(fund);
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return funds;

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
            stmt.executeUpdate("CREATE TABLE " + tableName + " (fund_id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255) NOT NULL, symbol VARCHAR(255) NOT NULL, PRIMARY KEY(fund_id))");           
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}
}
