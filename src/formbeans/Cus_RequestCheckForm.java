package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Cus_RequestCheckForm extends FormBean {
	private String userName;
    private String withdraw;
	
    public String getUserName()   { return userName;   }
	public String getWithdraw()   { return withdraw;   }
	
	public void setUserName(String s)   { userName = trimAndConvert(s,"<>\"");    } 
	public void setWithdraw(String s)   { withdraw  = s.trim();                   }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (withdraw == null || withdraw.length() == 0) {
			errors.add("Withdraw amount is required");
		}

		// missing validation for cash 
		// withdraw must be valid number
		// no need to consider overflow here
		
		try {
            if (Double.parseDouble(withdraw) < 1.00) {
                errors.add("Amount must be greater than or equal to $ 1.00"); 
            }
            if (Double.parseDouble(withdraw) >= 10000000) {
                errors.add("Amount must be less than $ 10,000,000"); 
            }
        }
        catch (NumberFormatException e) {
            errors.add("Amount must be a valid number");
        }
		
		if (errors.size() > 0) {
			return errors;
		}
		
		return errors;
	}
}
