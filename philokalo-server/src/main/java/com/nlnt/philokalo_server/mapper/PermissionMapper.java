package com.nlnt.philokalo_server.mapper;

import com.nlnt.philokalo_server.config.GlobalMapperConfig;
import com.nlnt.philokalo_server.dto.request.PermissionRequest;
import com.nlnt.philokalo_server.dto.response.PermissionResponse;
import com.nlnt.philokalo_server.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring", config = GlobalMapperConfig.class)
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);

}
