package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.entity.ExerciseResult;
import java.util.List;

@Repository
public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Integer> {
    List<ExerciseResult> findAllByUserAndLessonExerciseIn(User user, List<LessonExercise> lessonExercises);
    @Query(
            value = "SELECT COUNT(DISTINCT exercise_id) " +
                    "FROM exercise_results " +
                    "WHERE user_id = :userId AND exercise_id IN (:lessonExercises)",
            nativeQuery = true)
     int countDistinctByUserAndLessonExerciseIn(long userId, List<Integer> lessonExercises);

    @Query(value = "SELECT er FROM ExerciseResult er " +
            "WHERE er.user = :user " +
            "AND er.lessonExercise IN :lessonExercises " +
            "ORDER BY er.submissionTime DESC " +
            "FETCH FIRST :size ROWS ONLY", nativeQuery = false)
    List<ExerciseResult> findAllByUserAndTopOnReviewAndExerciseIn(User user,List<LessonExercise> lessonExercises, int size);


    @Query(
            value = "SELECT COUNT(DISTINCT er.exercise_id) " +
                    "FROM exercise_results er " +
                    "JOIN lesson_exercises le ON er.exercise_id = le.exercise_id " +
                    "WHERE er.user_id = :userId AND le.lesson_id = :lessonId",
            nativeQuery = true)
    int countAllResultsByUserAndLessonExerciseGroupByLessonExerciseId(long userId, int lessonId);
}
