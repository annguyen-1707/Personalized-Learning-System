package vn.fu_ohayo.entity;

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
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Study_Reminder")
public class StudyReminder {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int studyReminderId;
    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    @NotBlank(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String note;

    private LocalTime time;
    private Boolean isActive;

    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> daysOfWeek;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
