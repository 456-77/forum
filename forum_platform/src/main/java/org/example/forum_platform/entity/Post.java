package org.example.forum_platform.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"posts"})  // 忽略序列化作者里的 posts
    private User author;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonIgnoreProperties({"posts"})  // 忽略 board 的 posts
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // getters and setters
    // ===== Getter 和 Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }



}



