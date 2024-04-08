	package pt.unl.fct.di.apdc.firstwebapp.resources;
	
	import java.util.ArrayList;
	import java.util.List;
	import java.util.logging.Logger;
	
	import javax.ws.rs.Consumes;
	import javax.ws.rs.HeaderParam;
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
	import com.google.cloud.datastore.Query;
	import com.google.cloud.datastore.QueryResults;
	import com.google.cloud.datastore.StructuredQuery;
	import com.google.gson.Gson;
	
	import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
	import pt.unl.fct.di.apdc.firstwebapp.util.*;
	
	@Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public class DashboardResource {
		
		//ROLES
		public static final String ROLE_SU = "SU";
		public static final String ROLE_GA = "GA";
		public static final String ROLE_GBO = "GBO";
		private static final String ROLE_USER = "USER";	
		//STATES
		private static final String STATE_ACTIVE = "ACTIVE";	
		private static final String STATE_INACTIVE = "INACTIVE";	
		//PRIVACY
		private static final String PRIVACY_PUBLIC = "PUBLIC";	
		
		
	
		
		/**
		 * Logger Object
		 */
		private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
		
		private final Gson g = new Gson();
		
		private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
		public DashboardResource () { }
		
	
		@POST
		@Path("/changeRole")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response changeRole(ChangeRoleData data) {
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
			String changerUsername = token.getUsername();
			String changerRole = token.getRole();
			String changingUsername = data.getUsername();
			
			
			if(changingUsername.equals(changerUsername))
				return Response.status(Status.FORBIDDEN).entity("Can't change self role.").build();		
	
			Key changingUserKey = datastore.newKeyFactory().setKind("User").newKey(changingUsername);
			Entity changingUser = datastore.get(changingUserKey);
			
			if(changingUser == null) {
				LOG.warning("User " + changingUsername + " does not exist.");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			String changingRoleOld = changingUser.getString("user_role");
			String changingRoleNew = data.getRole();
			
			if(changingRoleNew.equals(changingRoleOld)) {
				LOG.warning("User " + changingUsername + " already has the specified role.");
				return Response.status(Status.FORBIDDEN).entity("User already has the specified role.").build();		
			}
			
			Entity updatedUser = Entity.newBuilder(changingUser).set("user_role", changingRoleNew).build();
			
			
			switch(changerRole) {
				case ROLE_SU:
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " role successfully updated");
					return Response.ok("{}").build();
				
				case ROLE_GA:
					
					if(changingRoleOld.equals(ROLE_SU) || changingRoleOld.equals(ROLE_GA)) {
						LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					} else if(changingRoleOld.equals(ROLE_GBO) || changingRoleOld.equals(ROLE_USER)) {
						datastore.update(updatedUser);
						LOG.info("User " + changingUsername + " role successfully updated");
						return Response.ok("{}").build();
					}
				default:
					LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
					return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
			}		
		}
		
		
	
	
		@POST
		@Path("/changeState")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response changeState(ChangeStateData data) {
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
	
			String changerUsername = token.getUsername();
			String changerRole = token.getRole();
			
			String changingUsername = data.getUsername();
			
			
			if(changingUsername.equals(changerUsername))
				return Response.status(Status.FORBIDDEN).entity("Can't change self role.").build();		
	
			Key changingUserKey = datastore.newKeyFactory().setKind("User").newKey(changingUsername);
			Entity changingUser = datastore.get(changingUserKey);
			
			if(changingUser == null) {
				LOG.warning("User " + changingUsername + " does not exist.");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			String changingRole = changingUser.getString("user_role");
			String changingState = changingUser.getString("user_state");
			
			Entity updatedUser;
			
			if(changingState.equals(STATE_ACTIVE))
				updatedUser = Entity.newBuilder(changingUser).set("user_state", STATE_INACTIVE).build();
			else
				updatedUser = Entity.newBuilder(changingUser).set("user_state", STATE_ACTIVE).build();
			
			switch(changerRole) {
			case ROLE_SU:
				datastore.update(updatedUser);
				LOG.info("User " + changingUsername + " state successfully updated");
				return Response.ok("{}").build();
			
			case ROLE_GA:
				
				if(changingRole.equals(ROLE_SU) || changingRole.equals(ROLE_GA)) {
					LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
					return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
				
				} else if(changingRole.equals(ROLE_GBO) || changingRole.equals(ROLE_USER)) {
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " state successfully updated");
					return Response.ok("{}").build();
				}
			case ROLE_GBO:
				
				if(changingRole.equals(ROLE_SU) || changingRole.equals(ROLE_GA) || changingRole.equals(ROLE_GBO)) {
					LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
					return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
				
				} else if(changingRole.equals(ROLE_USER)) {
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " state successfully updated");
					return Response.ok("{}").build();
				}
			default:
				LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
				return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
			}				
		}
		
		
		@POST
		@Path("/changeSettings")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response changeSettings(changeSettingsData data) {
			
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
	
			String changerUsername = token.getUsername();
			String changerRole = token.getRole();
			
			String changingUsername = data.username;
			String changingEmail = data.email;
			String changingFullname = data.fullname;
			String changingPhoneNum = data.phoneNum;
			String changingOccupation = data.occupation;
			String changingWorkplace = data.workplace;
			String changingAddress = data.address;
			String changingZipcode = data.zipcode;
			String changingNIF = data.nif;
			String changingPrivacy = data.privacy;
			
			Key changingUserKey = datastore.newKeyFactory().setKind("User").newKey(changingUsername);
			Entity changingUser = datastore.get(changingUserKey);
			
			String changingRole = changingUser.getString("user_role");
			
			if(data.email.equals(""))
				changingEmail = changingUser.getString("user_email");
			if(data.fullname.equals(""))
				changingFullname = changingUser.getString("user_name");
			if(data.phoneNum.equals(""))
				changingPhoneNum = changingUser.getString("user_phone");
			if(data.occupation.equals(""))
				changingOccupation = changingUser.getString("user_occupation");
			if(data.workplace.equals(""))
				changingWorkplace = changingUser.getString("user_workplace");
			if(data.address.equals(""))
				changingAddress = changingUser.getString("user_address");
			if(data.zipcode.equals(""))
				changingZipcode = changingUser.getString("user_zipcode");
			if(data.nif.equals(""))
				changingNIF = changingUser.getString("user_nif");
			if(data.privacy.equals("") || (!data.privacy.equals("PUBLIC") && !data.privacy.equals("PRIVATE")))
				changingPrivacy = changingUser.getString("user_privacy");
			
			Entity updatedUser;
			
			switch(changerRole) {
				case ROLE_USER:
					if(!changerUsername.equals(changingUsername))
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					updatedUser = Entity.newBuilder(changingUser)
									.set("user_phone", changingPhoneNum)
									.set("user_occupation", changingOccupation)
									.set("user_workplace", changingWorkplace)
									.set("user_address", changingAddress)
									.set("user_zipcode", changingZipcode)
									.set("user_nif", changingNIF)
									.set("user_privacy", changingPrivacy)
									.build();
					
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " settings successfully updated");
					return Response.ok("{}").build();
	
				case ROLE_GBO:
					if(!changingRole.equals(ROLE_USER))
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					updatedUser = Entity.newBuilder(changingUser)
							.set("user_name", changingFullname)
							.set("user_email", changingEmail)
							.set("user_phone", changingPhoneNum)
							.set("user_occupation", changingOccupation)
							.set("user_workplace", changingWorkplace)
							.set("user_address", changingAddress)
							.set("user_zipcode", changingZipcode)
							.set("user_nif", changingNIF)
							.set("user_privacy", changingPrivacy)
							.build();
					
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " settings successfully updated");
					return Response.ok("{}").build();
				
				case ROLE_GA:
					if(changingRole.equals(ROLE_GA) || changingRole.equals(ROLE_SU))
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					updatedUser = Entity.newBuilder(changingUser)
							.set("user_name", changingFullname)
							.set("user_email", changingEmail)
							.set("user_phone", changingPhoneNum)
							.set("user_occupation", changingOccupation)
							.set("user_workplace", changingWorkplace)
							.set("user_address", changingAddress)
							.set("user_zipcode", changingZipcode)
							.set("user_nif", changingNIF)
							.set("user_privacy", changingPrivacy)
							.build();
					
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " settings successfully updated");
					return Response.ok("{}").build();
				
				case ROLE_SU:
					
					if(changingRole.equals(ROLE_SU))
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					updatedUser = Entity.newBuilder(changingUser)
							.set("user_name", changingFullname)
							.set("user_email", changingEmail)
							.set("user_phone", changingPhoneNum)
							.set("user_occupation", changingOccupation)
							.set("user_workplace", changingWorkplace)
							.set("user_address", changingAddress)
							.set("user_zipcode", changingZipcode)
							.set("user_nif", changingNIF)
							.set("user_privacy", changingPrivacy)
							.build();
					
					datastore.update(updatedUser);
					LOG.info("User " + changingUsername + " settings successfully updated");
					return Response.ok("{}").build();
				
					
				default:
					return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					
			}			
		}
		
		
		@POST
		@Path("/changePassword")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response changePassword(ChangePasswordData data) {
			
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
			
			String username = token.getUsername();
			
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
			Entity user = datastore.get(userKey);
			
			Entity updatedUser;
			
			String hashedPwd = user.getString("user_password");
			
			if(!hashedPwd.equals(DigestUtils.sha512Hex(data.currentPassword)))
				return Response.status(Status.FORBIDDEN).entity("Wrong password.").build();		
	
			
			
			if(data.confirmPassword()) {
				updatedUser = Entity.newBuilder(user).set("user_password", DigestUtils.sha512Hex(data.newPassword)).build();
				datastore.update(updatedUser);
				return Response.ok("{}").build();
			} 
				return Response.status(Status.FORBIDDEN).entity("Password and confirmation do not match.").build();
		}
		
		
		@POST
		@Path("/listAccounts")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response listAccounts(listAccountsData data) {		
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
			
			String userRole = token.getRole();
	
			Query<Entity> query;
			Query<Entity> query2 = null;
			Query<Entity> query3 = null;
			
			switch (userRole) {
				case ROLE_SU:
					query = Query.newEntityQueryBuilder().setKind("User").build();
	                break;
				
				case ROLE_GA:
					

					 query = Query.newEntityQueryBuilder().setKind("User").setFilter(StructuredQuery.PropertyFilter.eq("user_role", ROLE_USER)).build();
					 query2 = Query.newEntityQueryBuilder().setKind("User").setFilter(StructuredQuery.PropertyFilter.eq("user_role", ROLE_GBO)).build();
					 query3 = Query.newEntityQueryBuilder().setKind("User").setFilter(StructuredQuery.PropertyFilter.eq("user_role", ROLE_GA)).build();
					 
					 break;
				
				case ROLE_GBO:
					query = Query.newEntityQueryBuilder()
	                			.setKind("User")
	                			.setFilter(StructuredQuery.PropertyFilter.eq("user_role", ROLE_USER))
	                			.build();
					break;
					
				case ROLE_USER:
					query = Query.newEntityQueryBuilder()
	                        	.setKind("User")
	                        	.setFilter(StructuredQuery.CompositeFilter.and(
	                                        StructuredQuery.PropertyFilter.eq("user_role", ROLE_USER), 
	                                        StructuredQuery.CompositeFilter.and(StructuredQuery.PropertyFilter.eq("user_privacy", PRIVACY_PUBLIC),
	                                        StructuredQuery.PropertyFilter.eq("user_state", STATE_ACTIVE ))))
	                        	.build();
					break;
					
				default:
					return Response.status(Response.Status.BAD_REQUEST).entity("Invalid role").build();
			}
			
			QueryResults<Entity> results = datastore.run(query);
			QueryResults<Entity> results2 = null;
			QueryResults<Entity> results3 = null;
			
			if(userRole.equals(ROLE_GA)) {
				results2 = datastore.run(query2);
				results3 = datastore.run(query3);
				 
			}
			
			List<UserToList> userList = new ArrayList<UserToList>();
			UserToList userToList;
			
			if(userRole.equals(ROLE_USER)) {
				while (results.hasNext()) {
					Entity userEntity = results.next();
		            userToList = new UserToList(userEntity.getKey().getName(), userEntity.getString("user_name"), userEntity.getString("user_email"));
		            userList.add(userToList);
				}
			} else if(userRole.equals(ROLE_GA)) {
				while (results.hasNext()) {
					Entity userEntity = results.next();
					userToList = new UserToList(userEntity.getKey().getName(), userEntity.getString("user_name"), userEntity.getString("user_email"), 
							userEntity.getString("user_phone"), userEntity.getString("user_occupation"), userEntity.getString("user_workplace"),
							userEntity.getString("user_address"), userEntity.getString("user_zipcode"), userEntity.getString("user_nif"));
		           userList.add(userToList);
				}
				while (results2.hasNext()) {
					Entity userEntity = results2.next();
					userToList = new UserToList(userEntity.getKey().getName(), userEntity.getString("user_name"), userEntity.getString("user_email"), 
							userEntity.getString("user_phone"), userEntity.getString("user_occupation"), userEntity.getString("user_workplace"),
							userEntity.getString("user_address"), userEntity.getString("user_zipcode"), userEntity.getString("user_nif"));
		           userList.add(userToList);
				}
				while (results3.hasNext()) {
					Entity userEntity = results3.next();
					userToList = new UserToList(userEntity.getKey().getName(), userEntity.getString("user_name"), userEntity.getString("user_email"), 
							userEntity.getString("user_phone"), userEntity.getString("user_occupation"), userEntity.getString("user_workplace"),
							userEntity.getString("user_address"), userEntity.getString("user_zipcode"), userEntity.getString("user_nif"));
		           userList.add(userToList);
				}
			}
			else {
				while (results.hasNext()) {
		            Entity userEntity = results.next();
		            userToList = new UserToList(userEntity.getKey().getName(), userEntity.getString("user_name"), userEntity.getString("user_email"), 
							userEntity.getString("user_phone"), userEntity.getString("user_occupation"), userEntity.getString("user_workplace"),
							userEntity.getString("user_address"), userEntity.getString("user_zipcode"), userEntity.getString("user_nif"));
		            
		            userList.add(userToList);
				}
			}
	
			return Response.ok(userList).build();
				
		}
		
		
		@POST
		@Path("/removeAccount")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response removeAccount(RemoveAccountData data) {
					
			AuthToken token = data.getToken();
			
			if(!token.isValid()){
				return Response.status(Status.FORBIDDEN).entity("Invalid token.").build();		
			}
	
			String changerUsername = token.getUsername();
			String changerRole = token.getRole();
			String deletingUsername = data.getUsername();
			
			
			Key deletingUserKey = datastore.newKeyFactory().setKind("User").newKey(deletingUsername);
			Entity deletingUser = datastore.get(deletingUserKey);
			
			if(deletingUser == null) {
				LOG.warning("User " + deletingUsername + " does not exist.");
				return Response.status(Status.FORBIDDEN).build();
			}
			
			String deletingRole = deletingUser.getString("user_role");
	
			if(deletingUsername.equals(changerUsername) && !deletingRole.equals(ROLE_USER))
				return Response.status(Status.FORBIDDEN).entity("Can't change self role.").build();		
	
			switch(changerRole) {
				case ROLE_SU:
					datastore.delete(deletingUserKey);
					LOG.info("User " + deletingUsername + " successfully removed");
					return Response.ok("{}").build();
				case ROLE_GA:
					
					if(deletingRole.equals(ROLE_SU) || deletingRole.equals(ROLE_GA)) {
						LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					
					} else if(deletingRole.equals(ROLE_GBO) || deletingRole.equals(ROLE_USER)) {
						datastore.delete(deletingUserKey);
						LOG.info("User " + deletingUsername + " successfully removed");
						return Response.ok("{}").build();
					}
				case ROLE_USER:
					if(deletingUsername.equals(changerUsername)) {
						datastore.delete(deletingUserKey);
						LOG.info("User " + deletingUsername + " successfully removed");
						return Response.ok("{}").build();
					} else {
						LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
						return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
					}
				default:
					LOG.info("User '" + changerUsername + "' doesn't have permission to execute this operation.");
					return Response.status(Response.Status.FORBIDDEN).entity("This user doesn't have permission to execute this operation").build();
				
			}
		}
	}
