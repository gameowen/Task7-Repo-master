package controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Fund;
import databeans.Portfolio;
import databeans.Position;
import databeans.Transaction;
import formbeans.Emp_ViewCustomerForm;

import model.FundDAO;
import model.HistoryDAO;
import model.Model;
import model.CustomerDAO;
import model.MyDAOException;
import model.PositionDAO;
import model.TransactionDAO;


public class Emp_ViewCustomerAccountAction extends Action {   
    private FormBeanFactory<Emp_ViewCustomerForm> formBeanFactory = FormBeanFactory.getInstance(Emp_ViewCustomerForm.class);
    private CustomerDAO customerDAO;
    private PositionDAO positionDAO;
    private FundDAO fundDAO;
    private HistoryDAO historyDAO;
    private TransactionDAO transactionDAO;
    
    public Emp_ViewCustomerAccountAction(Model model) {
        customerDAO = model.getCustomerDAO();
        positionDAO = model.getPositionDAO();
        fundDAO = model.getFundDAO();
        historyDAO = model.getHistoryDAO();
        transactionDAO = model.getTransactionDAO();
    }
    
    public String getName() { return "viewcustomeraccount.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
            Emp_ViewCustomerForm form = formBeanFactory.create(request);
            request.setAttribute("form",form);
            
            if (!form.isPresent()) {
                //return "manage-customers-emp.jsp";
            }
            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "manage-customers-emp.jsp";
            }
            
            // Look up the customer
            Customer customer = (Customer) request.getAttribute("customer");
            
            if (customer == null)
                customer = customerDAO.lookup(form.getUserName());
            
            if (customer == null)
                customer = (Customer) request.getSession().getAttribute("cus");
            
            if (customer == null) {
                errors.add("User not found");
                return "manage-customers-emp.jsp";
            }
            
            customer = customerDAO.lookup(customer.getCustomerID());
            request.getSession().setAttribute("cus", customer);
            
            int customerId = customer.getCustomerID();
            ArrayList<Position> positions = positionDAO.getPositionsByCustomerId(customerId);
            
            ArrayList<Portfolio> portfolios = new ArrayList<Portfolio>();
            for (Position position : positions) {
                int fundId = position.getFund_id();
                double shares = position.getShares();
                Fund fund = fundDAO.lookup(fundId);
                String fundName = fund.getName();
                String fundSymbol = fund.getSymbol();
                Date date = new Date(0);
                double price = historyDAO.lookupLatestPriceAndDate(fundId, date);
                
                Portfolio portfolio = new Portfolio();
                portfolio.setFundId(fundId);
                portfolio.setFundName(fundName);
                portfolio.setShares(shares);
                portfolio.setPrice(price);
                portfolio.setTotal(shares * price);
                
                portfolios.add(portfolio);
            }
            
            Transaction transaction = transactionDAO.getLastTransaction(customerId);
            java.util.Date date;
            if (transaction!=null){
                date = transaction.getExecute_date();
            } else {
                date = null;
            }
            
            // Attach (this copy of) the funds object to the session
            request.setAttribute("portfolios",portfolios);
            request.setAttribute("lastExecuteDate",date);
            
            // Attach (this copy of) the user bean to the session
            HttpSession session = request.getSession();
            session.setAttribute("cus",customer);
          
            return "view-customer-acct-emp.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
