package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.FavoriteListGrammar;
import vn.fu_ohayo.entity.FavoriteListGrammarId;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.enums.FlashcardEnum;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteListGrammarRepository
        extends JpaRepository<FavoriteListGrammar, FavoriteListGrammarId> {
    @Query("SELECT g.grammarId FROM FavoriteListGrammar g WHERE g.favoriteListId = :listId AND g.status = :status")
    List<Integer> findGrammarIdsByFavoriteListIdAndStatus(@Param("listId") long listId,
                                                          @Param("status") FlashcardEnum status);
    @Query("SELECT flg FROM FavoriteListGrammar flg JOIN FETCH flg.grammar g WHERE flg.favoriteList.favoriteId = :favoriteListId AND g.deleted = FALSE")
    List<FavoriteListGrammar> findByFavoriteListIdWithGrammar(@Param("favoriteListId") long favoriteListId);
    long countByFavoriteListId(int favoriteListId);
    long countByFavoriteListIdAndStatus(int favoriteListId, FlashcardEnum status);
    Optional<FavoriteListGrammar>findByFavoriteListIdAndGrammarId(long listId, int gramId);
    @Query("SELECT fg.grammar FROM FavoriteListGrammar fg " +
            "WHERE fg.favoriteList.favoriteId = :folderId " +
            "AND (:kw IS NULL OR (" +
            "   LOWER(fg.grammar.titleJp) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(fg.grammar.meaning) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(fg.grammar.structure) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(fg.grammar.example) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(fg.grammar.usage) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            ")) " +
            "AND (:jlptLevel IS NULL OR fg.grammar.jlptLevel = :jlptLevel) " +
            "AND fg.grammar.deleted = false")
    Page<Grammar> searchGrammarsByFolderId(
            @Param("folderId") Long folderId,
            @Param("kw") String keyword,
            @Param("jlptLevel") JlptLevel jlptLevel,
            Pageable pageable
    );

    @Query("SELECT g FROM Grammar g " +
            "WHERE g.deleted = false " +
            "AND (:kw IS NULL OR (" +
            "   LOWER(g.titleJp) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(g.meaning) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(g.structure) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(g.example) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(g.usage) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            ")) " +
            "AND (:jlptLevel IS NULL OR g.jlptLevel = :jlptLevel) " +
            "AND g.grammarId NOT IN (" +
            "   SELECT fg.grammar.grammarId FROM FavoriteListGrammar fg WHERE fg.favoriteList.favoriteId = :folderId" +
            ")")
    Page<Grammar> searchGrammarsNotInFolder(
            @Param("folderId") Long folderId,
            @Param("kw") String keyword,
            @Param("jlptLevel") JlptLevel jlptLevel,
            Pageable pageable
    );
    Optional<FavoriteListGrammar> findByFavoriteListIdAndGrammarId(int favoriteListId, int grammarId);
}
