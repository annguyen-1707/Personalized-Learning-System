package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.ProgressContentResponse;
import vn.fu_ohayo.entity.ProgressContent;

@Mapper(componentModel = "spring")
public interface ProgressContentMapper {
    ProgressContentResponse toResponse(ProgressContent progressContent);
}
