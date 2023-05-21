package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE= Mappers.getMapper(IAuthMapper.class);

   // TODO:AUTH REGISTER İŞLEMLERİ İÇİN
    Auth registerRequestDtoToAuth(final RegisterRequestDto dto);
    RegisterResponseDto authToRegisterResponseDto(final Auth auth);


    // TODO: AKTİVASYON İŞLEMLERİ İÇİN




    // TODO: AUTH LOGIN İŞLEMLERİ İÇİN
    Auth loginRequestDtoToAuth(final LoginRequestDto dto);

}
