package vn.fu_ohayo.entity;

import jakarta.persistence.*;
        import lombok.*;
        import vn.fu_ohayo.enums.FlashcardEnum;

import java.util.Date;

@Entity
@Table(name = "favorite_list_grammar")
@IdClass(FavoriteListGrammarId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteListGrammar {

    @Id
    @Column(name = "favorite_list_id")
    private int favoriteListId;

    @Id
    @Column(name = "grammar_id")
    private int grammarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_list_id", insertable = false, updatable = false)
    private FavoriteList favoriteList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grammar_id", insertable = false, updatable = false)
    private Grammar grammar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FlashcardEnum status;

    @Column(name = "last_reviewed_at")
    private Date lastReviewedAt;
}
