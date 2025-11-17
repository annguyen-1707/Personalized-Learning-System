package vn.fu_ohayo.repository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    @Query(
            value = "SELECT * from grammars where title_jp = :titleJp", nativeQuery = true
    )
    Grammar findByTitleJp(String titleJp);

    boolean existsByTitleJpAndMeaningAndGrammarIdNot(String titleJp, String meaning, int grammarId);

    @Query("SELECT g FROM Grammar g JOIN g.lessons l " +
            "WHERE l.lessonId = :lessonId AND g.deleted = false")
    Page<Grammar> findAllByLessonIdAndDeletedIsFalse(@Param("lessonId") int lessonId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM lesson_grammar where grammar_id = :id AND lesson_id = :lessonId", nativeQuery = true
    )
    void removeGrammarInLessonId(int id, int lessonId);

    @Query("SELECT g FROM Grammar g WHERE g.deleted = false AND g.grammarId NOT IN " +
            "(SELECT gl.grammarId  FROM  Lesson l  JOIN l.grammars gl WHERE l.lessonId = :lessonId)")
    Page<Grammar> findAllNotInLesson(int lessonId, Pageable pageable);


    @Transactional
    @Modifying
    @Query(
            value= "INSERT INTO lesson_grammar (lesson_id, grammar_id) VALUES (:lessonId, :grammarId)", nativeQuery = true
    )
    void saveGrammarIntoLesson(int lessonId, int grammarId);

    @Query("SELECT g FROM Grammar g JOIN g.lessons l WHERE l.lessonId = :lessonId")
    List<Grammar> findAllByLessonId(int lessonId);
}
