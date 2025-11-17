package vn.fu_ohayo.dto.request.admin.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import vn.fu_ohayo.enums.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdminCreateUserRequest {

    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    String fullName;

    @Email(message = ErrorEnum.INVALID_EMAIL_MS)
    String email;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PASSWORD)
    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    String password;

    @Pattern(regexp = "^0\\d{9,10}$", message = ErrorEnum.INVALID_PHONE)
    String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    String address;

    Gender gender;

    MembershipLevel membershipLevel;

    UserStatus status = UserStatus.ACTIVE;

    Provider provider = Provider.LOCAL;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dob;

    int roleId = 5;
}
