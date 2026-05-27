package com.nlnt.philokalo_server.service.impl;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nghia
 */
@Service
public class UserServiceImp implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setIsActive(Boolean.TRUE);
        userRepository.save(user);
        return user;
    }
}
