package com.nlnt.philokalo_server.security;

import com.nlnt.philokalo_server.exception.AppException;
import com.nlnt.philokalo_server.exception.ErrorCode;
import com.nlnt.philokalo_server.model.Permission;
import com.nlnt.philokalo_server.model.Role;
import com.nlnt.philokalo_server.model.RolePermission;
import com.nlnt.philokalo_server.model.RolePermissionPK;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.model.UserRole;
import com.nlnt.philokalo_server.model.UserRolePK;
import com.nlnt.philokalo_server.repository.PermissionRepository;
import com.nlnt.philokalo_server.repository.RoleRepository;
import com.nlnt.philokalo_server.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author nghia
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {

    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final PermissionRepository permissionRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            // 1. Seed Permissions
            if (permissionRepository.findByName("admin").isEmpty()) {
                permissionRepository.save(
                        Permission.builder()
                                .name("admin")
                                .description("All permission")
                                .build()
                );
            }

            if (permissionRepository.findByName("read-only").isEmpty()) {
                permissionRepository.save(
                        Permission.builder()
                                .name("read-only")
                                .description("Only view user")
                                .build()
                );
            }

            // 2. Seed Roles + gán Permission
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role role = Role.builder()
                        .name("ADMIN")
                        .description("Role Admin")
                        .build();
                roleRepository.save(role);

                Permission permission = permissionRepository.findByName("admin")
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));

                RolePermission rolePermission = new RolePermission();
                rolePermission.setRolePermissionPK(new RolePermissionPK(role.getId(), permission.getId()));
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);

                role.setRolePermissionSet(Set.of(rolePermission));
                roleRepository.save(role);
            }

            if (roleRepository.findByName("USER").isEmpty()) {
                Role role = Role.builder()
                        .name("USER")
                        .description("Role User")
                        .build();
                roleRepository.save(role);

                Permission permission = permissionRepository.findByName("read-only")
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTS));

                RolePermission rolePermission = new RolePermission();
                rolePermission.setRolePermissionPK(new RolePermissionPK(role.getId(), permission.getId()));
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);

                role.setRolePermissionSet(Set.of(rolePermission));
                roleRepository.save(role);
            }

            // 3. Seed admin User
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTS));

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("example999@gmail.com")
                        .build();
                userRepository.save(admin);

                UserRole userRole = new UserRole();
                userRole.setUserRolePK(new UserRolePK(admin.getId(), adminRole.getId()));
                userRole.setUser(admin);
                userRole.setRole(adminRole);
                userRole.setAssignedBy(admin); // tự assign cho chính mình

                admin.setUserRoleSet(Set.of(userRole));
                userRepository.save(admin);

                log.warn("Admin user created with default password: admin — please change it!");
            } else {
                log.info("Admin user already exists, skipping.");
            }
        };
    }
}
