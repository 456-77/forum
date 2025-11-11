package org.example.forum_platform.controller;

import org.example.forum_platform.dto.PostDTO;
import org.example.forum_platform.entity.Post;
import org.example.forum_platform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    // 发布帖子
    @PostMapping
    public Post createPost(@RequestBody PostDTO post) {
        return postService.createPost(post);
    }
    // 编辑帖子（使用 PostDTO）
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        postDTO.setId(id); // 确保 ID 一致
        try {
            Post updatedPost = postService.updatePost(postDTO);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("success", false, "message", e.getMessage())
            );
        }
    }
    //删除帖子
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        String result = postService.deletePost(id, authentication);

        if ("删除成功".equals(result)) {
            return ResponseEntity.ok(
                    java.util.Map.of("success", true, "message", result)
            );
        } else if ("帖子不存在".equals(result)) {
            return ResponseEntity.status(404).body(
                    java.util.Map.of("success", false, "message", result)
            );

        } else {
            return ResponseEntity.status(403).body(
                    java.util.Map.of("success", false, "message", result)
            );
        }
    }
    // 获取版块下的所有帖子
    @GetMapping("/board/{boardId}")
    public List<Post> getPostsByBoard(@PathVariable Long boardId) {
        return postService.getPostsByBoard(boardId);
    }
    // 获取单个帖子
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPostById(id).orElse(null);
    }
    //获取所有帖子
    @GetMapping
    public List<Post> getAllPosts() {
        // This method assumes that there is a method in PostService to get all posts
        return postService.getAllPosts();
    }

}