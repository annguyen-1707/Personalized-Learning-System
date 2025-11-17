package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DialogueRequest {
    @NotNull(message = "Question in Japanese cannot be blank")
    @NotBlank(message = "Question in Japanese cannot be blank")
    private String questionJp;
    @NotNull(message = "Question in Vietnamese cannot be blank")
    @NotBlank(message = "Question in Vietnamese cannot be blank")
    private String questionVn;
    @NotNull(message = "Answer in Vietnamese cannot be blank")
    @NotBlank(message = "Answer in Vietnamese cannot be blank")
    private String answerVn;
    @NotNull(message = "Answer in Japanese cannot be blank")
    @NotBlank(message = "Answer in Japanese cannot be blank")
    private String answerJp;

    private long contentSpeakingId;
}
