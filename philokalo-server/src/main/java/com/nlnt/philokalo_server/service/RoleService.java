package com.nlnt.philokalo_server.service;

import com.nlnt.philokalo_server.dto.request.RoleRequest;
import com.nlnt.philokalo_server.dto.response.RoleResponse;
import java.util.List;

/**
 *
 * @author nghia
 */
public interface RoleService {

    List<RoleResponse> getAllRoles();

    RoleResponse createRole(RoleRequest request);

    RoleResponse updateRole(String roleId, RoleRequest request);

    void deleteRole(String roleId);

}
