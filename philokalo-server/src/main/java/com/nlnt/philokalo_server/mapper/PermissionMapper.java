package com.nlnt.philokalo_server.mapper;

import com.nlnt.philokalo_server.dto.request.PermissionRequest;
import com.nlnt.philokalo_server.dto.response.PermissionResponse;
import com.nlnt.philokalo_server.model.Permission;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermission(@MappingTarget Permission permission, PermissionRequest request);

}
