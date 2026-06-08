package com.nlnt.philokalo_server.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.nlnt.philokalo_server.dto.request.PermissionRequest;
import com.nlnt.philokalo_server.dto.response.PermissionResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.mapper.PermissionMapper;
import com.nlnt.philokalo_server.model.Permission;
import com.nlnt.philokalo_server.repository.PermissionRepository;
import com.nlnt.philokalo_server.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author nghia
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PermissionServiceImpl implements PermissionService {

    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_NAME_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(String permissionId, PermissionRequest request) {
        Permission permission = permissionRepository
                .findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));
        permissionMapper.updatePermission(permission, request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public void deletePermission(String permissionId) {
        Permission permission = permissionRepository
                .findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));
        permissionRepository.delete(permission);
    }
}
