package com.nlnt.philokalo_server.service;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.request.UserUpdateRequest;
import com.nlnt.philokalo_server.dto.response.UserResponse;

/**
 *
 * @author nghia
 */
public interface UserService {

    public UserResponse getUser(String userId);

    public UserResponse createUser(UserCreateRequest request);

    public UserResponse updateUser(String userId, UserUpdateRequest request);
}
