package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "Lesson_Exercises")
public class LessonExercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private int exerciseId;

    @Column(name = "title")
    private String title;

//  @Size(min = 1, message = ErrorEnum.MIN_TIME_1)
    @Min(1)
    @Column(name = "duration")
    private long duration;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "lessonExercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExerciseResult> exerciseResults;

    @PrePersist
    public void createAtNow() {
        this.createdAt = new Date();}
}
