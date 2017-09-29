package com.wisdom.usermgmt.rest.util;

import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ACTIVE;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.CREATED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.CREATED_ON;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.LAST_UPDATED_ON;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MODIFIED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_DESC;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLES;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_DESC;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USERS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_FULL_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_DESC;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_NAME;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.wisdom.usermgmt.rest.domain.Permission;
import com.wisdom.usermgmt.rest.domain.Role;
import com.wisdom.usermgmt.rest.domain.User;
import com.wisdom.usermgmt.rest.domain.UserGroup;

/**
 * Utility class to map Entities to JSON and vice-versa
 * 
 * @author RamuV
 *
 */
@Repository
public class JsonEntityMapper {

	public static final String  DATE_FORMAT_MYSQL = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat sdf = null;
	static{
		sdf = new SimpleDateFormat(DATE_FORMAT_MYSQL);
	}

	
	/**
	 * Helper method to map Permission JSON object to Permission entity
	 * 
	 * @param permissionJson
	 * @return Permission
	 */
	public Permission mapJsonToPermission(JSONObject permissionJson){
		Permission permission = new Permission();
		if(permissionJson.has(PERMISSION_ID)){ // for new Permission, permissionId will not be there
			permission.setPermissionId(permissionJson.getInt(PERMISSION_ID));
		}
		permission.setPermissionName(permissionJson.getString(PERMISSION_NAME));
		permission.setPermissionDesc(permissionJson.getString(PERMISSION_DESC));
		
		permission.setActive(true);// for new/update active is true - inactive(i.e deleted) will not be updated
		permission.setCreatedBy(permissionJson.getString(CREATED_BY));
		if(permissionJson.has(CREATED_ON)){
			permission.setCreateDate(parseDate(permissionJson.getString(CREATED_ON)));
		}
		
		permission.setModifiedBy(permissionJson.getString(MODIFIED_BY));
		if(permissionJson.has(LAST_UPDATED_ON)){
			permission.setLastUpdatedDate(parseDate(permissionJson.getString(LAST_UPDATED_ON)));
		}
		return permission;
	}
	
	/**
	 * Helper method to map Permission entity to JSON object
	 * 
	 * @param permission
	 * @return JSONObject with Permission info.
	 */
	public JSONObject mapPermissionToJson(Permission permission){
		JSONObject permissionJson = new JSONObject();
		permissionJson.put(PERMISSION_ID, permission.getPermissionId());
		permissionJson.put(PERMISSION_NAME, permission.getPermissionName());
		permissionJson.put(PERMISSION_DESC, permission.getPermissionDesc());
		permissionJson.put(ACTIVE, permission.isActive());
		permissionJson.put(CREATED_BY, permission.getCreatedBy());
		permissionJson.put(CREATED_ON, formatDate(permission.getCreateDate()));
		permissionJson.put(MODIFIED_BY, permission.getModifiedBy());
		permissionJson.put(LAST_UPDATED_ON, formatDate(permission.getLastUpdatedDate()));

		return permissionJson;
	}

	/**
	 * Helper method to map Role JSON object to Role entity
	 * 
	 * @param roleJson
	 * @return Role
	 */
	public Role mapJsonToRole(JSONObject roleJson){
		Role role = new Role();
		if(roleJson.has(ROLE_ID)){ // for new role, roleId will not be there
			role.setRoleId(roleJson.getInt(ROLE_ID));
		}
		role.setRoleName(roleJson.getString(ROLE_NAME));
		role.setRoleDesc(roleJson.getString(ROLE_DESC));
		role.setActive(true);
		role.setCreatedBy(roleJson.getString(CREATED_BY));
		role.setModifiedBy(roleJson.getString(MODIFIED_BY));
		if(roleJson.has(CREATED_ON)){
			role.setCreateDate(parseDate(roleJson.getString(CREATED_ON)));
		}
		role.setModifiedBy(roleJson.getString(MODIFIED_BY));
		if(roleJson.has(LAST_UPDATED_ON)){
			role.setLastUpdatedDate(parseDate(roleJson.getString(LAST_UPDATED_ON)));
		}
		
		if(roleJson.has(PERMISSIONS)){
			JSONArray permissionsArr = roleJson.getJSONArray(PERMISSIONS);
			for(Iterator<Object> iterator = permissionsArr.iterator(); iterator.hasNext();){
				JSONObject json = (JSONObject) iterator.next();
				role.addPermissions(mapJsonToPermission(json));
			}
		}
		
		return role;
	}
	
	/**
	 * Helper method to map Role entity to JSON object
	 * 
	 * @param role
	 * @return JSONObject with role info.
	 */
	public JSONObject mapRoleToJson(Role role){
		JSONObject roleJson = new JSONObject();
		
		roleJson.put(ROLE_ID, role.getRoleId());
		roleJson.put(ROLE_NAME, role.getRoleName());
		roleJson.put(ROLE_DESC, role.getRoleDesc());
		roleJson.put(ACTIVE, role.isActive());
		roleJson.put(CREATED_BY, role.getCreatedBy());
		roleJson.put(CREATED_ON, formatDate(role.getCreateDate()));
		roleJson.put(MODIFIED_BY, role.getModifiedBy());
		roleJson.put(LAST_UPDATED_ON, formatDate(role.getLastUpdatedDate()));
		
		List<JSONObject> permissionsList = new ArrayList<JSONObject>();
		for(Permission permission: role.getPermissions()){
			permissionsList.add(mapPermissionToJson(permission));
		}
		roleJson.put(PERMISSIONS, new JSONArray(permissionsList));

		return roleJson;
	}

