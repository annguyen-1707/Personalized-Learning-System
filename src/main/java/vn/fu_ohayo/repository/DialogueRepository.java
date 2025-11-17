package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;

@Repository
public interface DialogueRepository extends JpaRepository<Dialogue, Long> {
    List<Dialogue> findByContentSpeaking(ContentSpeaking contentSpeaking);

    Page<Dialogue> findAllByContentSpeaking(ContentSpeaking contentSpeaking, Pageable pageable);

    void deleteDialogueByContentSpeaking(ContentSpeaking contentSpeaking);

    Page<Dialogue> findByContentSpeakingIsNull(Pageable pageable);
}
