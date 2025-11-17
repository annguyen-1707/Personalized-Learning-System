package vn.fu_ohayo.entity;

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
@Table(name = "User_Response_Questions")
public class UserResponseQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userResponseQuestionId;

    @OneToOne
    @JoinColumn(name = "exercise_question_id")
    private ExerciseQuestion question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "answer_question_id")
    private AnswerQuestion answerQuestion;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt =new Date();
    }
}
