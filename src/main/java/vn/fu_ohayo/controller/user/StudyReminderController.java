package vn.fu_ohayo.controller.user;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.StudyReminderRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.StudyReminderResponse;
import vn.fu_ohayo.service.StudyReminderService;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/study-reminder")
public class StudyReminderController {

    private final StudyReminderService studyReminderService;
    private final UserService userService;

    public StudyReminderController(StudyReminderService studyReminderService, UserService userService) {
        this.studyReminderService = studyReminderService;
        this.userService = userService;
    }

    @GetMapping()
    public ApiResponse<List<StudyReminderResponse>>  getStudyReminder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        return ApiResponse.<List<StudyReminderResponse>>builder()
                .code("200")
                .status("success")
                .message("Get progress lessons successfully")
                .data(studyReminderService.getStudyRemindersByUserId(userId))
                .build();
    }

    @PostMapping()
    public ApiResponse<StudyReminderResponse> createStudyReminder(@Valid @RequestBody StudyReminderRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        StudyReminderResponse response = studyReminderService.addStudyReminder(request, userId);
        return ApiResponse.<StudyReminderResponse>builder()
                .code("201")
                .status("success")
                .message("Study reminder created successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<StudyReminderResponse> updateStudyReminder(
            @PathVariable int id,
            @Valid @RequestBody StudyReminderRequest request) {
        StudyReminderResponse response = studyReminderService.updateStudyReminder(id, request);
        return ApiResponse.<StudyReminderResponse>builder()
                .code("201")
                .status("success")
                .message("Study reminder update successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteStudyReminder(@PathVariable int id) {
        studyReminderService.deleteStudyReminder(id);
        return ApiResponse.<String>builder()
                .code("201")
                .status("success")
                .message("Study reminder deleted successfully")
                .data("Study reminder deleted successfully")
                .build();
    }
}
