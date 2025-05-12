package com.nexign.hrs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", columnDefinition = "CHAR(2)", nullable = false)
    private String type;

    @Column(name = "msisdn_client", length = 15, nullable = false)
    private String msisdnClient;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private TariffEntity tariff;

    @Column(name = "is_partner_client", nullable = false)
    private Boolean isPartnerClient;

    @Column(nullable = false, precision = 10, scale = 1)
    private BigDecimal money;
}
