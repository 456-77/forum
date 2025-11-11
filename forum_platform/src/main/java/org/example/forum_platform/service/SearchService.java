package org.example.forum_platform.service;

import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.PostRepository;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    // 搜索帖子，只返回帖子ID或失败信息
    public Object searchPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        if (posts.isEmpty()) {
            // 若无匹配结果，返回失败信息
            return Map.of(
                    "success", false,
                    "message", "未找到相关帖子"
            );
        }

        // 提取帖子ID列表
        List<Long> ids = posts.stream()
                .map(Post::getId)
                .toList();

        return Map.of(
                "success", true,
                "postIds", ids
        );
    }

    // 搜索用户
    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }
}

