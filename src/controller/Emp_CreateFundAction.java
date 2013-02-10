package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Employee;
import databeans.Fund;
import formbeans.Emp_CreateFundForm;
import formbeans.Emp_RegisterForm;

import model.FundDAO;
import model.Model;
import model.MyDAOException;


public class Emp_CreateFundAction extends Action {   
    private FormBeanFactory<Emp_CreateFundForm> formBeanFactory = FormBeanFactory.getInstance(Emp_CreateFundForm.class);
    private FundDAO fundDAO;
    
    public Emp_CreateFundAction(Model model) {
        fundDAO = model.getFundDAO();
    }
    
    public String getName() { return "createfund.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
            Emp_CreateFundForm form = formBeanFactory.create(request);
            request.setAttribute("form",form);
            
            if (!form.isPresent()) {
                return "create-fund-emp.jsp";
            }

            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "create-fund-emp.jsp";
            }
            
            // Create new fund
            Fund fund = fundDAO.lookup(form.getFundName(), form.getFundSymbol());
            if (fund!=null) {
                //errors.add(fund.getName() + "["+fund.getSymbol()+"] already exists!");
                //return "create-fund-emp.jsp";
            }
			// Attach (this copy of) the user bean to the session

            fund = new Fund();
            fund.setName(form.getFundName());
            fund.setSymbol(form.getFundSymbol());
            
            if(fundDAO.create(fund)==null) {
                errors.add("Conflict! The Fund Name or the Fund Ticker has benn used");
                return "create-fund-emp.jsp";
            }
			HttpSession session = request.getSession();
			session.setAttribute("fund", fund);
			
            request.setAttribute("fund", fund);
            errors.add("Fund has been created");
            return "feedback-create-fund.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
