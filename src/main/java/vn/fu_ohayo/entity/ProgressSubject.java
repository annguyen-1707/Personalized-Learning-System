package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Progress_Subjects")
public class ProgressSubject {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int progressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "viewed_at")
    private Date viewedAt;

    @Column(name = "end_date")
    private Date endDate;

    @PrePersist
    protected void onCreate() {
        this.startDate = new Date();
    }
}
