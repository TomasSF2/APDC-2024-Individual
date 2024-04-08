package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangeRoleData {
	public String username;
	public String role;
	public AuthToken token;
	
	public ChangeRoleData() {}
	
	
	public ChangeRoleData(String username, String role, AuthToken token) {
		this.username = username;
		this.role = role;
		this.token = token;
	}

	public String getUsername() {
		return this.username;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public AuthToken getToken() {
		return this.token;
	}
}
