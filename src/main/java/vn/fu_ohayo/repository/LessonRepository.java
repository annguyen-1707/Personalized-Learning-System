package vn.fu_ohayo.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.LessonStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    boolean existsByName(String name);

    Page<Lesson> findAllBySubject(Subject subject, Pageable pageable);

    Optional<Lesson> getLessonByLessonId(int lessonId);

    boolean existsByNameAndLessonIdNot(String name, int lessonId);

    int countAllBySubject_SubjectIdAndStatus(int subjectId, LessonStatus status);

    int countAllBySubject_SubjectId(int subjectSubjectId);

    List<Lesson> findAllBySubjectAndStatus(Subject subject, LessonStatus status);

    int countAllByStatus(LessonStatus status);

    int countAllByStatusAndCreatedAtBefore(LessonStatus status, Date createdAt);

    Page<Lesson> findAllBySubjectAndDeletedAndStatus(Subject subject, boolean deleted, LessonStatus status, Pageable pageable);

    @Query(
            "SELECT COUNT(v) > 0 FROM Vocabulary v JOIN v.lessons l " +
            "WHERE v.vocabularyId = :vocabularyId AND l.lessonId = :lessonId"
    )
    boolean existsVocabularyInLesson(int vocabularyId, int lessonId);
}
