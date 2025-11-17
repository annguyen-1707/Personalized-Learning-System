package vn.fu_ohayo.repository;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.List;
import java.util.Optional;


public interface ProgressContentRepository extends JpaRepository<ProgressContent, Long> {
    Optional<ProgressContent> findByUser_UserIdAndContent_ContentId(Long userId, Long contentId);
    Optional<ProgressContent> findByUserAndContent(User user, Content content);
//    Optional<ProgressContent> findByUser_UserAndContent_Content(Long userId, Long contentId);
    List<ProgressContent> findAllByUserAndProgressStatus(User user, ProgressStatus progressStatus);

    User user(User user);
}
