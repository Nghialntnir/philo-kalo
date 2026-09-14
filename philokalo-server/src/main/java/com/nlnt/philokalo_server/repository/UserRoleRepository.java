package com.nlnt.philokalo_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlnt.philokalo_server.model.UserRole;
import com.nlnt.philokalo_server.model.UserRolePK;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {

    List<UserRole> findByUserRolePKUserId(String userId);

    List<UserRole> findByUserRolePKRoleId(String roleId);

    List<UserRole> findByAssignedById(String assignedById);

    void deleteByUserRolePKUserId(String userId);
}
