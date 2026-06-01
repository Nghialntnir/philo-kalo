package com.nlnt.philokalo_server.service.impl;

import com.nlnt.philokalo_server.dto.request.UserCreateRequest;
import com.nlnt.philokalo_server.dto.request.UserUpdateRequest;
import com.nlnt.philokalo_server.dto.response.PageResponse;
import com.nlnt.philokalo_server.dto.response.UserResponse;
import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.mapper.UserMapper;
import com.nlnt.philokalo_server.model.Role;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.model.UserRole;
import com.nlnt.philokalo_server.model.UserRolePK;
import com.nlnt.philokalo_server.repository.RoleRepository;
import com.nlnt.philokalo_server.repository.UserRepository;
import com.nlnt.philokalo_server.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author nghia
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by("createdAt").descending());
        Page<User> pageData = userRepository.findAll(pageable);

        return PageResponse.<UserResponse>builder()
                .content(pageData.getContent()
                        .stream()
                        .map(userMapper::toUserResponse)
                        .toList())
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .build();
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        User asignUser = userRepository.findByUsername(user.getUsername()).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTS));
        log.info(String.format("User: %s\nUser Assign: %s\nRole: %s", user, asignUser, role));
        UserRole userRole = new UserRole();
        Set<UserRole> userRoles = new HashSet<>();
        userRole.setUserRolePK(new UserRolePK(user.getId(), role.getId()));
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedBy(asignUser);
        userRoles.add(userRole);
        user.setUserRoleSet(userRoles);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User asignUser = userRepository.findByUsername(username).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<UserRole> userRoles = new HashSet<>();

            for (String roleId : request.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTS));

                UserRolePK pk = new UserRolePK(user.getId(), roleId);
                UserRole userRole = new UserRole();
                userRole.setUserRolePK(pk);
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setAssignedBy(asignUser);
                userRoles.add(userRole);
            }

            user.setUserRoleSet(userRoles);
            userRepository.save(user);
        }
        return userMapper.toUserResponse(user);
    }

}
