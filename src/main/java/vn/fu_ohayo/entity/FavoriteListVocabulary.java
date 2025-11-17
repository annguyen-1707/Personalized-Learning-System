package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.fu_ohayo.enums.FlashcardEnum;

import java.util.Date;

@Entity
@Table(name = "favorite_list_vocabulary")
@IdClass(FavoriteListVocabularyId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteListVocabulary {

    @Id
    @Column(name = "favorite_list_id")
    private int favoriteListId;

    @Id
    @Column(name = "vocabulary_id")
    private int vocabularyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_list_id", insertable = false, updatable = false)
    private FavoriteList favoriteList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", insertable = false, updatable = false)
    private Vocabulary vocabulary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FlashcardEnum status;

    @Column(name = "last_reviewed_at")
    private Date lastReviewedAt;
}