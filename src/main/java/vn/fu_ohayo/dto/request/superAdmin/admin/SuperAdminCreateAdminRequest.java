package vn.fu_ohayo.dto.request.superAdmin.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuperAdminCreateAdminRequest {

    @Size(min = 4, max = 50, message = ErrorEnum.INVALID_NAME)
    String username;

    @Size(min = 8, max = 36, message = ErrorEnum.INVALID_PASSWORD)
    String password;

    @NotEmpty(message = ErrorEnum.INVALID_ROLE)
    List<RoleEnum> roles;
}
