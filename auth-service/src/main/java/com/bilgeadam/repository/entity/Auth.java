package com.bilgeadam.repository.entity;

import com.bilgeadam.repository.entity.enums.ERole;
import com.bilgeadam.repository.entity.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Auth extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;
    private Long addressId;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String username;
    private String activationCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status=EStatus.PENDING;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERole role=ERole.USER;
}
