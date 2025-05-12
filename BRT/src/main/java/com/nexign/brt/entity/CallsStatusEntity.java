package com.nexign.brt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calls_statuses")
public class CallsStatusEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

}