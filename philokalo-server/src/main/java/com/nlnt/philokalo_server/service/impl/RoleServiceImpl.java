package com.nlnt.philokalo_server.service.impl;

import com.nlnt.philokalo_server.dto.request.RoleRequest;
import com.nlnt.philokalo_server.dto.response.RoleResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.mapper.RoleMapper;
import com.nlnt.philokalo_server.model.Role;
import com.nlnt.philokalo_server.repository.RoleRepository;
import com.nlnt.philokalo_server.service.RoleService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
    RoleMapper roleMapper;

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
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(()
                -> new AppException(ErrorCode.ROLE_NOT_EXISTS));
        roleRepository.delete(role);
    }

}
