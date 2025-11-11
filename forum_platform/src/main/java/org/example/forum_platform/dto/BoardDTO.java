package org.example.forum_platform.dto;



// 用于只返回必要字段的版块传输对象
public class BoardDTO {
    private Long id;
    private String name;
    private String description;

    public BoardDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
