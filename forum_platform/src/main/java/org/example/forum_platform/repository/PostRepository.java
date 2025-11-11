package org.example.forum_platform.repository;

import org.example.forum_platform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoardIdAndDeletedFalse(Long boardId);
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
    // 根据作者ID查询该用户的帖子（排除逻辑删除的）
    List<Post> findByAuthor_IdAndDeletedFalse(Long authorId);
}