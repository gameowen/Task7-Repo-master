package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

import com.sun.corba.se.spi.orbutil.fsm.State;

public class Cus_RegisterForm extends FormBean {
	private String firstName = "";
	private String lastName = "";
	private String userName = "";
	private String password = "";
	private String confirmPassword = "";
	private String addr1 = "";
	private String addr2 = "";
	private String city = "";
	private String state = "";
	private String zip = "";
	
	public String getFirstName() { return firstName; }
	public String getLastName()  { return lastName;  }
	public String getUserName()  { return userName;  }
	public String getPassword()  { return password;  }
	public String getConfirm()   { return confirmPassword;   }
	public String getAddr1()    { return addr1;    }
	public String getAddr2()    { return addr2;    }
	public String getCity()      { return city;      }
	public String getState()     { return state;     }
	public String getZip()       { return zip;       }
	
	public void setFirstName(String s) { firstName = trimAndConvert(s,"<>\"");  }
	public void setLastName(String s)  { lastName  = trimAndConvert(s,"<>\"");  }
	public void setUserName(String s)  { userName  = trimAndConvert(s,"<>\"");  }
	public void setPassword(String s)  { password  = s.trim();                  }
	public void setConfirmPassword(String s)   { confirmPassword   = trimAndConvert(s,"<>\"");  }
	
	public void setAddr1(String s) {
		this.addr1 = trimAndConvert(s,"<>\"");
	}
	public void setAddr2(String s) {
		this.addr2 = trimAndConvert(s,"<>\"");
	}
	public void setCity(String s) {
		this.city = trimAndConvert(s,"<>\"");
	}
	public void setState(String s) {
		this.state = trimAndConvert(s,"<>\"");
	}
	public void setZip(String s) {
		this.zip = trimAndConvert(s,"<>\"");
	}


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
		
		if (addr1 != null && addr1.length() >200) {
            errors.add("Address 1 is too long");
        }
		
		if (addr2 != null && addr2.length() >200) {
            errors.add("Address 2 is too long");
        }
		
		if (city != null && city.length() >200) {
            errors.add("City is too long");
        }
		
		if (state != null && state.length() >200) {
            errors.add("State Name is too long");
        }
		
		if (zip != null && zip.length() >200) {
            errors.add("Zip Name is too long");
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
		
		if (!addr1.matches("[0-9a-zA-Z\\s\\.]{0,200}")) {
            errors.add("Address is not valid");
        }
		
		if (!addr2.matches("[0-9a-zA-Z\\s\\.]{0,200}")) {
            errors.add("Address is not valid");
        }
		
		if (!state.matches("[0-9a-zA-Z]{0,200}")) {
            errors.add("Input must not contain special characters.");
        }
		
		if (!zip.matches("[0-9]{0,200}")) {
            errors.add("Zip code must be numbers.");
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
