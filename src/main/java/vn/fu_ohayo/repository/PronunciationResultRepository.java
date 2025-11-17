package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ProgressDialogue;
import vn.fu_ohayo.entity.PronunciationResult;

@Repository
public interface PronunciationResultRepository extends JpaRepository<PronunciationResult, Integer> {

}
