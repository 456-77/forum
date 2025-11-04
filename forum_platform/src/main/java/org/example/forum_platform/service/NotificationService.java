package org.example.forum_platform.service;

import org.example.forum_platform.entity.Notification;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.NotificationRepository;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // 发送通知
    public Notification sendNotification(Long senderId, Long receiverId, String type, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("发送者不存在"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("接收者不存在"));

        Notification n = new Notification();
        n.setSender(sender);
        n.setReceiver(receiver);
        n.setType(type);
        n.setContent(content);
        return notificationRepository.save(n);
    }

    // 获取用户通知
    public List<Notification> getUserNotifications(Long userId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return notificationRepository.findByReceiverOrderByCreateTimeDesc(receiver);
    }

    // 标记为已读
    public void markAsRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        n.setIsRead(true);
        notificationRepository.save(n);
    }

    // 删除通知
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
