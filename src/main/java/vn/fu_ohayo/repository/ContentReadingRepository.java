package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.enums.ContentStatus;

@Repository
public interface ContentReadingRepository extends JpaRepository<ContentReading, Long> {
    ContentReading findByContent(Content content);
    Page<ContentReading> findAllByDeleted(Pageable pageable, boolean deleted);
    Page<ContentReading> findAllByStatusAndDeleted(ContentStatus status,Boolean deteled , Pageable pageable);
}
