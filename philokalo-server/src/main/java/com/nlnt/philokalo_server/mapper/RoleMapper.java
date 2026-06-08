package com.nlnt.philokalo_server.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.RoleRequest;
import com.nlnt.philokalo_server.dto.response.PermissionResponse;
import com.nlnt.philokalo_server.dto.response.PermissionSimpleResponse;
import com.nlnt.philokalo_server.dto.response.RoleResponse;
import com.nlnt.philokalo_server.dto.response.RoleSimpleResponse;
import com.nlnt.philokalo_server.model.Role;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface RoleMapper {

    @Mapping(target = "userRoleSet", ignore = true)
    @Mapping(target = "rolePermissionSet", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", expression = "java(mapPermissions(role))")
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", expression = "java(mapSimplePermissions(role))")
    RoleSimpleResponse toRoleSimpleResponse(Role role);

    void updateRole(@MappingTarget Role role, RoleRequest request);

    default Set<PermissionResponse> mapPermissions(Role role) {
        if (role.getRolePermissionSet() == null) {
            return new HashSet<>();
        }
        return role.getRolePermissionSet().stream()
                .map(rp -> PermissionResponse.builder()
                        .id(rp.getPermission().getId())
                        .name(rp.getPermission().getName())
                        .description(rp.getPermission().getDescription())
                        .createdAt(rp.getCreatedAt())
                        .updatedAt(rp.getUpdatedAt())
                        .build())
                .collect(Collectors.toSet());
    }

    default Set<PermissionSimpleResponse> mapSimplePermissions(Role role) {
        if (role.getRolePermissionSet() == null) {
            return new HashSet<>();
        }
        return role.getRolePermissionSet().stream()
                .map(rp -> PermissionSimpleResponse.builder()
                        .name(rp.getPermission().getName())
                        .build())
                .collect(Collectors.toSet());
    }
}
