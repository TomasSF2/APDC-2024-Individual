package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {
	public String fullname;
	public String username;
	public String email;
	public String password;
	public String confirmPassword;
	public String phoneNum;

	public String occupation;
	public String workplace;
	public String address;
	public String zipcode;
	public String nif;
	public String privacy;
	
	
	public RegisterData() {
		this.fullname = null;
		this.username = null;
		this.email = null;
		this.password = null;
		this.phoneNum = null;
	}

	public RegisterData(String fullname, String username, String email, String phoneNum, 
						String password, String confirmPassword, String occupation, String workplace, 
						String address, String zipcode, String nif, String privacy) {
		
		this.fullname = fullname;
		this.username = username;
		this.email = email;
		this.phoneNum = phoneNum;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.occupation = occupation;		
		this.workplace = workplace;
		this.address = address;
		this.zipcode = zipcode;
		this.nif = nif;	
		this.privacy = privacy;
	}
	
	public boolean validRegistration() {
		if(this.username == null || this.password == null || this.confirmPassword == null || this.email == null || this.fullname == null || this.phoneNum == null)
			return false;
		return true;
	}
	
	public boolean confirmPassword() {
		if(this.password.equals(this.confirmPassword))
			return true;
		return false;
	}
	
	
}
