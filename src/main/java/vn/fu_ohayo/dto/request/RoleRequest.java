package vn.fu_ohayo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    RoleEnum name;
    String description;
    Set<Integer> permissionIds; // IDs của các Permission
}
