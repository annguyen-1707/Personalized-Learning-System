package vn.fu_ohayo.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AnswerListeningResponse {
    private Integer questionId;
    private String answerText;
    private boolean isCorrect;
}
