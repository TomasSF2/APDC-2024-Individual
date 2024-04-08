package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangePasswordData {

	public String currentPassword;
	public String newPassword;
	public String newPasswordConfirm;
	public AuthToken token;
	
	public ChangePasswordData() {}
	
	
	public ChangePasswordData(String currentPassword, String newPassword, 
							  String newPasswordConfirm, AuthToken token) {
		
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.newPasswordConfirm = newPasswordConfirm;
		this.token = token;
	}

	public boolean confirmPassword() {
		if(newPassword.equals(newPasswordConfirm))
			return true;
		return false;
	}
	
	public String getCurrentPassword() {
		return this.currentPassword;
	}
	
	public String getNewPassword() {
		return this.newPassword;
	}
	
	public String getNewPasswordConfirm() {
		return this.newPasswordConfirm;
	}
	
	public AuthToken getToken() {
		return this.token;
	}
}
