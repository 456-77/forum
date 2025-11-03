package org.example.forum_platform.controller;

import org.example.forum_platform.dto.LoginRequest;
import org.example.forum_platform.dto.RegisterRequest;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
// 控制器处理用户注册和登录请求
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest user) {//使用dto获取注册信息
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "注册成功",
                    "userId", savedUser.getId(),
                    "username", savedUser.getUsername()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {//使用dto获取登录信息
        Optional<User> user = userService.login(request.getUsername(), request.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "登录成功",
                    "username", user.get().getUsername(),
                    "role", user.get().getRole(),
                    "userId", user.get().getId(),
                    "phone", user.get().getPhone(),
                    "avatar", user.get().getAvatar(),
                    "email", user.get().getEmail(),
                    "points", user.get().getPoints(),
                    "level", user.get().getlevel()


            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "用户名或密码错误"
            ));
        }
    }
}