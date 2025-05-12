package com.nexign.cdr.repository;

import com.nexign.cdr.entity.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findTop10ByOrderByStartTimeAsc();
}
