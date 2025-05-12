package com.nexign.brt.repository;

import com.nexign.brt.entity.CallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRepository extends JpaRepository<CallEntity, Long> {

    List<CallEntity> findCallEntitiesByStatusId(int statusId);

}
