package vn.fu_ohayo.dto.request.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.validation.PasswordMatchConstant;

@PasswordMatchConstant(
        field = "password",
        fieldMatch = "confirmPassword"
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRegister {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ErrorEnum.INVALID_EMAIL_MS)
    @Email
    @NotNull(message = ErrorEnum.NOT_EMPTY_EMAIL)
    private String email;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PASSWORD)
    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String password;

    @NotNull(message = ErrorEnum.NOT_EMPTY_PASSWORD)
    @Size(min = 5, message = ErrorEnum.INVALID_PASSWORD)
    private String confirmPassword;

    @NotNull(message = ErrorEnum.NOT_EMPTY_NAME)
    @Size(max = 50, message = ErrorEnum.INVALID_NAME)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    private String phone;

    @Size(max = 255, message = ErrorEnum.INVALID_ADDRESS)
    private String address;

}
