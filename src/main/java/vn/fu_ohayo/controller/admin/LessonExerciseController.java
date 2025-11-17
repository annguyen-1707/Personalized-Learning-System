package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.LessonExerciseRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ExerciseQuestionResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.service.LessonExerciseService;

import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE;
import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_RESPONSE;

@RestController
@RequestMapping("/exercise-questions")
public class LessonExerciseController {


    private final LessonExerciseService lessonExerciseService;

    public LessonExerciseController(LessonExerciseService lessonExerciseService) {
        this.lessonExerciseService = lessonExerciseService;
    }

    @GetMapping
    public ApiResponse<Page<LessonExerciseResponse>> getExerciseQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int lessonId
    ) {
        Page<LessonExerciseResponse> response = lessonExerciseService.getAllContentByLesson(page, size, lessonId);
        return ApiResponse.<Page<LessonExerciseResponse>>builder()
                .message("Fetched exercise questions successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(response)
                .build();
    }
    @GetMapping("/available-exercise-questions")
    public ApiResponse<Page<ExerciseQuestionResponse>> getAvailableExerciseQuestions(
            @RequestParam Long lessonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<ExerciseQuestionResponse> response = lessonExerciseService.getAllExerciseQuestions(lessonId, page, size);
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .message("Fetched available exercise questions successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(response)
                .build();
    }

    @GetMapping("/exercise-details")
    public ApiResponse<Page<ExerciseQuestionResponse>> getExerciseQuestionsByLessonExerciseId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int exerciseId
    ) {
        Page<ExerciseQuestionResponse> response = lessonExerciseService.getExerciseQuestionByExerciseLesson(page, size, exerciseId);
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .message("Fetched exercise questions by lesson exercise ID successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(response)
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<LessonExerciseResponse> deleteExerciseLesson(@PathVariable int id) {
        lessonExerciseService.deleteExerciseLesson(id);
        return ApiResponse.<LessonExerciseResponse>builder()
                .message("Deleted exercise lesson successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .build();
    }

    @PostMapping
    public ApiResponse<LessonExerciseResponse> createExerciseQuestionLesson(@Valid @RequestBody LessonExerciseRequest request) {
        LessonExerciseResponse createdLessonExercise = lessonExerciseService.createExerciseLesson(request);
        return ApiResponse.<LessonExerciseResponse>builder()
                .message("Created new exercise lesson successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(createdLessonExercise)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<LessonExerciseResponse> updateExerciseQuestionLesson(
            @PathVariable int id, @RequestBody LessonExerciseRequest request) {
        LessonExerciseResponse updatedLessonExercise = lessonExerciseService.updateExerciseLesson(id, request);
        return ApiResponse.<LessonExerciseResponse>builder()
                .message("Updated exercise lesson successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(updatedLessonExercise)
                .build();
    }

    @PostMapping("/{lessonId}/add-exercise-question/{exerciseId}")
    public ApiResponse<Void> addExerciseQuestionToLesson(
            @PathVariable Long lessonId,
            @PathVariable Long exerciseId) {
        lessonExerciseService.handleSaveExerciseQuestionIntoLesson(lessonId, exerciseId);
        return ApiResponse.<Void>builder()
                .message("Added exercise question to lesson successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(null)
                .build();
    }// sua cai nay

    @DeleteMapping("/{lessonId}/remove-exercise-question/{exerciseQuestionId}")
    public ApiResponse<Void> removeExerciseQuestionFromLesson(
            @PathVariable Long lessonId,
            @PathVariable Long exerciseQuestionId) {
        lessonExerciseService.handleDeleteExerciseQuestionFromLesson(lessonId, exerciseQuestionId);
        return ApiResponse.<Void>builder()
                .message("Removed exercise question from lesson successfully")
                .status(HTTP_SUCCESS_RESPONSE)
                .code(HTTP_SUCCESS_CODE_RESPONSE)
                .data(null)
                .build();
    }
}
