package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.StudyReminder;
import vn.fu_ohayo.entity.User;

import java.util.List;

@Repository
public interface StudyReminderRepository extends JpaRepository<StudyReminder, Integer> {
     List<StudyReminder> findAllByUser(User user);
}
