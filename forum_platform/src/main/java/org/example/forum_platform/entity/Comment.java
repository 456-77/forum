package org.example.forum_platform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// 评论实体
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime = LocalDateTime.now();

    private Integer likes = 0;

    private Integer dislikes = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent; // 回复的评论

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies;

    public void setCreateTime(LocalDateTime now) {
        this.createTime = now;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public int getLikes() {
        return likes;
    }
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public int getDislikes() {
        return dislikes;
    }

    // getters and setters
}