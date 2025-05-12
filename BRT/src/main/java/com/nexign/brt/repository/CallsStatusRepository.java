package com.nexign.brt.repository;

import com.nexign.brt.entity.CallsStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallsStatusRepository extends JpaRepository<CallsStatusEntity, Short> {
}
