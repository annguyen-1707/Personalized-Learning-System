package vn.fu_ohayo.dto.response.superAdmin.systemLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.RoleEnum;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemLogResponse {
    LocalDateTime timestamp;
    String action;
    String details;
    RoleEnum role;
}
