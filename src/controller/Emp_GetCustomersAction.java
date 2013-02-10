package controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Fund;
import formbeans.Cus_FundSearchForm;

import model.Model;
import model.CustomerDAO;
import model.FundDAO;
import model.MyDAOException;


public class Emp_GetCustomersAction extends Action {   
    private FormBeanFactory<Cus_FundSearchForm> formBeanFactory = FormBeanFactory.getInstance(Cus_FundSearchForm.class);
    private CustomerDAO customerDAO;
    
    public Emp_GetCustomersAction(Model model) {
        customerDAO = model.getCustomerDAO();
    }
    
    public String getName() { return "getcustomers.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
            Cus_FundSearchForm form = formBeanFactory.create(request);
            request.setAttribute("form",form);

            
            if (!form.isPresent()) {
                return "manage-customers-emp.jsp";
            }
            
            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "get-cus-emp.jsp";
            }
            
            // Look up the fund
            ArrayList<Customer> customers = customerDAO.search(form.getQuery());
            
            // Attach (this copy of) the customers object to the session
            request.setAttribute("customers",customers);
            
            return "get-cus-emp.jsp";
        } catch (MyDAOException e) {
            System.out.println("DAO error");
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            System.out.println("FormBean error");
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
