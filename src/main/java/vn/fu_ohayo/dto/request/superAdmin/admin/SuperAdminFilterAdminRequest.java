package vn.fu_ohayo.dto.request.superAdmin.admin;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.dto.request.PagingRequest;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuperAdminFilterAdminRequest extends PagingRequest {
    String username;
    Set<RoleEnum> roles;
}
