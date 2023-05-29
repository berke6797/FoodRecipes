package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.repository.entity.Address;
import com.bilgeadam.repository.entity.Auth;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);

    /**
     * AUTH REGISTER İŞLEMLERİ İÇİN
     */
    Auth registerRequestDtoToAuth(final RegisterRequestDto dto);

    RegisterResponseDto authToRegisterResponseDto(final Auth auth);

    /**
     * Auth'ta register olan user'a aktarılsın diye yazıldı
     */
    CreateUserRequestDto fromAuthToCreateUserRequestDto(final Auth auth);

    /**
     * AUTH LOGIN İŞLEMLERİ İÇİN
     */
    Auth loginRequestDtoToAuth(final LoginRequestDto dto);

    /**
     * REGISTER'IN AKTIVASYON MAILI İÇİN YAZILDI
     */
    RegisterMailModel authToRegisterMailModel(final Auth auth);

    /**
     * CHANGEPASSWORD'ÜN AUTH KISMI İÇİN OLUŞTURULDU
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auth passwordChangeRequestDtoToAuth(PasswordChangeRequestDtoForAuth dto,@MappingTarget Auth auth);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Auth updateFromUser(final UpdateUserForAuthAndAddressDto dto,@MappingTarget Auth auth);

}
