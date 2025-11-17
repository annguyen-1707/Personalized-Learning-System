package vn.fu_ohayo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.ContentStatus;

import java.util.Date;

@Entity
@Table(name = "Dialogues")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Where(clause = "is_deleted = false")
@ToString(exclude = {"contentSpeaking"})
public class Dialogue {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "dialogue_id")
    private long dialogueId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "content_speaking_id")
    @JsonBackReference
    private ContentSpeaking contentSpeaking;

    @Column(name = "question_jp")
    private String questionJp;

    @Column(name = "question_vn")
    private String questionVn;

    @Column(name = "answer_vn")
    private String answerVn;

    @Column(name = "answer_jp")
    private String answerJp;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Column(name = "is_deleted")
    private boolean deleted = false;

}
