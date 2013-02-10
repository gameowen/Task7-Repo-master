package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Cus_FundSearchForm extends FormBean{
    private String id = "-1";
    private String query = "";
    
    public String getQuery()  { return query; }
    public String getId()        { return id;    }
    
    public void setQuery(String s)  { query = trimAndConvert(s,"<>>\"]"); }
    public void setId(String s)     { id = trimAndConvert(s,"<>>\"]");    }
    
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
