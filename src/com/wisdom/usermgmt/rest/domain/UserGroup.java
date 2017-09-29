package com.wisdom.usermgmt.rest.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.persistence.Table;

/**
 * Entity class for table 'tblUserGroups'
 * 
 * @author RamuV
 */
@Entity
@Table(name = "tblUserGroups")
@NamedQueries({
	@NamedQuery(name = "UserGroup.getAllUserGroup", query = "SELECT u FROM UserGroup u WHERE u.active=1 ORDER BY u.userGroupId DESC "),
	@NamedQuery(name = "UserGroup.deleteUserGroups", query = "UPDATE UserGroup u SET u.active = 0, u.modifiedBy= :modifiedBy "
			+ "where u.userGroupId in (:userGroupIds)"),
	@NamedQuery(name = "UserGroup.getUserGroupsOfRole", query = "SELECT u.userGroupName FROM UserGroup u join u.roles r WHERE r.roleId = :roleId"),
	@NamedQuery(name = "UserGroup.getUserGroupsOfUser", query = "SELECT ug FROM UserGroup ug join ug.users u WHERE u.userName = :userName")

})

public class UserGroup extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String GET_USER_GROUPS_SQL = "UserGroup.getAllUserGroup";
	public static final String DELETE_USER_GROUPS_SQL = "UserGroup.deleteUserGroups";
	public static final String GET_USER_GROUPS_BY_ROLE_SQL = "UserGroup.getUserGroupsOfRole";
	public static final String GET_USER_GROUPS_BY_USERID_SQL = "UserGroup.getUserGroupsOfUser";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userGroupId")
	private int userGroupId;
	
	@Column(name="userGroupName")
	private String userGroupName;
	
	@Column(name="userGroupDesc")
	private String userGroupDesc;

	// Mapping to Roles via tblUserGroupRoles
	@ManyToMany
	@JoinTable(
			name = "tblUserGroupRoles",
			joinColumns = @JoinColumn(name = "fkUserGroupId"),
			inverseJoinColumns = @JoinColumn(name = "fkRoleId")
			)
	private Set<Role> roles = new HashSet<Role>();
	
	// Mapping to Roles via tblUserGroupUsers
	@ManyToMany
	@JoinTable(
			name = "tblUserGroupUsers",
			joinColumns = @JoinColumn(name = "fkUserGroupId"),
			inverseJoinColumns = @JoinColumn(name = "fkUserId")
			)
	private Set<User> users = new HashSet<User>();

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getUserGroupDesc() {
		return userGroupDesc;
	}

	public void setUserGroupDesc(String userGroupDesc) {
		this.userGroupDesc = userGroupDesc;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRoles(Role role){
		this.roles.add(role);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void addUsers(User user) {
		this.users.add(user);
	}
}
