package vn.fu_ohayo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.enums.ParentCodeStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ParentStudentRepository extends JpaRepository<ParentStudent, Integer>
{
    ParentStudent findByVerificationCode(String code);
    boolean existsByVerificationCode(String verificationCode);
    ParentStudent findByVerificationCodeAndStudentEmail(String verificationCode, String email);
    List<ParentStudent> findByParentEmail(String code);
    ParentStudent findByParentEmailAndStudentEmailAndParentCodeStatus(String parentMail, String studentMail, ParentCodeStatus parentCodeStatus);
    List<ParentStudent> findByStudentEmail(String code);
    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM parent_students WHERE DATE(created_at) < CURDATE() AND student_id IS NULL",
            nativeQuery = true
    )
    void deleteAllExpiredUnlinkedCodes();

    @Query("SELECT COUNT(ps) FROM ParentStudent ps " +
            "WHERE ps.parent.email = :parentEmail " +
            "AND ps.createdAt >= CURRENT_DATE")
    long countTodayCodesByParent(@Param("parentEmail") String parentEmail);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO parent_students (parent_id, student_id, status, verification_code, created_at) " +
                    "VALUES (:parentId, NULL, NULL, :code, NOW())",
            nativeQuery = true
    )
    void insertParentStudent(
            @Param("parentId") Long parentId,
            @Param("code") String code
    );

    @Query("SELECT ps FROM ParentStudent ps " +
            "JOIN FETCH ps.student s " +
            "WHERE ps.parent.userId = :parentId " +
            "AND ps.parentCodeStatus = 'CONFIRM' " +
            "AND s.isDeleted = false")
    List<ParentStudent> findValidChildrenByParentId(@Param("parentId") Long parentId);
}
