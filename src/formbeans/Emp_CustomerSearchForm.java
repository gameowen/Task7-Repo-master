package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Emp_CustomerSearchForm extends FormBean{
    private String query = "";
    
    public String getQuery()  { return query; }
    
    public void setQuery(String s)  { query = trimAndConvert(s,"<>>\"]"); }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();
/*
        if (query == null || query.length() == 0) {
            errors.add("User Name is required");
        }
*/        
        return errors;
    }
}
