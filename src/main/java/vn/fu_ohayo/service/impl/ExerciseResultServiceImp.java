package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.CountLearnBySubjectResponse;
import vn.fu_ohayo.dto.response.ProgressExerciseResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarResponse;
import vn.fu_ohayo.dto.response.RecentlyLearnExerciseResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.mapper.ProgressMapper;
import vn.fu_ohayo.repository.ExerciseResultRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ExerciseResultService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseResultServiceImp implements ExerciseResultService {

    private final ExerciseResultRepository exerciseResultRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;
    private final ProgressMapper progressMapper;

    public ExerciseResultServiceImp(ExerciseResultRepository exerciseResultRepository, UserRepository userRepository, ProgressSubjectRepository progressSubjectRepository, ProgressMapper progressMapper) {
        this.exerciseResultRepository = exerciseResultRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
        this.progressMapper = progressMapper;
    }

    private List<LessonExercise> getLessonExercisesOfSubjectsInProgress(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        return progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getLessonExercises)
                .flatMap(List::stream)
                .toList();
    }

    private List<LessonExercise> getExercisesEachSubjectInProgress(User user, Subject subject) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        ProgressSubject progressSubjects = progressSubjectRepository
                .findBySubjectAndUserAndProgressStatusIn(subject, user, statuses);
        return progressSubjects.getSubject().getLessons()
                .stream()
                .map(Lesson::getLessonExercises)
                .flatMap(List::stream)
                .toList();
    }

    public double calculateAveragePercentage(List<LessonExercise> lessonExercises, User user) {
        List<ExerciseResult> exercises = exerciseResultRepository
                .findAllByUserAndLessonExerciseIn(user, lessonExercises);
        return exercises.stream()
                .mapToDouble(e -> (double) e.getCorrectAnswers() / e.getTotalQuestions() * 100)
                .average()
                .orElse(0.0);
    }
    // lấy ds số exercise đã học theo từng subject của user
    private List<CountLearnBySubjectResponse> getListCountLearnBySubject(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        List<Subject> subjects = progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .toList();
        return subjects.stream()
                .map(subject -> {
                    List<LessonExercise> exercises = getExercisesEachSubjectInProgress(user, subject);
                    List<Integer> exerciseIds = exercises.stream().map(le -> le.getExerciseId()).collect(Collectors.toList());
                    int countLearn = exerciseResultRepository
                            .countDistinctByUserAndLessonExerciseIn(user.getUserId(), exerciseIds);
                    int countAll = exercises.size();

                    return CountLearnBySubjectResponse.builder()
                            .subject(subject.getSubjectName())
                            .subjectId(subject.getSubjectId())
                            .average(calculateAveragePercentage(exercises, user))
                            .countLearn(countLearn)
                            .countAll(countAll)
                            .build();
                })
                .toList();
    }
    // lấy ds exercise đã học gần đây của user
    private List<RecentlyLearnExerciseResponse> getRecentlyLearnExerciseByUserId(User user, int size) {
        List<LessonExercise> exercises = getLessonExercisesOfSubjectsInProgress(user);
        List<ExerciseResult> progressExercises = exerciseResultRepository
                .findAllByUserAndTopOnReviewAndExerciseIn(user, exercises, size);
        return progressExercises.stream().map(progressMapper::toRecentlyLearnExerciseResponse).toList();
    }

    @Override
    public int countExerciseDoneSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<LessonExercise> exercises = getLessonExercisesOfSubjectsInProgress(user);
        return exerciseResultRepository.countDistinctByUserAndLessonExerciseIn(user.getUserId(), exercises.stream().map(le -> le.getExerciseId()).collect(Collectors.toList()));
    }

    @Override
    public int countAllExerciseSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorEnum.USER_NOT_FOUND));
        return getLessonExercisesOfSubjectsInProgress(user).size();
    }

    @Override
    public ProgressExerciseResponse getProgressEachSubjectByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        return ProgressExerciseResponse.builder()
                .countLearnBySubjectResponses(getListCountLearnBySubject(user))
                .recentlyLearnExercisesResponses(getRecentlyLearnExerciseByUserId(user, 5))
                .build();
    }
}
