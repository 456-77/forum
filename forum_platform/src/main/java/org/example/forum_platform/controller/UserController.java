package org.example.forum_platform.controller;

import org.example.forum_platform.dto.UserUpdateDTO;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    // 普通用户更新自己的资料（使用安全方法）
    @PutMapping("/profile")
    public ResponseEntity<?> updateCurrentUserProfile(@RequestBody UserUpdateDTO updateDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 普通用户调用，isAdmin = false
            User updatedUser = userService.updateUserProfile(currentUser.getId(), updateDTO,false);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 管理员更新任意用户资料
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        try {
            // 管理员调用，isAdmin = true
            User updatedUser = userService.updateUserProfile(id, updateDTO, true);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 查询用户信息（带权限验证）,供管理员使用
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("当前用户不存在"));

            // 权限验证：用户只能查看自己的信息，管理员可以查看所有
            String currentUserRole = currentUser.getRole();
            if (!"ADMIN".equals(currentUserRole) && !currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body("无权查看其他用户信息");
            }

            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 新增：获取当前用户自己的信息（不需要ID）
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 查询用户积分（带权限验证）
    @GetMapping("/profile/points/{id}")
    public ResponseEntity<?> getUserPoints(@PathVariable Long id) {
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("当前用户不存在"));

            // 权限验证：用户只能查看自己的积分，管理员可以查看所有
            String currentUserRole = currentUser.getRole();
            if (!"ADMIN".equals(currentUserRole) && !currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body("无权查看其他用户积分");
            }

            Integer points = userService.getUserPoints(id);
            return ResponseEntity.ok(points);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 新增：获取当前用户自己的积分（不需要ID）
    @GetMapping("/profile/points")
    public ResponseEntity<?> getCurrentUserPoints() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            Integer points = userService.getUserPoints(currentUser.getId());
            return ResponseEntity.ok(points);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}