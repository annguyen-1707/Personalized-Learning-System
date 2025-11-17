package vn.fu_ohayo.dto.response.superAdmin.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.dto.response.RoleResponse;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperAdminCheckUsernameAdminResponse {
    boolean usernameExists;
    @JsonProperty("isDeleted")
    Boolean isDeleted;
    Long adminId;
    Set<RoleResponse> roles;
}
