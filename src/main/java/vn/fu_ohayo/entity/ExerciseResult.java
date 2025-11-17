package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Exercise_Results")
public class ExerciseResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exerciseResultId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private LessonExercise lessonExercise;

    @Column(name = "total_questions")
    private int totalQuestions;

    @Column(name = "correct_answers")
    private int correctAnswers;

    @Column(name = "submission_time")
    private Date submissionTime;

    @jakarta.persistence.PrePersist
    protected void onCreate() {
        this.submissionTime = new Date();
    }
}
