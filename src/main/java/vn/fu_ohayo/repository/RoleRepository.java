package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.Role;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleEnum role);

    List<Role> findAllByNameIn(List<RoleEnum> codes);
}
