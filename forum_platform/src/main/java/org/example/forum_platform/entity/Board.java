package org.example.forum_platform.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.util.List;

// 版块实体
@Entity
@Table(name = "boards")
public class Board {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne
    private User moderator; // 版主

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Post> posts;

    // getters and setters
}
