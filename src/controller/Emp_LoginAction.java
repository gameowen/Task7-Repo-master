package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Employee;
import formbeans.Emp_LoginForm;

import model.Model;
import model.EmployeeDAO;
import model.MyDAOException;


public class Emp_LoginAction extends Action {	
	private FormBeanFactory<Emp_LoginForm> formBeanFactory = FormBeanFactory.getInstance(Emp_LoginForm.class);
	private EmployeeDAO employeeDAO;
	
	public Emp_LoginAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
	}
	
	public String getName() { return "employee-login.do"; }
	
    public String perform(HttpServletRequest request) {
    	List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
	    	Emp_LoginForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);

	        
	        if (!form.isPresent()) {
	            return "login-emp.jsp";
	        }

	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	        	
	            return "login-emp.jsp";
	        }
	        
	        // Look up the user
	        Employee employee = employeeDAO.lookup(form.getUserName());
	        
	        if (employee == null) {
	            errors.add("User Name not found");
	            return "login-emp.jsp";
	        }

	        // Check the password
	        if (!employee.checkPassword(form.getPassword())) {
	            errors.add("Incorrect password");
	            return "login-emp.jsp";
	        }
	
	        // Attach (this copy of) the user bean to the session
	        HttpSession session = request.getSession();
	        session.setAttribute("employee",employee);
	        
			String webapp = request.getContextPath();
			return webapp + "/getcustomers.do";
        } catch (MyDAOException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "error.jsp";
        }
    }
}
