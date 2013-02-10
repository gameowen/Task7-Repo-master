package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

import databeans.History;

public class HistoryDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();	
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;
	
	public HistoryDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException{
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
	public void create(History history) throws MyDAOException{
		Connection con = null;
		try{
			con = getConnection();
			
        	PreparedStatement pstmt = con.prepareStatement("INSERT INTO " + tableName + " (fund_id, price_date, price) VALUES (?,?,?)");
			pstmt.setInt(1, history.getId());
			pstmt.setDate(2, history.getDate());
			long int_price = (long) (history.getPrice() * 100);
			pstmt.setLong(3, int_price);					
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
	}
	
	public History lookup(int fund_id, Date priceDate) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE fund_id=?, price_date=?");
			pstmt.setInt(1, fund_id);
			pstmt.setDate(2, priceDate);
			ResultSet rs = pstmt.executeQuery();

			History history;
			if (!rs.next()) {
				history = null;
			} else {
				history = new History();				
				history.setId(rs.getInt("fund_id"));
				history.setDate(rs.getDate("price_date"));
				history.setPrice((double)rs.getLong("price")*1.0/100);
				
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return history;

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
	
	public ArrayList<History> lookup(int fund_id) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + tableName + 
                         " WHERE fund_id=? ORDER BY price_date ASC";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, fund_id);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<History> histories = new ArrayList<History>();
            History history = null;
            while (rs.next()) {
                history = new History();                
                history.setId(rs.getInt("fund_id"));
                history.setDate(rs.getDate("price_date"));
                history.setPrice((double)rs.getLong("price")*1.0/100);
                histories.add(history);
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return histories;

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
	
	public double lookupLatestPriceAndDate(int fund_id, java.util.Date date2) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();
            String sql = "SELECT price_date, price from " + tableName + " where fund_id=? " +
                    " AND price_date=(SELECT max(price_date) from " + tableName + " where fund_id=?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, fund_id);
            pstmt.setInt(2, fund_id);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                rs.close();
                pstmt.close();
                releaseConnection(con);
                return 0.0;
            } else {
                Date date = rs.getDate("price_date");
                if(date!=null)
                    date2.setTime(date.getTime());
                double price = (double)rs.getLong("price")*1.0/100;
                
                rs.close();
                pstmt.close();
                releaseConnection(con);
                
                return price;
            }

            
            

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
            stmt.executeUpdate("CREATE TABLE " + tableName + " (fund_id INT NOT NULL, price_date DATE NOT NULL, price BIGINT NOT NULL, PRIMARY KEY(fund_id, price_date))");           
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}
}
