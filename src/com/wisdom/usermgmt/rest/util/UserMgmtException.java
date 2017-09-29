package com.wisdom.usermgmt.rest.util;

/**
 * User defined Exception class for User Management module
 *  
 * @author RamuV
 *
 */
public class UserMgmtException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserMgmtException(String msg){
		super(msg);
	}
}
