package com.wisdom.usermgmt.rest.dao;

import java.util.List;

import com.wisdom.usermgmt.rest.domain.Permission;
import com.wisdom.usermgmt.rest.domain.Role;
import com.wisdom.usermgmt.rest.util.UserMgmtException;

/**
 * DAO to provide CURD operations for Roles, Permissions entities
 * 
 * @author RamuV
 *
 */
public interface ConfigMgmtDao {

	/**
	 * Method to create new Permission
	 * 
	 * @param permission entity
	 * @return permissionId newly created
	 * @throws UserMgmtException, if permissionName already exists
	 */
	public int createPermission(Permission permission) throws UserMgmtException;
	
	/**
	 * Method to get all Permissions
	 * 
	 * @return list of active Permissions 
	 */
	public List<Permission> getPermissions();
	
	/**
	 * Method to check whether a permission exists with a given permissionName
	 * 
	 * @param permissionName
	 * @return true/false
	 */
	public boolean checkPermissionExists(String permissionName);
	
	/**
	 * Method to create new Role
	 * 
	 * @param role entity
	 * @return roleId newly created
	 * @throws UserMgmtException, if roleName already exists
	 */
	public int createRole(Role role) throws UserMgmtException;
	
	/**
	 * Method to get all Roles
	 * 
	 * @return list of active Roles
	 */
	public List<Role> getRoles();
	
	/**
	 * Method to check whether a role exists with a given roleName
	 * 
	 * @param roleName
	 * @return true/false
	 */
	public boolean checkRoleExists(String roleName);
	
	/**
	 * Method to update a Role
	 *  
	 * @param role entity
	 * @return 
	 */
	public void updateRole(Role role);

	/**
	 * Method to delete a role.
	 * Role will not be allowed to delete if this role is assigned with UserGroup
	 * 
	 * @param roleId
	 * @param modifiedBy user
	 * @return result
	 * @throws UserMgmtException, if the role is assigned with any userGroup
	 */
	public int deleteRole(int roleId, String modifiedBy) throws UserMgmtException;
	
	/**
	 * Method to delete multiple roles.
	 * Role will not be allowed to delete if any role is assigned with UserGroup  
	 * 
	 * @param roleIds array
	 * @param modifiedBy user
	 * @return result
	 * @throws UserMgmtException, if any of the role is assigned with any userGroup
	 */
	public int deleteMultipleRoles(int[] roleIds, String modifiedBy) throws UserMgmtException;
	
}
