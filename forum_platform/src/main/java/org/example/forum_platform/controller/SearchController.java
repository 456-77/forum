package org.example.forum_platform.controller;

import org.example.forum_platform.entity.Post;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    //搜索帖子,只返回帖子id
    @GetMapping("/posts")
    public ResponseEntity<?> searchPosts(@RequestParam String keyword) {
        Object result = searchService.searchPosts(keyword);

        // 判断返回类型，如果是 Map 则直接返回
        if (result instanceof Map<?, ?> map) {
            return ResponseEntity.ok(map);
        }

        // 否则兜底返回错误信息
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "搜索失败"
        ));
    }


    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String keyword) {
        return searchService.searchUsers(keyword);
    }
}