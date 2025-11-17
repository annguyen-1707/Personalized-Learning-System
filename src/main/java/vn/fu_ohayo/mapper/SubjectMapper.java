package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    Subject toSubject(SubjectRequest subjectRequest);

    SubjectResponse toSubjectResponse(Subject subject);
}
