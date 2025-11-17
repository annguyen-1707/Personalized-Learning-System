package vn.fu_ohayo.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonExerciseRequest {

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;
    private long duration;
    private List<Integer> questionIds;
    private int lessonId;
    private List<Integer> questionIdsToAdd;
    private List<Integer> questionIdsToRemove;
}
