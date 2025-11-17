package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ProgressVocabulary;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressVocabularyRepository extends JpaRepository<ProgressVocabulary, Integer> {
    List<ProgressVocabulary> findAllByUserAndProgressStatusAndVocabularyIn(User user, ProgressStatus progressStatus, List<Vocabulary> vocabularies);
    List<ProgressVocabulary> findAllByUserAndVocabularyIn(User user, List<Vocabulary> vocabularies);
    ProgressVocabulary findAllByUserAndProgressStatusAndVocabulary(User user, ProgressStatus progressStatus, Vocabulary vocabulary);
    int countByUserAndProgressStatusAndVocabularyIn(User user, ProgressStatus progressStatus, List<Vocabulary> vocabularies);
    @Query(value = "SELECT pv FROM ProgressVocabulary pv " +
            "WHERE pv.user = :user " +
            "AND pv.vocabulary IN :vocabularies " +
            "ORDER BY pv.reviewedAt DESC " +
            "FETCH FIRST :size ROWS ONLY", nativeQuery = false)
    List<ProgressVocabulary> findAllByUserAndTopOnReviewAndVocabularyIn(User user,List<Vocabulary> vocabularies, int size);

    ProgressVocabulary findByUserAndVocabulary(User user, Vocabulary vocabulary);
}
