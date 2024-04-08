package pt.unl.fct.di.apdc.firstwebapp.util;

public class listAccountsData {
	public AuthToken token;
	
	public listAccountsData() {}
	
	
	public listAccountsData(AuthToken token) {
		this.token = token;
	}
	
	public AuthToken getToken() {
		return this.token;
	}
}
