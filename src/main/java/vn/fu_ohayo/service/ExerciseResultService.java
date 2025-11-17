package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressExerciseResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarResponse;

public interface ExerciseResultService {
    int countExerciseDoneSubjectInProgressByUserId(long userId);
    int countAllExerciseSubjectInProgressByUserId(long userId);
    ProgressExerciseResponse getProgressEachSubjectByUserId(long userId);

}
