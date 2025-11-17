//package vn.fu_ohayo.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import vn.fu_ohayo.dto.response.ContentListeningResponse;
//import vn.fu_ohayo.entity.ExerciseQuestion;
//import vn.fu_ohayo.service.ContentListeningService;
//import vn.fu_ohayo.service.ContentListeningServiceUser;
//import vn.fu_ohayo.service.ExerciseQuestionService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/listening")
//@RequiredArgsConstructor
//public class ContentListeningControllerUser {
//
//    private final ContentListeningServiceUser contentListeningServiceUser;
//    private final ExerciseQuestionService exerciseQuestionService;
//
//    @GetMapping("/lessons")
//    public List<ContentListeningResponse> getAllLessons() {
//        return contentListeningService.getAllContentListening();
//    }
//
//    @GetMapping("/lesson/{id}")
//    public LessonDetailResponse getLessonDetail(@PathVariable Long id) {
//        ContentListeningResponse lesson = contentListeningService.getContentListeningByIdResponse(id);
//        List<ExerciseQuestion> questions = exerciseQuestionService.getQuestionsByContentListeningId(id);
//        return new LessonDetailResponse(lesson, questions);
//    }
//}