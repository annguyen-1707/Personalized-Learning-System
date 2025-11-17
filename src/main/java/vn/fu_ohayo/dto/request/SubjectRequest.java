package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.SubjectStatus;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SubjectRequest {

    @NotNull(message = ErrorEnum.NOT_EMPTY_SUBJECT_CODE)
    private String subjectCode;

    @NotNull(message = ErrorEnum.NOT_EMPTY_SUBJECT_NAME)
    @Size(max = 50, message = ErrorEnum.INVALID_SUBJECT_NAME)
    private String subjectName;

    private String description;

    private SubjectStatus status;

    @Size(max = 255, message = ErrorEnum.INVALID_THUMBNAIL_URL)
    private String thumbnailUrl;

}
