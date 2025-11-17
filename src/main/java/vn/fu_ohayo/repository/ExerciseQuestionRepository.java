package vn.fu_ohayo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.entity.LessonExercise;

import java.util.List;

@Repository
public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Integer> {
    List<ExerciseQuestion> findAllByContentListening(ContentListening contentListening);
    List<ExerciseQuestion> findAllByLessonExercise(LessonExercise lessonExercise);
    Page<ExerciseQuestion> findAllByLessonExercise(LessonExercise lessonExercise, Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM ExerciseQuestion eq WHERE eq.lessonExercise.exerciseId = :exerciseId")
    void deleteByExerciseId(@Param("exerciseId") int exerciseId);
    List<ExerciseQuestion> findByContentListening(ContentListening contentListening);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO lesson_exercises (exercise_id, lesson_id) " +
                    "VALUES (:exerciseId, :lessonId) " +
                    "ON DUPLICATE KEY UPDATE lesson_id = VALUES(lesson_id)", // Ghi đè nếu trùng exerciseId
            nativeQuery = true
    )
    void saveExerciseQuestionIntoLessonId(
            @Param("exerciseId") Long exerciseId,
            @Param("lessonId") Long lessonId
    );

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM lesson_exercises WHERE exercise_id = :exerciseId AND lesson_id = :lessonId",
            nativeQuery = true
    )
    void removeExerciseQuestionInLessonId(
            @Param("exerciseId") Long exerciseId,
            @Param("lessonId") Long lessonId
    );

    @Query("SELECT eq FROM ExerciseQuestion eq WHERE eq.exerciseQuestionId NOT IN"
    + " (SELECT eq2.exerciseId FROM Lesson le JOIN le.lessonExercises eq2 WHERE le.lessonId = :lessonId)")
    Page<ExerciseQuestion> findAllAvailableExerciseQuestions(Long lessonId, Pageable pageable);

    Page<ExerciseQuestion> findAllByType(Pageable pageable, String type);

    Page<ExerciseQuestion> findAllByTypeAndLessonExerciseIsNullAndContentListeningIsNull(Pageable pageable, String type);
}
