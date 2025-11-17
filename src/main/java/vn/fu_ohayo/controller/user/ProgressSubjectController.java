package vn.fu_ohayo.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.service.ProgressSubjectService;

@RestController
@RequestMapping("/progress-subjects")
public class ProgressSubjectController {

    private final ProgressSubjectService progressSubjectService;

    public ProgressSubjectController(ProgressSubjectService progressSubjectService) {
        this.progressSubjectService = progressSubjectService;
    }

    @GetMapping()
    public ApiResponse<ProgressSubjectResponse> getProgressSubject(
            @RequestParam int subjectId
    ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<ProgressSubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Get progress subject successfully")
                .data(progressSubjectService.getProgressSubject(username, subjectId))
                .build();

    }

    @PatchMapping("/complete")
    public ApiResponse<ProgressSubjectResponse> completeProgressSubject(
            @RequestParam int subjectId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<ProgressSubjectResponse>builder()
                .code("200")
                .status("success")
                .message("Update progress subject successfully")
                .data(progressSubjectService.markSubjectComplete(username, subjectId))
                .build();
    }
}
