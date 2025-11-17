package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Content_Readings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Where(clause = "is_deleted = false")
public class ContentReading {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_reading_id")
    private long contentReadingId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    //khi lưu 1 đối tượng ContentSpeaking , nếy chưa có content thì tự động lưu đối tượng content rồi mới lưu ContentSpeaking
    // khi xóa ContentSpeaking thì tự động xóa content
    @JoinColumn(name = "content_id")
    private Content content;

    @NotNull
    @PastOrPresent(message = "Time can not over today")
    private Date timeNew;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String audioFile;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @Column(name = "script_jp", columnDefinition = "TEXT")
    private String scriptJp;

    @Column(name = "script_vn", columnDefinition = "TEXT")
    private String scriptVn;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    @Enumerated(EnumType.STRING)
    private CategoryReadingEnum category;

    @ManyToMany
    @JoinTable(
            name = "Content_Reading_Vocabulary",
            joinColumns = @JoinColumn(name = "content_reading_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private List<Vocabulary> vocabularies;

    @ManyToMany
    @JoinTable(
            name = "Content_Reading_Grammar",
            joinColumns = @JoinColumn(name = "content_reading_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id")
    )
    private List<Grammar> grammars;

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

}
