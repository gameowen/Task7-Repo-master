package controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.SimpleAttributeSet;

import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Fund;
import databeans.History;
import databeans.Portfolio;
import databeans.Position;
import databeans.Transaction;
import formbeans.Emp_TransitionDayForm;
import formbeans.Emp_ViewCustomerForm;

import model.FundDAO;
import model.HistoryDAO;
import model.Model;
import model.CustomerDAO;
import model.MyDAOException;
import model.PositionDAO;
import model.TransactionDAO;


public class Emp_TransitionDayAction extends Action {   
    private FormBeanFactory<Emp_ViewCustomerForm> formBeanFactory = FormBeanFactory.getInstance(Emp_ViewCustomerForm.class);
    private CustomerDAO customerDAO;
    private PositionDAO positionDAO;
    private FundDAO fundDAO;
    private HistoryDAO historyDAO;
    private TransactionDAO transactionDAO;
    
    public Emp_TransitionDayAction(Model model) {
        customerDAO = model.getCustomerDAO();
        positionDAO = model.getPositionDAO();
        fundDAO = model.getFundDAO();
        historyDAO = model.getHistoryDAO();
        transactionDAO = model.getTransactionDAO();
    }
    
    public String getName() { return "transitionday.do"; }
    
    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        try {
            
            //If employee is not logged in, direct him/her to the employee login page.
            Employee employee = (Employee) request.getSession(false).getAttribute("employee");
            if(employee == null) {
                return "employee-login.do";
            }
            
          //get fund info:
            ArrayList<Fund> funds = fundDAO.lookup(".");
            Date date = new Date(0);
            for (Fund fund : funds) {
                fund.setPrice(historyDAO.lookupLatestPriceAndDate(fund.getId(), date));
            }
            if (date.getTime()==0) {
                Calendar c = new GregorianCalendar();
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                date.setTime(c.getTime().getTime());
            }
            date.setTime(date.getTime() + 86400000);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            request.setAttribute("date", format.format(date));
            request.setAttribute("funds", funds);
            
            //process request form
            Emp_TransitionDayForm form = new Emp_TransitionDayForm(request);
            request.setAttribute("form",form);
            
            // if the transition day form is not ready:
            // present the form
            if (!form.isPresent()) {
                return "transition-day-emp.jsp";
            }
            
            //Validate the form
            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "transition-day-emp.jsp";
            }
            
            //get form info:
            Date transitionDay = new Date(form.getDate().getTime());
            ArrayList<Integer> fundIds = form.getFundIds();
            ArrayList<Double> prices = form.getPrices();
            ArrayList<Transaction> transactions = transactionDAO.getPendingTransactions();
            /**
             * start transition day
             */
            boolean result = transactionDAO.transitionDay(fundIds, prices, transitionDay, transactions);
            if(result==false) {
                errors.add("PLEASE REFRESH: Prices have been updated for this day by other employees.");
                errors.add("Or new funds have been created!");
                return "transition-day-emp.jsp";
            }
            
            /**
             * end of transition day
             */
            
            return "manage-customers-emp.jsp";
        } catch (MyDAOException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
