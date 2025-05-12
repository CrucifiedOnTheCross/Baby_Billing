package com.nexign.crm.config;

import com.nexign.crm.entity.UserEntity;
import com.nexign.crm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class AdminInitializer {

    @Value("${admin.username}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByLogin(adminLogin).isEmpty()) {
                UserEntity admin = UserEntity.builder()
                        .login(adminLogin)
                        .role("manager")
                        .password(passwordEncoder.encode(adminPassword))
                        .build();
                userRepository.save(admin);
                log.info("Admin user created: {}", adminLogin);
            }
        };
    }
}
