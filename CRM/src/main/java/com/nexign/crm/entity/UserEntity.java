package com.nexign.crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false, length = 15, unique = true)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;

}
