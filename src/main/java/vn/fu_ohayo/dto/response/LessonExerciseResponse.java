package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonExerciseResponse {

    private int id;
    private String title;
    private long duration;
    private List<ExerciseQuestionResponse> content;
    private int lessonId;
}
