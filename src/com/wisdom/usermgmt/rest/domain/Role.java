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
 * Entity class for table 'tblRoles'
 * 
 * @author RamuV
 */
@Entity
@Table(name = "tblRoles")
@NamedQueries({
	@NamedQuery(name = "Role.getAllRoles", query = "SELECT r FROM Role r WHERE r.active=1 ORDER BY r.roleId DESC"),
	@NamedQuery(name = "Role.deleteRoles", query = "UPDATE Role r SET r.active = 0, r.modifiedBy= :modifiedBy"
			+ " where r.roleId in (:roleIds) "),
	@NamedQuery(name = "Role.getRoleByName", query = "SELECT r FROM Role r WHERE r.roleName = :roleName ")
})
public class Role extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String GET_ROLES_SQL = "Role.getAllRoles";
	public static final String DELETE_ROLES_SQL = "Role.deleteRoles";
	public static final String GET_ROLE_BY_NAME_SQL = "Role.getRoleByName";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="roleId")
	private int roleId;
	
	@Column(name="roleName")
	private String roleName;
	
	@Column(name="roleDesc")
	private String roleDesc;
	
	// Mapping to permissions table via tblRolePermissions table
	@ManyToMany
	@JoinTable(
			name = "tblRolePermissions",
			joinColumns = @JoinColumn(name = "fkRoleId"),
			inverseJoinColumns = @JoinColumn(name = "fkPermissionId")
			)
	private Set<Permission> permissions = new HashSet<Permission>();
	
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	} 
		
	public void addPermissions(Permission permission) {
		this.permissions.add(permission);
	}

}
