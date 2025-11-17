package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.entity.LessonExercise;

@Mapper(componentModel = "spring")
public interface LessonExerciseMapper {

    LessonExerciseResponse toResponse(LessonExercise lessonExercise);
}
