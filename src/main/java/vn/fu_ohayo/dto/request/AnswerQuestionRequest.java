package vn.fu_ohayo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnswerQuestionRequest {
//    @NotNull(message = "Answer text cannot be null")
//    @NotBlank(message = "Answer text cannot be blank")
    private int answerId;
    @NotNull(message = "Answer not null")
    @NotBlank(message = "Answer not null")
    private String answerText;
    @JsonProperty("correct")
    private Boolean isCorrect;

}
