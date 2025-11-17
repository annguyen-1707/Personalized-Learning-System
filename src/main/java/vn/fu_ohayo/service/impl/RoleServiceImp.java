package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.Role;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.RoleEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.RoleRepository;
import vn.fu_ohayo.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    public Set<Role> getRoles(List<RoleEnum> roleNames) {
        Set<Role> roles = new HashSet<>(roleRepository.findAllByNameIn(roleNames));
        if (roles.size() != roleNames.size()) {
            throw new AppException(ErrorEnum.ROLE_NOT_FOUND);
        }
        return roles;
    }

}
