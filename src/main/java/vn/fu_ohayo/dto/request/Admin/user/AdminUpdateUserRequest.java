package vn.fu_ohayo.dto.request.admin.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdminUpdateUserRequest {

    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @Email(message = ErrorEnum.INVALID_EMAIL_MS)
    String email;

    UserStatus status;

    MembershipLevel membershipLevel;

}
