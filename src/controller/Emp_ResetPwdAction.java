package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.CustomerDAO;
import model.EmployeeDAO;
import model.Model;

import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import formbeans.Emp_ChangePwdForm;
import formbeans.Emp_ResetPwdForm;

public class Emp_ResetPwdAction extends Action{
	private FormBeanFactory<Emp_ResetPwdForm> formBeanFactory = FormBeanFactory
			.getInstance(Emp_ResetPwdForm.class);

	private CustomerDAO customerDAO;

	public Emp_ResetPwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "emp_resetPwd.do";
	}

	@SuppressWarnings("unused")
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
		    Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
			Emp_ResetPwdForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			String newPwd = null;
			String username;
			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				return "reset-customer-pwd-emp.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				System.out.println(errors.toString());
				return "reset-customer-pwd-emp.jsp";
			}
			

			username = form.getUserName();
			if(username == null){
				errors.add("No such users!");
			}
			// Change the password
			newPwd = customerDAO.resetPassword(username);
			if(newPwd == null || newPwd.length() == 0)
				errors.add("No such user!");
			else
				errors.add("The password has been reset to " + newPwd);
	        return "reset-customer-pwd-emp.jsp";
	  } catch (Exception e) {
      	errors.add(e.toString());
      	return "reset-customer-pwd-emp.jsp";
      }
	}
}
