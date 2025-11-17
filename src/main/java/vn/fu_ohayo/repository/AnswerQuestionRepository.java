package vn.fu_ohayo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.dto.request.AnswerQuestionRequest;
import vn.fu_ohayo.dto.response.AnswerQuestionResponse;
import vn.fu_ohayo.entity.AnswerQuestion;
import vn.fu_ohayo.entity.ExerciseQuestion;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion, Integer> {
    List<AnswerQuestion> findByExerciseQuestion(ExerciseQuestion exerciseQuestion);

    @Modifying
    @Transactional
    @Query("DELETE FROM AnswerQuestion a WHERE a.exerciseQuestion.lessonExercise.exerciseId = :exerciseId")
    void deleteAllByExerciseId(@Param("exerciseId") int exerciseId);
    @Query(
            "SELECT new vn.fu_ohayo.dto.request.AnswerQuestionRequest(a.answerId,a.answerText, a.isCorrect)" +
                    "FROM AnswerQuestion a " +
                    "WHERE a.exerciseQuestion = :exerciseQuestion"
    )
    List<AnswerQuestionRequest> findAllByExerciseQuestion(ExerciseQuestion exerciseQuestion);

    @Query(
            "SELECT new vn.fu_ohayo.dto.request.AnswerQuestionRequest(a.answerId, a.answerText, a.isCorrect)"
            + "FROM AnswerQuestion a "
            + "WHERE a.exerciseQuestion = :id"
    )
    List<AnswerQuestionRequest> findAllByExerciseQuestion_ExerciseQuestionId(int id);

    List<AnswerQuestion> findByExerciseQuestion_ExerciseQuestionIdAndIsCorrect(Integer exerciseQuestionId, Boolean isCorrect);

    List<AnswerQuestion> findByExerciseQuestion_ExerciseQuestionId(Integer exerciseQuestionId);
}
