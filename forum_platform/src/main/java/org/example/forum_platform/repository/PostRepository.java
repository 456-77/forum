package org.example.forum_platform.repository;

import org.example.forum_platform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoardIdAndDeletedFalse(Long boardId);
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
    List<Post> findByAuthorId(Long userId);
}