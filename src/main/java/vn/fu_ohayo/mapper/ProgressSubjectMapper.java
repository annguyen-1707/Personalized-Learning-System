package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;

@Mapper(componentModel = "spring")
public interface ProgressSubjectMapper {

    ProgressSubjectResponse toProgressSubjectResponse(vn.fu_ohayo.entity.ProgressSubject progressSubject);
}
