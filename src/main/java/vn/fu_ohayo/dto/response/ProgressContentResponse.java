package vn.fu_ohayo.dto.response;

import lombok.*;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProgressContentResponse {
    private int progressId;
    private ContentResponse content;
    private Date createdAt;
    private int totalQuestions;
    private int correctAnswers;
    private Integer version;
    private ProgressStatus progressStatus;
    private UserResponse user;

}
