package com.wisdom.usermgmt.rest.dao;

import static com.wisdom.usermgmt.rest.domain.User.GET_USERS_SQL;
import static com.wisdom.usermgmt.rest.domain.UserGroup.DELETE_USER_GROUPS_SQL;
import static com.wisdom.usermgmt.rest.domain.UserGroup.GET_USER_GROUPS_BY_USERID_SQL;
import static com.wisdom.usermgmt.rest.domain.UserGroup.GET_USER_GROUPS_SQL;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MODIFIED_BY;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_GROUP_IDS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.USER_NAME;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wisdom.usermgmt.rest.domain.User;
import com.wisdom.usermgmt.rest.domain.UserGroup;
import com.wisdom.usermgmt.rest.util.UserMgmtUtil;


/**
 * DAO class to provide CURD operations for UserGroups entities
 * 
 * @author RamuV
 */
@Repository
@Transactional
public class UserGroupDaoImpl implements UserGroupDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	UserMgmtUtil userMgmtUtil;
	
	/**
	 * Method to create new UserGroup along with users/roles, if assigned
	 * 
	 * @param userGroup entity
	 * @return userGroupId created 
	 */
	public int createUserGroup(UserGroup userGroup) {
		entityManager.persist(userGroup);
		entityManager.flush();
		
		return userGroup.getUserGroupId();
	}

	/**
	 * Method to return all userGroups (active only)
	 * 
	 * @return list of userGroup entities 
	 */
	public List<UserGroup> getUserGroups() {
		
		Query getUserGroupsQuery = entityManager.createNamedQuery(GET_USER_GROUPS_SQL);
		@SuppressWarnings("unchecked")
		List<UserGroup> userGroupsList = (List<UserGroup>)getUserGroupsQuery.getResultList();
		return userGroupsList;
	}

	/**
	 * Method to Update UserGroup along with users/roles modifications
	 * 
	 * @param userGroup entity
	 * @return
	 */
	public UserGroup updateUserGroup(UserGroup userGroup) {
		entityManager.merge(userGroup);
		entityManager.flush();
		return userGroup;
	}

	/**
	 * Method to delete User Group
	 * 
	 * @param userGroupId
	 * @param modifiedBy user
	 * @return result
	 */
	public int deleteUserGroup(int userGroupId, String modifiedBy) {
		int[] userGroupIds = {userGroupId};
		return deleteMultipleUserGroups(userGroupIds, modifiedBy);
	}

	/**
	 * Method to delete multiple UserGroups
	 * 
	 * @param userGroupIds array
	 * @param modifiedBy user
	 * @return result
	 */
	public int deleteMultipleUserGroups(int[] userGroupIds, String modifiedBy){
		
		List<Integer> userGroupIdsList = userMgmtUtil.arrayToList(userGroupIds);
		Query deleteUserGroupsQuery = entityManager.createNamedQuery(DELETE_USER_GROUPS_SQL);
		deleteUserGroupsQuery.setParameter(USER_GROUP_IDS, userGroupIdsList);
		deleteUserGroupsQuery.setParameter(MODIFIED_BY, modifiedBy);
		return deleteUserGroupsQuery.executeUpdate();
	}

	/**
	 * Helper method to get the UserGroups (to which a user is associated with) by given userId
	 * 
	 * @param userId
	 * @return userGroups
	 */
	public List<UserGroup> getUserGroupsByUserId(String userName){
		Query groupsOfUserQuery = entityManager.createNamedQuery(GET_USER_GROUPS_BY_USERID_SQL);
		groupsOfUserQuery.setParameter(USER_NAME, userName);
		
		@SuppressWarnings("unchecked")
		List<UserGroup> userGroups = (List<UserGroup>)groupsOfUserQuery.getResultList();
		return userGroups;
	}
	
	/**
	 * Method to return all users for authorization (active only)
	 * 
	 * @return list of user entities 
	 */
	public List<User> getUsers() {
		
		Query getUsersQuery = entityManager.createNamedQuery(GET_USERS_SQL);
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>)getUsersQuery.getResultList();
		return users;
	}
}
