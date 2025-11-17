package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckAnswerWrapperRequest {
    private List<AnswerQuestionRequest> userAnswers;
    private int userId;
    private int contentId;
}