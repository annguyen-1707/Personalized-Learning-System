package vn.fu_ohayo.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExerciseResultRequest {

    long userId;

    int exerciseId;

    int totalQuestions;

    int correctAnswers;
}
