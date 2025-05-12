package com.nexign.cdr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "call")
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", length = 1)
    private String type;

    @Column(name = "msisdn_client", length = 20)
    private String clientMsisdn;

    @Column(name = "msisdn_partner", length = 20)
    private String partnerMsisdn;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

}
