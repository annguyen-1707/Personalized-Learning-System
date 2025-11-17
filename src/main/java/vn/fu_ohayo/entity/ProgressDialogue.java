package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name =  "progress_dialogue")
@Entity
@ToString(exclude = {"user", "dialogue", "pronunciationResult"})
public class ProgressDialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "dialogue_id")
    private Dialogue dialogue;

    @OneToOne
    @JoinColumn(name = "pronunciation_result_id")
    private PronunciationResult pronunciationResult;

    @Version
    private Integer version;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
