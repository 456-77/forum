package org.example.forum_platform.dto;

public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long  userId;
    private Long boardId;
    private String images_url;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public CharSequence getImages() {
        return images_url;
    }

    public Long getBoardId() {
        return boardId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
