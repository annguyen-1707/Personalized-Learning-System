package vn.fu_ohayo.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonRequest {

        @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
        private String name;

        @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
        private String description;

        @Enumerated(EnumType.STRING)
        private LessonStatus status;

        @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
        private int subjectId;

        private String videoUrl;

}
