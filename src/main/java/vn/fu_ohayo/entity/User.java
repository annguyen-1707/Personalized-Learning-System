package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.enums.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
                @UniqueConstraint(columnNames = "email")
        },

        indexes = {
                @Index(columnList = "email",
                        name = "email_index"),
                @Index(columnList = "phone", unique = true,
                        name = "phone_index"),
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Where(clause = "is_deleted = false")
@SuppressWarnings("deprecation")
public class User implements UserDetails, Serializable {
    @Id @GeneratedValue(
             strategy = GenerationType.IDENTITY
    )
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Email
    @NotNull(message = ErrorEnum.NOT_EMPTY_EMAIL)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ErrorEnum.INVALID_EMAIL_MS)
    private String email;

    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;


    @Column(name = "full_name")
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    private String fullName;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0\\d{9,10}$", message = ErrorEnum.INVALID_PHONE)
    @Column(unique = true)
    private String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    private String address;

    private Date dob;

    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.INACTIVE;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MembershipLevelOfUser membershipInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private MembershipLevel membershipLevel = MembershipLevel.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider = Provider.LOCAL;

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @OneToMany(mappedBy = "parent")
    private List<ParentStudent> children;

    @OneToMany(mappedBy = "student")
    private List<ParentStudent> parents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserResponseQuestion> userResponseQuestions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FavoriteList> favoriteLists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProgressGrammar> progressGrammars;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProgressVocabulary> progressVocabularies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProgressSubject> progressSubjects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExerciseResult> exerciseResults;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProgressContent> progressContents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgressLesson> progressLessons;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StudyReminder> studyReminders;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_vocabulary",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_vocabulary_id")
    )
    private Set<FavoriteVocabulary> favoriteVocabularies;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.role);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}
