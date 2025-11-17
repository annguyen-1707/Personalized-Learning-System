package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.service.ContentSpeakingService;
import vn.fu_ohayo.service.DialogueService;
import vn.fu_ohayo.service.ProgressDialogueService;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("content-speaking-client")
@RequiredArgsConstructor
public class ContentUserSpeakingController {
    private final DialogueService dialogueService;
    private final ContentSpeakingService contentSpeakingService;
    private final ProgressDialogueService progressDialogueService;
    private final UserService userService;

    @GetMapping("/{contentId}/dialogues")
    public ApiResponse<List<DialogueResponse>> getDialogueByContentSpeaking(
            @PathVariable Long contentId){
        List<DialogueResponse> dialogues = dialogueService.getDialoguesByContentSpeakingId(contentId);
        return ApiResponse.<List<DialogueResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(dialogues)
                .build();
    }

    @GetMapping()
    public ApiResponse<Page<ContentSpeakingResponse>> getContentSpeakingPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ApiResponse.<Page<ContentSpeakingResponse>>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(contentSpeakingService.getContentSpeakingPublicPage(page, size))
                .build();
    }

    @PostMapping("/upload")
    public ApiResponse<PronunciationResultResponse> assessPronunciation(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dialogueId") long dialogueId
    ) throws Exception {
        if(file.isEmpty() || dialogueId <= 0) {
            return ApiResponse.<PronunciationResultResponse>builder()
                    .code("400")
                    .status("error")
                    .message("File and reference text must not be empty")
                    .build();
        }
        return ApiResponse.<PronunciationResultResponse>builder()
                .code("200")
                .status("success")
                .message("pronunciation result successfully")
                .data(contentSpeakingService.assessPronunciation(file, dialogueId))
                .build();
    }
    @GetMapping("/dialogue/{dialogueId}/progress")
    public ApiResponse<PronunciationResultResponse> getProgressByDialogueId(
            @PathVariable Long dialogueId
    ) {  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userService.getUserByEmail(email);
        Dialogue dialogue = dialogueService.getDialogueById(dialogueId);
        ProgressDialogueResponse progressDialogueResponse = progressDialogueService.getProgressByUserAndDialogue(user, dialogue);
        PronunciationResultResponse response = progressDialogueService.toPronunciationResultResponse(progressDialogueResponse.getPronunciationResult());
        return ApiResponse.<PronunciationResultResponse>builder()
                .code("200")
                .status("success")
                .message("success")
                .data(response)
                .build();
    }

}
