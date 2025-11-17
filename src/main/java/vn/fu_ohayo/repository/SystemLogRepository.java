package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.SystemLog;
import vn.fu_ohayo.enums.RoleEnum;

import java.time.LocalDateTime;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    @Query("SELECT s FROM SystemLog s WHERE " +
            "(:start IS NULL OR s.timestamp >= :start) AND " +
            "(:end IS NULL OR s.timestamp <= :end) AND " +
            "(:action IS NULL OR LOWER(s.action) LIKE LOWER(CONCAT('%', :action, '%'))) AND " +
            "(:details IS NULL OR LOWER(s.details) LIKE LOWER(CONCAT('%', :details, '%'))) AND " +
            "(:role IS NULL OR s.role = :role)")
    Page<SystemLog> filterSystemLogs(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("action") String action,
            @Param("details") String details,
            @Param("role") RoleEnum role,
            Pageable pageable
    );
}
