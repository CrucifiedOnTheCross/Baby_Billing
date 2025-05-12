package com.nexign.hrs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "month_tariff_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthTariffHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String msisdn;

    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private TariffEntity tariff;

    @Column(name = "minute_balance", nullable = false)
    private Integer minuteBalance;

}
