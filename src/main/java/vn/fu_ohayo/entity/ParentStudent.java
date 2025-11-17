package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ParentCodeStatus;
import vn.fu_ohayo.enums.Relationship;

@Entity
@Table(name = "Parent_Students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ParentStudent {
    @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "user_id")
    private User parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParentCodeStatus parentCodeStatus;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
    }

}
