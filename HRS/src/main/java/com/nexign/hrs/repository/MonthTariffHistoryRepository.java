package com.nexign.hrs.repository;

import com.nexign.hrs.entity.MonthTariffHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MonthTariffHistoryRepository extends JpaRepository<MonthTariffHistoryEntity, Long> {

    @Query("SELECT m FROM MonthTariffHistoryEntity m WHERE m.msisdn = ?1 AND m.periodEnd >= current_date ORDER BY m.periodStart DESC")
    Optional<MonthTariffHistoryEntity> findActivePeriod(String msisdn);

    @Query("SELECT m FROM MonthTariffHistoryEntity m WHERE m.msisdn = ?1 ORDER BY m.periodStart DESC")
    Optional<MonthTariffHistoryEntity> findPreviousPeriod(String msisdn);

    @Query("SELECT m FROM MonthTariffHistoryEntity m WHERE m.msisdn = ?1 ORDER BY m.periodEnd DESC")
    Optional<MonthTariffHistoryEntity> findLastPeriodByMsisdn(String msisdn);

    List<MonthTariffHistoryEntity> findAllByPeriodEnd(LocalDateTime periodEnd);

    @Query("SELECT m FROM MonthTariffHistoryEntity m WHERE m.periodEnd = :endDate")
    List<MonthTariffHistoryEntity> findByPeriodEnd(@Param("endDate") LocalDateTime endDate);


}
