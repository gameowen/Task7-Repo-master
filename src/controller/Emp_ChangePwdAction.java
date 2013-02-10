package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.EmployeeDAO;
import model.Model;

import org.mybeans.form.FormBeanFactory;

import databeans.Employee;
import formbeans.Emp_ChangePwdForm;

public class Emp_ChangePwdAction extends Action{
	private FormBeanFactory<Emp_ChangePwdForm> formBeanFactory = FormBeanFactory
			.getInstance(Emp_ChangePwdForm.class);

	private EmployeeDAO employeeDAO;

	public Emp_ChangePwdAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
	}

	public String getName() {
		return "emp_changePwd.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
		    Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
			Emp_ChangePwdForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				return "change-pwd-emp.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				System.out.println(errors.toString());
				return "change-pwd-emp.jsp";
			}
		
			// Change the password
			employeeDAO.setPassword(employee.getUsername(), form.getNewPassword());
			
			request.setAttribute("message","Password changed for "+employee.getUserId());
	        return "getcustomers.do";
	  } catch (Exception e) {
      	errors.add(e.toString());
      	return "change-pwd-emp.jsp";
      }
	}
}
