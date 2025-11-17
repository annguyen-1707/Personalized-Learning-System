package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.LessonExercise;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExerciseQuestionRequest {
    @NotNull(message = "Question text cannot be null")
    @NotBlank(message = "Question text cannot be blank")
    private String questionText;
    @NotNull(message = "The list must contain at least 2 answer")
    @Size(min = 2, message = "The list must contain at least 2 answer")
    private List<AnswerQuestionRequest> answerQuestions;
    private Long contentListeningId;
    private Integer exerciseId;
    @NotNull(message = "Type cannot be null or blank")
    @NotBlank(message = "Type cannot be null or blank")
    private String type;

}
