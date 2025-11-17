package vn.fu_ohayo.dto.request.superAdmin.systemLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import vn.fu_ohayo.dto.request.PagingRequest;
import vn.fu_ohayo.enums.RoleEnum;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemLogRequest extends PagingRequest {
    LocalDateTime startTimestamp;
    LocalDateTime endTimestamp;
    String action;
    String details;
    RoleEnum role;
}
