package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fu_ohayo.entity.MembershipLevelOfUser;


public interface MemberShipLevelOfUserRepository extends JpaRepository<MembershipLevelOfUser, Long> {
    // Define any custom query methods if needed
    MembershipLevelOfUser findByUserUserId(Long userId);
    boolean existsByUserUserId(Long userId);
}
