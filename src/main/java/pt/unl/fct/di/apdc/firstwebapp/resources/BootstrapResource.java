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
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;

@Path("/bootstrap")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class BootstrapResource {
	
	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	public BootstrapResource() { }

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegisterRoot() {
		
		LOG.fine("Attempt to register user: root");
		
		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey("root");
			Entity user = txn.get(userKey);
			if(user != null) {
				txn.rollback();
				return Response.status(Status.FORBIDDEN).entity("User already exists").build();
			} else {
				user = Entity.newBuilder(userKey)
						.set("user_name", "root")
						.set("user_email", "root@root.toot")
						.set("user_password", DigestUtils.sha512Hex("pwd"))
						.set("user_phone", "")
						.set("user_state", "ACTIVE")
						.set("user_role", "SU")
						.set("user_occupation", "")
						.set("user_workplace", "")
						.set("user_address", "")
						.set("user_zipcode", "")
						.set("user_nif", "")
						.set("user_privacy", "PUBLIC")
						.set("user_creation_time", Timestamp.now())
						.build();
				
				txn.add(user);
				LOG.info("User registered: root");
				txn.commit();
				return Response.ok("{}").build();
			}

		} finally {
			if(txn.isActive())
				txn.rollback();
		}
	}
}
