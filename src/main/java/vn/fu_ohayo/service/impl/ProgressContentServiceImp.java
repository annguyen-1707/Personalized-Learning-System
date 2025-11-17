package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.repository.ContentRepository;
import vn.fu_ohayo.service.ProgressContentService;

import java.util.*;

@Service
@AllArgsConstructor
public class ProgressContentServiceImp implements ProgressContentService {
    private final ProgressContentRepository progressContentRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository; // Add this if not present

    @Override
    public ProgressContent saveUserReadingProgress(User user, ContentReading contentReading, int progress) {
        ProgressContent progressContent = new ProgressContent();
        progressContent.setUser(user);
        progressContent.setContent(contentReading.getContent());
        progressContent.setProgressId(progress);
        return progressContentRepository.save(progressContent);
    }

    @Override
    public ProgressContent saveUserListeningProgress(User user, ContentListening contentListening, int progress, int totalQuestions) {
        ProgressContent progressContent = new ProgressContent();
        progressContent.setUser(user);
        progressContent.setContent(contentListening.getContent());
        progressContent.setProgressId(progress);
        progressContent.setTotalQuestions(totalQuestions);
        return progressContentRepository.save(progressContent);
    }

    @Override
    public List<AnswerQuestionResponse> checkListeningAnswers(List<AnswerQuestionRequest> userAnswers, int userId, int contentId) {
        int correctCount = 0;
        List<AnswerQuestionResponse> responses = new ArrayList<>();
        for (AnswerQuestionRequest req : userAnswers) {
            Optional<AnswerQuestion> answerOpt = answerQuestionRepository.findById(req.getAnswerId());
            boolean isCorrect = answerOpt.isPresent() && Boolean.TRUE.equals(answerOpt.get().getIsCorrect());
            if (isCorrect) correctCount++;
            responses.add(AnswerQuestionResponse.builder()
                    .answerId(req.getAnswerId())
                    .answerText(req.getAnswerText())
                    .isCorrect(isCorrect)
                    .build());
        }
        // Save progress
        ProgressContent progress = ProgressContent.builder()
                .user(userRepository.findById((long)userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId)))
                .content(contentRepository.findById((long)contentId).orElseThrow(()-> new RuntimeException("Content not found with id: " + contentId)))
                .totalQuestions(userAnswers.size())
                .correctAnswers(correctCount)
                .progressStatus(correctCount == userAnswers.size() ? ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS)
                .build();
        progressContentRepository.save(progress);
        return responses;
    }
}