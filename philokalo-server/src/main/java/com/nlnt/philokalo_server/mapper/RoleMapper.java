package com.nlnt.philokalo_server.mapper;

import com.nlnt.philokalo_server.dto.request.RoleRequest;
import com.nlnt.philokalo_server.dto.response.RoleResponse;
import com.nlnt.philokalo_server.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "userRoleSet", ignore = true)
    @Mapping(target = "rolePermissionSet", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    void updateRole(@MappingTarget Role role, RoleRequest request);

}
