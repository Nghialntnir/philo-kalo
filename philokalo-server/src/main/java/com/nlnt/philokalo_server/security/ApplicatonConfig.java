package com.nlnt.philokalo_server.security;

import com.nlnt.philokalo_server.enums.Role;
import com.nlnt.philokalo_server.model.User;
import com.nlnt.philokalo_server.repository.UserRepository;
import java.util.HashSet;
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
public class ApplicatonConfig {

    final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return agrs -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("example999@gmail.com")
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it!");
            }
        };
    }
}
