package pt.unl.fct.di.apdc.firstwebapp.util;

public class UserToList {
	public String username = null;
	public String fullname = null;
	public String email = null;
	public String phoneNum = null;
	public String occupation = null;
	public String workplace = null;
	public String address = null;
	public String zipcode = null;
	public String nif = null;
	
	
	public UserToList(String username, String fullname,String email,
					  String phoneNum,String occupation,String workplace,
					  String address,String zipcode,String nif) {
		
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.phoneNum = phoneNum;
		this.occupation = occupation;
		this.workplace = workplace;
		this.address = address;
		this.zipcode = zipcode;
		this.nif = nif;
	}
	
	public UserToList(String username, String fullname,String email) {

		this.username = username;
		this.fullname = fullname;
		this.email = email;
}
}
