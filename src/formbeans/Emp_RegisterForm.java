package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class Emp_RegisterForm extends FormBean {
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String confirmPassword;
	
	public String getFirstName() { return firstName; }
	public String getLastName()  { return lastName;  }
	public String getUserName()  { return userName;  }
	public String getPassword()  { return password;  }
	public String getConfirm()   { return confirmPassword;   }
	
	public void setFirstName(String s) { firstName = trimAndConvert(s,"<>\"");  }
	public void setLastName(String s)  { lastName  = trimAndConvert(s,"<>\"");  }
	public void setUserName(String s)  { userName  = trimAndConvert(s,"<>\"");  }
	public void setPassword(String s)  { password  = s.trim();                  }
	public void setConfirmPassword(String s)   { confirmPassword = s.trim();            }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (firstName == null || firstName.length() == 0) {
			errors.add("First Name is required");
		}

		if (lastName == null || lastName.length() == 0) {
			errors.add("Last Name is required");
		}

		if (userName == null || userName.length() == 0) {
			errors.add("User Name is required");
		}

		if (password == null || password.length() == 0) {
			errors.add("Password is required");
		}

		if (confirmPassword == null || confirmPassword.length() == 0) {
			errors.add("Confirm Password is required");
		}
		
		if (firstName != null && firstName.length() >200) {
            errors.add("First Name is too long");
        }
        
        if (lastName != null && lastName.length() >200) {
            errors.add("Last Name is too long");
        }
        
        if (userName != null && userName.length() >200) {
            errors.add("Username is too long");
        }
        
        if (password != null && password.length() >200) {
            errors.add("Password is too long");
        }
        
        if (!firstName.matches("[0-9a-zA-Z]+")) {
            errors.add("Input must not contain special characters.");
        }
        
        if (!lastName.matches("[0-9a-zA-Z]+")) {
            errors.add("Input must not contain special characters.");
        }
        
        if (!userName.matches("[0-9a-zA-Z]+")) {
            errors.add("Input must not contain special characters.");
        }
        
        if (!password.matches("[0-9a-zA-Z]{1,12}$")) {
            errors.add("Password must be alphanumeric character of length 1~12");
        }
		
		if (errors.size() > 0) {
			return errors;
		}
		
		if (!password.equals(confirmPassword)) {
			errors.add("Passwords are not the same");
		}
		
		return errors;
	}
}
