package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import vn.fu_ohayo.dto.DTO.NotificationDTO;
import vn.fu_ohayo.entity.Notification;

@Mapper(componentModel = "spring")

public interface NotificationMapper {
    @Mappings({
            @Mapping(source = "notificationId", target = "notificationId"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "user.userId", target = "userId"),
            @Mapping(source = "userSend.userId", target = "userSendId"),
            @Mapping(source = "statusSend", target = "statusSend"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "type", target = "type")
    })
    NotificationDTO notificationDTO(Notification notification);
}

