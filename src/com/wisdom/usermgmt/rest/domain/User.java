package com.wisdom.usermgmt.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class for table 'tblPermissions'
 * 
 * @author RamuV
 */

@Entity
@Table(name = "tblUsers")
@NamedQuery(name = "User.getAllUsers", query = "SELECT u FROM User u ORDER BY u.userId ASC")

public class User {

	//private static final long serialVersionUID = 1L;

	public static final String GET_USERS_SQL = "User.getAllUsers";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "userName")
	private String userName;
	
	@Column(name = "userFullName")
	private String userFullName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}	
	
}
