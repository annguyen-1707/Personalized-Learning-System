package vn.fu_ohayo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.DTO.FeedbackDTO;
import vn.fu_ohayo.entity.Feedback;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.mapper.FeedbackMapper;
import vn.fu_ohayo.repository.FeedbackRepository;
import vn.fu_ohayo.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class FeedBackController {
    final FeedbackMapper feedbackMapper;
    final FeedbackRepository feedbackRepository;
    final UserRepository userRepository;
    @GetMapping("/get-feedback")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) Integer rating
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Feedback> feedbacks;

        if(rating != null) {
            feedbacks = feedbackRepository.findByRating(rating, pageable);
        }
        else {
            feedbacks = feedbackRepository.findAll(pageable);
        }
        List<FeedbackDTO> dtoList = feedbacks
                .stream()
                .map(feedbackMapper::toResponse)
                .sorted(Comparator.comparing(FeedbackDTO ::getRating).reversed())
                .toList();

        return ResponseEntity.ok(Map.of(
                "content", dtoList,
                "currentPage", feedbacks.getNumber(),
                "totalPages", feedbacks.getTotalPages()
        ));
    }
    @PostMapping("/postFeedback")
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackDTO feedbackDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int countToday = feedbackRepository.countTodayFeedbacks(user.getUserId());
        if (countToday >= 3) {
            return ResponseEntity.badRequest().body("You can only post 3 feedbacks per day.");
        }

        Feedback feedback = new Feedback();
        feedback.setContent(feedbackDTO.getContent());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setUser(user);

        feedbackRepository.save(feedback);

        return ResponseEntity.ok("Feedback posted successfully!");
    }
}
