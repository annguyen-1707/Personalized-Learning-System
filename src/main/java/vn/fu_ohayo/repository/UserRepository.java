package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);


    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmailIncludingDeleted(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    User getByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, Provider provider);
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Optional<User> findByIdIncludingDeleted(@Param("userId") Long userId);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE " +
            "(:fullName IS NULL OR u.fullName LIKE %:fullName%) AND " +
            "(:membershipLevel IS NULL OR u.membershipLevel = :membershipLevel) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:registeredFrom IS NULL OR u.createdAt >= :registeredFrom) AND " +
            "(:registeredTo IS NULL OR u.createdAt <= :registeredTo)")
    Page<User> filterUsers(
            @Param("fullName") String fullName,
            @Param("membershipLevel") MembershipLevel membershipLevel,
            @Param("status") UserStatus status,
            @Param("registeredFrom") Date registeredFrom,
            @Param("registeredTo") Date registeredTo,
            Pageable pageable
    );


    int countAllByStatus(UserStatus status);

    int countAllByStatusAndCreatedAtBefore(UserStatus status, Date createdAt);

    int countAllByStatusAndMembershipLevel(UserStatus UserStatus, MembershipLevel membershipLevel);
    boolean existsByEmailAndStatus(String email, UserStatus status);

    @Query(value = """
        SELECT m.month AS monthNumber,
               COUNT(u.user_id) AS activeCount
        FROM (
            SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
            UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8
            UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
        ) m
        LEFT JOIN users u ON MONTH(u.created_at) = m.month
                         AND YEAR(u.created_at) = :year
                         AND u.status = 'ACTIVE'
        GROUP BY m.month
        ORDER BY m.month
        """, nativeQuery = true)
    List<Object[]> countActiveUsersEachMonth(@Param("year") int year);

//    @Query(
//            "SELECT u FROM User u JOIN u.subjects s WHERE s.subjectId = :courseId  AND u.userId = :userId"
//    )
//    User findBySubjectId(int courseId, int userId);
}
