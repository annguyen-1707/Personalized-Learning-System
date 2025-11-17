package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Feedback {
    @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    @Column(name = "feedback_id")
    private int feedbackId;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "rating")
    private int rating = 0;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
