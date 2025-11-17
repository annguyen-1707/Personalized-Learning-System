package vn.fu_ohayo.dto.response;

import lombok.*;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProgressLessonResponse {

    private int id;

    private UserResponse user;

    private LessonResponse lesson;

    private ProgressStatus status;

    private Date startDate;

    private Date viewedAt;

    private Date endDate;

}
