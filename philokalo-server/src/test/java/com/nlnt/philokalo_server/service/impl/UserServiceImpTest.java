package com.nlnt.philokalo_server.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.response.UserResponse;
import com.nlnt.philokalo_server.mapper.UserMapper;
import com.nlnt.philokalo_server.model.Role;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.RoleRepository;
import com.nlnt.philokalo_server.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    @Test
    void createUser_shouldKeepProvidedAvatarUrl() {
        String customAvatarUrl = "https://cdn.example.com/users/alice.png";
        UserCreateRequest request = UserCreateRequest.builder()
                .username("alice")
                .password("Abcdef1!")
                .email("alice@example.com")
                .avatarUrl(customAvatarUrl)
                .fullName("Alice")
                .build();

        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode("Abcdef1!")).thenReturn("encoded-password");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        Role userRole = new Role();
        userRole.setId("role-1");
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userMapper.toUserResponse(user)).thenReturn(UserResponse.builder()
                .username("alice")
                .avatarUrl(customAvatarUrl)
                .build());

        UserResponse response = userService.createUser(request);

        assertEquals(customAvatarUrl, user.getAvatarUrl());
        assertEquals(customAvatarUrl, response.getAvatarUrl());
    }

    @Test
    void createUser_shouldFallbackToDefaultAvatarWhenRequestDoesNotProvideOne() {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("bob")
                .password("Abcdef1!")
                .email("bob@example.com")
                .avatarUrl("   ")
                .fullName("Bob")
                .build();

        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");

        when(userRepository.existsByUsername("bob")).thenReturn(false);
        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode("Abcdef1!")).thenReturn("encoded-password");
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));

        Role userRole = new Role();
        userRole.setId("role-2");
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userMapper.toUserResponse(user)).thenReturn(UserResponse.builder()
                .username("bob")
                .avatarUrl(UserServiceImp.DEFAULT_AVATAR_URL)
                .build());

        UserResponse response = userService.createUser(request);

        assertEquals(UserServiceImp.DEFAULT_AVATAR_URL, user.getAvatarUrl());
        assertEquals(UserServiceImp.DEFAULT_AVATAR_URL, response.getAvatarUrl());
    }
}
