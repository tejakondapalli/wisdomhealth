package com.wisdom.usermgmt.rest.util;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;


@Repository
public class UserMgmtUtil {

	public static final String PERMISSION_ID = "permissionId";
	public static final String PERMISSION_IDS = "permissionIds"; 
	public static final String PERMISSION_NAME = "permissionName";
	public static final String PERMISSION_DESC = "permissionDesc";
	public static final String ROLE_ID = "roleId";
	public static final String ROLE_IDS = "roleIds";
	public static final String ROLE_NAME = "roleName";
	public static final String ROLE_DESC = "roleDesc";
	public static final String PERMISSIONS = "permissions";
	public static final String USER_GROUP_ID = "userGroupId";
	public static final String USER_GROUP_IDS = "userGroupIds";
	public static final String USER_GROUP_NAME = "userGroupName";
	public static final String USER_GROUP_DESC = "userGroupDesc";
	public static final String ROLES = "roles";
	public static final String USERS = "users";
	public static final String USER_GROUPS = "userGroups";
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String USER_FULL_NAME = "userFullName";
	public static final String USER_GROUP_USERS_ID = "userGroupUsersId";
	public static final String ACTIVE = "active";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_ON = "createdOn";
	public static final String MODIFIED_BY = "modifiedBy";
	public static final String LAST_UPDATED_ON = "lastUpdatedOn";
	public static final String MODIFY_USER = "modifyUser";
	public static final String ERROR_MSG = "errorMsg";
	public static final String DELETE_SUCCESS = "deleteSuccess";
	public static final String UPDATE_SUCCESS = "updateSuccess";
	public static final String IS_PERM_EXISTS = "isPermissionExists";
	public static final String IS_ROLE_EXISTS = "isRoleExists";
	
	public static final String TEST_PERMISSIONS = "testPermissions";
	public static final String AUTHORIZED_PERMISSIONS = "authorizedPermissions";
	public static final String MERGE_PERMISSIONS = "mergePermissions";
	
	
	/**
	 * Helper method to convert int[] to List<Integer> 
	 * @param intArray
	 * @return
	 */
	public List<Integer> arrayToList(int[] intArray){
		List<Integer> list = new ArrayList<Integer>();
		if(intArray == null)
			return list;
		for(int i=0; i< intArray.length; i++){
			list.add(intArray[i]);
		}
		return list;
	}
	
}
