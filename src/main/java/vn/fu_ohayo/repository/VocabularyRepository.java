package vn.fu_ohayo.repository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;

import java.util.List;


@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {

    @Query("SELECT v FROM Vocabulary v JOIN v.lessons l WHERE l.lessonId = :lessonId")
    Page<Vocabulary> findAllByLessonId(@Param("lessonId") int lessonId, Pageable pageable);

    @Query("SELECT v FROM Vocabulary v JOIN v.lessons l WHERE l.lessonId = :lessonId")
    List<Vocabulary> findAllByLessonId(@Param("lessonId") int lessonId);

    @Query("SELECT COUNT(v) > 0 FROM Vocabulary v JOIN v.lessons l " +
            "WHERE v.kanji = :kanji AND v.kana = :kana AND v.meaning = :meaning AND l.lessonId = :lessonId")
    boolean existsByKanjiAndKanaAndMeaningAndLessonId(
            @Param("kanji") String kanji,
            @Param("kana") String kana,
            @Param("meaning") String meaning,
            @Param("lessonId") int lessonId
    );

    @Query("SELECT COUNT(v) > 0 FROM Vocabulary v " +
            "WHERE v.kanji = :kanji AND v.meaning = :meaning " +
            "AND v.vocabularyId <> :vocabularyId")
    boolean existsDuplicateVocabularyExceptId(
            @Param("kanji") String kanji,
            @Param("meaning") String meaning,
            @Param("vocabularyId") int vocabularyId
    );

    @Query("SELECT v FROM Vocabulary v JOIN v.favoriteVocabularies fv WHERE fv.id = :folderId")
    List<Vocabulary> findAllByFavoriteVocabularyId(@Param("folderId") int folderId);

    @Query("SELECT COUNT(v) FROM Vocabulary v JOIN v.favoriteVocabularies fv WHERE fv.id = :folderId")
    int countByFavoriteVocabularyId(@Param("folderId") int folderId);


    Vocabulary findAllByKanjiAndKanaAndMeaning( String kanji, String kana,String meaning);

    Page<Vocabulary> findAllByDeleted(Boolean deleted, Pageable pageable);

    @Query("SELECT v FROM Vocabulary v WHERE v.deleted = false AND v.vocabularyId NOT IN " +
            "(SELECT voc.vocabularyId  FROM  Lesson l  JOIN l.vocabularies voc WHERE l.lessonId = :lessonId)")
    Page<Vocabulary> findAllNotInLesson(int lessonId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM lesson_vocabulary where vocabulary_id = :id AND lesson_id = :lessonId", nativeQuery = true
    )
    void removeVocabInLessonId(int id, int lessonId);


}