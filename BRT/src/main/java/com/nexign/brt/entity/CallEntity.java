package com.nexign.brt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calls")
public class CallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calls_id_gen")
    @SequenceGenerator(name = "calls_id_gen", sequenceName = "calls_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false, length = 2)
    private String type;

    @Column(name = "msisdn_client", nullable = false, length = 15)
    private String msisdnClient;

    @Column(name = "msisdn_partner", nullable = false, length = 15)
    private String msisdnPartner;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "is_partner_client", nullable = false)
    private Boolean isPartnerClient = false;

    @Column(name = "id_status", nullable = false)
    private Short statusId;

}