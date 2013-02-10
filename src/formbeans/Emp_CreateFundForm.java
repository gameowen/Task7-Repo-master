package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Emp_CreateFundForm extends FormBean {
	private String fundName;
	private String fundSymbol;
	
	public String getFundName() {
        return fundName;
    }
    public void setFundName(String fundName) {
        this.fundName = trimAndConvert(fundName,"<>\"");
    }
    public String getFundSymbol() {
        return fundSymbol;
    }
    public void setFundSymbol(String fundSymbol) {
        this.fundSymbol = trimAndConvert(fundSymbol,"<>\"");
    }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (fundName == null || fundName.length() == 0) {
			errors.add("Fund name is required");
		}

		if (fundSymbol == null || fundSymbol.length() == 0) {
			errors.add("Fund symbol (ticker) is required");
		}
		
		if (!fundName.matches("[0-9a-zA-Z\\s]+")) {
            errors.add("Input must not contain special characters.");
        }
		
		if (!fundSymbol.matches("[0-9a-zA-Z]{4,6}")) {
            errors.add("Fund Ticker's length must be 4~6.");
        }
				
		return errors;
	}
}
