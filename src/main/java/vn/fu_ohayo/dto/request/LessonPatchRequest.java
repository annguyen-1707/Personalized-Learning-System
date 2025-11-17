package vn.fu_ohayo.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.LessonStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonPatchRequest {

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    private int subjectId;

    private String videoUrl;
}
