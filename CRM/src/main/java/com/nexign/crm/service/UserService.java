package com.nexign.crm.service;

import com.nexign.crm.client.BrtClient;
import com.nexign.crm.dto.ClientDto;
import com.nexign.crm.dto.CreateUserRequest;
import com.nexign.crm.entity.UserEntity;
import com.nexign.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final BrtClient brtClient;

    @Transactional
    public ResponseEntity<?> createUser(CreateUserRequest request) {
        if (userRepository.existsByLogin(request.getMsisdn())) {
            return ResponseEntity.badRequest().body("A user with this number already exists");
        }

        UserEntity newUser = UserEntity.builder()
                .role("user")
                .login(request.getMsisdn())
                .build();

        ClientDto clientDto = brtClient.registerUser(request, new BigDecimal(100), LocalDateTime.now());

        if (clientDto == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Client not exist");
        }

        userRepository.save(newUser);
        return ResponseEntity.ok(clientDto);
    }

}
