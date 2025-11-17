package vn.fu_ohayo.dto.response.admin.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCheckEmailUserResponse {
    Boolean emailExists;
    @JsonProperty("isDeleted")
    boolean isDeleted;
    Long userId;
    String fullName;
    UserStatus status;
    MembershipLevel membershipLevel;
}
