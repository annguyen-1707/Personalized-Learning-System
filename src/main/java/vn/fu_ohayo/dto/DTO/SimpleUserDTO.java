package vn.fu_ohayo.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUserDTO {
    private Long userId;
    private String email;
    private String fullName;
    private Gender gender;
    private String phone;
    private String address;
    private Date dob;
    private String avatar;
    private UserStatus status;
    private MembershipLevel membershipLevel;
    private Provider provider;
}