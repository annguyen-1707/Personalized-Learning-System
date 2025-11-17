package vn.fu_ohayo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProgressSubjectResponse {
    private int progressId;

    private UserResponse user;

    private SubjectResponse subject;

    private ProgressStatus progressStatus;

    private Date startDate;

    private Date viewedAt;

    private Date endDate;
}
