package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.repository.entity.Point;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface IPointMapper {
    IPointMapper INSTANCE= Mappers.getMapper(IPointMapper.class);
    Point givePointRequestDtoToPoint(final GivePointRequestDto dto);
}
