package com.wisdom.usermgmt.rest.util;

import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.DELETE_SUCCESS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ERROR_MSG;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.IS_PERM_EXISTS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.IS_ROLE_EXISTS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MODIFIED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLES;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_IDS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.UPDATE_SUCCESS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USERS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUPS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_IDS;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wisdom.usermgmt.rest.dao.ConfigMgmtDao;
import com.wisdom.usermgmt.rest.dao.UserGroupDao;
import com.wisdom.usermgmt.rest.domain.Permission;
import com.wisdom.usermgmt.rest.domain.Role;
import com.wisdom.usermgmt.rest.domain.User;
import com.wisdom.usermgmt.rest.domain.UserGroup;

/**
 * Service class acting as bridge between Controller and DAO layer 
 * 
 * 
 * @author RamuV
 */
@Repository
public class UserMgmtService {

	@Autowired
	UserMgmtUtil userMgmtUtil;
	
	@Autowired
	ConfigMgmtDao configMgmtDao;
	
	@Autowired
	UserGroupDao userGroupDao;

	@Autowired
	JsonEntityMapper jsonMapper;
	
	static Logger logger = LoggerFactory.getLogger(UserMgmtService.class);
	
	/**
	 * Method to create new Permission
	 * 
	 * @param JSONObject with permissionObj
	 * @return JSONObject with permissionId
	 */
	public JSONObject createPermission(JSONObject permissionJson) {
		logger.info("Inside createPermission(), request Json is :" + permissionJson);
		JSONObject responseJson = new JSONObject();
		int permissionId = 0;
		try{
			permissionId = configMgmtDao.createPermission(jsonMapper.mapJsonToPermission(permissionJson));
			responseJson.put(PERMISSION_ID, permissionId);
			logger.info("Inside createPermission(), created new permission with Id  :" + permissionId);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Inside createPermission(), Error creating new permission - " + e.getMessage());
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
	}
	
	/**
	 * Method to get all Permissions
	 * 
	 * @return JSONObject with permissionsList
	 */
	public JSONObject getPermissions() {
		JSONObject responseJson = new JSONObject();
		List<JSONObject> permissionsList = new ArrayList<JSONObject>();
		
		try{
			List<Permission> permissions = configMgmtDao.getPermissions();

			for(Permission permission : permissions){
				permissionsList.add(jsonMapper.mapPermissionToJson(permission));
			}
			responseJson.put(PERMISSIONS, new JSONArray(permissionsList));
			logger.info("Inside getPermissions(), retrieved permisions successfully");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Inside getPermissions(), Error retrieving all permissions - " + e.getMessage());
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
	}
	
	/**
	 * Method to check a Permission exists with a given permissionName
	 * 
	 * @param permissionJson
	 * @return JSONObject with true/false
	 */
	public JSONObject checkPermissionExists(JSONObject permissionJson) {
		String permissionName = permissionJson.getString(PERMISSION_NAME);
		boolean isPermExists = false;
		
		JSONObject responseJson = new JSONObject();
		try{
			isPermExists = configMgmtDao.checkPermissionExists(permissionName);

			responseJson.put(IS_PERM_EXISTS, isPermExists);
			logger.info("Inside checkPermissionExists(), is permission \'"+ permissionName + "\' already exists ? " + isPermExists);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Inside checkPermissionExists(), Error checking Permission - " + e.getMessage());
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		
		return responseJson;
	}
	
	//////////////////////////////////////
	/**
	 * Method to create new Role
	 * 
	 * @param JSONObject with roleObj
	 * @return JSONObject with roleId
	 */
	public JSONObject createRole(JSONObject roleJson) {
		
		logger.info("Inside createRole(), request Json is :" + roleJson);
		JSONObject responseJson = new JSONObject();
		int roleId = 0;
		try{
			roleId = configMgmtDao.createRole(jsonMapper.mapJsonToRole(roleJson));
			responseJson.put(ROLE_ID, roleId);
			logger.info("Inside createPermission(), created new permission with Id  :" + roleId);
		}catch (Exception e) {
			logger.error("Inside createPermission(), Error creating role - " + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}

		return responseJson;
	}
	
	/**
	 * Method to get all Roles
	 * 
	 * @return JSONObject with list of roles
	 */
	public JSONObject getRoles() {
		JSONObject responseJson = new JSONObject();
		List<JSONObject> rolesList = new ArrayList<JSONObject>();
		
		try{
			List<Role> roles = configMgmtDao.getRoles();

			for(Role role : roles){
				rolesList.add(jsonMapper.mapRoleToJson(role));
				//roles.get()
			}
			responseJson.put(ROLES, new JSONArray(rolesList));
			logger.info("Inside getRoles(), retrieved roles successfully");
		}catch (Exception e) {
			logger.error("Inside getRoles(), Error retrieving all roles - " + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
	}
	
	/**
	 * Method to check a Role exists with a given roleName
	 * 
	 * @param roleJson
	 * @return JSONObject with true/false
	 */
	public JSONObject checkRoleExists(JSONObject roleJson) {
		String roleName = roleJson.getString(ROLE_NAME);
		boolean isRoleExists = false;
		
		JSONObject responseJson = new JSONObject();
		try{
			isRoleExists = configMgmtDao.checkRoleExists(roleName);

			responseJson.put(IS_ROLE_EXISTS, isRoleExists);
			logger.info("Inside checkRoleExists(), is role \'"+ roleName + "\' already exists ? " + isRoleExists);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Inside checkRoleExists(), Error checking Role - " + e.getMessage());
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		
		return responseJson;
	}
	
	/**
	 * Method to update a Role
	 *  
	 * @param JSONObject with roleObj
	 * @return JSONObject with result
	 */
	public JSONObject updateRole(JSONObject roleJson) {
		logger.info("Inside updateRole(), request Json is :" + roleJson);
		
		JSONObject responseJson = new JSONObject();
		try{
			configMgmtDao.updateRole(jsonMapper.mapJsonToRole(roleJson));
			responseJson.put(UPDATE_SUCCESS, true);
			logger.info("Inside updateRole(), updated role " + roleJson.getInt(ROLE_ID)  + " successfully");
		}catch (Exception e) {
			logger.error("Inside updateRole(), Error updating role " + roleJson.getInt(ROLE_ID) + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(UPDATE_SUCCESS, false);
			responseJson.put(ERROR_MSG, e.getMessage());
		}

		return responseJson;
	}
	
	/**
	 * Method to delete a role
	 * 
	 * @param JSONObject with roleId
	 * @return JSONObject
	 */
	public JSONObject deleteRole(JSONObject roleJson){
		JSONObject responseJson = new JSONObject();
		try{
			configMgmtDao.deleteRole(roleJson.getInt(ROLE_ID), roleJson.getString(MODIFIED_BY));
			responseJson.put(DELETE_SUCCESS, true);
			logger.info("Inside deleteRole(), deleted role successfully, roleId :" + roleJson.getInt(ROLE_ID));
		}catch (Exception e) {
			logger.error("Inside deleteRole(), Error deleting role " + roleJson.getInt(ROLE_ID) + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		
		return responseJson;
	}
	
	/**
	 * Method to delete multiple roles
	 * 
	 * @param JSONObject with array of roleIds
	 * @return JSONObject
	 */
	public JSONObject deleteRoles(JSONObject roleJson){
		JSONObject responseJson = new JSONObject();
		
		JSONArray jsonArray = roleJson.getJSONArray(ROLE_IDS);
		int[] roleIds = new int[jsonArray.length()];
		for(int i=0; i<jsonArray.length() ; i++){
			roleIds[i] = jsonArray.getInt(i);
		}
		logger.info("Inside deleteRoles(), deleting roles : " + roleIds);
		try{
			int noOfRecsDeleted = configMgmtDao.deleteMultipleRoles(roleIds, roleJson.getString(MODIFIED_BY));
			if(roleIds.length == noOfRecsDeleted){
				responseJson.put(DELETE_SUCCESS, true);
			}
			else{
				logger.error("Inside deleteRoles(), only " + noOfRecsDeleted + " roles got deleted out of "+ roleIds.length ); 
			}
		}catch (Exception e) {
			logger.error("Inside deleteRoles(), Error deleting roles " + roleIds + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
	}
	
	
	////////////////////////////////////////////////
	/**
	 * Method to create new UserGroup
	 * 
	 * @param JSONObject with userGroupObj
	 * @return JSONObject with userGroupId 
	 */
	public JSONObject createUserGroup(JSONObject userGroupJson) {
		logger.info("Inside service, createUserGroup() , request Json is :" + userGroupJson);
		
		JSONObject responseJson = new JSONObject();
		int userGroupId = 0;
		try{
			userGroupId = userGroupDao.createUserGroup(jsonMapper.mapJsonToUserGroup(userGroupJson));
			responseJson.put(USER_GROUP_ID, userGroupId);
			logger.info("Inside createUserGroup(), created new UserGroup with Id  :" + userGroupId);
		}catch (Exception e) {
			logger.error("Inside createUserGroup(), Error creating UserGroup - " + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}

		return responseJson;
	}
	
	/**
	 * Method to update a UserGroup
	 * 
	 * @param JSONObject with userGroupObj
	 * @return JSONObject with result 
	 */
	public JSONObject updateUserGroup(JSONObject userGroupJson) {
		logger.info("Inside updateUserGroup(), request Json is :" + userGroupJson);
		
		JSONObject responseJson = new JSONObject();
		try{
			userGroupDao.updateUserGroup(jsonMapper.mapJsonToUserGroup(userGroupJson));
			responseJson.put(UPDATE_SUCCESS, true);
			logger.info("Inside updateUserGroup(), updated userGroup " + userGroupJson.getInt(USER_GROUP_ID)  + " successfully");
		}catch (Exception e) {
			logger.error("Inside updateUserGroup(), Error updating userGroup " + userGroupJson.getInt(USER_GROUP_ID) + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(UPDATE_SUCCESS, false);
			responseJson.put(ERROR_MSG, e.getMessage());
		}

		return responseJson;
	}
	
	/**
	 * Method to return all userGroups
	 * 
	 * @return JSONObject with list of userGroups 
	 */
	public JSONObject getUserGroups() {
		JSONObject responseJson = new JSONObject();
		List<JSONObject> userGroupsList = new ArrayList<JSONObject>();
		logger.info("Entered in to user groups................");
		try{
			List<UserGroup> userGroups = userGroupDao.getUserGroups();

			for(UserGroup userGroup : userGroups){
				userGroupsList.add(jsonMapper.mapUserGroupToJson(userGroup));
			}
			responseJson.put(USER_GROUPS, new JSONArray(userGroupsList));
			logger.info("Inside getUserGroups(), retrieved userGroups successfully");
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to retrieve User Groups,", e);
			responseJson.put(ERROR_MSG, e.getMessage());
		}

		return responseJson;
	}
	
	/**
	 * Method to delete User Group
	 * 
	 * @param JSONObject with userGroupId
	 * @return JSONObject
	 */
	public JSONObject deleteUserGroup(JSONObject userGroupJson){
		JSONObject responseJson = new JSONObject();
		try{
			userGroupDao.deleteUserGroup(userGroupJson.getInt(USER_GROUP_ID), userGroupJson.getString(MODIFIED_BY));
			responseJson.put(DELETE_SUCCESS, true);
			logger.info("Inside deleteUserGroup(),  deleted UserGroup successfully, UserGroupId :" + userGroupJson.getInt(ROLE_ID));
		}catch (Exception e) {
			logger.error("Inside deleteUserGroup(), Error deleting userGroup " + userGroupJson.getInt(USER_GROUP_ID) + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		
		return responseJson;
	}
	
	/**
	 * Method to delete multiple UserGroups
	 * 
	 * @param JSONObject with array of userGroupIds
	 * @return JSONObject
	 */
	public JSONObject deleteUserGroups(JSONObject userGroupJson){
		JSONObject responseJson = new JSONObject();
		
		JSONArray jsonArray = userGroupJson.getJSONArray(USER_GROUP_IDS);
		int[] userGroupIds = new int[jsonArray.length()];
		for(int i=0; i<jsonArray.length() ; i++){
			userGroupIds[i] = jsonArray.getInt(i);
		}
		logger.info("Inside deleteUserGroups(), deleting UserGroups : " + userGroupIds);
		try{
			int noOfRecsDeleted = userGroupDao.deleteMultipleUserGroups(userGroupIds, userGroupJson.getString(MODIFIED_BY));
			if(userGroupIds.length == noOfRecsDeleted){
				responseJson.put(DELETE_SUCCESS, true);
			}
			else{
				logger.error("Inside deleteRoles(), only " + noOfRecsDeleted + " roles got deleted out of "+ userGroupIds.length );
			}
		}catch (Exception e) {
			logger.error("Inside deleteUserGroups(), Error deleting userGroups " + userGroupIds + " -" + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
	}
	
	
	//TODO This users data need to be fetched from LDAP
	// returning static data until then
	/**
	 * Method to get List of users to be associated for userGroups
	 * 
	 * @return JSONObject with list of Users
	 */
	public JSONObject getUsers(){
		
		JSONObject responseJson = new JSONObject();
		List<JSONObject> usersList = new ArrayList<JSONObject>();
		
		try{
			List<User> users = userGroupDao.getUsers();

			for(User user : users){
				usersList.add(jsonMapper.mapUserToJson(user));
			}
			responseJson.put(USERS, new JSONArray(usersList));
			logger.info("Inside getUsers(), retrieved users successfully");
		}catch (Exception e) {
			logger.error("Inside getUsers(), Error retrieving all users - " + e.getMessage());
			e.printStackTrace();
			responseJson.put(ERROR_MSG, e.getMessage());
		}
		return responseJson;
		
	}
	
}

