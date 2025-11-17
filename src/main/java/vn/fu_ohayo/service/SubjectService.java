package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {

     Page<SubjectResponse> getAllActiveSubjects(int page, int size, String username);
     SubjectResponse createSubject(SubjectRequest subjectRequest);
     SubjectResponse updateSubject(int id, SubjectRequest subjectRequest);
     void deleteSubject(int id);
    SubjectResponse getSubjectById(int id);
    Page<SubjectResponse> getAllSubjectsForAdmin(int page, int size);
    Page<ProgressSubjectResponse> getAllByUserId(int page, int size, String email);
    Page<SubjectResponse> getAllPublicSubjects(int page, int size);
    List<SubjectResponse> getAllListActiveSubjectsByStatus();
    List<SubjectResponse> getAllListSubjects();

    SubjectResponse acceptSubject(int id);

    SubjectResponse rejectSubject(int id);

    SubjectResponse inactiveSubject(int id);
}
