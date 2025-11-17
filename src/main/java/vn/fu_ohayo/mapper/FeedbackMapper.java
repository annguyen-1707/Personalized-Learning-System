package vn.fu_ohayo.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import vn.fu_ohayo.dto.DTO.FeedbackDTO;
import vn.fu_ohayo.entity.Feedback;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    @Mapping(source = "user", target = "user")
    FeedbackDTO toResponse(Feedback feedback);

}