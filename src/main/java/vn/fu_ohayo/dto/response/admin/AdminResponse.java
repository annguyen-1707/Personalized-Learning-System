package vn.fu_ohayo.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.fu_ohayo.dto.response.RoleResponse;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminResponse {
    private Long adminId;
    private String username;
    private Set<RoleResponse> roles;
}
