package org.example.forum_platform.controller;

import org.example.forum_platform.entity.Notification;
import org.example.forum_platform.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 获取当前用户的通知
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // 发送通知（可用于管理员公告、私信、系统提醒）
    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(
            @RequestBody Map<String, Object> payload) {
        Long senderId = ((Number) payload.get("senderId")).longValue();
        Long receiverId = ((Number) payload.get("receiverId")).longValue();
        String type = (String) payload.get("type");
        String content = (String) payload.get("content");

        Notification n = notificationService.sendNotification(senderId, receiverId, type, content);
        return ResponseEntity.ok(n);
    }

    // 标记为已读
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "标记为已读"));
    }

    // 删除通知
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }
}
