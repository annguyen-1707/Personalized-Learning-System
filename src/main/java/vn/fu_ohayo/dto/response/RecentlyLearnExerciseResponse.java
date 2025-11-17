package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.LessonExercise;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecentlyLearnExerciseResponse {
    private int exerciseResultId;
    private int totalQuestions;
    private int correctAnswers;
    private Date submissionTime;
    private LessonExerciseResponse2 lessonExercise;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class LessonExerciseResponse2 {
        private int id;
        private String title;
        private long duration;
        private int lessonId;
    }

}
