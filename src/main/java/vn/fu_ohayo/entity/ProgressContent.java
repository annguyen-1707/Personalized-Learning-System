package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setter
@Getter
@Table(name = "Content_Progress")
public class ProgressContent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "total_questions")
    private int totalQuestions;

    @Column(name = "correct_answers")
    private int correctAnswers;

    @Version
    private Integer version;
    
    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
