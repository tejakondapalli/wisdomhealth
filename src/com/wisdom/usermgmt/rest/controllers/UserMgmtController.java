package com.wisdom.usermgmt.rest.controllers;

import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MODIFIED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_ID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.usermgmt.rest.aspects.Authorized;
import com.wisdom.usermgmt.rest.util.UserMgmtService;

/**
 * REST controller for User management module.
 * provides CURD operations for UserGroups & Configuration of Roles, Permissions 
 * 
 * @author RamuV
 *
 */
@RestController
@RequestMapping("/usermgmt")
@EnableAutoConfiguration
public class UserMgmtController {

	@Autowired
	private UserMgmtService userMgmtService;
	
	static Logger logger = LoggerFactory.getLogger(UserMgmtController.class);
	
	/**
	 * Method to create new Permission
	 * 
	 * @param permissionObj
	 * @return permissionId newly created
	 */
	@Authorized
	@RequestMapping(value = "/config/permission", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createPermission(@RequestBody String permissionObj) {
		JSONObject responseJson = userMgmtService.createPermission(new JSONObject(permissionObj));
		return responseJson.toString();
	}
	
	/**
	 * Method to get all Permissions
	 * 
	 * @return permissionsList
	 */
	@Authorized
	@RequestMapping(value = "/config/permission", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getPermissions() {
		JSONObject responseJson = userMgmtService.getPermissions();
		return responseJson.toString();
	}
	
	/**
	 * Method to check whether a permission exists with a given permissionName
	 * 
	 * @param permissionName
	 * @return response with true/false
	 */
	@Authorized
	@RequestMapping(value = "/config/permission/{permissionName}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String checkPermissionExists(@PathVariable String permissionName){
		JSONObject permissionJson = new JSONObject();
		permissionJson.put(PERMISSION_NAME, permissionName);
		JSONObject responseJson = userMgmtService.checkPermissionExists(permissionJson);
		return responseJson.toString();
	}
	
	///////////////////////
	/**
	 * Method to create new Role
	 * 
	 * @param roleObj
	 * @return roleId newly created
	 */
	@Authorized
	@RequestMapping(value = "/config/role", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createRole(@RequestBody String roleObj) {
		JSONObject responseJson = userMgmtService.createRole(new JSONObject(roleObj));
		return responseJson.toString();
	}
	
	/**
	 * Method to get all Roles
	 * 
	 * @return rolesList
	 */
	@Authorized
	@RequestMapping(value = "/config/role", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getRoles() {
		JSONObject responseJson = userMgmtService.getRoles();
		return responseJson.toString();
	}
	
	/**
	 * Method to check whether a role exists with a given roleName
	 * 
	 * @param roleName
	 * @return response with true/false
	 */
	@Authorized
	@RequestMapping(value = "/config/role/{roleName}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String checkRoleExists(@PathVariable String roleName){
		JSONObject roleJson = new JSONObject();
		roleJson.put(ROLE_NAME, roleName);
		JSONObject responseJson = userMgmtService.checkRoleExists(roleJson);
		return responseJson.toString();
	}
	
	/**
	 * Method to update a Role, including assigning permission to Role
	 * Client should pass Request payload with Role details and assigned permissions(complete objects) 
	 *  
	 * @param roleObj along with permissions
	 * @return result 
	 */
	@Authorized
	@RequestMapping(value = "/config/role/{roleId}", method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String updateRole(@PathVariable int roleId, @RequestBody String roleObj) {
		JSONObject roleJson = new JSONObject(roleObj);
		roleJson.put(ROLE_ID, roleId);
		JSONObject responseJson = userMgmtService.updateRole(roleJson);
		return responseJson.toString();
	}
	
	/**
	 * Method to delete a role
	 * 
	 * @param roleId
	 * @return result
	 */
	@Authorized
	@RequestMapping(value = "/config/role/{roleId}/{modifiedBy}", method = RequestMethod.DELETE, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String deleteRole(@PathVariable int roleId, @PathVariable String modifiedBy) {
		JSONObject roleJson = new JSONObject();
		roleJson.put(ROLE_ID, roleId);
		roleJson.put(MODIFIED_BY, modifiedBy);
		JSONObject responseJson = userMgmtService.deleteRole(roleJson);
		return responseJson.toString();
	}
	
	/**
	 * Method to delete multiple roles
	 * 
	 * @param roleIds array and modifiedBy in JSON
	 * @return result
	 */
	@Authorized
	@RequestMapping(value = "/config/role", method = RequestMethod.DELETE, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String deleteMultipleRoles(@RequestBody String roleIds) {
		JSONObject responseJson = userMgmtService.deleteRoles(new JSONObject(roleIds));
		return responseJson.toString();
	}
	
	/////////////////////
	
	/**
	 * Method to create new UserGroup along with users/roles, if assigned
	 * Client should pass Request payload with UserGroup details along with assigned roles/users(complete objects) 
	 * 
	 * @param userGroupObj
	 * @return userGroupId created 
	 */
	@Authorized
	@RequestMapping(value = "/usergrp", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createUserGroup(@RequestBody String userGroupObj) {
		JSONObject responseJson = userMgmtService.createUserGroup(new JSONObject(userGroupObj));
		return responseJson.toString();
	}
	
	/**
	 * Method to return all userGroups (active only)
	 * 
	 * @return userGroupList 
	 */
	@Authorized
	@RequestMapping(value = "/usergrp", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getUserGroups() {
		JSONObject responseJson = userMgmtService.getUserGroups();
		return responseJson.toString();
	}
	
	/**
	 * Method to Update UserGroup along with users/roles modifications
	 * Client should pass Request payload with UserGroup details along with assigned roles/users(complete objects) 
	 * 
	 * @param userGroupObj
	 * @return result
	 */
	@Authorized
	@RequestMapping(value = "/usergrp/{userGroupId}", method = RequestMethod.PUT, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String updateUserGroup(@PathVariable int userGroupId, @RequestBody String userGroupObj) {
		JSONObject userGroupJson = new JSONObject(userGroupObj);
		userGroupJson.put(USER_GROUP_ID, userGroupId);
		JSONObject responseJson = userMgmtService.updateUserGroup(userGroupJson);
		return responseJson.toString();
	}
	
	/**
	 * Method to delete User Group
	 * 
	 * @param userGroupId
	 * @return result
	 */
	@Authorized
	@RequestMapping(value = "/usergrp/{userGroupId}/{modifiedBy}", method = RequestMethod.DELETE, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String deleteUserGroup(@PathVariable int userGroupId, @PathVariable String modifiedBy) {
		JSONObject userGroupJson = new JSONObject();
		userGroupJson.put(USER_GROUP_ID, userGroupId);
		userGroupJson.put(MODIFIED_BY, modifiedBy);
		JSONObject responseJson = userMgmtService.deleteUserGroup(userGroupJson);
		return responseJson.toString();
	}
	
	/**
	 * Method to delete multiple UserGroups
	 * 
	 * @param userGroupIds array and modifiedBy in JSON
	 * @return result
	 */
	@Authorized
	@RequestMapping(value = "/usergrp", method = RequestMethod.DELETE, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String deleteMultipleUserGroups(@RequestBody String userGroupIds) {
		JSONObject responseJson = userMgmtService.deleteUserGroups(new JSONObject(userGroupIds));
		return responseJson.toString();
	}

	/**
	 * Method to get List of users to be associated for userGroups
	 * 
	 * @return list of Users
	 */
	@Authorized
	@RequestMapping(value = "/usergrp/users", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
	public String getUsers(){
		JSONObject responseJson = userMgmtService.getUsers();
		return responseJson.toString();
	}
	
}
