package com.bilgeadam.repository.entity;

import com.bilgeadam.repository.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@SuperBuilder
public class UserProfile extends Base{
    @Id
    private String userId;
    private Long authId;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String street;              // Sokak
    private String neighbourhood;       // Mahalle
    private String district;            // İlçe
    private String province;            // İl
    private String country;             // Ülke
    private String buildingNumber;      // Bina Numarası
    private String apartmentNumber;     // Daire Numarası
    private Integer zipCode;            // Posta Kodu
    @Builder.Default
    private EStatus status=EStatus.PENDING;
}
