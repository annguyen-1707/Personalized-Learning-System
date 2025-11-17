package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.UserResponseQuestion;

@Repository
public interface UserResponseQuestionRepository extends JpaRepository<UserResponseQuestion, Integer> {
    boolean existsByQuestion_ExerciseQuestionId(int questionExerciseQuestionId);

    UserResponseQuestion findByQuestion_ExerciseQuestionId(int exerciseQuestionId);
}
