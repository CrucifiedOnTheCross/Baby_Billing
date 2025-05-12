package com.nexign.brt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "msisdn", referencedColumnName = "msisdn", nullable = false)
    private ClientEntity msisdn;

    @Column(name = "money", nullable = false, precision = 10, scale = 1)
    private BigDecimal money;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}