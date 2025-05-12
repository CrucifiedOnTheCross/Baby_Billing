package com.nexign.crm.repository;

import com.nexign.crm.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String username);

    boolean existsByLogin(String msisdn);

}
