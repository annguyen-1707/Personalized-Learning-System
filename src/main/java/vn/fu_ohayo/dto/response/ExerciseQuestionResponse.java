package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.enums.ContentStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExerciseQuestionResponse {
    private int exerciseQuestionId;
    private String questionText;
    private Date createdAt;
    private Date updatedAt;
    private List<AnswerQuestionRequest> answerQuestions;
    private ContentStatus status;
    ContentListeningResponse contentListening;
}
