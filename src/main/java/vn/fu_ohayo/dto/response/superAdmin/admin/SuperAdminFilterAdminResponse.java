package vn.fu_ohayo.dto.response.superAdmin.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.dto.response.RoleResponse;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperAdminFilterAdminResponse {
    Long adminId;
    String username;
    Set<RoleResponse> roles;
}
