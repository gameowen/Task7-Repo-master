package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Model;
import model.EmployeeDAO;
import model.MyDAOException;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Employee;
import formbeans.Emp_RegisterForm;

public class Emp_RegisterAction extends Action {
	private FormBeanFactory<Emp_RegisterForm> formBeanFactory = FormBeanFactory.getInstance(Emp_RegisterForm.class);

	private EmployeeDAO employeeDAO;
	
	public Emp_RegisterAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
	}

	public String getName() { return "create-employee-acct.do"; } 

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
        	Emp_RegisterForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);
	
	        // If no params were passed, return with no errors so that the form will be
	        // presented (we assume for the first time).
	        if (!form.isPresent()) {
	            return "create-acct-emp.jsp";
	        }
	
	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	        	System.out.println(errors.toString());
	            return "create-acct-emp.jsp";
	        }

	        // Create the user bean
	        Employee user = new Employee();
	        user.setUsername(form.getUserName());
	        user.setFirstName(form.getFirstName());
	        user.setLastName(form.getLastName());
	        user.setPassword(form.getPassword());
        	
	        if (!employeeDAO.create(user)) {
                errors.add("Username already exists!");
                return "create-acct-emp.jsp";
            }
	        
	        employeeDAO.create(user);
        
	
			// Attach (this copy of) the user bean to the session
			HttpSession session = request.getSession();
			session.setAttribute("user", user);

			// If redirectTo is null, redirect to the "manage" action
			// return "manage-customers-emp.jsp";
			return "feedback-create-acct-emp.jsp";
		} catch (MyDAOException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
