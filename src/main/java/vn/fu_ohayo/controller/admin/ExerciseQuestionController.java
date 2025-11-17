package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.service.ContentListeningService;
import vn.fu_ohayo.service.ExerciseQuestionService;
import vn.fu_ohayo.service.LessonExerciseService;

import java.util.List;

@RestController
@RequestMapping("/question")
public class ExerciseQuestionController {
    private final ExerciseQuestionService exerciseQuestionService;
    private final LessonExerciseService lessonExerciseService;

    public ExerciseQuestionController(ExerciseQuestionService exerciseQuestionService, LessonExerciseService lessonExerciseService) {
        this.exerciseQuestionService = exerciseQuestionService;
        this.lessonExerciseService = lessonExerciseService;
    }

    @GetMapping("/content_listening/{contentListeningId}")
    public ApiResponse<List<ExerciseQuestionResponse>> getExerciseQuestionByContentListening(
            @PathVariable long contentListeningId) {
        return ApiResponse.<List<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions by content listening id")
                .data(exerciseQuestionService.getExerciseQuestionByContentListening(contentListeningId))
                .build();
    }

    @GetMapping("/exercise/{exerciseId}")
    public ApiResponse<List<ExerciseQuestionResponse>> getExerciseQuestionByExercise(
            @PathVariable int exerciseId) {
        return ApiResponse.<List<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions by exercise id")
                .data(exerciseQuestionService.getExerciseQuestionByExercise(exerciseId))
                .build();
    }

    @GetMapping("/empty")
    public ApiResponse<Page<ExerciseQuestionResponse>> getExerciseQuestionEmptyPageByType(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String type
    ) {
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions")
                .data(exerciseQuestionService.getExerciseQuestionEmptyPageByType(page - 1, size, type))
                .build();
    }

    @GetMapping()
    public ApiResponse<Page<ExerciseQuestionResponse>> getExerciseQuestionPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions")
                .data(exerciseQuestionService.getExerciseQuestionPage(page - 1, size))
                .build();
    }

    @GetMapping("/type")
    public ApiResponse<Page<ExerciseQuestionResponse>> getExerciseQuestionPageByType(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String type
    ) {
        return ApiResponse.<Page<ExerciseQuestionResponse>>builder()
                .code("200")
                .status("success")
                .message("get page of exercise questions")
                .data(exerciseQuestionService.getExerciseQuestionPageByType(page - 1, size, type))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExerciseQuestionResponse> getExerciseQuestion(@PathVariable int id) {
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Get exerciseQuestion by id")
                .data(exerciseQuestionService.getExerciseQuestionById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ExerciseQuestionResponse> createExerciseQuestion(@Valid @RequestBody ExerciseQuestionRequest request) {
        ExerciseQuestionResponse newExerciseQuestion = exerciseQuestionService.handleCreateExerciseQuestion(request);
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("201")
                .status("success")
                .message("Created new ExerciseQuestion successfully")
                .data(newExerciseQuestion)
                .build();
    }

    @PostMapping("/many")
    public ApiResponse<List<ExerciseQuestionResponse>> createManyExerciseQuestion(@Valid @RequestBody List<ExerciseQuestionRequest> requests) {
        List<ExerciseQuestionResponse> newExerciseQuestion = exerciseQuestionService.handleCreateAllExerciseQuestion(requests);
        return ApiResponse.<List<ExerciseQuestionResponse>>builder()
                .code("201")
                .status("success")
                .message("Created new ExerciseQuestion successfully")
                .data(newExerciseQuestion)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ExerciseQuestion> deleteExerciseQuestion(@PathVariable int id) {
        exerciseQuestionService.softDeleteExerciseQuestionById(id);
        return ApiResponse.<ExerciseQuestion>builder()
                .code("200")
                .status("success")
                .message("Deleted content speaking successfully")
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<ExerciseQuestionResponse> patchExerciseQuestion(
            @PathVariable int id,
            @Valid @RequestBody ExerciseQuestionRequest request) {
        ExerciseQuestionResponse ExerciseQuestionResponse = exerciseQuestionService.updatePatchExerciseQuestion(id, request);
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(ExerciseQuestionResponse)
                .build();
    }

    @PatchMapping("/{questionId}/addExercise/{exerciseId}")
    public ApiResponse<ExerciseQuestionResponse> addQuestionIntoExercise(
            @PathVariable int questionId,
            @PathVariable int exerciseId) {
        ExerciseQuestionResponse exerciseQuestionResponse = exerciseQuestionService.addQuestionIntoExercise(questionId, exerciseId);
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Added exercise question into lesson successfully")
                .data(exerciseQuestionResponse)
                .build();
    }
    @PatchMapping("/{questionId}/addContentListening/{contentListeningId}")
    public ApiResponse<ExerciseQuestionResponse> addQuestionIntoContentListening(
            @PathVariable int questionId,
            @PathVariable long contentListeningId) {
        ExerciseQuestionResponse exerciseQuestionResponse = exerciseQuestionService.addQuestionIntoContentListening(questionId, contentListeningId);
        return ApiResponse.<ExerciseQuestionResponse>builder()
                .code("200")
                .status("success")
                .message("Added exercise question into lesson successfully")
                .data(exerciseQuestionResponse)
                .build();
    }
    @PatchMapping("/{questionId}/removeFromExercise")
    public ApiResponse<Void> removeQuestionFromExercise(
            @PathVariable int questionId) {
        exerciseQuestionService.removeQuestionFromExercise(questionId);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Added exercise question into lesson successfully")
                .build();
    }
    @PatchMapping("/{questionId}/removeFromContentListening")
    public ApiResponse<Void> removeQuestionFromListening(
            @PathVariable int questionId) {
        exerciseQuestionService.removeQuestionFromContentListening(questionId);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Added exercise question into lesson successfully")
                .build();
    }

}
