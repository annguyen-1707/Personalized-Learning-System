package vn.fu_ohayo.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResultResponse {
    private ExerciseQuestionResponse exerciseQuestionResponse;
    private int userResponseId;
    private boolean isCorrect;
}
