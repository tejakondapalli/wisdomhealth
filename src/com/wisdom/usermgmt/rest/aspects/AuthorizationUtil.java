package com.wisdom.usermgmt.rest.aspects;

import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.AUTHORIZED_PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MERGE_PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.TEST_PERMISSIONS;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wisdom.usermgmt.rest.dao.UserGroupDao;
import com.wisdom.usermgmt.rest.domain.Permission;
import com.wisdom.usermgmt.rest.domain.Role;
import com.wisdom.usermgmt.rest.domain.UserGroup;
import com.wisdom.usermgmt.rest.util.UserMgmtException;

/**
 * Utility class for Authorization of users 
 * 
 * @author RamuV
 *
 */
@Component
public class AuthorizationUtil {

	@Autowired
	UserGroupDao userGroupDao;
	
	@Value("${usermgmt.permissions.Authorized}")
	private String authorizedPermissions;
	
	@Value("${usermgmt.permissions.Test}")
	private String testPermissions;
	
	@Value("${usermgmt.permissions.Merge}")
	private String mergePermissions;
	
	private String[] authorizedPermissionsArr = {};
	private String[] testPermissionsArr = {};
	private String[] mergePermissionsArr = {};

	boolean initPermissions = false;
	
	/**
	 * Helper Method to get the UserGrops of user and then authorize the user
	 *  
	 * @param userName
	 * @param permissionType - which type of permissions to check
	 */
	public boolean checkUserPermissions(String userName, String permissionType){
		// get user groups of the user
		List<UserGroup> groupsOfUser = userGroupDao.getUserGroupsByUserId(userName);
		if(groupsOfUser.size() == 0){
			throw new UserMgmtException("You don't belong to any User Group. Please contact Administrator.");
		}
		String[] allowedPermissions = getPermissionsArray(permissionType);
		return checkUserPermissions(allowedPermissions, groupsOfUser);
		
	}
	
	/**
	 * Helper method to iterate over userGroups and its Permissions 
	 * and check whether user has any of the allowedPermissions
	 * 
	 * @param allowedPermissions
	 * @param userGroups
	 * @return boolean
	 */
	private boolean checkUserPermissions(String[] allowedPermissions, List<UserGroup> userGroups){
		// go through all user groups and its permissions and verify
		// whether the user is authorized or not by checking against allowedPermissions
		boolean isUserHasPermission = false;
		
		if(allowedPermissions != null){

			groupsLoop: 
			for(UserGroup group : userGroups){
				for(Role role : group.getRoles()){
					for(Permission permission : role.getPermissions()){
						if(Arrays.stream(allowedPermissions).
								anyMatch(x -> (permission.getPermissionName()).equals(x))){
							isUserHasPermission = true;
							break groupsLoop; // If any permission matches , no need to check others
						}
					}
				}
			}
		}
		
		return isUserHasPermission;
	}
	
	private String[] getPermissionsArray(String permissionType){
		initAllPermissions();
		
		switch(permissionType){
			case AUTHORIZED_PERMISSIONS : return authorizedPermissionsArr;
			case TEST_PERMISSIONS : return testPermissionsArr;
			case MERGE_PERMISSIONS : return mergePermissionsArr;
		}
		return null;
	}
	
	private void initAllPermissions(){
		if (!initPermissions) {
			if (authorizedPermissions != null) {
				authorizedPermissionsArr = authorizedPermissions.split(",");
			}
			if (testPermissions != null) {
				testPermissionsArr = testPermissions.split(",");
			}
			if (mergePermissions != null) {
				mergePermissionsArr = mergePermissions.split(",");
			}
			initPermissions = true;
		}
	}
}
