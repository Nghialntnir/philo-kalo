package com.nlnt.philokalo_server.mapper;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.request.UserUpdateRequest;
import com.nlnt.philokalo_server.dto.response.RoleSimpleResponse;
import com.nlnt.philokalo_server.dto.response.UserResponse;
import com.nlnt.philokalo_server.model.User;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);

    @Mapping(target = "roles", expression = "java(mapSimpleRoles(user))")
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    default Set<RoleSimpleResponse> mapSimpleRoles(User user) {
        if (user.getUserRoleSet() == null) {
            return new HashSet<>();
        }
        return user.getUserRoleSet().stream()
                .map(ur -> RoleSimpleResponse.builder()
                .name(ur.getRole().getName())
                .build())
                .collect(Collectors.toSet());
    }
}
