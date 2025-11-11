package org.example.forum_platform.service;

import org.example.forum_platform.dto.PostDTO;
import org.example.forum_platform.entity.Board;
import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.BoardRepository;
import org.example.forum_platform.repository.PostRepository;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository; // 注入 BoardRepository 实例
    @Autowired
    private UserRepository userRepository; // 注入 UserRepository 实例

    // 发布帖子
    public Post createPost(PostDTO postDTO) {
        // 1. 根据 boardId 查找 Board 实体
        Board board = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(() -> new RuntimeException("板块不存在"));
        //2.通过userId查询User对象
        User author = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + postDTO.getUserId()));

        // 3. 创建 Post 实体并设置属性
        Post post = new Post();
        post.setBoard(board);//board不为空
        post.setTitle(postDTO.getTitle());
        post.setAuthor(author);//author不为空
        post.setContent(postDTO.getContent());
        post.setCreateTime(LocalDateTime.now());
        post.setImageUrl(String.join(",", postDTO.getImages())); // 如果用字符串存储多张图片

        // 3. 保存
        return postRepository.save(post);
    }

    // 编辑帖子（使用 DTO）
    public Post updatePost(PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new RuntimeException("帖子不存在"));

        // 更新帖子内容
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUpdateTime(LocalDateTime.now());
        // 更新所属版块
        if (postDTO.getBoardId() != null) {
            Board board = boardRepository.findById(postDTO.getBoardId())
                    .orElseThrow(() -> new RuntimeException("版块不存在"));
            post.setBoard(board);
        }

        // 若用户ID存在，校验用户
        if (postDTO.getUserId() != null) {
            User user = userRepository.findById(postDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            post.setAuthor(user);
        }

        // 更新图片链接（如果有）
        if (postDTO.getImages() != null) {
            post.setImageUrl(postDTO.getImages().toString());
        }

        return postRepository.save(post);
    }

    // 删除帖子（逻辑删除 + 权限校验）
    public String deletePost(Long postId, Authentication authentication) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return "帖子不存在";
        }

        Post post = optionalPost.get();
        //当前登录用户名
        String currentUsername = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);

        if (userOpt .isEmpty()) {
            return "用户身份无效";
        }

        // 如果是管理员 或 帖子作者本人 → 允许删除
        User currentUser = userOpt .get();
        boolean isAdmin = currentUser.getRole().equalsIgnoreCase("ADMIN");
        boolean isAuthor = post.getAuthor() != null &&
                post.getAuthor().getUsername().equals(currentUsername);

        if (isAdmin || isAuthor) {
            post.setDeleted(true);
            postRepository.save(post);
            return "删除成功";
        }

        return "权限不足，无法删除他人帖子";
    }
    // 获取版块下的所有帖子
    public List<Post> getPostsByBoard(Long boardId) {
        return postRepository.findByBoardIdAndDeletedFalse(boardId);
    }
    // 根据ID获取帖子
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    // 获取所有帖子（包括已删除的）
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
