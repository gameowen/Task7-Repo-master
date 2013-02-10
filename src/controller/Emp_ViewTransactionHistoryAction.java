package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Fund;
import databeans.Transaction;
import formbeans.Cus_FundSearchForm;
import formbeans.Emp_ViewCustomerForm;

import model.CustomerDAO;
import model.Model;
import model.FundDAO;
import model.MyDAOException;
import model.TransactionDAO;


public class Emp_ViewTransactionHistoryAction extends Action {   
    private FormBeanFactory<Emp_ViewCustomerForm> formBeanFactory = FormBeanFactory.getInstance(Emp_ViewCustomerForm.class);
    private FundDAO fundDAO;
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;
    
    public Emp_ViewTransactionHistoryAction(Model model) {
        fundDAO = model.getFundDAO();
        transactionDAO = model.getTransactionDAO();
        customerDAO = model.getCustomerDAO();
    }
    
    public String getName() { return "empviewhistory.do"; }
    
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
            
            // Look up the Customer
            //Customer customer = customerDAO.lookup(form.getUserName());
            Customer customer = (Customer) request.getSession(false).getAttribute("cus");
            if (customer == null) {
                errors.add("No such customer with user name");
                return "error.jsp";
            }
            
            if (errors.size() != 0) {
                return "error.jsp";
            }
            
            int customerId = customer.getCustomerID();
            ArrayList<Transaction> transactions = transactionDAO.getTransactions(customerId);
            
            for (Transaction transaction : transactions) {
                String type = transaction.getTransaction_type();
                if (type.equalsIgnoreCase("BUY") || type.equalsIgnoreCase("SELL")) {
                    int fundId = transaction.getFund_id();
                    Fund fund = fundDAO.lookup(fundId);
                    transaction.setFundName(fund.getName());
                    transaction.setFundPrice(transaction.getAmount() / transaction.getShares());
                }//withdraw or deposit
            }
            // Attach (this copy of) the transactions object to the request
            request.setAttribute("transactions",transactions);
            
            return "view-transaction-history-emp.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
