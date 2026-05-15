package com.nlnt.philokalo_server.service.impl;

import com.nlnt.philokalo_server.dto.request.UserCreateDtoRequest;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nghia
 */
@Service
public class UserServiceImp {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreateDtoRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        
        userRepository.save(user);
        return user;
    }
}
