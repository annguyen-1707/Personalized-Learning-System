package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.entity.ProgressDialogue;
import vn.fu_ohayo.entity.ProgressGrammar;
import vn.fu_ohayo.entity.User;
@Repository
public interface ProgressDialogueRepository extends JpaRepository<ProgressDialogue, Integer> {
    ProgressDialogue findByUserAndDialogue(User user, Dialogue dialogue);
}
