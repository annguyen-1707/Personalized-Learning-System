package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.service.ContentSpeakingService;

import java.util.List;


@RestController
@RequestMapping("/content_speaking")
public class ContentSpeakingController {
    private final ContentSpeakingService contentSpeakingService;

    public ContentSpeakingController(ContentSpeakingService contentSpeakingService) {
        this.contentSpeakingService = contentSpeakingService;

    }

    @GetMapping("/all")
    public ApiResponse<List<ContentSpeaking>> getContentSpeakings() {
        return ApiResponse.<List<ContentSpeaking>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentSpeakingService.getAllContentSpeakings())
                .build();
    }

    @GetMapping()
    public ApiResponse<Page<ContentSpeakingResponse>> getContentSpeakingPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ApiResponse.<Page<ContentSpeakingResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentSpeakingService.getContentSpeakingPage(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ContentSpeaking> getContentSpeaking(@PathVariable long id) {
        return ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Get contentSpeaking by id")
                .data(contentSpeakingService.getContentSpeakingById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ContentSpeaking> createContentSpeaking(@Valid @RequestBody ContentSpeakingRequest request) {
        ContentSpeaking newContentSpeaking = contentSpeakingService.handleCreateContentSpeaking(request);
        return ApiResponse.<ContentSpeaking>builder()
                .code("201")
                .status("success")
                .message("Created new ContentSpeaking successfully")
                .data(newContentSpeaking)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentSpeaking> deleteContentSpeaking(@PathVariable long id) {
        contentSpeakingService.deleteContentSpeakingById(id);
        return ApiResponse.<ContentSpeaking>builder()
                .code("200")
                .status("success")
                .message("Deleted content speaking successfully")
                .build();

    }

    @PutMapping("/{id}")
    public ApiResponse<ContentSpeakingResponse> updateContentSpeaking(
            @PathVariable Long id,
            @Valid @RequestBody ContentSpeakingRequest request) {
        ContentSpeakingResponse contentSpeakingResponse = contentSpeakingService.updatePutContentSpeaking(id, request);
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(contentSpeakingResponse)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<ContentSpeakingResponse> patchContentSpeaking(
            @PathVariable Long id,
            @Valid @RequestBody ContentSpeakingRequest request) {
        ContentSpeakingResponse contentSpeakingResponse = contentSpeakingService.updatePatchContentSpeaking(id, request);
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Updated all fields of content speaking")
                .data(contentSpeakingResponse)
                .build();
    }

    @PatchMapping("/accept/{id}")
    public ApiResponse<ContentSpeakingResponse> acceptContentSpeaking(
            @PathVariable Long id
    ) {
        ContentSpeakingResponse response = contentSpeakingService.acceptContentSpeaking(id);
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<ContentSpeakingResponse> rejectContentSpeaking(
            @PathVariable Long id
    ) {
        ContentSpeakingResponse response = contentSpeakingService.rejectContentSpeaking(id);
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @GetMapping("/jlptLevel")
    public ApiResponse<List<ContentSpeakingResponse>> getContentSpeakingByLever(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam JlptLevel jlptLevel) {
        return ApiResponse.<List<ContentSpeakingResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentSpeakingService.getListContentSpeakingBylever(jlptLevel))
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<ContentSpeakingResponse> inActiveContentSpeaking(
            @PathVariable Long id
    ) {
        ContentSpeakingResponse response = contentSpeakingService.inActiveContentSpeaking(id);
        return ApiResponse.<ContentSpeakingResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }
}
