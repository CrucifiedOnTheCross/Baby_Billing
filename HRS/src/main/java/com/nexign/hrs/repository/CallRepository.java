package com.nexign.hrs.repository;

import com.nexign.hrs.entity.CallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<CallEntity, Long> {
}
