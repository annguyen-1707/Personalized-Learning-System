package vn.fu_ohayo.dto.request;

import lombok.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseRequest {
    private long  userId;
    private int exerciseId;
    private long totalTime;
    List<UserQuestionResponseRequest> userQuestionResponseRequests;
}
