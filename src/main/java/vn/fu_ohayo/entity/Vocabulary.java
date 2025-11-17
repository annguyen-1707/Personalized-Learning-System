package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Where;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Vocabularies",
        indexes = {
                @Index(columnList = "kanji", name = "kanji_index"),
                @Index(columnList = "jlpt_level", name = "jlpt_level_index")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Where(clause = "is_deleted = false AND is_system = true")
@ToString(onlyExplicitlyIncluded = true)
public class Vocabulary {

    @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    @Column(name = "vocabulary_id")
    @EqualsAndHashCode.Include
    private int vocabularyId;


    @NotEmpty(message = ErrorEnum.NOT_EMPTY_KANJI)
    @Size(max = 50, message = ErrorEnum.MAX_LENGTH_50)
    private String kanji;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_KANA)
    @Size(max = 50, message = ErrorEnum.MAX_LENGTH_50)
    private String kana; // e.g., "こうこう" for 高校

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_ROMAJI)
    @Size(max = 50, message = ErrorEnum.MAX_LENGTH_50)
    private String romaji; // e.g., "kōkō" for 高校

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_MEANING)
    @Size(max = 100, message =  ErrorEnum.MAX_LENGTH_100)
    private String meaning;


    @Size(max = 500, message =  ErrorEnum.MAX_LENGTH_500)
    private String description;


    @Size(max = 500, message =  ErrorEnum.MAX_LENGTH_500)
    private String example;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PART_OF_SPEECH)
    @Enumerated(EnumType.STRING)
    @Column(name = "part_of_speech")
    private PartOfSpeech partOfSpeech;

    @Column(name = "is_system")
    private Boolean isSystem = true;

    @NotNull(message = ErrorEnum.NOT_EMPTY_JLPT_LEVEL)
    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level")
    private JlptLevel jlptLevel;

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FavoriteListVocabulary> favoriteListVocabularies = new HashSet<>();

    @Column(name = "created_at")
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    @ManyToMany(mappedBy = "vocabularies", fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();

    @ManyToMany(mappedBy = "vocabularies", fetch = FetchType.LAZY)
    private List<ContentReading> contentReadings;

    @ManyToMany(mappedBy = "vocabularies", fetch = FetchType.LAZY)
    private List<FavoriteVocabulary> favoriteVocabularies ;


    @OneToOne(mappedBy = "vocabulary", cascade = CascadeType.ALL)
    private QuizQuestion quizQuestion;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
        updatedAt = new java.util.Date();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new java.util.Date();
    }

}
