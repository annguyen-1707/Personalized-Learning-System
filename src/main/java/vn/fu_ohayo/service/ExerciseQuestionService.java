package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.LessonExercise;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;

import java.util.List;
import java.util.Set;

public interface ExerciseQuestionService {


    List<ExerciseQuestionResponse> getExerciseQuestionByContentListening(long contentListeningId);
    List<ExerciseQuestionResponse> getExerciseQuestionByExercise(int exerciseId);
    ExerciseQuestionResponse getExerciseQuestionById(int id);
    ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequest ExerciseQuestionRequest);
    void softDeleteExerciseQuestionById(int id);
    void hardDeleteExerciseQuestionById(int id);
    ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequest ExerciseQuestionRequest);
    List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(List<ExerciseQuestionRequest> ExerciseQuestionRequests);
    Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page,int size);
    Page<ExerciseQuestionResponse> getExerciseQuestionPageByType(int page,int size, String type);
    Page<ExerciseQuestionResponse> getExerciseQuestionEmptyPageByType(int page,int size, String type);
    ExerciseQuestionResponse addQuestionIntoExercise(int questionId, int exerciseId);
    void removeQuestionFromExercise(int questionId);
    ExerciseQuestionResponse addQuestionIntoContentListening(int questionId, long contentListeningId);
    void removeQuestionFromContentListening(int questionId);
}
