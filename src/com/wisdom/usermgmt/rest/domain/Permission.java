package com.wisdom.usermgmt.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity class for table 'tblPermissions'
 * 
 * @author RamuV
 */

@Entity
@Table(name = "tblPermissions")
@NamedQueries({
	@NamedQuery(name = "Permission.getAllPermissions", query = "SELECT p FROM Permission p ORDER BY p.permissionId DESC"),
	@NamedQuery(name = "Permission.getPermissionByName", query = "SELECT p FROM Permission p WHERE p.permissionName = :permissionName ")
})
public class Permission extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String GET_PERMISSIONS_SQL = "Permission.getAllPermissions";
	public static final String GET_PERMISSION_BY_NAME_SQL = "Permission.getPermissionByName";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permissionId")
	private int permissionId;
	
	@Column(name = "permissionName")
	private String permissionName;
	
	@Column(name = "permissionDesc")
	private String permissionDesc;

	/*@ManyToMany(mappedBy="permissions")
	private Set<Role> role = new HashSet<Role>(); */
	
	public int getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionDesc() {
		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}

	/*public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}*/

}
