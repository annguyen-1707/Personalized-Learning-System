package vn.fu_ohayo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Answer_Questions")
public class AnswerQuestion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private int answerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_question_id")
    @JsonIgnore
    private ExerciseQuestion exerciseQuestion;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "is_correct", columnDefinition = "BIT")
    private Boolean isCorrect;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
