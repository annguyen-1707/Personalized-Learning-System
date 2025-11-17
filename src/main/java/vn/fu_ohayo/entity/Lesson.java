package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Lessons",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "lesson_name")
},
        indexes = {
                @Index(name = "idx_lesson_id", columnList = "lesson_id"),
                @Index(name = "idx_lesson_name", columnList = "lesson_name"),
                @Index(name = "idx_subject_id", columnList = "subject_id")
}
        )
@Where(clause = "is_deleted = false")

public class Lesson {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "lesson_id")
    private int lessonId;

    @Column(name = "lesson_name", nullable = false)
    private String name;

    @Column(name = "lesson_description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @Size(max = 255, message = ErrorEnum.INVALID_VIDEO_URL)
    private String videoUrl;

    @Size(max = 255, message = ErrorEnum.INVALID_THUMBNAIL_URL)
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToMany
    @JoinTable(
            name = "Lesson_Vocabulary",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private java.util.List<Vocabulary> vocabularies;

    @ManyToMany
    @JoinTable(
            name = "Lesson_Grammar",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id")
    )
    private java.util.List<Grammar> grammars;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<LessonExercise> lessonExercises;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgressLesson> progressLessons;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}
