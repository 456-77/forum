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

    private Boolean isRead = false;

    private LocalDateTime createTime = LocalDateTime.now();

    //发送者
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    // 接收者
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;


    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        this.isRead = read;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    }