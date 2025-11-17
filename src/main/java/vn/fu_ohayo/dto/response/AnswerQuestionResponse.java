package vn.fu_ohayo.dto.response;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnswerQuestionResponse {
    private int answerId;
    private String answerText;
    private Boolean isCorrect;
    private Date createdAt;
    private Date updatedAt;

}
