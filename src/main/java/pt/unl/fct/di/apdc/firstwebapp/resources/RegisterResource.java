package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.security.auth.callback.ConfirmationCallback;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
	
	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public RegisterResource() { }

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegister2(RegisterData data) {
		LOG.fine("Attempt to register user: " + data.username);
		
		LOG.info(data.privacy);
		
		if( !data.validRegistration() || !data.confirmPassword()) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
			Entity user = txn.get(userKey);
			if(user != null) {
				txn.rollback();
				return Response.status(Status.FORBIDDEN).entity("User already exists").build();
			} else {
				user = Entity.newBuilder(userKey)
						.set("user_name", data.fullname)
						.set("user_email", data.email)
						.set("user_password", DigestUtils.sha512Hex(data.password))
						.set("user_phone", data.phoneNum)
						.set("user_state", "INACTIVE")
						.set("user_role", "USER")
						.set("user_occupation", data.occupation)
						.set("user_workplace", data.workplace)
						.set("user_address", data.address)
						.set("user_zipcode", data.zipcode)
						.set("user_nif", data.nif)
						.set("user_privacy", data.privacy)
						.set("user_creation_time", Timestamp.now())
						.build();
				
				txn.add(user);
				LOG.info("User registered " + data.username);
				txn.commit();
				return Response.ok("{}").build();
			}

		} finally {
			if(txn.isActive())
				txn.rollback();
		}
	}
}
	
