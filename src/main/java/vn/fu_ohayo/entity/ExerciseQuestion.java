package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.ContentStatus;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Where(clause = "is_deleted = false")
@Table(name = "Exercise_Questions")
public class ExerciseQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_question_id")
    private int exerciseQuestionId;

    @Column(name = "question_text", columnDefinition = "TEXT")
    @NotNull(message = "Question text cannot be null")
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = true)
    private LessonExercise lessonExercise;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = true)
    private ContentListening contentListening;

    @OneToMany(mappedBy = "exerciseQuestion", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
//    @Size(min = 2, message = "The list must contain at least 2 answer")
    private List<AnswerQuestion> answerQuestions;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    private String type;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

    @Column(name = "is_deleted")
    private boolean deleted = false;

}
