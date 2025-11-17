package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressSubjectResponse;

public interface ProgressSubjectService {
    void enrollCourse(int courseId, String email);

    ProgressSubjectResponse getProgressSubject(String email, int subjectId);

    ProgressSubjectResponse markSubjectComplete(String username, int subjectId);
}
