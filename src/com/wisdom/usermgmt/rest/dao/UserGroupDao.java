package com.wisdom.usermgmt.rest.dao;

import java.util.List;

import com.wisdom.usermgmt.rest.domain.User;
import com.wisdom.usermgmt.rest.domain.UserGroup;

/**
 * DAO to provide CURD operations for USerGroups entities
 * 
 * @author RamuV
 */
public interface UserGroupDao {

	/**
	 * Method to create new UserGroup along with users/roles, if assigned
	 * 
	 * @param userGroup entity
	 * @return userGroupId created 
	 */
	public int createUserGroup(UserGroup userGroup);
	
	/**
	 * Method to return all userGroups (active only)
	 * 
	 * @return list of userGroup entities 
	 */
	public List<UserGroup> getUserGroups();
	
	/**
	 * Method to Update UserGroup along with users/roles modifications
	 * 
	 * @param userGroup entity
	 * @return
	 */
	public UserGroup updateUserGroup(UserGroup userGroup);
	
	/**
	 * Method to delete User Group
	 * 
	 * @param userGroupId
	 * @param modifiedBy user
	 * @return result
	 */
	public int deleteUserGroup(int userGroupId, String modifiedBy);
	
	/**
	 * Method to delete multiple UserGroups
	 * 
	 * @param userGroupIds array
	 * @param modifiedBy user
	 * @return result
	 */
	public int deleteMultipleUserGroups(int[] userGroupIds, String modifiedBy);
	
	/**
	 * Method to get user groups (its roles and permissions) of a user by given userId(i.e A-number)
	 * used for Authorization of user
	 * 
	 * @param userId
	 */
	public List<UserGroup> getUserGroupsByUserId(String userId);
	
	/**
	 * Method to get users to check logged in person for Authorization
	 * used for Authorization of user
	 * 
	 * @returns user entity
	 */
	public List<User> getUsers();
	
}
