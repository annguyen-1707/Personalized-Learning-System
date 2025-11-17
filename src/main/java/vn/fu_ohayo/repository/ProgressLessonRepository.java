package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.ProgressLesson;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProgressLessonRepository extends JpaRepository<ProgressLesson, Integer> {
    ProgressLesson findByUserAndLesson(User user, Lesson lesson);

    List<ProgressLesson> findAllByUserAndLessonIn(User user, List<Lesson> lessons);

    int countAllByUserAndLesson_Subject_SubjectIdAndStatus(User user, int lessonSubjectSubjectId, ProgressStatus status);
}
