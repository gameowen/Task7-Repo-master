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

import databeans.Position;

public class PositionDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();	
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;

	public PositionDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException{
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

	public void create(Position position) throws MyDAOException{
		Connection con = null;
		try{
			con = getConnection();

        	PreparedStatement pstmt = con.prepareStatement("INSERT INTO " + tableName + " (customer_id, fund_id, shares) VALUES (?,?,?)");
			pstmt.setInt(1, position.getCustomer_id());
			pstmt.setInt(2, position.getFund_id());
			pstmt.setLong(3, (long)(position.getShares() * 1000));		
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

	public void update(Position position) throws MyDAOException{
        Connection con = null;
        try{
            con = getConnection();
            
            PreparedStatement pstmt = con.prepareStatement("UPDATE " + tableName + " SET shares=? WHERE customer_id=?, fund_id=?");
            pstmt.setLong(1, (long)(position.getShares() * 1000));
            pstmt.setInt(2, position.getCustomer_id());
            pstmt.setInt(3, position.getFund_id());
            int count = pstmt.executeUpdate();
            if(count != 1) throw new SQLException("Update updated" + count + "rows");
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

	public void remove(Position position) throws MyDAOException{
        Connection con = null;
        try{
            con = getConnection();
            
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE customer_id=?, fund_id=?");
            pstmt.setInt(1, position.getCustomer_id());
            pstmt.setInt(2, position.getFund_id());
            int count = pstmt.executeUpdate();
            if(count != 1) throw new SQLException("delete updated" + count + "rows");
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
    
	public Position lookup(int customer_id, int fund_id) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE customer_id=? and fund_id=?");
			pstmt.setInt(1, customer_id);
			pstmt.setInt(2, fund_id);			
			ResultSet rs = pstmt.executeQuery();

			Position position;
			if (!rs.next()) {
				position = null;
			} else {
				position = new Position();				
				position.setCustomer_id(rs.getInt("customer_id"));
				position.setFund_id(rs.getInt("fund_id"));
				position.setShares((double)rs.getLong("shares")*1.0/1000);

			}

			rs.close();
			pstmt.close();
			releaseConnection(con);
			return position;

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


	public ArrayList<Position> getPositionsByCustomerId(int customer_id) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
                    + tableName + " WHERE customer_id=?");
            pstmt.setInt(1, customer_id);         
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Position> positions = new ArrayList<Position>();
            Position position = null;
            while (rs.next()) {
                position = new Position();
                position.setCustomer_id(rs.getInt("customer_id"));
                position.setFund_id(rs.getInt("fund_id"));
                position.setShares((double)rs.getLong("shares")*1.0/1000);
                positions.add(position);
            }

            rs.close();
            pstmt.close();
            releaseConnection(con);
            return positions;

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
            stmt.executeUpdate("CREATE TABLE " + tableName + " (customer_id INT NOT NULL, fund_id INT NOT NULL, shares INT NOT NULL, PRIMARY KEY(customer_id, fund_id))");           
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}
}