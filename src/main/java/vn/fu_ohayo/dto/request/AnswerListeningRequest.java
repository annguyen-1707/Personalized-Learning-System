package vn.fu_ohayo.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class AnswerListeningRequest {
    private Integer questionId;
    private String answerText;
}
