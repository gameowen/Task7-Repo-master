package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Fund;
import databeans.History;
import formbeans.Cus_FundSearchForm;

import model.CustomerDAO;
import model.Model;
import model.HistoryDAO;
import model.FundDAO;
import model.MyDAOException;


public class Cus_ResearchFundAction extends Action {   
    private FormBeanFactory<Cus_FundSearchForm> formBeanFactory = FormBeanFactory.getInstance(Cus_FundSearchForm.class);
    private FundDAO fundDAO;
    private HistoryDAO historyDAO;
    private CustomerDAO customerDAO;
    
    public Cus_ResearchFundAction(Model model) {
        fundDAO = model.getFundDAO();
        historyDAO = model.getHistoryDAO();
        customerDAO = model.getCustomerDAO();
    }
    
    public String getName() { return "researchfund.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            Customer customer = (Customer) request.getSession(false).getAttribute("customer");
            if (customer == null){
                errors.add("You must login to research fund");
                return "login-cus.jsp";
            }
            
            customer = customerDAO.lookup(customer.getCustomerID());
            request.getSession(false).setAttribute("customer", customer);
            
            Cus_FundSearchForm form = formBeanFactory.create(request);
            request.setAttribute("form",form);

            /*
            if (!form.isPresent()) {
                return "getfunddetail.jsp";
            }*/
            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "get-funddetail-cus.jsp";
            }
            
            // Look up the fund
            Fund fund = fundDAO.lookup(Integer.parseInt(form.getId()));
            ArrayList<History> histories = historyDAO.lookup(Integer.parseInt(form.getId()));
            
            // Attach (this copy of) the funds object to the session
            request.getSession().setAttribute("fund",fund);
            request.setAttribute("histories", histories);
            
            return "get-funddetail-cus.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
