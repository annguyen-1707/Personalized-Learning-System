package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.StudyReminderRequest;
import vn.fu_ohayo.dto.response.StudyReminderResponse;
import vn.fu_ohayo.entity.StudyReminder;

import java.util.List;

public interface StudyReminderService {
    List<StudyReminderResponse> getStudyRemindersByUserId(long userId);
    StudyReminderResponse addStudyReminder(StudyReminderRequest studyReminderRequest, long userId);
    StudyReminderResponse updateStudyReminder(int studyReminderId, StudyReminderRequest studyReminderRequest);
    void deleteStudyReminder(int studyReminderId);
    StudyReminder getStudyReminderById(int studyReminderId);
}
