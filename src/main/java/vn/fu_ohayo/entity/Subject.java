package vn.fu_ohayo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.SubjectStatus;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "Subjects",
uniqueConstraints = @UniqueConstraint(columnNames = "subject_code"),
indexes = {
    @Index(name = "idx_subject_id", columnList = "subject_id"),
    @Index(name = "idx_subject_name", columnList = "subject_name")
})

public class Subject {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectId;

    @Column(name = "subject_code", nullable = false, unique = true)
    @NotNull(message = ErrorEnum.NOT_EMPTY_SUBJECT_CODE)
    private String subjectCode;

    @Column(name = "subject_name", nullable = false)
    @NotNull(message = ErrorEnum.NOT_EMPTY_SUBJECT_NAME)
    @Size(max = 50, message = ErrorEnum.INVALID_SUBJECT_NAME)
    private String subjectName;

    private String description;

    @Size(max = 255, message = ErrorEnum.INVALID_THUMBNAIL_URL)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private SubjectStatus status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<Lesson> lessons;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgressSubject> progressSubjects;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

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
