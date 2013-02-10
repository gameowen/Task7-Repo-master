package model;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
public class Model {
	private CustomerDAO customerDAO;
	private EmployeeDAO employeeDAO;
	private TransactionDAO transactionDAO;
	private FundDAO fundDAO;
	private PositionDAO positionDAO;
	private HistoryDAO historyDAO;
	
	public Model(ServletConfig config) throws ServletException {
		
		String jdbcDriverName = config.getInitParameter("jdbcDriverName");
		String jdbcURL   = config.getInitParameter("jdbcURL");
		
		try{
			customerDAO = new CustomerDAO(jdbcDriverName, jdbcURL, "task7_customer");
			employeeDAO = new EmployeeDAO(jdbcDriverName, jdbcURL, "task7_employee");
			transactionDAO = new TransactionDAO(jdbcDriverName, jdbcURL, "task7_transaction");
			fundDAO = new FundDAO(jdbcDriverName, jdbcURL, "task7_fund");
			positionDAO = new PositionDAO(jdbcDriverName, jdbcURL, "task7_position");
			historyDAO = new HistoryDAO(jdbcDriverName, jdbcURL, "task7_history");
		}catch(Exception e){
			throw new ServletException(e);
		}
}

	public CustomerDAO getCustomerDAO() { return customerDAO; }
	public EmployeeDAO  getEmployeeDAO()  { return employeeDAO;  }
	public TransactionDAO getTransactionDAO() { return transactionDAO; }
	public FundDAO  getFundDAO()  { return fundDAO;  }
	public PositionDAO getPositionDAO() { return positionDAO; }
	public HistoryDAO  getHistoryDAO()  { return historyDAO;  }
}

