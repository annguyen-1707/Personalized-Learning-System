package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.entity.Vocabulary;

@Repository
public interface QuizRepository extends JpaRepository<QuizQuestion, Integer>
{
    boolean existsByVocabulary(Vocabulary vocabulary);

}
