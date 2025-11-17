package vn.fu_ohayo.dto.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.fu_ohayo.entity.Role;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AdminDTO {
    Long adminId;
    String username;
    Set<Role> roles;
    @JsonProperty("role") // ✅ Sẽ xuất hiện trong JSON dưới tên "roleEnums"
    public List<RoleEnum> getRoleEnums() {
        if (roles == null) return Collections.emptyList();
        return roles.stream()
                .map(Role::getName) // getName() trả về RoleEnum
                .collect(Collectors.toList());
    }

}
