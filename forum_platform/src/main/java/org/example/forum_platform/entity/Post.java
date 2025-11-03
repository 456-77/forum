package org.example.forum_platform.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
// 帖子实体
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime;

    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void setCreateTime(LocalDateTime now) {
        this.createTime = now;
    }

    public void setUpdateTime(LocalDateTime now) {
        this.updateTime = now;
    }

    public void setDeleted(boolean b) {
        this.deleted = b;
    }

    public void setId(Long id) {
        this.id=id;
    }

    // getters and setters
}
