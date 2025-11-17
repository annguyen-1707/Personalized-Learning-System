package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.dto.response.LessonExerciseResponse;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.mapper.LessonMapper;
import vn.fu_ohayo.service.ContentListeningService;

import java.util.List;

import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE;
import static vn.fu_ohayo.constant.ConstantGolbal.HTTP_SUCCESS_RESPONSE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content_listening")
public class ContentListeningController {
    private final ContentListeningService contentListeningService;
    private final ContentMapper contentMapper;

    @GetMapping()
    public ApiResponse<Page<ContentListeningResponse>> getContentListeningPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ApiResponse.<Page<ContentListeningResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentListeningService.getContentListeningPage(page, size))
                .build();
    }
    @GetMapping("/public")
    public ApiResponse<Page<ContentListeningResponse>> getPublicContentListeningPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ApiResponse.<Page<ContentListeningResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentListeningService.getContentListeningPublicPage(page, size))
                .build();
    }

    @GetMapping("/details/{id}")
    public ApiResponse<ContentListeningResponse> getContentListening(@PathVariable long id) {
        ContentListeningResponse contentListeningResponse = contentMapper.toContentListeningResponse(contentListeningService.getContentListeningById(id));
        return  ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Get contentListening by id")
                .data(contentListeningResponse)
                .build();
    }

    @PostMapping
    public ApiResponse<ContentListening> createContentListening(@Valid @RequestBody ContentListeningRequest request) {
        ContentListening newContentListening = contentListeningService.handleCreateContentListening(request);
        return ApiResponse.<ContentListening>builder()
                .code("201")
                .status("success")
                .message("Created new ContentListening successfully")
                .data(newContentListening)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentListening> deleteContentListening(@PathVariable long id) {
        contentListeningService.deleteContentListeningById(id);
        return ApiResponse.<ContentListening>builder()
                .code("200")
                .status("success")
                .message("Deleted content listening successfully")
                .build();

    }

    @PatchMapping("/{id}")
    public ApiResponse<ContentListeningResponse> patchContentListening(
            @PathVariable Long id,
            @Valid @RequestBody ContentListeningRequest request) {
        ContentListeningResponse contentListeningResponse = contentListeningService.updatePatchContentListening(id, request);
        return ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Updated some fields of content listening")
                .data(contentListeningResponse)
                .build();
    }

    @PatchMapping("/accept/{id}")
    public ApiResponse<ContentListeningResponse> acceptContentListening(
            @PathVariable Long id
    ) {
        ContentListeningResponse response = contentListeningService.acceptContentListening(id);
        return ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<ContentListeningResponse> rejectContentListening(
            @PathVariable Long id
    ) {
        ContentListeningResponse response = contentListeningService.rejectContentListening(id);
        return ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @GetMapping("/jlptLevel")
    public ApiResponse<List<ContentListeningResponse>> getContentListeningByLever(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam JlptLevel jlptLevel) {
        return ApiResponse.<List<ContentListeningResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentListeningService.getListContentListeningsBylever(jlptLevel))
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<ContentListeningResponse> inActiveContentListening(
            @PathVariable Long id
    ) {
        ContentListeningResponse response = contentListeningService.inActiveContentListening(id);
        return ApiResponse.<ContentListeningResponse>builder()
                .code("200")
                .status("success")
                .message("Inactive successfully")
                .data(response)
                .build();
    }

}

