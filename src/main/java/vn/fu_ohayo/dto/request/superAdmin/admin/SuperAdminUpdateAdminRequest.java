package vn.fu_ohayo.dto.request.superAdmin.admin;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SuperAdminUpdateAdminRequest {

    @Size(min = 8, max = 36, message = ErrorEnum.INVALID_PASSWORD)
    String password;

    List<RoleEnum> roles;
}
