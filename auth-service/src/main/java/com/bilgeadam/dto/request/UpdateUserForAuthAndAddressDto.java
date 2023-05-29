package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserForAuthAndAddressDto {
    private Long authId;
    private String name;
    private String surname;
    private String street;              // Sokak
    private String neighbourhood;       // Mahalle
    private String district;            // İlçe
    private String province;            // İl
    private String country;             // Ülke
    private String buildingNumber;      // Bina Numarası
    private String apartmentNumber;     // Daire Numarası
    private Integer zipCode;            // Posta Kodu
}
