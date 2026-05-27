package com.nlnt.philokalo_server.service;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.model.User;

/**
 *
 * @author nghia
 */
public interface UserService {

    public User createUser(UserCreateRequest request);
}
