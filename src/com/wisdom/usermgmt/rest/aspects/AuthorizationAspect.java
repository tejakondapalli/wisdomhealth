package com.wisdom.usermgmt.rest.aspects;

import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.AUTHORIZED_PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.MERGE_PERMISSIONS;
import static com.wisdom.usermgmt.rest.util.UserMgmtUtil.TEST_PERMISSIONS;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wisdom.usermgmt.rest.util.UserMgmtException;

/**
 * Aspect for Authorization of users by intercepting 
 * before the method calls based on Annotations 
 *  
 * @author RamuV
 */
@Aspect
@Component
public class AuthorizationAspect {

	@Autowired
	AuthorizationUtil authUtil;
	
	private final static String USERNAME_HEADER = "X-Logged-in-User"; 
	
	/**
	 * before advice for all methods annotated with @Authorized
	 *  
	 * @throws Throwable
	 */
	@Before("@annotation(Authorized)")
	public void processAuthorizedAnnotation() throws Throwable {
		
		// check whether user is authorized
		if(!authUtil.checkUserPermissions(getUserNameFromHeader(), AUTHORIZED_PERMISSIONS)){
			throw new UserMgmtException("You are not Authorized to perform requested Action");
		}
	}
	
	/**
	 * before advice for all methods annotated with @Test
	 *  
	 * @throws Throwable
	 */
	@Before("@annotation(Test)")
	public void processTestAnnotation() throws Throwable {
		
		// check whether user is authorized for Test
		if(!authUtil.checkUserPermissions(getUserNameFromHeader(), TEST_PERMISSIONS)){
			throw new UserMgmtException("You are not Authorized to perform Test");
		}
	}
	
	/**
	 * before advice for all methods annotated with @Merge
	 *  
	 * @throws Throwable
	 */
	@Before("@annotation(Merge)")
	public void processMergeAnnotation() throws Throwable {
		
		// check whether user is authorized for Release
		if(!authUtil.checkUserPermissions(getUserNameFromHeader(), MERGE_PERMISSIONS)){
			throw new UserMgmtException("You are not Authorized to perform Merge");
		}
	}
	
	private String getUserNameFromHeader(){
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String userName = request.getHeader(USERNAME_HEADER);
		
		if(userName == null || (userName != null && userName.length()==0)){
			throw new UserMgmtException("Please provide user name in the Header 'X-Logged-in-User' ");
		}
		return userName;
	}
}
