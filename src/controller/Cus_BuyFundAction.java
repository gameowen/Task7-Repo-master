package controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import model.Model;
import model.CustomerDAO;
import model.TransactionDAO;
import model.FundDAO;
import javax.servlet.http.HttpServletRequest;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Transaction;

import formbeans.Cus_BuyFundForm;
import formbeans.Cus_ChangePwdForm;


public class Cus_BuyFundAction extends Action{
	private FormBeanFactory<Cus_BuyFundForm> formBeanFactory = FormBeanFactory
			.getInstance(Cus_BuyFundForm.class);
	
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;
	public Cus_BuyFundAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
	}
	
	public String getName() {
		return "cus_buyFund.do";
	}
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
		    Customer customer = (Customer) request.getSession(false).getAttribute("customer");
            
            if(customer == null) {
                return "login-cus.jsp";
            }
            
            customer = customerDAO.lookup(customer.getCustomerID());
            request.getSession(false).setAttribute("customer", customer);
		    Cus_BuyFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			int customer_id = customerDAO.lookup(customer.getUsername()).getCustomerID();
			double available = customer.getAvailableCash();
			// If no params were passed, return with no errors so that the form
			// will be presented (we assume for the first time).
			if (!form.isPresent()) {
				return "buy-fund-cus.jsp";
			}
			
			
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "buy-fund-cus.jsp";
			}
			
			double amount = Double.parseDouble(form.getAmount());
            if(amount > available){
            	errors.add("You don't have enough money!");
            	return "buy-fund-cus.jsp";
            }
                
			
			Transaction t = new Transaction();
			
			t.setCustomer_id(customer_id);
			t.setFund_id(Integer.parseInt(form.getFundId()));
			//Date date = new Date();
			t.setDate(null);
			t.setTransaction_type("BUY");
			t.setStatus("PENDING");
			t.setAmount(amount);
			
			if(!transactionDAO.createWithUpdate_Buy(t, customer_id, amount)) {
			    errors.add("You don't have enough available cash!");
			    available = customer.getAvailableCash();
			    customer = customerDAO.lookup(customer.getCustomerID());
			    request.getSession().setAttribute("customer", customer);
			    return "buy-fund-cus.jsp";
			}
			
	        return "viewportfolio.do";
	  } catch (Exception e) {
      	errors.add(e.toString());
      	return "error.jsp";
      }
	}
}
