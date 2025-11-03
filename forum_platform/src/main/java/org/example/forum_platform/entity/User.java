package org.example.forum_platform.entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDateTime;

// 用户实体
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private String avatar;

    private Integer points = 0;

    private String role = "USER"; // USER, MODERATOR, ADMIN

    private Integer level = 1;

    private LocalDateTime createTime = LocalDateTime.now();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public String getPassword() {
        return password;
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void setRole(String user) {
        this.role = user;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public Serializable getPhone() {
        return phone;
    }

    public Object getAvatar() {
        return avatar;
    }

    public Object getEmail() {
        return email;
    }

    public Object getPoints() {
        return points;
    }

    public Object getlevel() {
        return level;
    }

    // getters and setters
}
