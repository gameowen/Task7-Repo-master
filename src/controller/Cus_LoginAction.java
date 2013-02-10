package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import formbeans.Cus_LoginForm;

import model.Model;
import model.CustomerDAO;
import model.MyDAOException;


public class Cus_LoginAction extends Action {	
	private FormBeanFactory<Cus_LoginForm> formBeanFactory = FormBeanFactory.getInstance(Cus_LoginForm.class);
	private CustomerDAO customerDAO;
	
	public Cus_LoginAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}
	
	public String getName() { return "customer-login.do"; }
	
    public String perform(HttpServletRequest request) {
    	List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
	    	Cus_LoginForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);

	        
	        if (!form.isPresent()) {
	            return "login-cus.jsp";
	        }

	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
	        
	        if (errors.size() != 0) {
	            return "login-cus.jsp";
	        }
	        
	        // Look up the user
	        Customer customer = customerDAO.lookup(form.getUserName());
	        
	        if (customer == null) {
	            errors.add("User Name not found");	           
	            return "login-cus.jsp";
	        }
	        
	        // Check the password
	        if (!customer.checkPassword(form.getPassword())) {
	            errors.add("Incorrect password");
	            return "login-cus.jsp";
	        }
	
	        // Attach (this copy of) the user bean to the session
	        HttpSession session = request.getSession();
	        session.setAttribute("customer",customer);
	      
			String webapp = request.getContextPath();
			return webapp + "/cus_changePwd.do";
        } catch (MyDAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
