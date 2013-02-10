package formbeans;

import org.mybeans.form.FormBean;

public class Emp_ViewCustomerForm extends FormBean{
    private String userName = "";
    
    public String getUserName()  { return userName; }
    
    public void setUserName(String s)  { userName = trimAndConvert(s,"<>>\"]"); }
    
}
