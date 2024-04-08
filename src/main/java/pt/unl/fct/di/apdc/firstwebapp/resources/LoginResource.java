package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

	private static final String STATE_INACTIVE = "INACTIVE";
	private static final String STATE_ACTIVE = "ACTIVE";
	
	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	
	private final Gson g = new Gson();
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

 
	public LoginResource() { }

	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		LOG.fine("Login attempt by user: " + data.username);
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
					
		try {
			Entity user = datastore.get(userKey);
			
			if(!checkCredentials(user, data)) {
				LOG.warning("Failed login attempt for username " + data.username);
				return Response.status(Status.FORBIDDEN).build();		
			}
			
			if(user.getString("user_state").equals(STATE_INACTIVE)) {
				LOG.warning("Account with username " + data.username + " is inactive.");
				return Response.status(Status.FORBIDDEN).build();
			}
				
			
			AuthToken token = new AuthToken(data.getUsername(), user.getString("user_role"));
			LOG.info("User '" + data.username + "' logged in successfully.");
			return Response.ok(g.toJson(token)).build();
			
		} catch(Exception e) {
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	private boolean checkCredentials(Entity user, LoginData data) {
		if(user == null) {
			return false;
		}
		
		String hashedPWD = (String) user.getString("user_password");
		if(!hashedPWD.equals(DigestUtils.sha512Hex(data.password)))
			return false;
		
		return true;
	}	
}