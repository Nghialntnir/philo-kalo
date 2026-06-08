package com.nlnt.philokalo_server.model;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author nghia
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.findByUserId", query = "SELECT u FROM UserRole u WHERE u.userRolePK.userId = :userId"),
    @NamedQuery(name = "UserRole.findByRoleId", query = "SELECT u FROM UserRole u WHERE u.userRolePK.roleId = :roleId"),
    @NamedQuery(
            name = "UserRole.findByAssignedAt",
            query = "SELECT u FROM UserRole u WHERE u.assignedAt = :assignedAt"),
    @NamedQuery(name = "UserRole.findByCreatedAt", query = "SELECT u FROM UserRole u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UserRole.findByUpdatedAt", query = "SELECT u FROM UserRole u WHERE u.updatedAt = :updatedAt")
})
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected UserRolePK userRolePK;

    @Column(name = "assigned_at")
    @UpdateTimestamp
    private Instant assignedAt;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Role role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "assigned_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User assignedBy;
}
