package vn.fu_ohayo.dto.response.admin.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminFilterUserResponse {
    int userId;
    String fullName;
    String email;
    UserStatus status;
    Date createdAt;
    Date updatedAt;
    Provider provider;
    MembershipLevel membershipLevel;
    @JsonProperty("isDeleted")
    boolean isDeleted;
}