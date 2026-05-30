package com.nlnt.philokalo_server.mapper;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.request.UserUpdateRequest;
import com.nlnt.philokalo_server.dto.response.UserResponse;
import com.nlnt.philokalo_server.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 *
 * @author nghia
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
