package org.example.forum_platform.controller;

import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    // 更新资料
    @PutMapping("/{id}")
    public User updateProfile(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateProfile(user);
    }

    // 查询用户
    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
    // 搜索用户
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
        // 这里可以调用 userService 的搜索方法
        return ResponseEntity.ok("搜索用户: " + keyword);
    }
}