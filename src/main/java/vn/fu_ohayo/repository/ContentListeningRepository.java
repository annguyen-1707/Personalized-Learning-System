package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.List;
@Repository
public interface ContentListeningRepository extends JpaRepository<ContentListening, Long> {
    ContentListening findByContent(Content content);
    ContentListening findBycontentListeningId(long id);
    Page<ContentListening> findAllByDeleted(Pageable pageable, boolean deleted);
    List<ContentListening> findAllByJlptLevel(JlptLevel jlptLevel);
    Page<ContentListening> findAllByStatusAndDeleted(ContentStatus status, boolean deleted, Pageable pageable);

}
