package com.wisdom.usermgmt.rest.dao;

import static com.wisdom.usermgmt.rest.domain.Permission.GET_PERMISSIONS_SQL;
import static com.wisdom.usermgmt.rest.domain.Permission.GET_PERMISSION_BY_NAME_SQL;
import static com.wisdom.usermgmt.rest.domain.Role.DELETE_ROLES_SQL;
import static com.wisdom.usermgmt.rest.domain.Role.GET_ROLES_SQL;
import static com.wisdom.usermgmt.rest.domain.Role.GET_ROLE_BY_NAME_SQL;
import static com.wisdom.usermgmt.rest.domain.UserGroup.GET_USER_GROUPS_BY_ROLE_SQL;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MODIFIED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.PERMISSION_NAME;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_ID;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_IDS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.ROLE_NAME;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wisdom.usermgmt.rest.domain.Permission;
import com.wisdom.usermgmt.rest.domain.Role;
import com.wisdom.usermgmt.rest.util.UserMgmtException;
import com.wisdom.usermgmt.rest.util.UserMgmtUtil;


/**
 * DAO class to provide CURD operations for Roles, Permissions entities
 * 
 * @author RamuV
 *
 */
@Repository
@Transactional
public class ConfigMgmtDaoImpl implements ConfigMgmtDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	UserMgmtUtil userMgmtUtil;
	
	static Logger logger = LoggerFactory.getLogger(ConfigMgmtDaoImpl.class);
	
	/**
	 * Method to create new Permission
	 * 
	 * @param permission entity
	 * @return permissionId newly created
	 * @throws UserMgmtException, if permissionName already exists
	 */
	public int createPermission(Permission permission) {
		
		if(!checkPermissionExists(permission.getPermissionName())){
			entityManager.persist(permission);
			entityManager.flush();
			
			return permission.getPermissionId();
		}else{
			throw new UserMgmtException("Permission \'" + permission.getPermissionName() + "\' already Exists.");
		}
	}

	/**
	 * Method to get all Permissions
	 * 
	 * @return list of active Permissions 
	 */
	public List<Permission> getPermissions() {
		
		Query getPermissionsQuery = entityManager.createNamedQuery(GET_PERMISSIONS_SQL);
		@SuppressWarnings("unchecked")
		List<Permission> permissionsList = (List<Permission>)getPermissionsQuery.getResultList();
		return permissionsList;
	}

	/**
	 * Method to check whether a permission exists with a given permissionName
	 * 
	 * @param permissionName
	 * @return true/false
	 */
	public boolean checkPermissionExists(String permissionName){
		
		Permission permission = getPermissionByName(permissionName);
		if(permission != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Method to get Permission by name
	 * 
	 * @param permissionName
	 * @return Permission
	 */
	private Permission getPermissionByName(String permissionName){
		Permission permission = null;
		Query getPermissionQuery = entityManager.createNamedQuery(GET_PERMISSION_BY_NAME_SQL);
		getPermissionQuery.setParameter(PERMISSION_NAME, permissionName);
		try{
			permission = (Permission)getPermissionQuery.getSingleResult();	
		}catch(NoResultException nre ){
			logger.error("No Permission found with Permission name : " + permissionName);
		}
		return permission;
	}
	////////////////////
	/**
	 * Method to create new Role
	 * 
	 * @param role entity
	 * @return roleId newly created
	 * @throws UserMgmtException, if roleName already exists 
	 */
	public int createRole(Role role) throws UserMgmtException {
		Role roleDb = getRoleByName(role.getRoleName());
		
		if(roleDb == null){
			entityManager.persist(role);
			entityManager.flush();
			return role.getRoleId();
		}else{
			throw new UserMgmtException("Role \'" + role.getRoleName() + "\' already Exists.");
		}
	}

	/**
	 * Method to get all Roles
	 * 
	 * @return list of active Roles
	 */
	public List<Role> getRoles() {
		
		Query getRolesQuery = entityManager.createNamedQuery(GET_ROLES_SQL);
		@SuppressWarnings("unchecked")
		List<Role> rolesList = (List<Role>)getRolesQuery.getResultList();
		return rolesList;
	}

	/**
	 * Method to check whether a role exists with a given roleName
	 * 
	 * @param roleName
	 * @return true/false
	 */
	public boolean checkRoleExists(String roleName){
		
		Role role = getRoleByName(roleName);
		if(role != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Helper method to get Role by roleName
	 * 
	 * @param roleName
	 * @return Role
	 */
	private Role getRoleByName(String roleName){
		Role role = null;
		Query getRoleQuery = entityManager.createNamedQuery(GET_ROLE_BY_NAME_SQL);
		getRoleQuery.setParameter(ROLE_NAME, roleName);
		try{
			role = (Role)getRoleQuery.getSingleResult();	
		}catch(NoResultException nre ){
			logger.error("No Role found with role name : " + roleName);
		}
		return role;
	}
	
	/**
	 * Method to update a Role
	 *  
	 * @param role entity
	 * @return  
	 */
	public void updateRole(Role role) {
		entityManager.merge(role);
		entityManager.flush();
	}
	
	/**
	 * Method to delete a role. 
	 * Role will not be allowed to delete if this role is assigned with UserGroup  
	 * 
	 * @param roleId
	 * @param modifiedBy user
	 * @return result
	 * @throws UserMgmtException, if the role is assigned with any userGroup
	 */
	public int deleteRole(int roleId, String modifiedBy){
		int[] roleIds = {roleId};
		return deleteMultipleRoles(roleIds, modifiedBy);
	}
	
	/**
	 * Method to delete multiple roles. 
	 * Role will not be allowed to delete if this role is assigned with UserGroup  
	 * 
	 * @param roleIds array
	 * @param modifiedBy user
	 * @return result
	 * @throws UserMgmtException, if any of the role is assigned with any userGroup
	 */
	public int deleteMultipleRoles(int[] roleIds, String modifiedBy){
		
		for (int roleId : roleIds) {
			List<String> groups = getUserGroupsByRoleId(roleId);
			if (groups.size() > 0) {
				throw new UserMgmtException("Action Denied. Role already assigned to User Group(s) \n" + groups);
			}
		}
		
		List<Integer> roleIdsList = userMgmtUtil.arrayToList(roleIds);
		Query deleteRolesQuery = entityManager.createNamedQuery(DELETE_ROLES_SQL);
		
		deleteRolesQuery.setParameter(ROLE_IDS, roleIdsList);
		deleteRolesQuery.setParameter(MODIFIED_BY, modifiedBy);
		int noOfRecsDeleted = deleteRolesQuery.executeUpdate();
		return noOfRecsDeleted;
	}

	/**
	 * Helper method to get the UserGroups (to which a role is associated with) by given roleId
	 * 
	 * @param roleId
	 * @return GroupNames CSV
	 */
	private List<String> getUserGroupsByRoleId(int roleId){
		Query groupsOfRoleQuery = entityManager.createNamedQuery(GET_USER_GROUPS_BY_ROLE_SQL);
		groupsOfRoleQuery.setParameter(ROLE_ID, roleId);
		
		@SuppressWarnings("unchecked")
		List<String> userGroups = (List<String>)groupsOfRoleQuery.getResultList();
		return userGroups;
	}
}
