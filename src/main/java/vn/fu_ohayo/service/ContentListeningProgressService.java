package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.AnswerListeningRequest;
import vn.fu_ohayo.dto.response.AnswerListeningResponse;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import java.util.List;

public interface ContentListeningProgressService {
    List<AnswerListeningResponse> getListAnser(Long contentListeningId,
                                                        List<AnswerListeningRequest> userAnswers);
}