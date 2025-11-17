package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name =  "Grammar_Progress")
@Entity
public class ProgressGrammar {

    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "grammar_id")
    private Grammar grammar;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @Column(name = "reviewed_at")
    private Date reviewedAt;

    @PrePersist
    public void onReview() {
        this.reviewedAt = new Date();
}

}
