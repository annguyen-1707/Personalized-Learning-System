package vn.fu_ohayo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Content_Listenings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Where(clause = "is_deleted = false")
public class ContentListening {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_listening_id")
    private long contentListeningId;

    //practiceID
//    @OneToMany(mappedBy = "Exercise")
//    private Exercise exercise;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    //khi lưu 1 đối tượng ContentSpeaking , nếy chưa có content thì tự động lưu đối tượng content rồi mới lưu ContentSpeaking
    // khi xóa ContentSpeaking thì tự động xóa content
    @JoinColumn(name = "content_id")
    private Content content;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Column(name = "audio_file")
    private String audioFile;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @Column(name = "script_jp", columnDefinition = "TEXT")
    private String scriptJp;

    @Column(name = "script_vn", columnDefinition = "TEXT")
    private String scriptVn;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    @Enumerated(EnumType.STRING)
    private CategoryListeningEnum category;

    @OneToMany(mappedBy = "contentListening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExerciseQuestion> exerciseQuestions;

    @NotNull(message = ErrorEnum.NOT_EMPTY_JLPT_LEVEL)
    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level")
    private JlptLevel jlptLevel;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private ContentStatus status = ContentStatus.DRAFT;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Override
    public String toString() {
        return "ContentListening{" +
                ", title='" + title + '\'' +
                '}';
    }
}
