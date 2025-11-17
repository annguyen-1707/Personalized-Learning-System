package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Page<Feedback> findByRating(int rating, Pageable pageable);
    @Query("SELECT COUNT(f) FROM Feedback f " +
            "WHERE f.user.userId = :userId " +
            "AND DATE(f.createdAt) = CURRENT_DATE")
    int countTodayFeedbacks(@Param("userId") Long userId);
}
