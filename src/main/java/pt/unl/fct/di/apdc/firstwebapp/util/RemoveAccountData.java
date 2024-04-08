package pt.unl.fct.di.apdc.firstwebapp.util;

public class RemoveAccountData {
	public String username;
	public AuthToken token;
	
	public RemoveAccountData() {}
	
	
	public RemoveAccountData(String username, AuthToken token) {
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
