package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.StudyReminderResponse;
import vn.fu_ohayo.entity.StudyReminder;

@Mapper(componentModel = "spring")
public interface StudyReminderMapper {
    StudyReminderResponse toStudyReminderResponse(StudyReminder studyReminder);
}
