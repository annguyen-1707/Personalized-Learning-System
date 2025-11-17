package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.service.ContentReadingService;

import java.util.List;


@RestController
@RequestMapping("/content_reading")
public class ContentReadingController {
    private final ContentReadingService contentReadingService;
    String success = "success";
    public ContentReadingController(ContentReadingService contentReadingService) {
        this.contentReadingService = contentReadingService;
    }

    @GetMapping()
    public ApiResponse<Page<ContentReadingResponse>> getContentReadingPage(
            @RequestParam(defaultValue="1" ) int page,
            @RequestParam(defaultValue = "5") int size                                                                             ) {
        return ApiResponse.<Page<ContentReadingResponse>>builder()
                .code("200")
                .status(success)
                .message(success)
                .data(contentReadingService.getContentReadingPage(page, size))
                .build();
    }
    @GetMapping("/public")
    public ApiResponse<Page<ContentReadingResponse>> getPublicContentReadingPage(
            @RequestParam(defaultValue="1" ) int page,
            @RequestParam(defaultValue = "5") int size                                                                             ) {
        return ApiResponse.<Page<ContentReadingResponse>>builder()
                .code("200")
                .status(success)
                .message(success)
                .data(contentReadingService.getContentReadingPublicPage(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ContentReading> getContentReading(@PathVariable long id) {
        return  ApiResponse.<ContentReading>builder()
                .code("200")
                .status(success)
                .message("Get contentReading by id")
                .data(contentReadingService.getContentReadingById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ContentReading> createContentReading(@Valid @RequestBody ContentReadingRequest request) {
        ContentReading newContentReading =contentReadingService.handleCreateContentReading(request);
        return ApiResponse.<ContentReading>builder()
                .code("201")
                .status(success)
                .message("Created new ContentReading successfully")
                .data(newContentReading)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ContentReading> deleteContentReading(@PathVariable long id) {
        contentReadingService.deleteContentReadingById(id);
        return ApiResponse.<ContentReading>builder()
                .code("200")
                .status(success)
                .message("Deleted content reading successfully")
                .build();
    }

    @PatchMapping("/{id}")
    public  ApiResponse<ContentReadingResponse> patchContentReading(
            @PathVariable Long id,
            @Valid @RequestBody ContentReadingRequest request){
        ContentReadingResponse contentReadingResponse = contentReadingService.updatePatchContentReading(id,request );
        return ApiResponse.<ContentReadingResponse>builder()
                .code("200")
                .status(success)
                .message("Updated all fields of content reading")
                .data(contentReadingResponse)
                .build();
    }
    @PatchMapping("/{id}/add_vocabulary/{vocabularyId}")
    public ApiResponse<ContentReadingVocabularyResponse> addVocabularyToContentReading(
            @PathVariable Long id,
            @PathVariable int vocabularyId) {
        ContentReadingVocabularyResponse contentReadingResponse = contentReadingService.addVocabularyToContentReading(id, vocabularyId);
        return ApiResponse.<ContentReadingVocabularyResponse>builder()
                .code("200")
                .status(success)
                .message("Added vocabulary to content reading successfully")
                .data(contentReadingResponse)
                .build();
    }

    @PatchMapping("/{id}/remove_vocabulary/{vocabularyId}")
    public ApiResponse<Void> removeVocabularyFromContentReading(
            @PathVariable Long id,
            @PathVariable int vocabularyId) {
        contentReadingService.removeVocabularyFromContentReading(id, vocabularyId);
        return ApiResponse.<Void>builder()
                .code("200")
                .status(success)
                .message("Removed vocabulary from content reading successfully")
                .build();
    }

    @PatchMapping("/{id}/add_grammar/{grammarId}")
    public ApiResponse<ContentReadingGrammarResponse> addGrammarToContentReading(
            @PathVariable Long id,
            @PathVariable int grammarId) {
        ContentReadingGrammarResponse contentReadingResponse = contentReadingService.addGrammarToContentReading(id, grammarId);
        return ApiResponse.<ContentReadingGrammarResponse>builder()
                .code("200")
                .status(success)
                .message("Added grammar to content reading successfully")
                .data(contentReadingResponse)
                .build();
    }

    @PatchMapping("/{id}/remove_grammar/{grammarId}")
    public ApiResponse<Void> removeGrammarFromContentReading(
            @PathVariable Long id,
            @PathVariable int grammarId) {
        contentReadingService.removeGrammarFromContentReading(id, grammarId);
        return ApiResponse.<Void>builder()
                .code("200")
                .status(success)
                .message("Removed grammar from content reading successfully")
                .build();
    }
    @GetMapping("/{id}/vocabularies")
    public ApiResponse<List<VocabularyResponse>> getVocabularies(@PathVariable long id) {
        List<VocabularyResponse> vocabularies = contentReadingService.getVocabulariesByContentReadingId(id);
        return ApiResponse.<List<VocabularyResponse>>builder()
                .code("200")
                .status(success)
                .message("Retrieved vocabularies for content reading")
                .data(vocabularies)
                .build();
    }

    @GetMapping("/{id}/grammars")
    public ApiResponse<List<GrammarResponse>> getGrammars(@PathVariable long id) {
        List<GrammarResponse> grammars = contentReadingService.getGrammarsByContentReadingId(id);
        return ApiResponse.<List<GrammarResponse>>builder()
                .code("200")
                .status(success)
                .message("Retrieved grammars for content reading")
                .data(grammars)
                .build();
    }

    @PatchMapping("/accept/{id}")
    public ApiResponse<ContentReadingResponse> acceptContentReading(
            @PathVariable Long id
    ) {
        ContentReadingResponse response = contentReadingService.acceptContentReading(id);
        return ApiResponse.<ContentReadingResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<ContentReadingResponse> rejectContentReading(
            @PathVariable Long id
    ) {
        ContentReadingResponse response = contentReadingService.rejectContentReading(id);
        return ApiResponse.<ContentReadingResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<ContentReadingResponse> inActiveContentReading(
            @PathVariable Long id
    ) {
        ContentReadingResponse response = contentReadingService.inActiveContentReading(id);
        return ApiResponse.<ContentReadingResponse>builder()
                .code("200")
                .status("success")
                .message("Inactive successfully")
                .data(response)
                .build();
    }
}
