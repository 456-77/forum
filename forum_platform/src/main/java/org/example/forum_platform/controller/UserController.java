package org.example.forum_platform.controller;

import org.example.forum_platform.dto.UserUpdateDTO;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 普通用户更新自己的资料
    @PutMapping("/profile")
    public ResponseEntity<?> updateCurrentUserProfile(@RequestBody UserUpdateDTO updateDTO) {
        logger.info("=== 用户资料更新请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            logger.info("当前用户名: {}", currentUsername);
            logger.info("更新数据: username={}, email={}, phone={}, avatar={}",
                    updateDTO.getUsername(), updateDTO.getEmail(), updateDTO.getPhone(), updateDTO.getAvatar());

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            logger.info("用户ID: {}, 角色: {}", currentUser.getId(), currentUser.getRole());

            User updatedUser = userService.updateUserProfile(currentUser.getId(), updateDTO, false);
            logger.info("用户资料更新成功: {}", updatedUser.getUsername());
            logger.info("=== 用户资料更新请求完成 ===");

            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            logger.error("用户资料更新失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    // 管理员更新任意用户资料
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        logger.info("=== 管理员更新用户资料请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentAdmin = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("管理员不存在"));

            logger.info("管理员: {} (ID: {}) 正在更新用户ID: {}",
                    currentAdmin.getUsername(), currentAdmin.getId(), id);
            logger.info("更新数据: username={}, role={}, points={}, level={}",
                    updateDTO.getUsername(), updateDTO.getRole(), updateDTO.getPoints(), updateDTO.getLevel());

            User updatedUser = userService.updateUserProfile(id, updateDTO, true);
            logger.info("管理员更新用户资料成功: 用户ID {}", id);
            logger.info("=== 管理员更新用户资料请求完成 ===");

            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            logger.error("管理员更新用户资料失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    // 查询用户信息（带权限验证）
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        logger.info("=== 查询用户信息请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("当前用户不存在"));

            logger.info("请求用户: {} (ID: {}, 角色: {})",
                    currentUser.getUsername(), currentUser.getId(), currentUser.getRole());
            logger.info("查询目标用户ID: {}", id);

            // 权限验证
            if (!"ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
                logger.warn("权限拒绝: 用户 {} 尝试查询用户 {}", currentUser.getId(), id);
                return ResponseEntity.status(403).body("无权查看其他用户信息");
            }

            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                logger.info("查询用户成功: {}", user.get().getUsername());
                logger.info("=== 查询用户信息请求完成 ===");
                return ResponseEntity.ok(user.get());
            } else {
                logger.warn("用户不存在: ID {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("查询用户信息失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("查询失败: " + e.getMessage());
        }
    }

    // 获取当前用户自己的信息
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        logger.info("=== 获取当前用户资料请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            logger.info("当前用户名: {}", currentUsername);

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            logger.info("返回用户资料: {} (ID: {}, 角色: {})",
                    currentUser.getUsername(), currentUser.getId(), currentUser.getRole());
            logger.info("=== 获取当前用户资料请求完成 ===");

            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            logger.error("获取当前用户资料失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("获取失败: " + e.getMessage());
        }
    }

    // 查询用户积分（带权限验证）
    @GetMapping("/profile/points/{id}")
    public ResponseEntity<?> getUserPoints(@PathVariable Long id) {
        logger.info("=== 查询用户积分请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("当前用户不存在"));

            logger.info("请求用户: {} (ID: {})", currentUser.getUsername(), currentUser.getId());
            logger.info("查询目标用户积分 - 用户ID: {}", id);

            // 权限验证
            if (!"ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
                logger.warn("权限拒绝: 用户 {} 尝试查询用户 {} 的积分", currentUser.getId(), id);
                return ResponseEntity.status(403).body("无权查看其他用户积分");
            }

            Integer points = userService.getUserPoints(id);
            logger.info("用户 {} 的积分为: {}", id, points);
            logger.info("=== 查询用户积分请求完成 ===");

            return ResponseEntity.ok(points);
        } catch (RuntimeException e) {
            logger.error("查询用户积分失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("查询失败: " + e.getMessage());
        }
    }

    // 获取当前用户自己的积分
    @GetMapping("/profile/points")
    public ResponseEntity<?> getCurrentUserPoints() {
        logger.info("=== 获取当前用户积分请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            logger.info("当前用户名: {}", currentUsername);

            User currentUser = userService.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            Integer points = userService.getUserPoints(currentUser.getId());
            logger.info("当前用户 {} 的积分为: {}", currentUser.getUsername(), points);
            logger.info("=== 获取当前用户积分请求完成 ===");

            return ResponseEntity.ok(points);
        } catch (RuntimeException e) {
            logger.error("获取当前用户积分失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("获取失败: " + e.getMessage());
        }
    }

    // 新增：调试接口 - 返回当前用户的所有信息（包括认证信息）
    @GetMapping("/debug/info")
    public ResponseEntity<?> getDebugInfo() {
        logger.info("=== 调试信息请求开始 ===");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 构建详细的调试信息
            var debugInfo = new java.util.HashMap<String, Object>();
            debugInfo.put("当前时间", java.time.LocalDateTime.now());
            debugInfo.put("认证主体", authentication.getPrincipal());
            debugInfo.put("用户名", authentication.getName());
            debugInfo.put("权限", authentication.getAuthorities());
            debugInfo.put("是否认证", authentication.isAuthenticated());

            // 获取用户详细信息
            User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
            if (currentUser != null) {
                debugInfo.put("用户ID", currentUser.getId());
                debugInfo.put("用户角色", currentUser.getRole());
                debugInfo.put("用户积分", currentUser.getPoints());
                debugInfo.put("用户等级", currentUser.getLevel());
            }

            logger.info("调试信息: {}", debugInfo);
            logger.info("=== 调试信息请求完成 ===");

            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            logger.error("获取调试信息失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body("调试信息获取失败: " + e.getMessage());
        }
    }
}