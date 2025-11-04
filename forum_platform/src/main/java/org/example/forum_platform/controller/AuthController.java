package org.example.forum_platform.controller;

import org.example.forum_platform.dto.LoginRequest;
import org.example.forum_platform.dto.RegisterRequest;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> user = userService.login(request.getUsername(), request.getPassword());
            if (user.isPresent()) {
                User foundUser = user.get();

                // 使用 HashMap 避免问题
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("username", foundUser.getUsername());
                response.put("role", foundUser.getRole());
                response.put("userId", foundUser.getId());
                response.put("phone", foundUser.getPhone());
                response.put("avatar", foundUser.getAvatar());
                response.put("email", foundUser.getEmail());
                response.put("points", foundUser.getPoints());
                response.put("level", 1); // 暂时写死，避免调用错误的方法

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "用户名或密码错误"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "登录失败: " + e.getMessage()
            ));
        }
    }
}