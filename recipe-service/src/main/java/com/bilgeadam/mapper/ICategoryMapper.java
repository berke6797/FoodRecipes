package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.CreateCategoryRequestDto;
import com.bilgeadam.dto.request.UpdateCategoryRequestDto;
import com.bilgeadam.repository.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {
    ICategoryMapper INSTANCE= Mappers.getMapper(ICategoryMapper.class);
    Category fromCreateCategoryRequestDtoToCategory(final CreateCategoryRequestDto createCategoryRequestDto);
    Category fromUpdateCategoryRequestDtoToCategory(final UpdateCategoryRequestDto updateCategoryRequestDto);

}
