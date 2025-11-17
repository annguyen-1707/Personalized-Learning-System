package vn.fu_ohayo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/course-lessons")
public class CourseContentController {
    private final LessonService lessonService;

    public CourseContentController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getLessonBySubjectId(
            @RequestParam int subjectId
    ) {
        List<LessonResponse> lessons = lessonService.getLessonBySubjectId(subjectId);
        return ApiResponse.<List<LessonResponse>>builder()
                .status("success")
                .message("Fetched all lessons successfully")
                .data(lessons)
                .build();

    }
}
