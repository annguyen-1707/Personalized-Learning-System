package vn.fu_ohayo.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExerciseResultResponse {
    private long userId;
    private int exerciseId;
    private int totalCorrect;
    private int totalQuestion;
    private long totalTime;
    List<QuestionResultResponse> questionResultResponses;
}
