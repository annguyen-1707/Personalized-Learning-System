package vn.fu_ohayo.enums;

import lombok.Getter;

@Getter
public enum NotificationEnum {

    NORMAL("You have new notification"),
    PAYMENT("A payment is requested"),
    ACCEPT_STUDENT("Confirm your child's registration");

    private final String title;

    NotificationEnum(String title) {
        this.title = title;
    }

}
