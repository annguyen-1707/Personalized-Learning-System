package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.entity.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    Lesson toLesson(LessonPatchRequest lessonRequest);

    Lesson toLesson(LessonRequest lessonRequest);

    LessonResponse toLessonResponse(Lesson lesson);
}
