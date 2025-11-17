package vn.fu_ohayo.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.converter.DayOfWeekListConverter;
import vn.fu_ohayo.enums.ErrorEnum;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudyReminderRequest {

    private int studyReminderId;
    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    @NotBlank(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String note;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TIME)
    private LocalTime time;
    private Boolean isActive;

    @Convert(converter = DayOfWeekListConverter.class)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek;

}
