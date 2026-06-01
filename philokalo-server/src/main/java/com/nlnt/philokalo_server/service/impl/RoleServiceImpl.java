package com.nlnt.philokalo_server.service.impl;

import com.nlnt.philokalo_server.dto.request.RoleRequest;
import com.nlnt.philokalo_server.dto.response.RoleResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.mapper.RoleMapper;
import com.nlnt.philokalo_server.model.Permission;
import com.nlnt.philokalo_server.model.Role;
import com.nlnt.philokalo_server.model.RolePermission;
import com.nlnt.philokalo_server.model.RolePermissionPK;
import com.nlnt.philokalo_server.repository.PermissionRepository;
import com.nlnt.philokalo_server.repository.RoleRepository;
import com.nlnt.philokalo_server.service.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nghia
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Transactional
    @Override
    public List<RoleResponse> getAllRoles() {
        var roles = roleRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_NAME_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(String roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId).orElseThrow(()
                -> new AppException(ErrorCode.ROLE_NOT_EXISTS));
        roleMapper.updateRole(role, request);
        roleRepository.save(role);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<RolePermission> rolePermissions = new HashSet<>();

            for (String permissionId : request.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));

                RolePermissionPK pk = new RolePermissionPK(role.getId(), permissionId);
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRolePermissionPK(pk);
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);

                rolePermissions.add(rolePermission);
            }

            role.setRolePermissionSet(rolePermissions);
            roleRepository.save(role);
        }

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(()
                -> new AppException(ErrorCode.ROLE_NOT_EXISTS));
        roleRepository.delete(role);
    }

}
