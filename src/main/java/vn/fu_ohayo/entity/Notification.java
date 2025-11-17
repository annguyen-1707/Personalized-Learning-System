package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.NotificationEnum;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    @Column(name = "notification_id")
    private int notificationId;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @NotNull(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    public void createAtNow() {
        this.createdAt = new Date();
    }

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "user_send_id")
    private User userSend;

    @Column(name = "status_send")
    private boolean statusSend;


    @Column(name = "status")
    private boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationEnum type = NotificationEnum.NORMAL;


}
