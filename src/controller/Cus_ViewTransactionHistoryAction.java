package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import databeans.Customer;
import databeans.Fund;
import databeans.Transaction;

import model.CustomerDAO;
import model.Model;
import model.FundDAO;
import model.MyDAOException;
import model.TransactionDAO;

public class Cus_ViewTransactionHistoryAction extends Action {
	private FundDAO fundDAO;
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;

	public Cus_ViewTransactionHistoryAction(Model model) {
		fundDAO = model.getFundDAO();
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "cusviewhistory.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {

			Customer customer = (Customer) request.getSession(false)
					.getAttribute("customer");

			if (customer == null) {
				return "login-cus.jsp";
			}

			customer = customerDAO.lookup(customer.getCustomerID());
			request.getSession(false).setAttribute("customer", customer);

			if (errors.size() != 0) {
				return "error.jsp";
			}

			// Look up the fund
			int customerId = customer.getCustomerID();

			ArrayList<Transaction> transactions = transactionDAO.getTransactions(customerId);
			
			for (Transaction transaction : transactions) {
				String type = transaction.getTransaction_type();
				if (type.equalsIgnoreCase("BUY")
						|| type.equalsIgnoreCase("SELL")) {
					int fundId = transaction.getFund_id();
					Fund fund = fundDAO.lookup(fundId);
					transaction.setFundName(fund.getName());
					transaction.setFundPrice((double)(Math.round(transaction.getAmount() * 100
							/ transaction.getShares())/100));
				}// withdraw or deposit
			}

			// Attach (this copy of) the transactions object to the request
			request.setAttribute("transactions", transactions);

			return "view-transaction-history-cus.jsp";
		} catch (MyDAOException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
