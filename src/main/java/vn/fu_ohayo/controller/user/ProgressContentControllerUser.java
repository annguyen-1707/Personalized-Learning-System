//package vn.fu_ohayo.controller.user;
//
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
//import vn.fu_ohayo.dto.request.CheckAnswerWrapperRequest;
//import vn.fu_ohayo.entity.ContentListening;
//import vn.fu_ohayo.entity.ContentReading;
//import vn.fu_ohayo.entity.ProgressContent;
//import vn.fu_ohayo.entity.User;
//import vn.fu_ohayo.repository.ContentListeningRepository;
//import vn.fu_ohayo.repository.ContentReadingRepository;
//import vn.fu_ohayo.repository.ProgressContentRepository;
//import vn.fu_ohayo.repository.UserRepository;
//import vn.fu_ohayo.service.ProgressContentService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/user/contents_listening")
//@AllArgsConstructor
//public class ProgressContentControllerUser {
//
//    private ProgressContentService progressContentService;
//    private UserRepository userRepository;
//    private ProgressContentRepository progressContentRepository;
//    private ContentListeningRepository contentListeningRepository;
//    private ContentReadingRepository contentReadingRepository;
//
//    @PostMapping("/progressListening")
//    public ProgressContent saveListeningProgress(
//            @RequestParam Long userId,
//            @RequestParam Long contentListeningId,
//            @RequestParam int correct_answers
//    ,       @RequestParam int total_questions) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//        ContentListening contentListening = contentListeningRepository.findById(contentListeningId).orElseThrow(()
//                -> new RuntimeException("ContentListening not found with id: " + contentListeningId));
//        return progressContentService.saveUserListeningProgress(user, contentListening, correct_answers, total_questions);
//    }
//
//    @PostMapping("/progressReading")
//    public ProgressContent saveReadingProgress(
//            @RequestParam Long userId,
//            @RequestParam Long contentReadingId,
//            @RequestParam int progress) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//        ContentReading contentReading = contentReadingRepository.findById(contentReadingId).orElseThrow(()
//                -> new RuntimeException("ContentReading not found with id: " + contentReadingId));
//        return progressContentService.saveUserReadingProgress(user, contentReading, progress);
//    }
//    @PostMapping("/check-answer")
//    public List<vn.fu_ohayo.dto.response.AnswerQuestionResponse> checkAnswers(
//            @RequestBody CheckAnswerWrapperRequest checkAnswerWrapperRequest) {
//        return progressContentService.checkListeningAnswers(checkAnswerWrapperRequest.getUserAnswers()
//                , checkAnswerWrapperRequest.getUserId()
//                , checkAnswerWrapperRequest.getContentId());
//    }
//}