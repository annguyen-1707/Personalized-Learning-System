package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Progress_lesson")
public class ProgressLesson {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressStatus status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "viewed_at")
    private Date viewedAt;

    @Column(name = "end_date")
    private Date endDate;


    @PrePersist
    public void onStart() {
        this.startDate = new Date();
        this.viewedAt = new Date();
    }
}
