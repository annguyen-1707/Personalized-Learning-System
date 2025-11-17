package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.FavoriteListVocabulary;
import vn.fu_ohayo.entity.FavoriteListVocabularyId;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.FlashcardEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteListVocabularyRepository
        extends JpaRepository<FavoriteListVocabulary, FavoriteListVocabularyId> {
    @Query("SELECT v.vocabularyId FROM FavoriteListVocabulary v WHERE v.favoriteListId = :listId AND v.status = :status")
    List<Integer> findVocabularyIdsByFavoriteListIdAndStatus(@Param("listId") long listId,
                                                             @Param("status") FlashcardEnum status);
    @Query("SELECT flv FROM FavoriteListVocabulary flv JOIN FETCH flv.vocabulary v WHERE flv.favoriteList.favoriteId = :favoriteListId AND v.deleted = FALSE")
    List<FavoriteListVocabulary> findByFavoriteListIdWithVocabulary(@Param("favoriteListId") long favoriteListId);
    long countByFavoriteListId(int favoriteListId);
    long countByFavoriteListIdAndStatus(int favoriteListId, FlashcardEnum status);
    Optional<FavoriteListVocabulary> findByFavoriteListIdAndVocabularyId(long listId, int vocabId);
    @Query("SELECT fv.vocabulary FROM FavoriteListVocabulary fv " +
            "WHERE fv.favoriteListId = :folderId " +
            "AND (:kw IS NULL OR (" +
            "   LOWER(fv.vocabulary.kanji) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(fv.vocabulary.kana) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(fv.vocabulary.romaji) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(fv.vocabulary.meaning) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(fv.vocabulary.example) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(fv.vocabulary.description) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            ")) " +
            "AND (:category IS NULL OR fv.vocabulary.partOfSpeech = :category) " +
            "AND (:jlptLevel IS NULL OR fv.vocabulary.jlptLevel = :jlptLevel) " +
            "AND fv.vocabulary.deleted = false")
    Page<Vocabulary> searchVocabulariesByFolderId(
            @Param("folderId") Long folderId,
            @Param("kw") String keyword,
            @Param("category") PartOfSpeech category,
            @Param("jlptLevel") JlptLevel jlptLevel,
            Pageable pageable
    );

    @Query("SELECT v FROM Vocabulary v " +
            "WHERE v.deleted = false " +
            "AND v.vocabularyId NOT IN (" +
            "  SELECT fv.vocabulary.vocabularyId FROM FavoriteListVocabulary fv WHERE fv.favoriteListId = :folderId" +
            ") " +
            "AND (:kw IS NULL OR (" +
            "   LOWER(v.kanji) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(v.kana) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(v.romaji) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(v.meaning) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(v.example) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(v.description) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            ")) " +
            "AND (:category IS NULL OR v.partOfSpeech = :category) " +
            "AND (:jlptLevel IS NULL OR v.jlptLevel = :jlptLevel)")
    Page<Vocabulary> searchVocabulariesNotInFolder(
            @Param("folderId") Long folderId,
            @Param("kw") String keyword,
            @Param("category") PartOfSpeech category,
            @Param("jlptLevel") JlptLevel jlptLevel,
            Pageable pageable
    );
    Optional<FavoriteListVocabulary> findByFavoriteListIdAndVocabularyId(int favoriteListId, int vocabularyId);
}
