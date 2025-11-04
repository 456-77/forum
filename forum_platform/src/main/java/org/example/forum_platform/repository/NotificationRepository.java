package org.example.forum_platform.repository;

import org.example.forum_platform.entity.Notification;
import org.example.forum_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdOrderByCreateTimeDesc(Long receiverId);
    List<Notification> findByReceiverAndIsReadFalse(User receiver);

    List<Notification> findByReceiverOrderByCreateTimeDesc(User receiver);
}

