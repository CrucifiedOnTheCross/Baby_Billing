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
@Table(name = "tariffs")
public class TariffEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;

}