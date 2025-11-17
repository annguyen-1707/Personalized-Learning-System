package vn.fu_ohayo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.DTO.NotificationDTO;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{notificationId}/confirm")
    public ResponseEntity<String> confirmNotification(@PathVariable Long notificationId) {
        notificationService.handleNotificationAction(notificationId, true);
        return ResponseEntity.ok("Notification confirmed");
    }

    @PostMapping("/{notificationId}/deny")
    public ResponseEntity<String> denyNotification(@PathVariable Long notificationId) {
        notificationService.handleNotificationAction(notificationId, false);
        return ResponseEntity.ok("Notification denied");
    }

    @PostMapping("/notify")
    public ResponseEntity<Notification> notifyUser(@RequestBody Notification notification) {
        Notification saved = notificationService.notifyUser(notification);
        return ResponseEntity.ok(saved);
    }

        @GetMapping("/{userId}/notificationUser")
        public ResponseEntity<?> getAllNotificationsOfUser (@PathVariable("userId") Long userId) {
            List<NotificationDTO> list = notificationService.getNotificationList(userId);
            return ResponseEntity.ok().body(list);
        }

    @GetMapping("/{notificationId}/{status}/sendNotification")
    public ResponseEntity<?> updateNotificationStatus(@PathVariable("notificationId") Long id, @PathVariable("status") boolean status ) {
        log.info("DA Vao");
        notificationService.handleNotificationAction(id, status);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
    }