package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.LessonExercise;

import java.util.Optional;

@Repository
public interface LessonExerciseRepository extends JpaRepository<LessonExercise, Integer> {

    Page<LessonExercise> findAllByLesson(Lesson lesson, Pageable pageable);

    Optional<LessonExercise> findByLesson_LessonId(int lessonLessonId);

    int countAllByLesson_LessonId(int lessonLessonId);

    LessonExercise findByLesson_LessonIdAndExerciseId(int lessonLessonId, int exerciseId);
}
