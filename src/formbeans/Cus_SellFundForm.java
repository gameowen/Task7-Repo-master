package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Cus_SellFundForm extends FormBean{
	private String fundName;
	private String shares;
	private String fundId;
	
	
	public void setShares(String shares) {
		this.shares = shares;
	}


	public String getFundName() {
		return fundName;
	}

	public String getFundId() {
	    return fundId;
	}
	
	public void setFundId(String id) {
	    this.fundId = id;
	}
	
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getShares() {
		return shares;
	}


	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (fundId == null || fundId.length() == 0) {
			errors.add("No such fund !");
		}
		
		try {
            if (Double.parseDouble(shares) < 1.00) {
                errors.add("Shares must not be greater than or equal to 1.00"); 
            }
            if (Double.parseDouble(shares) >= 1000000) {
                errors.add("Shares must be less than 10,000,000"); 
            }
        }
        catch (NumberFormatException e) {
            errors.add("Shares must be a valid number");
        }
		
		if (errors.size() > 0) {
			return errors;
		}
		
		

		return errors;
	}


	

	
}
