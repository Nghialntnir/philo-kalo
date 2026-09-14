package com.nlnt.philokalo_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.RolePermission;
import com.nlnt.philokalo_server.model.RolePermissionPK;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPK> {

    List<RolePermission> findByRolePermissionPKRoleId(String roleId);

    List<RolePermission> findByRolePermissionPKPermissionId(String permissionId);

    void deleteByRolePermissionPKRoleId(String roleId);
}
