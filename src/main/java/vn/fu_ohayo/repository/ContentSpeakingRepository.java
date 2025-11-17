package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.List;

@Repository
public interface ContentSpeakingRepository extends JpaRepository<ContentSpeaking,Long> {

    ContentSpeaking findByContent(Content content);
    Page<ContentSpeaking> findAllByDeleted(Pageable pageable, boolean deleted);
    List<ContentSpeaking> findAllByJlptLevel(JlptLevel jlptLevel);
    Page<ContentSpeaking> findAllByDeletedAndStatus(Pageable pageable, boolean deleted, ContentStatus status);

}
