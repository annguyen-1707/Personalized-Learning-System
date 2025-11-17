package vn.fu_ohayo.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ExerciseResultRequest;
import vn.fu_ohayo.dto.request.UserResponseRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ExerciseResultResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.service.ProgressExerciseService;

@RestController
@RequestMapping("/do-exercises")
public class ProgressExerciseController {

    private final ProgressExerciseService progressExerciseService;
    public ProgressExerciseController(ProgressExerciseService progressExerciseService) {
        this.progressExerciseService = progressExerciseService;
    }

    @PostMapping("/submit-result-details")
    public ApiResponse<ExerciseResultResponse> submit(
            @RequestBody UserResponseRequest userResponseRequest
    ) {
        ExerciseResultResponse result = progressExerciseService.submitExercise(userResponseRequest);

        return ApiResponse.<ExerciseResultResponse>builder()
                .code("200")
                .status("success")
                .message("Exercise submitted successfully")
                .data(result)
                .build();
    }

    @GetMapping("/get-sources")
    public ApiResponse<LessonExerciseResponse> getSource(
            @RequestParam int exerciseId

    ) {
        return ApiResponse.<LessonExerciseResponse>builder()
                .status("success")
                .message("Fetched source successfully")
                .data(progressExerciseService.getSource(exerciseId))
                .build();
    }

    @PostMapping("/submit-result")
    public ApiResponse<Void> postProgressExercise(
            @RequestBody ExerciseResultRequest exerciseResultRequest
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        progressExerciseService.createExerciseResult(exerciseResultRequest, email);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Post progress exercise successfully")
                .build();
    }
}
