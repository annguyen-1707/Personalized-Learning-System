package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.MembershipLevel;

import java.util.List;


@Repository
public interface MemberShipLevelRepository extends JpaRepository<MembershipLevel, Long> {
    MembershipLevel findByPrice(Long price);
    List<MembershipLevel> findByIdNotIn(List<Long> ids);
}
