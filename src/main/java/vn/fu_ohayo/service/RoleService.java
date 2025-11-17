package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.Role;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Set<Role> getRoles(List<RoleEnum> roleNames);
}
