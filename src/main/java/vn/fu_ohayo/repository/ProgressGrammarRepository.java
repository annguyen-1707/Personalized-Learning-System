package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressGrammarRepository extends JpaRepository<ProgressGrammar, Integer> {

    List<ProgressGrammar> findAllByUserAndProgressStatusAndGrammarIn(User user, ProgressStatus progressStatus, List<Grammar> grammars);
    List<ProgressGrammar> findAllByUserAndGrammarIn(User user, List<Grammar> grammars);
    int countByUserAndProgressStatusAndGrammarIn(User user, ProgressStatus progressStatus, List<Grammar> grammars);
    @Query(value = "SELECT pv FROM ProgressGrammar pv " +
            "WHERE pv.user = :user " +
            "AND pv.grammar IN :grammars " +
            "ORDER BY pv.reviewedAt DESC " +
            "FETCH FIRST :size ROWS ONLY", nativeQuery = false)
    List<ProgressGrammar> findAllByUserAndTopOnReviewAndGrammarIn(User user, List<Grammar> grammars, int size);

    ProgressGrammar findAllByGrammar_GrammarIdAndUser(int grammarId, User user);
}
