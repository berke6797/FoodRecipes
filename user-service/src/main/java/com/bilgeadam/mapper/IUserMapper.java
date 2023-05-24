package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDtoForAuth;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE= Mappers.getMapper(IUserMapper.class);
    UserProfile createUserProfileToUserProfile(final CreateUserRequestDto dto);

    /**
     * COMMENT SERVİSTE makeComment metodunda USER BİLGİLERİ İÇİN OLUŞTURULDU
     */
    GetUserProfileResponseDto getUserProfileFromUserProfile(final UserProfile userProfile);
}
