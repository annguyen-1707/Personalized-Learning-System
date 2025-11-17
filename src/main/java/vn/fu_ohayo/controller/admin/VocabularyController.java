package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;
import vn.fu_ohayo.service.VocabularyService;

import java.util.List;


@RestController
@RequestMapping("/vocabularies")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping
    public ApiResponse<Page<VocabularyResponse>> getAllVocabularies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam int lessonId
    ) {
        return ApiResponse.<Page<VocabularyResponse>>builder()
                .code("200")
                .message("Get all vocabularies successfully")
                .status("success")
                .data(vocabularyService.getVocabularyPage(page, size, lessonId))
                .build();
    }

    @PostMapping("/{vocabularyId}/add-to-lesson/{lessonId}")
    public ApiResponse<Void> addVocabularyIntoLesson(
            @PathVariable int vocabularyId,
            @PathVariable int lessonId) {
         vocabularyService.handleSaveVocabulary(lessonId, vocabularyId);
        return ApiResponse.<Void>builder()
                .code("201")
                .message("Vocabulary created successfully")
                .status("success")
                .data(null)
                .build();
    }

    @PostMapping
    public ApiResponse<VocabularyResponse> addVocabulary(@Valid @RequestBody VocabularyRequest vocabularyRequest) {
        VocabularyResponse createdVocabulary = vocabularyService.handleSaveVocabulary(vocabularyRequest);
        return ApiResponse.<VocabularyResponse>builder()
                .code("201")
                .message("Vocabulary created successfully")
                .status("success")
                .data(createdVocabulary)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VocabularyResponse> updateVocabulary(@PathVariable int id, @Valid @RequestBody VocabularyRequest vocabularyRequest) {
        VocabularyResponse updatedVocabulary = vocabularyService.updatePutVocabulary(id, vocabularyRequest);
        return ApiResponse.<VocabularyResponse>builder()
                .code("200")
                .message("Vocabulary updated successfully")
                .status("success")
                .data(updatedVocabulary)
                .build();
    }

    @DeleteMapping("/{id}/remove-from-lesson/{lessonId}")
    public ApiResponse<Void> deleteVocabularyFromLesson(@PathVariable int id,
                                              @PathVariable int lessonId) {
        vocabularyService.deleteVocabularyFromLesson(id, lessonId);
        return ApiResponse.<Void>builder()
                .code("204")
                .message("Vocabulary deleted successfully")
                .status("success")
                .data(null)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVocabulary(@PathVariable int id) {
        vocabularyService.deleteVocabularyById(id);
        return ApiResponse.<Void>builder()
                .code("204")
                .message("Vocabulary deleted successfully")
                .status("success")
                .data(null)
                .build();
    }

    @GetMapping("/levels")
    public ApiResponse<List<JlptLevel>> getLessonLevels() {
        List<JlptLevel> levels = List.of(JlptLevel.values());
        return ApiResponse.<List<JlptLevel>>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson statuses successfully")
                .data(levels)
                .build();
    }

    @GetMapping("/part-of-speech")
    public ApiResponse<List<PartOfSpeech>> getLessonPartOfSpeech() {
        List<PartOfSpeech> partOfSpeeches = List.of(PartOfSpeech.values());
        return ApiResponse.<List<PartOfSpeech>>builder()
                .code("200")
                .status("success")
                .message("Fetched lesson statuses successfully")
                .data(partOfSpeeches)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<Page<VocabularyResponse>> getAllVocabularies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<VocabularyResponse> vocabularyResponses = vocabularyService.getAllVocabulariesPage(page, size);
        return ApiResponse.<Page<VocabularyResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched all vocabularies successfully")
                .data(vocabularyResponses)
                .build();
    }

    @GetMapping("/favorite/{id}")
    public ApiResponse<List<VocabularyResponse>> getVocabularyByFavoriteVocabularyId(@PathVariable int id) {
        List<VocabularyResponse> vocabularyResponses = vocabularyService.getVocabularysByFavoriteVocabularyId(id);
        return ApiResponse.<List<VocabularyResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched vocabularies by favorite folder successfully")
                .data(vocabularyResponses)
                .build();
    }

    @GetMapping("get-all-without-lesson/{lessonId}")
    public ApiResponse<Page<VocabularyResponse>> getAllVocabulariesWithoutLesson(
            @PathVariable int lessonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Page<VocabularyResponse> vocabularyResponses = vocabularyService.getAllVocabularies(lessonId, page, size);
        return ApiResponse.<Page<VocabularyResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched all vocabularies without lesson successfully")
                .data(vocabularyResponses)
                .build();
    }

}
