package databeans;

public class Employee {

	private int userId = 0;
	private String username = null;
	private String password = null;
	private String firstName = null;
	private String lastName = null;
	
	public boolean checkPassword(String password) {
		return password.equals(getPassword());
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public int getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setPassword(String s) {
		password = s;
	}

	public void setUserId(int x) {
		userId = x;
	}

	public void setFirstName(String s) {
		firstName = s;
	}

	public void setLastName(String s) {
		lastName = s;
	}

	public void setUsername(String s) {
		username = s;
	}
	
	public String toString() {
		return "User(" + getUserId() + ")";
	}
}
