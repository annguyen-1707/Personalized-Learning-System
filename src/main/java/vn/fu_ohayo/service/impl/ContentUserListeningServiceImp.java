package vn.fu_ohayo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fu_ohayo.dto.request.AnswerListeningRequest;
import vn.fu_ohayo.dto.response.AnswerListeningResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.dto.request.ExerciseQuestionRequest;
import vn.fu_ohayo.repository.AnswerQuestionRepository;
import vn.fu_ohayo.repository.ContentListeningRepository;
import vn.fu_ohayo.repository.ExerciseQuestionRepository;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ContentListeningProgressService;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentUserListeningServiceImp implements ContentListeningProgressService {

    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final ProgressContentRepository progressContentRepository;
    private final UserRepository userRepository;
    private final ContentListeningRepository contentListeningRepository;


    @Override
    public List<AnswerListeningResponse> getListAnser( Long contentListeningId,
                                                 List<AnswerListeningRequest> userAnswers) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        ContentListening contentListening = contentListeningRepository.findById(contentListeningId)
                .orElseThrow(() -> new EntityNotFoundException("Content listening not found"));

        List<ExerciseQuestion> questions = exerciseQuestionRepository
                .findByContentListening(contentListening);

        int totalQuestions = questions.size();
        int correctAnswers = 0;
        List<AnswerListeningResponse> userAnswerList = new ArrayList<>();
        // Calculate correct answers

        for (AnswerListeningRequest userAnswer : userAnswers) {
            ExerciseQuestion question = exerciseQuestionRepository.findById(userAnswer.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));
            AnswerListeningResponse answerResponse = new AnswerListeningResponse();
            answerResponse.setAnswerText(userAnswer.getAnswerText());
            answerResponse.setQuestionId(userAnswer.getQuestionId());

            List<AnswerQuestion> correctAnswer = answerQuestionRepository
                    .findByExerciseQuestion_ExerciseQuestionIdAndIsCorrect(question.getExerciseQuestionId(), true);

            boolean hasCorrectAnswer = false;
            if (userAnswer.getAnswerText() != null) {
                for (AnswerListeningRequest answer : userAnswers) {

                    for (AnswerQuestion correctAns : correctAnswer) {
                        if (answer.getQuestionId() == correctAns.getExerciseQuestion().getExerciseQuestionId() &&
                                answer.getAnswerText().equals(correctAns.getAnswerText())) {
                            hasCorrectAnswer = true;
                            answerResponse.setCorrect(true);
                            userAnswerList.add(answerResponse);
                            break;
                        }
                    }
                    if (hasCorrectAnswer) break;

                }
            }
            if (hasCorrectAnswer) {
                correctAnswers++;
            } else {
                answerResponse.setCorrect(false);
                userAnswerList.add(answerResponse);
            }
        }

        // Check if progress already exists
        ProgressContent progressContent = progressContentRepository
                .findByUser_UserIdAndContent_ContentId(user.getUserId(), contentListening.getContent().getContentId())
                .orElse(new ProgressContent());

        progressContent.setUser(user);
        progressContent.setContent(contentListening.getContent());
        progressContent.setCorrectAnswers(correctAnswers);
        progressContent.setTotalQuestions(totalQuestions);
        progressContent.setProgressStatus(correctAnswers == totalQuestions ?
                ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS);
        progressContentRepository.save(progressContent);
        return userAnswerList;
    }
}