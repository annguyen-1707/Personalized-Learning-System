package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.ExerciseResultRequest;
import vn.fu_ohayo.dto.request.UserResponseRequest;
import vn.fu_ohayo.dto.response.ExerciseResultResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;

public interface ProgressExerciseService {

    ExerciseResultResponse submitExercise(UserResponseRequest userResponseRequest);

    LessonExerciseResponse getSource(int exerciseId);

    void createExerciseResult(ExerciseResultRequest exerciseResultRequest, String email);
}
