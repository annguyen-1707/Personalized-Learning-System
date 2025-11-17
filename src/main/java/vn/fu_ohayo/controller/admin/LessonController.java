package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.service.LessonService;

import java.util.List;


@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ApiResponse<Page<LessonResponse>> getAllLessons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int subjectId
        ) {
        Page<LessonResponse> lessons = lessonService.getAllLessons(subjectId,page, size);
        return ApiResponse.<Page<LessonResponse>>builder()
                .status("success")
                .message("Fetched all lessons successfully")
                .data(lessons)
                .build();
    }

    @PostMapping
    public ApiResponse<LessonResponse> createLesson(@Valid @RequestBody LessonRequest lessonRequest) {
        LessonResponse createdLesson = lessonService.createLesson(lessonRequest);
        return ApiResponse.<LessonResponse>builder()
                .status("success")
                .message("Lesson created successfully")
                .data(createdLesson)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<LessonResponse> updateLesson(@PathVariable Integer id, @RequestBody LessonPatchRequest lessonRequest) {
        LessonResponse updatedLesson = lessonService.updateLesson(id, lessonRequest);
        return ApiResponse.<LessonResponse>builder()
                .status("success")
                .message("Lesson updated successfully")
                .data(updatedLesson)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Lesson deleted successfully")
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<List<LessonStatus>> getLessonStatuses() {
        List<LessonStatus> statuses = List.of(LessonStatus.values());
        return ApiResponse.<List<LessonStatus>>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson statuses successfully")
                .data(statuses)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LessonResponse> getLessonById(@PathVariable int id) {
        LessonResponse lesson = lessonService.getLessonById(id);
        return ApiResponse.<LessonResponse>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson by ID successfully")
                .data(lesson)
                .build();
    }

    @PatchMapping("/accept/{id}")
    public ApiResponse<LessonResponse> acceptLesson(@PathVariable("id") int id) {
        LessonResponse acceptedLesson = lessonService.acceptLesson(id);
        return ApiResponse.<LessonResponse>builder()
                .code("200")
                .status("success")
                .message("Lesson accepted successfully")
                .data(acceptedLesson)
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<LessonResponse> rejectLesson(@PathVariable("id") int id) {
        LessonResponse rejectedLesson = lessonService.rejectLesson(id);
        return ApiResponse.<LessonResponse>builder()
                .code("200")
                .status("success")
                .message("Lesson rejected successfully")
                .data(rejectedLesson)
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<LessonResponse> inactiveLesson(@PathVariable("id") int id) {
        LessonResponse inactiveLesson = lessonService.inactiveLesson(id);
        return ApiResponse.<LessonResponse>builder()
                .code("200")
                .status("success")
                .message("Lesson inactive successfully")
                .data(inactiveLesson)
                .build();
    }
}
