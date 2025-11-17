package vn.fu_ohayo.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ExerciseResultRequest;
import vn.fu_ohayo.dto.request.UserQuestionResponseRequest;
import vn.fu_ohayo.dto.request.UserResponseRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ExerciseQuestionMapper;
import vn.fu_ohayo.mapper.LessonExerciseMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.ProgressExerciseService;
import vn.fu_ohayo.service.ProgressLessonService;
import vn.fu_ohayo.service.ProgressSubjectService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProgressExerciseServiceImp implements ProgressExerciseService {

    private final UserResponseQuestionRepository userResponseQuestionRepository;
    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final UserRepository userRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final LessonExerciseRepository lessonExerciseRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final ExerciseResultRepository exerciseResultRepository;
    private final ProgressLessonService progressLessonService;
    private final ProgressLessonServiceImp progressLessonServiceImp;
    private final ProgressSubjectService progressSubjectService;

    public ProgressExerciseServiceImp(UserResponseQuestionRepository userResponseQuestionRepository,
                                      ExerciseQuestionRepository exerciseQuestionRepository,
                                      UserRepository userRepository,
                                      ExerciseQuestionMapper exerciseQuestionMapper,
                                      LessonExerciseRepository lessonExerciseRepository,
                                      AnswerQuestionRepository answerQuestionRepository,
                                      ExerciseResultRepository exerciseResultRepository,
                                      ProgressLessonService progressLessonService,
                                      ProgressLessonServiceImp progressLessonServiceImp,
                                      ProgressSubjectService progressSubjectService)  {
        this.userResponseQuestionRepository = userResponseQuestionRepository;
        this.exerciseQuestionRepository = exerciseQuestionRepository;
        this.userRepository = userRepository;
        this.exerciseQuestionMapper = exerciseQuestionMapper;
        this.lessonExerciseRepository = lessonExerciseRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.exerciseResultRepository = exerciseResultRepository;
        this.progressLessonService = progressLessonService;
        this.progressLessonServiceImp = progressLessonServiceImp;
        this.progressSubjectService = progressSubjectService;
    }


    @Override
    public LessonExerciseResponse getSource(int exerciseId) {
        LessonExercise lessonExercise = lessonExerciseRepository.findById(exerciseId).orElseThrow(
                () -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND)
        );

        List<ExerciseQuestionResponse> questions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise).stream()
                .map(exerciseQuestionMapper::toExerciseQuestionResponse).toList();
        return LessonExerciseResponse.builder()
                .title(lessonExercise.getTitle())
                .duration(lessonExercise.getDuration())
                .content(questions)
                .id(exerciseId)
                .build();
    }

    @Override
    public void createExerciseResult(ExerciseResultRequest exerciseResultRequest, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        LessonExercise lessonExercise = lessonExerciseRepository.findById(exerciseResultRequest.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));

        ExerciseResult exerciseResult = ExerciseResult.builder()
                .user(user)
                .lessonExercise(lessonExercise)
                .totalQuestions(exerciseResultRequest.getTotalQuestions())
                .correctAnswers(exerciseResultRequest.getCorrectAnswers())
                .submissionTime(new Date())
                .build();

        exerciseResultRepository.save(exerciseResult);
    }


    @Override
    @Transactional
    public ExerciseResultResponse submitExercise(UserResponseRequest userResponseRequest) {
        User user = userRepository.findById(userResponseRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        LessonExercise lessonExercise = lessonExerciseRepository.findById(userResponseRequest.getExerciseId())
                .orElseThrow(() -> new AppException(ErrorEnum.EXERCISE_NOT_FOUND));
        
        List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByLessonExercise(lessonExercise);

        List<ExerciseQuestionResponse> exerciseQuestionResponse = exerciseQuestions.stream().map(
                exerciseQuestionMapper::toExerciseQuestionResponse
        ).toList();

        List<QuestionResultResponse> questionResultResponse = getQuestionResultResponses(exerciseQuestionResponse, userResponseRequest.getUserQuestionResponseRequests());

        ExerciseResultResponse response = ExerciseResultResponse.builder()
                .exerciseId(lessonExercise.getExerciseId())
                .totalQuestion(exerciseQuestions.size())
                .questionResultResponses(questionResultResponse)
                .totalCorrect((int) questionResultResponse.stream()
                        .filter(QuestionResultResponse::isCorrect)
                        .count())
                .totalTime(userResponseRequest.getTotalTime())
                .userId(user.getUserId())
                .build();

        saveUserResponses(questionResultResponse, exerciseQuestionResponse, user);
        progressLessonServiceImp.markLessonAsCompleted(user.getUserId(), lessonExercise.getLesson().getLessonId());
        int subjectId = lessonExercise.getLesson().getSubject().getSubjectId();
        if(progressLessonService.isAllLessonsCompleted(user.getUserId(), subjectId)) {
            progressSubjectService.markSubjectComplete(user.getEmail(), subjectId);
        }

         return  response;
    }


    public List<QuestionResultResponse> getQuestionResultResponses(
            List<ExerciseQuestionResponse> exerciseQuestionResponses,
            List<UserQuestionResponseRequest> userQuestionResponseRequests) {
        List<QuestionResultResponse> questionResultResponses = new ArrayList<>();
        if(userQuestionResponseRequests.isEmpty()) {
            exerciseQuestionResponses.forEach(e -> {
                QuestionResultResponse questionResultResponse = QuestionResultResponse.builder()
                        .isCorrect(false)
                        .userResponseId(-1)
                        .exerciseQuestionResponse(e)
                        .build();
                questionResultResponses.add(questionResultResponse);
            });
        }else {
            exerciseQuestionResponses.forEach(e-> {
                userQuestionResponseRequests.forEach(u -> {
                    if(e.getExerciseQuestionId() == u.getQuestionId()){
                        QuestionResultResponse questionResultResponse = QuestionResultResponse.builder()
                                .isCorrect(checkCorrectAnswer(e,u.getSelectedAnswerId()))
                                .userResponseId(u.getSelectedAnswerId())
                                .exerciseQuestionResponse(e)
                                .build();
                        questionResultResponses.add(questionResultResponse);
                    }
                });
            });
        }
        return  questionResultResponses;

    }

    public boolean checkCorrectAnswer(ExerciseQuestionResponse e, int userResponseId) {
        if(userResponseId == -1) {
            return false;
        }
        return e.getAnswerQuestions().stream()
                .anyMatch(
                        t -> t.getAnswerId() == userResponseId
                        && t.getIsCorrect() == true
                );
    }

    public void saveUserResponses(
            List<QuestionResultResponse> questionResultResponse,
            List<ExerciseQuestionResponse> exerciseQuestionResponses,
            User user
    ) {
        List<UserResponseQuestion> userResponseQuestions = new ArrayList<>();
        for (ExerciseQuestionResponse e : exerciseQuestionResponses) {
            for(QuestionResultResponse u : questionResultResponse) {
                if(e.getExerciseQuestionId() == u.getExerciseQuestionResponse().getExerciseQuestionId()) {
                    UserResponseQuestion userResponseQuestion = UserResponseQuestion.builder()
                            .question(exerciseQuestionRepository.findById(u.getExerciseQuestionResponse().getExerciseQuestionId()).get())
                            .user(user)
                            .answerQuestion(
                                    answerQuestionRepository.findById(u.getUserResponseId()).isPresent() ?
                                            answerQuestionRepository.findById(u.getUserResponseId()).get() :
                                            null
                            )
                            .isCorrect(checkCorrectAnswer(e,u.getUserResponseId()))
                            .build();
                    userResponseQuestions.add(userResponseQuestion);
                }
            }
        }
        userResponseQuestions.forEach(u -> {
            if(userResponseQuestionRepository.existsByQuestion_ExerciseQuestionId(u.getQuestion().getExerciseQuestionId())){
                UserResponseQuestion existingResponse = userResponseQuestionRepository
                        .findByQuestion_ExerciseQuestionId(u.getQuestion().getExerciseQuestionId());
                existingResponse.setAnswerQuestion(u.getAnswerQuestion());
                existingResponse.setIsCorrect(u.getIsCorrect());
                existingResponse.setCreatedAt(new Date());
                userResponseQuestionRepository.save(existingResponse);
            } else {
                UserResponseQuestion newResponse = UserResponseQuestion.builder()
                        .question(u.getQuestion())
                        .user(u.getUser())
                        .answerQuestion(u.getAnswerQuestion())
                        .isCorrect(u.getIsCorrect())
                        .build();
                userResponseQuestionRepository.save(newResponse);
            }
        });
    }

}
