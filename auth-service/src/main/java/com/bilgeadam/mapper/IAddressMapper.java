package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.UpdateUserForAuthAndAddressDto;
import com.bilgeadam.repository.entity.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface IAddressMapper {
    IAddressMapper INSTANCE = Mappers.getMapper(IAddressMapper.class);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address updateFromUser(final UpdateUserForAuthAndAddressDto dto, @MappingTarget Address address);

    Address saveAddress(final UpdateUserForAuthAndAddressDto dto);
}
