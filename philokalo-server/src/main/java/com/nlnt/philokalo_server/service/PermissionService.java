package com.nlnt.philokalo_server.service;

import java.util.List;

import com.nlnt.philokalo_server.dto.request.PermissionRequest;
import com.nlnt.philokalo_server.dto.response.PermissionResponse;

/**
 *
 * @author nghia
 */
public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

    PermissionResponse createPermission(PermissionRequest request);

    PermissionResponse updatePermission(String permissionId, PermissionRequest request);

    void deletePermission(String permissionId);
}
