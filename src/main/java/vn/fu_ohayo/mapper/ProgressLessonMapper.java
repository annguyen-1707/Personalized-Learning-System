package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.ProgressLessonResponse;
import vn.fu_ohayo.entity.ProgressLesson;

@Mapper(componentModel = "spring")
public interface ProgressLessonMapper {

    ProgressLessonResponse toProgressLessonMapper(ProgressLesson progressLesson);
}
