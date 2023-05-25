package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.request.UpdateCommentRequestDto;
import com.bilgeadam.repository.entity.Comment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommentMapper {
    ICommentMapper INSTANCE= Mappers.getMapper(ICommentMapper.class);

    Comment makeCommentDtoToComment(final MakeCommentRequestDto makeCommentRequestDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment updateCommentDtoToComment(final UpdateCommentRequestDto updateCommentRequestDto,@MappingTarget Comment comment);


}
