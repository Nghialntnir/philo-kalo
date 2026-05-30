/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nlnt.philokalo_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author nghia
 */
@Entity
@Table(name = "role_permission")
@NamedQueries({
    @NamedQuery(name = "RolePermission.findAll", query = "SELECT r FROM RolePermission r"),
    @NamedQuery(name = "RolePermission.findByRoleId", query = "SELECT r FROM RolePermission r WHERE r.rolePermissionPK.roleId = :roleId"),
    @NamedQuery(name = "RolePermission.findByPermissionId", query = "SELECT r FROM RolePermission r WHERE r.rolePermissionPK.permissionId = :permissionId"),
    @NamedQuery(name = "RolePermission.findByCreatedAt", query = "SELECT r FROM RolePermission r WHERE r.createdAt = :createdAt")})
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolePermissionPK rolePermissionPK;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Permission permission;
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role role;

    public RolePermission() {
    }

    public RolePermission(RolePermissionPK rolePermissionPK) {
        this.rolePermissionPK = rolePermissionPK;
    }

    public RolePermission(String roleId, String permissionId) {
        this.rolePermissionPK = new RolePermissionPK(roleId, permissionId);
    }

    public RolePermissionPK getRolePermissionPK() {
        return rolePermissionPK;
    }

    public void setRolePermissionPK(RolePermissionPK rolePermissionPK) {
        this.rolePermissionPK = rolePermissionPK;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolePermissionPK != null ? rolePermissionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolePermission)) {
            return false;
        }
        RolePermission other = (RolePermission) object;
        if ((this.rolePermissionPK == null && other.rolePermissionPK != null) || (this.rolePermissionPK != null && !this.rolePermissionPK.equals(other.rolePermissionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nlnt.philokalo_server.model.RolePermission[ rolePermissionPK=" + rolePermissionPK + " ]";
    }

}
