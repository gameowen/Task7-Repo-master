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

import org.apache.catalina.Session;

import com.mysql.jdbc.BalanceStrategy;



import databeans.Customer;
import databeans.Fund;
import databeans.History;
import databeans.Position;
import databeans.Transaction;

public class TransactionDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();	
	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;

	public TransactionDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException{
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

	public synchronized boolean createForRequestCheck(Transaction transaction) throws MyDAOException{
		Connection con = null;
		try{
			con = getConnection();

			PreparedStatement pstmt;
            pstmt = con.prepareStatement("SELECT * FROM task7_customer WHERE customer_id=?");
            pstmt.setInt(1, transaction.getCustomer_id());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            double newAvailable = rs.getLong("available_cash")*1.0/100 - transaction.getAmount();
            if(newAvailable<0) {
                rs.close();
                pstmt.close();
                return false;
            }
            rs.close();
            pstmt.close();
			
        	pstmt = con.prepareStatement("INSERT INTO " + tableName + " (customer_id, fund_id, execute_date, shares, transaction_type, amount, status) VALUES (?,?,?,?,?,?,?)");
			pstmt.setInt(1, transaction.getCustomer_id());
			pstmt.setInt(2, transaction.getFund_id());
			pstmt.setDate(3, (Date) transaction.getExecute_date());
			pstmt.setLong(4, (long)(transaction.getShares() * 1000));	
			pstmt.setString(5, transaction.getTransaction_type());
			pstmt.setLong(6, (long)(transaction.getAmount()*100));
			pstmt.setString(7, transaction.getStatus());
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
	
	public void create(Transaction transaction) throws MyDAOException{
        Connection con = null;
        try{
            con = getConnection();

            PreparedStatement pstmt;
            
            
            pstmt = con.prepareStatement("INSERT INTO " + tableName + " (customer_id, fund_id, execute_date, shares, transaction_type, amount, status) VALUES (?,?,?,?,?,?,?)");
            pstmt.setInt(1, transaction.getCustomer_id());
            pstmt.setInt(2, transaction.getFund_id());
            pstmt.setDate(3, (Date) transaction.getExecute_date());
            pstmt.setLong(4, (long)(transaction.getShares() * 1000));   
            pstmt.setString(5, transaction.getTransaction_type());
            pstmt.setLong(6, (long)(transaction.getAmount()*100));
            pstmt.setString(7, transaction.getStatus());
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
	
	public synchronized boolean createWithUpdate_Buy(Transaction transaction, int customer_id, double available) {
		Connection con = null;
		try{

			con = getConnection();
			con.setAutoCommit(false);

			PreparedStatement pstmt;
            pstmt = con.prepareStatement("SELECT * FROM task7_customer WHERE customer_id=?");
            pstmt.setInt(1, customer_id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            double newAvailable = rs.getLong("available_cash")*1.0/100 - available;
            if(newAvailable<0) {
                rs.close();
                pstmt.close();
                return false;
            }
            rs.close();
            pstmt.close();
			
        	pstmt = con.prepareStatement("INSERT INTO " + tableName + " (customer_id, fund_id, execute_date, shares, transaction_type, amount, status) VALUES (?,?,?,?,?,?,?)");
			pstmt.setInt(1, transaction.getCustomer_id());
			pstmt.setInt(2, transaction.getFund_id());
			pstmt.setDate(3, (Date) transaction.getExecute_date());
			pstmt.setLong(4, (long)(transaction.getShares() * 1000));	
			pstmt.setString(5, transaction.getTransaction_type());
			pstmt.setLong(6, (long)(Math.round(transaction.getAmount()*100)));
			pstmt.setString(7, transaction.getStatus());
			int count = pstmt.executeUpdate();
			if(count != 1) throw new SQLException("Insert updated" + count + "rows");
			pstmt.close();

			PreparedStatement pstmt2 = con.prepareStatement("UPDATE task7_customer SET available_cash=? WHERE customer_id=?");
            pstmt2.setLong(1, (long)(newAvailable*100));
            pstmt2.setInt(2, customer_id);
            pstmt2.executeUpdate();     
            pstmt2.close();

            con.commit();
			releaseConnection(con);
		}catch(Exception e){
				try{
					if(con != null)
						con.close();
				}catch(SQLException e2){
					try {
						throw new MyDAOException(e);
					} catch (MyDAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		}
		return true;
	}
	public synchronized boolean createWithUpdate_Sell(Transaction transaction, int customer_id, int fund_id, double shares) {
		Connection con = null;
		try{
			con = getConnection();
			con.setAutoCommit(false);
			
			PreparedStatement pstmt;
			pstmt = con.prepareStatement("SELECT * FROM task7_position WHERE customer_id=?");
			pstmt.setInt(1, customer_id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			double newShares = rs.getLong("shares")*1.0/1000 - shares;
			if(newShares<0) {
			    rs.close();
			    pstmt.close();
			    return false;
			}
			rs.close();
			pstmt.close();
			

        	pstmt = con.prepareStatement("INSERT INTO " + tableName + " (customer_id, fund_id, execute_date, shares, transaction_type, amount, status) VALUES (?,?,?,?,?,?,?)");
			pstmt.setInt(1, transaction.getCustomer_id());
			pstmt.setInt(2, transaction.getFund_id());
			pstmt.setDate(3, (Date) transaction.getExecute_date());
			pstmt.setLong(4, (long)(transaction.getShares() * 1000));	
			pstmt.setString(5, transaction.getTransaction_type());
			pstmt.setLong(6, (long)(transaction.getAmount()*100));
			pstmt.setString(7, transaction.getStatus());
			int count = pstmt.executeUpdate();
			if(count != 1) throw new SQLException("Insert updated" + count + "rows");
			pstmt.close();


			/*double newAvailable = available - sellAmount;
			
			PreparedStatement pstmt2 = con.prepareStatement("UPDATE task7_customer SET available_cash=? WHERE customer_id=?");
            pstmt2.setInt(1, (int)(newAvailable*100));
            pstmt2.setInt(2, customer_id);
            pstmt2.executeUpdate();     
            pstmt2.close();*/

            PreparedStatement pstmt2 = con.prepareStatement("UPDATE task7_position SET shares=? WHERE customer_id=? and fund_id=?");
            pstmt2.setLong(1, (long)(newShares*1000));
            pstmt2.setInt(2, customer_id);
			pstmt2.setInt(3, fund_id);
			pstmt2.executeUpdate(); 
		    pstmt2.close();

			con.commit();
			releaseConnection(con);
		}catch(Exception e){
			try{
				if(con != null)
					con.close();
			}catch(SQLException e2){
				try {
					throw new MyDAOException(e);
				} catch (MyDAOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return true;
	}

	public ArrayList<Transaction> getTransactions(int customer_id) throws MyDAOException {
		Connection con = null;
    	try {
        	con = getConnection();

            Statement stmt = con.createStatement();
            String sql = "select * from "+tableName+" where customer_id=? order by (CASE when status='PENDING' THEN 1 ELSE 2 END), execute_date DESC";
            PreparedStatement pstmt = con.prepareStatement(sql);
        	pstmt.setInt(1, customer_id);
            
        	ResultSet rs = pstmt.executeQuery();
            
        	ArrayList<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
            	Transaction transaction = new Transaction();
            	transaction = new Transaction();
				transaction.setTransaction_id(rs.getInt("transaction_id"));
				transaction.setCustomer_id(rs.getInt("customer_id"));
				transaction.setFund_id(rs.getInt("fund_id"));
				transaction.setDate(rs.getDate("execute_date"));
				transaction.setShares((double)rs.getLong("shares")*1.0/1000);
				transaction.setTransaction_type(rs.getString("transaction_type"));
				transaction.setAmount((double)rs.getLong("amount")*1.0/100);
            	transaction.setStatus(rs.getString("status"));
            	
            	list.add(transaction);
            }
            stmt.close();
            releaseConnection(con);
            
            return list;
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

	public Transaction getLastTransaction(int customer_id) throws MyDAOException {
        Connection con = null;
        try {
            con = getConnection();

            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " where customer_id=? AND execute_date=(SELECT max(execute_date) from "+tableName+" where customer_id=?)");
            pstmt.setInt(1, customer_id);
            pstmt.setInt(2, customer_id);
            
            ResultSet rs = pstmt.executeQuery();
            
            Transaction transaction;;
            if (!rs.next()) {
                transaction = null;
            } else {
                transaction = new Transaction();
                transaction.setTransaction_id(rs.getInt("transaction_id"));
                transaction.setCustomer_id(rs.getInt("customer_id"));
                transaction.setFund_id(rs.getInt("fund_id"));
                transaction.setDate(rs.getDate("execute_date"));
                transaction.setShares((double)rs.getLong("shares")*1.0/1000);
                transaction.setTransaction_type(rs.getString("transaction_type"));
                transaction.setAmount((double)rs.getLong("amount")*1.0/100);                
            }
            stmt.close();
            releaseConnection(con);
            
            return transaction;
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

	public ArrayList<Transaction> getPendingTransactions() throws MyDAOException{
	    Connection con = null;
        try {
            con = getConnection();

            Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " where status=?");
            pstmt.setString(1, "PENDING");
            
            ResultSet rs = pstmt.executeQuery();
            
            ArrayList<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction = new Transaction();
                transaction.setTransaction_id(rs.getInt("transaction_id"));
                transaction.setCustomer_id(rs.getInt("customer_id"));
                transaction.setFund_id(rs.getInt("fund_id"));
                transaction.setDate(rs.getDate("execute_date"));
                transaction.setShares((double)rs.getLong("shares")*1.0/1000);
                transaction.setTransaction_type(rs.getString("transaction_type"));
                transaction.setAmount((double)rs.getLong("amount")*1.0/100);
                transaction.setStatus(rs.getString("status"));
                
                list.add(transaction);
            }
            stmt.close();
            releaseConnection(con);
            
            return list;
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

	public void updateTransaction(Transaction transaction) throws MyDAOException{
	    Connection con = null;
        try {
            con = getConnection();
            
            PreparedStatement pstmt = con.prepareStatement("UPDATE "  + tableName + " SET execute_date=?, status=? WHERE transaction_id=?");
            pstmt.setDate(1, new Date((transaction.getExecute_date().getTime())));
            pstmt.setString(2, transaction.getStatus());
            pstmt.setInt(3, transaction.getTransaction_id());
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
	
	public synchronized boolean transitionDay(ArrayList<Integer> fundIds, ArrayList<Double> prices, Date transitionDay, ArrayList<Transaction> transactions) throws MyDAOException {
	    Connection con = null;
	    try{
            con = getConnection();
            con.setAutoCommit(false);

            PreparedStatement pstmt;
            ResultSet rs;
            Customer customer;
            
            
            pstmt = con.prepareStatement("SELECT max(price_date) from task7_history");
            rs = pstmt.executeQuery(); 
            if(rs.next() && rs.getDate("max(price_date)")!=null && rs.getDate("max(price_date)").getTime() >= transitionDay.getTime()) {
                rs.close();
                pstmt.close();
                return false;
            }
            rs.close();
            pstmt.close();
            
            pstmt = con.prepareStatement("SELECT * FROM task7_fund");
            rs = pstmt.executeQuery();
            int cnt=0;
            while(rs.next())
                cnt ++;
            if(cnt>fundIds.size())
                return false;
            
            //update price history
            String sql = "INSERT INTO task7_history (fund_id, price_date, price) VALUES (?,?,?)";
            for (int i=0; i<fundIds.size(); i++) {
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, fundIds.get(i));
                pstmt.setDate(2, transitionDay);
                long int_price = (long) (prices.get(i) * 100);
                pstmt.setLong(3, int_price);
                int count = pstmt.executeUpdate();
                if(count != 1) throw new SQLException("Insert updated" + count + "rows");
            }
            
            for (Transaction transaction : transactions) {
                transaction.setDate(transitionDay);
                int customerId = transaction.getCustomer_id();
                
                //get the fund of the transaction is available
                int fundId = transaction.getFund_id();
                
                //get the type of the transaction
                String type = transaction.getTransaction_type();
                
                //get the transaction amount
                double amount = transaction.getAmount();
                amount = Math.round(amount*100)*1.0/100;
                
                //get the number of shares from the transaction
                double shares = transaction.getShares();
                shares = Math.round(shares*1000)*1.0/1000;
                
                //get the customer associated with this transaction
                pstmt = con.prepareStatement("SELECT * FROM task7_customer WHERE customer_id=?");
                pstmt.setInt(1, customerId);
                rs = pstmt.executeQuery();
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
                
                //get the latest price
                sql = "SELECT price_date, price from task7_history where fund_id=? " +
                        " AND price_date=(SELECT max(price_date) from task7_history where fund_id=?)";
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, fundId);
                pstmt.setInt(2, fundId);
                rs = pstmt.executeQuery();
                double price = -1.0;
                if (rs.next()) {
                    price = (double)(rs.getLong("price")*1.0/100);
                }
                rs.close();
                pstmt.close();
                
                if (type.equalsIgnoreCase("BUY")) {
                    //update position table
                    shares = Math.round(amount * 1000 / price)*1.0/1000;
                    if(shares<0.001 || price<=0) {
                        transaction.setStatus("DENIED");
                        transaction.setFundPrice(price);
                        transaction.setShares(shares);
                        transaction.setAmount(amount);
                        //return the money
                        sql = "UPDATE task7_customer SET available_cash=? WHERE customer_id=?";
                        pstmt = con.prepareStatement(sql);
                        pstmt.setLong(1, (long) ((customer.getCash())*100));
                        pstmt.setInt(2, customerId);
                        int count = pstmt.executeUpdate();
                        if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                        pstmt.close();
                    } else {
                        double balance = customer.getCash();
                        transaction.setStatus("APPROVED");
                        transaction.setFundPrice(price);
                        transaction.setShares(shares);
                        transaction.setAmount(amount);
                        pstmt = con.prepareStatement("UPDATE task7_customer SET cash=?, available_cash=? WHERE customer_id=?");
                        pstmt.setLong(1, (long)((balance - amount)*100));
                        pstmt.setLong(2, (long)((balance - amount)*100));
                        pstmt.setInt(3, customerId);
                        pstmt.executeUpdate();                    
                        pstmt.close();
                        
                        //check if the position exists
                        pstmt = con.prepareStatement("SELECT * FROM task7_position WHERE customer_id=? AND fund_id=?");
                        pstmt.setInt(1, customerId);
                        pstmt.setInt(2, fundId);
                        rs = pstmt.executeQuery();
                        
                        if (!rs.next()) {
                            PreparedStatement pstmt2;
                            pstmt2 = con.prepareStatement("INSERT INTO task7_position (customer_id, fund_id, shares) VALUES (?,?,?)");
                            pstmt2.setInt(1, customerId);
                            pstmt2.setInt(2, fundId);
                            pstmt2.setLong(3, (long)(shares * 1000));      
                            int count = pstmt2.executeUpdate();
                            if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                            pstmt2.close();
                        } else {
                            PreparedStatement pstmt2;
                            sql = "UPDATE task7_position SET shares=? WHERE customer_id=? AND fund_id=?";
                            pstmt2 = con.prepareStatement(sql);
                            pstmt2.setLong(1, (long)( shares*1000 + rs.getLong("shares")));
                            pstmt2.setInt(2, customerId);
                            pstmt2.setInt(3, fundId);
                            int count = pstmt2.executeUpdate();
                            if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                            pstmt2.close();
                            
                        }
                        rs.close();
                        pstmt.close();

                    }
                } else if (type.equalsIgnoreCase("SELL")) {
                    amount = shares * price;
                    transaction.setStatus("APPROVED");
                    transaction.setFundPrice(price);
                    transaction.setShares(shares);
                    transaction.setAmount(amount);
                    
                    //update position table
                    pstmt = con.prepareStatement("SELECT * FROM task7_position WHERE customer_id=? AND fund_id=?");
                    pstmt.setInt(1, customerId);
                    pstmt.setInt(2, fundId);
                    rs = pstmt.executeQuery();
                    double availableShares = 0;
                    if(rs.next())
                        availableShares = ((double)rs.getLong("shares")*1.0)/1000.0;
                    rs.close();
                    pstmt.close();
                    
                    sql = "UPDATE task7_customer SET cash=?, available_cash=? WHERE customer_id=?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setLong(1, (long)((amount + customer.getCash())*100));
                    pstmt.setLong(2, (long)( (amount + customer.getCash())*100));
                    pstmt.setInt(3, customerId);
                    int count = pstmt.executeUpdate();
                    if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                    pstmt.close();
                    
                    if(availableShares < 0.001) {
                        // Sold out all shares
                        pstmt = con.prepareStatement("DELETE FROM task7_position WHERE customer_id=? AND fund_id=?");
                        pstmt.setInt(1, customerId);
                        pstmt.setInt(2, fundId);
                        count = pstmt.executeUpdate();
                        if(count != 1) throw new SQLException("delete updated" + count + "rows");
                        pstmt.close();
                    }
                } else if (type.equalsIgnoreCase("WITHDRAW")) {
                    sql = "UPDATE task7_customer SET cash=?, available_cash=? WHERE customer_id=?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setLong(1, (long) ((customer.getCash() - amount)*100));
                    pstmt.setLong(2, (long) ((customer.getCash() - amount)*100));
                    pstmt.setInt(3, customerId);
                    int count = pstmt.executeUpdate();
                    if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                    pstmt.close();
                    transaction.setStatus("APPROVED");
                    transaction.setFundPrice(price);
                    transaction.setShares(shares);
                    transaction.setAmount(amount);
                } else if (type.equalsIgnoreCase("DEPOSIT")) {
                    //update the balance
                    sql = "UPDATE task7_customer SET cash=?, available_cash=? WHERE customer_id=?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setLong(1, (long) ((amount + customer.getCash())*100));
                    pstmt.setLong(2, (long) ((amount + customer.getCash())*100));
                    pstmt.setInt(3, customerId);
                    int count = pstmt.executeUpdate();
                    if(count != 1) throw new SQLException("Insert updated" + count + "rows");
                    pstmt.close();
                    transaction.setStatus("APPROVED");
                    transaction.setFundPrice(price);
                    transaction.setShares(shares);
                    transaction.setAmount(amount);
                } else {
                    System.out.println("Unknown Type of transaction");
                }
                pstmt = con.prepareStatement("UPDATE "  + tableName + " SET execute_date=?, status=?, shares=?, amount=? WHERE transaction_id=?");
                pstmt.setDate(1, new Date((transaction.getExecute_date().getTime())));
                pstmt.setString(2, transaction.getStatus());
                pstmt.setLong(3, (long)(transaction.getShares()*1000));
                pstmt.setLong(4, (long)(transaction.getAmount()*100));
                pstmt.setInt(5, transaction.getTransaction_id());
                int count = pstmt.executeUpdate();
                if(count != 1) throw new SQLException("update updated" + count + "rows");
                pstmt.close();
            }

            con.commit();
            releaseConnection(con);
        }catch(Exception e){
            try{
                if(con != null)
                    con.close();
            }catch(SQLException e2){
                try {
                    throw new MyDAOException(e);
                } catch (MyDAOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
	    return true;
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
            stmt.executeUpdate("CREATE TABLE " + tableName + " (transaction_id INT NOT NULL AUTO_INCREMENT, customer_id INT NOT NULL, fund_id INT NOT NULL, execute_date DATE, shares BIGINT, transaction_type VARCHAR(255), amount BIGINT, status VARCHAR(255), PRIMARY KEY(transaction_id))");           
            stmt.close();
        	
        	releaseConnection(con);

        } catch (SQLException e) {
            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
        	throw new MyDAOException(e);
        }
	}

}
