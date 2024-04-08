package pt.unl.fct.di.apdc.firstwebapp.util;

public class changeSettingsData {

		public String fullname;
		public String username;
		public String email;
		public String phoneNum;

		public String occupation;
		public String workplace;
		public String address;
		public String zipcode;
		public String nif;
		public String privacy;
		
		public AuthToken token;
		
		
		
		public changeSettingsData() {
			this.fullname = null;
			this.username = null;
			this.email = null;
			this.phoneNum = null;
		}
		
		public changeSettingsData(String username, String fullname, String email, String phoneNum, 
							String occupation, String workplace, String address, 
							String zipcode, String nif, String privacy, AuthToken token) {
			
			this.fullname = fullname;
			this.username = username;
			this.email = email;
			this.phoneNum = phoneNum;
			this.occupation = occupation;		
			this.workplace = workplace;
			this.address = address;
			this.zipcode = zipcode;
			this.nif = nif;	
			this.privacy = privacy;
			this.token = token;
		
		}
		
		public AuthToken getToken() {
			return this.token;
		}	
}
