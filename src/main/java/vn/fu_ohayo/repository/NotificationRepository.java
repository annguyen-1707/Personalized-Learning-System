package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fu_ohayo.entity.Notification;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(long userId);
    @Query(value = "SELECT COUNT(*) FROM notifications " +
            "WHERE user_send_id = :userId " +
            "AND type = 'PAYMENT' " +
            "AND DATE(created_at) = CURDATE()",
            nativeQuery = true)
    long countTodayPaymentRequests(@Param("userId") Long userId);
}
