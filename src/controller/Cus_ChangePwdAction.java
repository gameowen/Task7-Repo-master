package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.CustomerDAO;
import model.Model;

import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import formbeans.Cus_ChangePwdForm;

public class Cus_ChangePwdAction extends Action {
	private FormBeanFactory<Cus_ChangePwdForm> formBeanFactory = FormBeanFactory
			.getInstance(Cus_ChangePwdForm.class);

	private CustomerDAO customerDAO;

	public Cus_ChangePwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "cus_changePwd.do";
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
		    
		    Cus_ChangePwdForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			// If no params were passed, return with no errors so that the form
			// will be presented (we assume for the first time).
			if (!form.isPresent()) {
				return "change-pwd-cus.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				System.out.println(errors.toString());
				return "change-pwd-cus.jsp";
			}
			
			customerDAO.setPassword(customer.getUsername(), form.getNewPassword());
			
			request.setAttribute("message","Password changed for "+customer.getCustomerID());
	        return "viewportfolio.do";
	  } catch (Exception e) {
      	errors.add(e.toString());
      	return "viewPortafolio.jsp";
      }
	}
}
