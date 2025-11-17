package vn.fu_ohayo.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    boolean existsBySubjectCode(String subjectCode);

    boolean existsBySubjectName(String subjectName);

    boolean existsBySubjectCodeAndSubjectIdNot(String subjectCode, int subjectId);

    boolean existsBySubjectNameAndSubjectIdNot(String subjectName, int subjectId);

    int countAllByStatusAndCreatedAtBefore(SubjectStatus status, Date createdAt);

    int countAllByStatus(SubjectStatus status);

    @Query("""
    SELECT s FROM Subject s
    LEFT JOIN ProgressSubject ps ON s = ps.subject AND ps.user.email = :email
    WHERE ps IS NULL
    AND s.status = :status
""")
    Page<Subject> findAllByStatusAndProgressSubjectsIsEmpty(SubjectStatus status,String email ,Pageable pageable);


    Page<Subject> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);

    List<Subject> findAllBySubjectIdIn(List<Integer> subjectIds);

    List<Subject> findAllByStatus(SubjectStatus status);

    Page<Subject> findAllByStatusAndIsDeleted(SubjectStatus status, Boolean isDeleted, Pageable pageable);
}
