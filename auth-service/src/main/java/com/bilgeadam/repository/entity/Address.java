package com.bilgeadam.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Address extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String street;              // Sokak
    private String neighbourhood;       // Mahalle
    private String district;            // İlçe
    private String province;            // İl
    private String country;             // Ülke
    private String buildingNumber;      // Bina Numarası
    private String apartmentNumber;     // Daire Numarası
    private Integer zipCode;            // Posta Kodu
}
