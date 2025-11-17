package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.repository.LessonExerciseRepository;
import vn.fu_ohayo.service.ContentListeningService;
import vn.fu_ohayo.service.ExerciseQuestionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseQuestionServiceImp implements ExerciseQuestionService {
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ContentListeningService contentListeningService;
    private final LessonExerciseRepository lessonExerciseRepository;

    public ExerciseQuestionServiceImp(ExerciseQuestionRepository exerciseQuestionRepository,
                                      AnswerQuestionRepository answerQuestionRepository, ExerciseQuestionMapper exerciseQuestionMapper, ContentListeningService contentListeningService, LessonExerciseRepository lessonExerciseRepository) {
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.contentListeningService = contentListeningService;
        this.lessonExerciseRepository = lessonExerciseRepository;
    }

    @Override
    public List<ExerciseQuestionResponse> getExerciseQuestionByContentListening(long contentListeningId) {
        ContentListening contentListening = contentListeningService.getContentListeningById(contentListeningId);
        List<ExerciseQuestion> prs = exerciseQuestionRepository.findAllByContentListening(contentListening);
        List<ExerciseQuestionResponse> responsePage = prs.stream().map(exerciseQuestionMapper::toExerciseQuestionResponse).toList();
        return responsePage;
    }

    @Override
    public List<ExerciseQuestionResponse> getExerciseQuestionByExercise(int exerciseId) {
        LessonExercise lessonExercise = lessonExerciseRepository.findById(exerciseId).get();
        List<ExerciseQuestion> prs = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise);
        List<ExerciseQuestionResponse> responsePage = prs.stream().map(exerciseQuestionMapper::toExerciseQuestionResponse).toList();
        return responsePage;    }

    @Override
    public ExerciseQuestionResponse getExerciseQuestionById(int id) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public ExerciseQuestionResponse handleCreateExerciseQuestion(ExerciseQuestionRequest exerciseQuestionRequest) {
//        if (exerciseQuestionRequest.getExerciseId() == null && exerciseQuestionRequest.getContentListeningId() == null) {
//            throw new AppException(ErrorEnum.EXIST_AT_LEAST_CONTENT_LISTENING_OR_EXERCISE);
//        }
        List<AnswerQuestionRequest> answerRequests = exerciseQuestionRequest.getAnswerQuestions();
        int correctCount = 0;
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                correctCount++;
            }
        }
        if (correctCount != 1) {
            throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT);
        }
        ExerciseQuestion.ExerciseQuestionBuilder builder = ExerciseQuestion.builder()
                .questionText(exerciseQuestionRequest.getQuestionText())
                .type(exerciseQuestionRequest.getType());
        Optional.ofNullable(exerciseQuestionRequest.getContentListeningId())
                .ifPresent(id -> builder.contentListening(contentListeningService.getContentListeningById(id)));

        Optional.ofNullable(exerciseQuestionRequest.getExerciseId())
                .ifPresent(id -> builder.lessonExercise(lessonExerciseRepository.findById(id).get()));

        ExerciseQuestion exerciseQuestion = builder.build();

        exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            AnswerQuestion answerQuestion = AnswerQuestion.builder()
                    .answerText(answerRequest.getAnswerText())
                    .isCorrect(answerRequest.getIsCorrect())
                    .exerciseQuestion(exerciseQuestion)
                    .build();
            answerQuestionRepository.save(answerQuestion);
        }
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public void softDeleteExerciseQuestionById(int id) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        exerciseQuestion.setDeleted(true);
        exerciseQuestionRepository.save(exerciseQuestion);
    }

    @Override
    public void hardDeleteExerciseQuestionById(int id) {
        exerciseQuestionRepository.deleteById(id);
    }

    @Override
    public ExerciseQuestionResponse updatePatchExerciseQuestion(int id, ExerciseQuestionRequest exerciseQuestionRequest) {
        List<AnswerQuestionRequest> answerRequests = exerciseQuestionRequest.getAnswerQuestions();
        int correctCount = 0;
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            if (Boolean.TRUE.equals(answerRequest.getIsCorrect())) {
                correctCount++;
            }
        }

        if (correctCount != 1) {
            throw new AppException(ErrorEnum.INVALID_ANSWER_CORRECT_COUNT); // bạn cần tự định nghĩa ErrorEnum này
        }
        // Tìm ExerciseQuestion
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));

        // Cập nhật questionText nếu có
        if (exerciseQuestionRequest.getQuestionText() != null) {
            exerciseQuestion.setQuestionText(exerciseQuestionRequest.getQuestionText());
        }
        if (exerciseQuestionRequest.getExerciseId() != null) {
            exerciseQuestion.setLessonExercise(lessonExerciseRepository.findById((exerciseQuestionRequest.getExerciseId())).get());
            exerciseQuestion.setContentListening(null);
        }
        if (exerciseQuestionRequest.getContentListeningId() != null) {
            exerciseQuestion.setContentListening(contentListeningService.getContentListeningById(exerciseQuestionRequest.getContentListeningId()));
            exerciseQuestion.setLessonExercise(null);
        }
        // Xóa tất cả answer hiện tại
        List<AnswerQuestion> oldAnswers = answerQuestionRepository.findByExerciseQuestion(exerciseQuestion);
        for (AnswerQuestion oldAnswer : oldAnswers) {
            answerQuestionRepository.deleteById(oldAnswer.getAnswerId());
        }
        oldAnswers.clear();

        // Thêm lại các answer mới từ request
        answerRequests = exerciseQuestionRequest.getAnswerQuestions();
        for (AnswerQuestionRequest answerRequest : answerRequests) {
            AnswerQuestion newAnswer = AnswerQuestion.builder()
                    .answerText(answerRequest.getAnswerText())
                    .isCorrect(answerRequest.getIsCorrect())
                    .exerciseQuestion(exerciseQuestion)
                    .build();
            answerQuestionRepository.save(newAnswer);
        }
        exerciseQuestion = exerciseQuestionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public List<ExerciseQuestionResponse> handleCreateAllExerciseQuestion(List<ExerciseQuestionRequest> questionRequest) {
        List<ExerciseQuestionResponse> responses = new ArrayList<>();
        for (ExerciseQuestionRequest exerciseQuestionRequest : questionRequest) {
            List<AnswerQuestionRequest> answerRequests = exerciseQuestionRequest.getAnswerQuestions();

            List<AnswerQuestion> answerQuestionSet = new ArrayList<>();
            for (AnswerQuestionRequest answerRequest : answerRequests) {
                AnswerQuestion answerQuestion = AnswerQuestion.builder()
                        .answerText(answerRequest.getAnswerText())
                        .isCorrect(answerRequest.getIsCorrect())
                        .build();
                answerQuestionSet.add(answerQuestion);
            }
            ExerciseQuestion exerciseQuestion = ExerciseQuestion.builder()
                    .questionText(exerciseQuestionRequest.getQuestionText())
                    .contentListening(contentListeningService.getContentListeningById(exerciseQuestionRequest.getContentListeningId()))
                    .lessonExercise(lessonExerciseRepository.findById((exerciseQuestionRequest.getExerciseId())).get())

                    .answerQuestions(answerQuestionSet)
                    .build();
            exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
            responses.add(exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion));
        }
        return responses;
    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExerciseQuestion> prs = exerciseQuestionRepository.findAll(pageable);
        Page<ExerciseQuestionResponse> responsePage = prs.map(exerciseQuestionMapper::toExerciseQuestionResponse);
        return responsePage;
    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionPageByType(int page, int size, String type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExerciseQuestion> prs = exerciseQuestionRepository.findAllByType(pageable, type);
        return prs.map(exerciseQuestionMapper::toExerciseQuestionResponse);
    }

    @Override
    public Page<ExerciseQuestionResponse> getExerciseQuestionEmptyPageByType(int page, int size, String type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExerciseQuestion> prs = exerciseQuestionRepository.findAllByTypeAndLessonExerciseIsNullAndContentListeningIsNull(pageable, type);
        return prs.map(exerciseQuestionMapper::toExerciseQuestionResponse);    }

    @Override
    public ExerciseQuestionResponse addQuestionIntoExercise(int questionId, int exerciseId) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        exerciseQuestion.setLessonExercise(lessonExerciseRepository.findById(exerciseId).get());
        exerciseQuestionRepository.save(exerciseQuestion);
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public ExerciseQuestionResponse addQuestionIntoContentListening(int questionId, long contentListeningId) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        exerciseQuestion.setContentListening(contentListeningService.getContentListeningById(contentListeningId));
        exerciseQuestionRepository.save(exerciseQuestion);
        return exerciseQuestionMapper.toExerciseQuestionResponse(exerciseQuestion);
    }

    @Override
    public void removeQuestionFromExercise(int questionId) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        exerciseQuestion.setLessonExercise(null);
        exerciseQuestionRepository.save(exerciseQuestion);
    }

    @Override
    public void removeQuestionFromContentListening(int questionId) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorEnum.QUESTION_NOT_FOUND));
        exerciseQuestion.setContentListening(null);
        exerciseQuestionRepository.save(exerciseQuestion);
    }
}
