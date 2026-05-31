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
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author nghia
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_permission")
@NamedQueries({
    @NamedQuery(name = "RolePermission.findAll", query = "SELECT r FROM RolePermission r"),
    @NamedQuery(name = "RolePermission.findByRoleId", query = "SELECT r FROM RolePermission r WHERE r.rolePermissionPK.roleId = :roleId"),
    @NamedQuery(name = "RolePermission.findByPermissionId", query = "SELECT r FROM RolePermission r WHERE r.rolePermissionPK.permissionId = :permissionId"),
    @NamedQuery(name = "RolePermission.findByUpdatedAt", query = "SELECT r FROM RolePermission r WHERE r.updatedAt = :updatedAt"),
    @NamedQuery(name = "RolePermission.findByCreatedAt", query = "SELECT r FROM RolePermission r WHERE r.createdAt = :createdAt")})
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolePermissionPK rolePermissionPK;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    Instant updatedAt;
    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Permission permission;
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role role;

}
