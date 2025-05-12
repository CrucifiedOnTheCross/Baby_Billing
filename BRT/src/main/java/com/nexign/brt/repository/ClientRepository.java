package com.nexign.brt.repository;

import com.nexign.brt.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {

    Optional<ClientEntity> findClientByMsisdn(String msisdn);

    boolean existsByMsisdn(String owner);

}
