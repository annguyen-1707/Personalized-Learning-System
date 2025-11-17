package vn.fu_ohayo.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.service.ProgressSubjectService;
import vn.fu_ohayo.service.SubjectService;

@RestController()
@RequestMapping("/subject-enrollments")
public class SubjectEnrollmentController {


    private final ProgressSubjectService progressSubjectService;

    public SubjectEnrollmentController(ProgressSubjectService progressSubjectService) {
        this.progressSubjectService = progressSubjectService;
    }

    @PostMapping()
    public ApiResponse<String> enrollCourse(
            @RequestParam() int subjectId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        progressSubjectService.enrollCourse(subjectId, username);
        return ApiResponse.<String>builder()
                .code("200")
                .status("success")
                .message("User enrolled in course successfully")
                .data("Enrollment successful")
                .build();
    }
}
