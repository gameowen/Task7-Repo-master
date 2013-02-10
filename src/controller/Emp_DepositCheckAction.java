package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Transaction;
import formbeans.Emp_DepositCheckForm;

import model.Model;
import model.TransactionDAO;
import model.CustomerDAO;
import model.MyDAOException;


public class Emp_DepositCheckAction extends Action {   
    private FormBeanFactory<Emp_DepositCheckForm> formBeanFactory = FormBeanFactory.getInstance(Emp_DepositCheckForm.class);
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;
    
    public Emp_DepositCheckAction(Model model) {
        customerDAO = model.getCustomerDAO();
        transactionDAO = model.getTransactionDAO();
    }
    
    public String getName() { return "depositcheck.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
            Emp_DepositCheckForm form = formBeanFactory.create(request);
            request.setAttribute("form",form);

            
            if (!form.isPresent()) {
                return "deposit-check-emp.jsp";
            }
            
            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "deposit-check-emp.jsp";
            }
            
            // Look up the customer
            Customer customer = customerDAO.lookup(form.getUserName());
            double amount = Double.parseDouble(form.getDeposit());
            //customer.setCash(customer.getCash() + amount);
            
            //customerDAO.updateCash(customer);
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setCustomer_id(customer.getCustomerID());
            transaction.setDate(null);
            transaction.setTransaction_type("DEPOSIT");
            transaction.setStatus("PENDING");
            transaction.setFund_id(0);
            transaction.setShares(0);
            transactionDAO.create(transaction);
            
            // Attach (this copy of) the customer object to the session
            request.setAttribute("customer",customer);
            
            return "feedback-create-deposit.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
