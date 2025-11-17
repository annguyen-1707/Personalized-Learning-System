package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.ContentListening;
import java.util.List;

public interface ProgressContentService {
    ProgressContent saveUserListeningProgress(User user, ContentListening contentListening, int correct_answers, int total_questions);
    ProgressContent saveUserReadingProgress(User user, ContentReading contentReading, int progress);
    List<AnswerQuestionResponse> checkListeningAnswers(List<AnswerQuestionRequest> userAnswers, int userId, int contentId);
}