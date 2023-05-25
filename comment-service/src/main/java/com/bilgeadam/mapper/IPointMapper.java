package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.dto.request.UpdatePointRequestDto;
import com.bilgeadam.repository.entity.Point;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface IPointMapper {
    IPointMapper INSTANCE= Mappers.getMapper(IPointMapper.class);
    Point givePointRequestDtoToPoint(final GivePointRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Point updatePointRequestDtoToPoint(final UpdatePointRequestDto dto, @MappingTarget Point point);

}
