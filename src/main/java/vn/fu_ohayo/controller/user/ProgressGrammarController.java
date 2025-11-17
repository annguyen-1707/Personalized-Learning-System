package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.constant.ConstantGolbal;
import vn.fu_ohayo.dto.request.ProgressUpdateRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarsFlashCardResponse;
import vn.fu_ohayo.dto.response.ProgressVocabularyFlashCardResponse;
import vn.fu_ohayo.service.ProgressGrammarService;
import vn.fu_ohayo.service.ProgressVocabularyService;

import java.util.List;

@RestController
@RequestMapping("/progress-grammars")
@RequiredArgsConstructor
public class ProgressGrammarController {
    private final ProgressGrammarService progressGrammarService;

    @PatchMapping("/{grammarId}/complete")
    public ApiResponse<?> updateProgress(
            @PathVariable int grammarId,
            @RequestBody ProgressUpdateRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        progressGrammarService.updateProgress(grammarId, request, email);
        return ApiResponse.builder()
                .code(ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE)
                .status(ConstantGolbal.HTTP_SUCCESS_RESPONSE)
                .message("Update progress vocabulary successfully")
                .build();
    }

    @GetMapping("/known")
    public ApiResponse<List<ProgressGrammarsFlashCardResponse>> getKnownProgressVocabularies(
            @RequestParam int lessonId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<ProgressGrammarsFlashCardResponse> response = progressGrammarService.getKnownProgressGrammars(email, lessonId);
        return ApiResponse.<List<ProgressGrammarsFlashCardResponse>>builder()
                .code(ConstantGolbal.HTTP_SUCCESS_CODE_RESPONSE)
                .status(ConstantGolbal.HTTP_SUCCESS_RESPONSE)
                .message("Get known progress vocabularies successfully")
                .data(response)
                .build();
    }
}
