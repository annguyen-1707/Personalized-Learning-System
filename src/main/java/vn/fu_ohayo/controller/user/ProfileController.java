package vn.fu_ohayo.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.LearningProgressOverviewResponse;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.service.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ExerciseResultService exerciseResultService;
    private final ProgressGrammarService progressGrammarService;
    private final ProgressVocabularyService progressVocabularyService;
    private final UserService userService;
    private final UserMapper userMapper;

    public ProfileController(ExerciseResultService exerciseResultService, ProgressGrammarService progressGrammarService, ProgressVocabularyService progressVocabularyService, UserService userService, UserMapper userMapper) {
        this.exerciseResultService = exerciseResultService;
        this.progressGrammarService = progressGrammarService;
        this.progressVocabularyService = progressVocabularyService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/overview/learning_progress")
    ApiResponse<LearningProgressOverviewResponse> getLearningProgressResponse(
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
         LearningProgressOverviewResponse response = LearningProgressOverviewResponse.builder()
                 .totalVocabularyAllSubject(progressVocabularyService.countAllVocabularySubjectInProgressByUserId(userId))
                 .totalVocabularyLearn(progressVocabularyService.countVocabularyLearnSubjectInProgressByUserId(userId))
                 .exerciseAllSubject(exerciseResultService.countAllExerciseSubjectInProgressByUserId(userId))
                 .exerciseCompleted(exerciseResultService.countExerciseDoneSubjectInProgressByUserId(userId))
                 .totalGrammarAllSubject(progressVocabularyService.countAllVocabularySubjectInProgressByUserId(userId))
                 .totalGrammarLearn(progressGrammarService.countGrammarLearnSubjectInProgressByUserId(userId))
                .build();
        return ApiResponse.<LearningProgressOverviewResponse>builder()
                .status("success")
                .message("Fetched all lessons successfully")
                .data(response)
                .build();
    }

    @GetMapping("/progress/vocabulary")
    ApiResponse<?> getProgressVocabularyByUserId(
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched vocabulary progress successfully")
                .data(progressVocabularyService.getProgressEachSubjectByUserId(userId))
                .build();
    }

    @GetMapping("/progress/grammar")
    ApiResponse<?> getProgressGrammarByUserId(
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched grammar progress successfully")
                .data(progressGrammarService.getProgressEachSubjectByUserId(userId))
                .build();
    }

    @GetMapping("/progress/exercise")
    ApiResponse<?> getProgressExerciseByUserId(
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        long userId = userService.getUserByEmail(email).getUserId();
        return ApiResponse.<Object>builder()
                .status("success")
                .message("Fetched exercise progress successfully")
                .data(exerciseResultService.getProgressEachSubjectByUserId(userId))
                .build();
    }
    @GetMapping("/information")
    public ApiResponse<?> getInformationByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }

        UserResponse userResponse = userMapper.toUserResponse(userService.getUserByEmail(email));
        return ApiResponse.<UserResponse>builder()
                .status("success")
                .message("Fetched user information successfully")
                .data(userResponse)
                .build();
    }

    @PatchMapping("/updateAvatar")
    public ApiResponse<String> updateAvatar(@RequestParam String avatar) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        String updatedAvatarUrl = userService.updateAvatar(email, avatar);
        return ApiResponse.<String>builder()
                .status("success")
                .message("Avatar updated successfully")
                .data(updatedAvatarUrl)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return ApiResponse.<UserResponse>builder()
                .status("success")
                .message("Fetched user successfully")
                .data(userResponse)
                .build();
    }

}
