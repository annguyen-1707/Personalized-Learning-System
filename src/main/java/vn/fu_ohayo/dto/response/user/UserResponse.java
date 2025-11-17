package vn.fu_ohayo.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.dto.DTO.ParentStudentDTO;
import vn.fu_ohayo.dto.DTO.StudentDTO;
import vn.fu_ohayo.enums.Gender;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

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
    private String roleName;
    private List<StudentDTO> children;
    private List<ParentStudentDTO> parents;

}
