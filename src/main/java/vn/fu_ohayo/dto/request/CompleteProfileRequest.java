package vn.fu_ohayo.dto.request;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.RoleEnum;
import vn.fu_ohayo.validation.AgeRange;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
public class CompleteProfileRequest {
    @NotNull(message = ErrorEnum.NOT_EMPTY_NAME)
    @Size(min = 2, max = 50, message = ErrorEnum.INVALID_NAME)
    @Pattern(regexp = "^[\\p{L} ]+$", message = ErrorEnum.INVALID_NAME2)
    private String fullName;

    private Gender gender; // hoặc Enum

    @NotNull
    @AgeRange(min = 5, max = 100)
    private Date dob;

    @NotNull
    private RoleEnum role = RoleEnum.USER;

    private String address;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = ErrorEnum.INVALID_PHONE)
    private String phone;

}
