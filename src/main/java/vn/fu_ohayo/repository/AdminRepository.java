package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @EntityGraph(attributePaths = "roles")

    boolean existsByUsername(String username);
    Optional<Admin> findByUsername(String username);

    @Query("SELECT DISTINCT a FROM Admin a " +
            "LEFT JOIN a.roles r " +
            "WHERE a.isDeleted = false " +
            "AND (:username IS NULL OR a.username LIKE %:username%) " +
            "AND (:roles IS NULL OR r.name IN :roles)")
    Page<Admin> filterAdmins(
            @Param("username") String username,
            @Param("roles") Set<RoleEnum> roles,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM admins WHERE username = :username", nativeQuery = true)
    Optional<Admin> findByUsernameIncludingDeleted(@Param("username") String username);

    @Query(value = "SELECT * FROM admins WHERE admin_id = :adminId", nativeQuery = true)
    Optional<Admin> findByIdIncludingDeleted(@Param("adminId") Long adminId);
}
