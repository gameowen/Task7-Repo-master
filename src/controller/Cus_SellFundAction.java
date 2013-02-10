package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.CustomerDAO;
import model.FundDAO;
import model.HistoryDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;

import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Fund;
import databeans.Position;
import databeans.Transaction;

import formbeans.Cus_SellFundForm;

public class Cus_SellFundAction extends Action{
	private FormBeanFactory<Cus_SellFundForm> formBeanFactory = FormBeanFactory
			.getInstance(Cus_SellFundForm.class);
	
	private TransactionDAO transactionDAO;
	private PositionDAO positionDAO;
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private HistoryDAO historyDAO;
	public Cus_SellFundAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		positionDAO = model.getPositionDAO();
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
		historyDAO = model.getHistoryDAO();
	}
	public String getName() {
		
		return "cus_sellFund.do";
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
		    
			Cus_SellFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			int customer_id = customerDAO.lookup(customer.getUsername()).getCustomerID();
			String fundName = form.getFundName();
			int fundId = Integer.parseInt(form.getFundId());
			String shares = form.getShares();
			double dShares;
			if(shares != null){
			    try {
			        dShares = Double.parseDouble(shares);
			    } catch (NumberFormatException e) {
			        errors.add("Number is invalid!");
			        Fund fund = fundDAO.lookup(fundId);
			        double totalShares = positionDAO.lookup(customer_id, fund.getId()).getShares();;
			        fund.setShares(totalShares);
	                request.setAttribute("fund", fund);                 
	                return "sell-fund-cus.jsp"; 
			    }
			}
			else
				 dShares = 0;
			
			Fund fund = fundDAO.lookup(fundId);
			double totalShares = positionDAO.lookup(customer_id, fund.getId()).getShares();;
		
			
			if (shares == null||shares.length() == 0) {			
				fund.setShares(totalShares);
				request.setAttribute("fund", fund);					
                return "sell-fund-cus.jsp";
			}
			
			if(dShares > totalShares)
				errors.add("You can't sell more than you have!");

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				fund.setShares(totalShares);
				request.setAttribute("fund", fund);	
				return "sell-fund-cus.jsp";
			}
			
			
			Transaction t = new Transaction();
			t.setCustomer_id(customer_id);
			
			int fund_id = fund.getId();
			
			
			t.setFund_id(fund_id);
			
			t.setDate(null);
			
			
			t.setTransaction_type("SELL");
			
			
			t.setShares(dShares);
			t.setStatus("PENDING");

			if(!transactionDAO.createWithUpdate_Sell(t, customer_id, fund_id, dShares)){
			    errors.add("You don't have enough shares!");
			    totalShares = positionDAO.lookup(customer_id, fund.getId()).getShares();
			    fund.setShares(totalShares);
                request.setAttribute("fund", fund); 
                return "sell-fund-cus.jsp";
            }
			
	        return "viewportfolio.do";
	  } catch (Exception e) {
      	errors.add(e.toString());
      	return "error.jsp";
      }
	}

	
	
}
