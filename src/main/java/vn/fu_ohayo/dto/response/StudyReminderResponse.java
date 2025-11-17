package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudyReminderResponse {
    private int studyReminderId;
    private String note;
    private LocalTime time;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private List<DayOfWeek> daysOfWeek;
}
