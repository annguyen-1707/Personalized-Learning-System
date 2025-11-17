package vn.fu_ohayo.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserQuestionResponseRequest {
    private int questionId;
    private int selectedAnswerId;
}
