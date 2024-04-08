package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangeStateData {

	public String username;
	public AuthToken token;
	
	public ChangeStateData() {}
	
	
	public ChangeStateData(String username, AuthToken token) {
		this.username = username;
		this.token = token;
	}

	public String getUsername() {
		return this.username;
	}
	
	public AuthToken getToken() {
		return this.token;
	}
}
