package vn.fu_ohayo.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressLessonResponse;
import vn.fu_ohayo.service.ProgressLessonService;

@RestController
@RequestMapping("/progress-lessons")
public class ProgressLessonController {
    private final ProgressLessonService progressLessonService;

    public ProgressLessonController(ProgressLessonService progressLessonService) {
        this.progressLessonService = progressLessonService;
    }
    @GetMapping
    public ApiResponse<ProgressLessonResponse> getProgressLessons(
            @RequestParam int lessonId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<ProgressLessonResponse>builder()
                .code("200")
                .status("success")
                .message("Get progress lessons successfully")
                .data(progressLessonService.getProgressLessons(username, lessonId))
                .build();
    }

    @PostMapping()
    public ApiResponse<ProgressLessonResponse> postProgressLesson(
            @RequestParam int lessonId
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<ProgressLessonResponse>builder()
                .code("200")
                .status("success")
                .message("Post progress lesson successfully")
                .data(progressLessonService.createProgressLesson(username, lessonId))
                .build();
    }

    @PatchMapping()
    public ApiResponse<ProgressLessonResponse> patchProgressLesson(
            @RequestParam int lessonId,
            @RequestParam boolean isCompleted
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<ProgressLessonResponse>builder()
                .code("200")
                .status("success")
                .message("Patch progress lesson successfully")
                .data(progressLessonService.updateProgressLesson(username, lessonId, isCompleted))
                .build();
    }



}
