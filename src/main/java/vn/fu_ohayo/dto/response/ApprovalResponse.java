package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApprovalResponse {
    private boolean approved;
    private String mess;
}
