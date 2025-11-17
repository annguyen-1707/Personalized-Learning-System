package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.fu_ohayo.dto.request.AnswerListeningRequest;
import vn.fu_ohayo.dto.response.AnswerListeningResponse;
import vn.fu_ohayo.mapper.ProgressContentMapper;
import vn.fu_ohayo.service.ContentListeningProgressService;
import vn.fu_ohayo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("content-listening")
@RequiredArgsConstructor
@Slf4j(topic = "ContentUserListeningController")
public class ContentUserListeningController {
    private  final ProgressContentMapper progressContentMapper;
    private final ContentListeningProgressService contentListeningProgressService;
    private final UserService userService;

    @PostMapping("/submit-answers")
    public ResponseEntity<List<AnswerListeningResponse>> submitListeningAnswers(
            @RequestParam Long contentListeningId,
            @RequestBody List<AnswerListeningRequest> userAnswers) {

        List<AnswerListeningResponse> list = contentListeningProgressService.getListAnser(
                 contentListeningId, userAnswers );
        log.info("User answers submitted for content listening ID: {}", list);
        return ResponseEntity.ok(list);
    }
}