	/**
	 * Helper method to map UserGroup JSON object to USergroup entity
	 * 
	 * @param userGroupJson
	 * @return UserGroup
	 */
	public UserGroup mapJsonToUserGroup(JSONObject userGroupJson){
		UserGroup userGroup = new UserGroup();
		if(userGroupJson.has(USER_GROUP_ID)){ // for new userGroup, userGroupId will not be there
			userGroup.setUserGroupId(userGroupJson.getInt(USER_GROUP_ID));
		}
		userGroup.setUserGroupName(userGroupJson.getString(USER_GROUP_NAME));
		userGroup.setUserGroupDesc(userGroupJson.getString(USER_GROUP_DESC));
		userGroup.setActive(true);
		userGroup.setCreatedBy(userGroupJson.getString(CREATED_BY));
		if(userGroupJson.has(CREATED_ON)){
			userGroup.setCreateDate(parseDate(userGroupJson.getString(CREATED_ON)));
		}
		userGroup.setModifiedBy(userGroupJson.getString(MODIFIED_BY));
		if(userGroupJson.has(LAST_UPDATED_ON)){
			userGroup.setLastUpdatedDate(parseDate(userGroupJson.getString(LAST_UPDATED_ON)));
		}
		
		if(userGroupJson.has(ROLES)){
			JSONArray rolesArr = userGroupJson.getJSONArray(ROLES);
			for(Iterator<Object> iterator = rolesArr.iterator(); iterator.hasNext();){
				JSONObject roleJson = (JSONObject) iterator.next();
				userGroup.addRoles(mapJsonToRole(roleJson));
			}
		}
		
		if(userGroupJson.has(USERS)){
			JSONArray usersArr = userGroupJson.getJSONArray(USERS);
			for(Iterator<Object> iterator = usersArr.iterator(); iterator.hasNext();){
				JSONObject userJson = (JSONObject) iterator.next();
				userGroup.addUsers(mapJsonToUser(userJson));
			}
		}
		return userGroup;
	}
	
	/**
	 * Helper method to map group's user JSON to UserGroupUser entity
	 * 
	 * @param groupUserJson
	 * @return UserGroupUser
	 */
	public User mapJsonToUser(JSONObject userJson){
		User user = new User();
		/*if(groupUserJson.has(USER_GROUP_USERS_ID)){ // for new userGroupUser, Id will not be there
			groupUser.setId(groupUserJson.getInt(USER_GROUP_USERS_ID));
		}*/
		user.setUserId(userJson.getInt(USER_ID));
		user.setUserName(userJson.getString(USER_NAME));
		user.setUserFullName(userJson.getString(USER_FULL_NAME));
		
		return user;
	}
	
	/**
	 * Helper method to map UserGroup entity to JSON object
	 * 
	 * @param userGroup
	 * @return JSONObject
	 */
	public JSONObject mapUserGroupToJson(UserGroup userGroup){
		JSONObject userGroupJson = new JSONObject();
		
		userGroupJson.put(USER_GROUP_ID, userGroup.getUserGroupId());
		userGroupJson.put(USER_GROUP_NAME, userGroup.getUserGroupName());
		userGroupJson.put(USER_GROUP_DESC, userGroup.getUserGroupDesc());
		userGroupJson.put(ACTIVE, userGroup.isActive());
		userGroupJson.put(CREATED_BY, userGroup.getCreatedBy());
		userGroupJson.put(CREATED_ON, formatDate(userGroup.getCreateDate()));
		userGroupJson.put(MODIFIED_BY, userGroup.getModifiedBy());
		userGroupJson.put(LAST_UPDATED_ON, formatDate(userGroup.getLastUpdatedDate()));

		List<JSONObject> rolesList = new ArrayList<JSONObject>();
		for(Role role : userGroup.getRoles()){
			rolesList.add(mapRoleToJson(role));
		}
		userGroupJson.put(ROLES, new JSONArray(rolesList));
			
		List<JSONObject> usersList = new ArrayList<JSONObject>();
		for(User user: userGroup.getUsers()){
			usersList.add(mapUserToJson(user));
		}
		userGroupJson.put(USERS, new JSONArray(usersList));

		return userGroupJson;
	}


	//TODO this method will change little bit after LDAP integration
	/**
	 * Helper method to map User to JSON object
	 * 
	 * @param user
	 * @return
	 */
	public JSONObject mapUserToJson(User user){
		JSONObject userJson = new JSONObject();
		userJson.put(USER_ID, user.getUserId());
		userJson.put(USER_NAME, user.getUserName());
		userJson.put(USER_FULL_NAME, user.getUserFullName() + " " + "["+ user.getUserName() + "]");
		return userJson;
	}
	
	/**
	 * Converts Date in String (MYSQL date format string) to util Date object
	 * @param dateStr
	 * @return Date
	 */
	private Date parseDate(String inputDate) {
		if(inputDate != null && !inputDate.isEmpty()) {
			try {
				return sdf.parse(inputDate); 
			} catch (ParseException e) {
				return null;
			}
		}
		else{
			return null;
		}
	}

	/**
	 * Converts util Date to String (MYSQL date format string)
	 * @param dateStr
	 * @return Date
	 */
	private String formatDate(Date inputDate){
		if(inputDate != null){
			return sdf.format(inputDate);
		}
		else{
			return "";
		}
	}
	
}
