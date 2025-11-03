package org.example.forum_platform.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// 消息通知实体
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // 回复、私信、系统公告

    private String content;

    private Boolean read = false;

    private LocalDateTime createTime = LocalDateTime.now();

    @ManyToOne
    private User receiver;
    public void setRead(boolean read) {
        this.read = read;
    }
    // getters and setters
}