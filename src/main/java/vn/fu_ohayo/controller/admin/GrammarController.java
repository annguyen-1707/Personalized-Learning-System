package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.request.PatchGrammarRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.service.GrammarService;

import java.util.List;

@RestController
@RequestMapping("/grammars")
public class GrammarController {

    private final GrammarService grammarService;

    public GrammarController(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @GetMapping
    public ApiResponse<Page<GrammarResponse>> getAllGrammars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam() int lessonId
    ) {
        Page<GrammarResponse> grammars = grammarService.getAllGrammars(lessonId, page, size);
        return ApiResponse.<Page<GrammarResponse>>builder()
                .status("success")
                .message("Fetched all grammars successfully")
                .data(grammars)
                .build();
    }


    @PostMapping
    public ApiResponse<GrammarResponse> createGrammar(@Valid @RequestBody GrammarRequest grammarRequest) {
        GrammarResponse createdGrammar = grammarService.saveGrammar(grammarRequest);
        return ApiResponse.<GrammarResponse>builder()
                .status("success")
                .message("Grammar created successfully")
                .data(createdGrammar)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteGrammar(@PathVariable int id) {
        grammarService.deleteGrammarById(id);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Grammar deleted successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<GrammarResponse> updateGrammar(@PathVariable int id, @Valid @RequestBody GrammarRequest grammarRequest) {
        GrammarResponse updatedGrammar = grammarService.updateGrammar(id, grammarRequest);
        return ApiResponse.<GrammarResponse>builder()
                .status("success")
                .message("Grammar updated successfully")
                .data(updatedGrammar)
                .build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<GrammarResponse> partialUpdateGrammar(@PathVariable int id, @Valid @RequestBody PatchGrammarRequest patchGrammarRequest) {
        GrammarResponse patchedGrammar = grammarService.patchGrammar(id, patchGrammarRequest);
        return ApiResponse.<GrammarResponse>builder()
                .status("success")
                .message("Grammar partially updated successfully")
                .data(patchedGrammar)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<Page<GrammarResponse>> getAllGrammars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<GrammarResponse> grammarResponses = grammarService.getAllGrammarsPage(page, size);
        return ApiResponse.<Page<GrammarResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched all grammars successfully")
                .data(grammarResponses)
                .build();
    }

    @GetMapping("/not-in-lesson/{lessonId}")
    public ApiResponse<Page<GrammarResponse>> getGrammarsNotInLesson(
            @PathVariable int lessonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Page<GrammarResponse> grammarsNotInLesson = grammarService.getGrammarsNotInLesson(lessonId, page, size);
        return ApiResponse.<Page<GrammarResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched grammars not in lesson successfully")
                .data(grammarsNotInLesson)
                .build();
    }

    @DeleteMapping("/{id}/remove-from-lesson/{lessonId}")
    public ApiResponse<Void> deleteGrammarFromLesson(@PathVariable int id, @PathVariable int lessonId) {
        grammarService.deleteGrammarFromLesson(id, lessonId);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Grammar removed from lesson successfully")
                .build();
    }

    @PostMapping("{grammarId}/add-to-lesson/{lessonId}")
    public ApiResponse<Void> addGrammarToLesson(@PathVariable int grammarId, @PathVariable int lessonId) {
        grammarService.handleSaveGrammarIntoLesson(lessonId, grammarId);
        return ApiResponse.<Void>builder()
                .status("success")
                .message("Grammar added to lesson successfully")
                .build();
    }

}
