package formbeans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

public class Emp_TransitionDayForm {
    private ArrayList<Integer> fundIds;
    private ArrayList<Double> prices;
    private String date;
    private Date formatDate;
    private boolean isPresent = false;
    
    public Emp_TransitionDayForm(HttpServletRequest request){
        fundIds = new ArrayList<Integer>();
        prices = new ArrayList<Double>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) { 
            String id = parameterNames.nextElement();
            if (id.startsWith("fund_")) {
                try {  
                    double price = Double.parseDouble(request.getParameter(id));
                    if(price<1 || price>10000){
                        prices = null;
                        break;
                    }
                    fundIds.add(Integer.parseInt(id.substring(5)));  
                    prices.add(price);
                }
                catch(NumberFormatException nfe){ 
                    fundIds = null;
                    break;
                }
            }
        }
        date = request.getParameter("date");
        if(date==null) return;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            formatDate = format.parse(date);
        } catch (ParseException e) {
            formatDate = null;
        }
        if(request.getParameter("button")!=null)
            isPresent = true;
    }
    
    public ArrayList<Integer> getFundIds() {
        return fundIds;
    }
    
    public ArrayList<Double> getPrices() {
        return prices;
    }
    
    public Date getDate() {
        return formatDate;
    }
    
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();
        if (fundIds == null) {
            errors.add("There are errors in the form");
        }
        
        if (prices == null) {
            errors.add("Price is out of range [$1.00 ~ $10,000]");
        }
        
        if (formatDate == null) {
            errors.add("Invalid Date Format. Must be MM/DD/YYYY");
        }
        
        return errors;
    }
    
    public boolean isPresent() {
        return isPresent;
    }
}
