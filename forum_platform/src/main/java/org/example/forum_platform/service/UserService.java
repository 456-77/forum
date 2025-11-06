package org.example.forum_platform.service;

import org.example.forum_platform.dto.RegisterRequest;
import org.example.forum_platform.dto.UserUpdateDTO;
import org.example.forum_platform.entity.User;
import org.example.forum_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册用户
    public User register(RegisterRequest request) {
        // 验证必要字段
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }

        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在: " + request.getUsername());
        }

        // 创建并设置用户信息
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setPoints(0);
        user.setLevel(1);

        // 设置可选字段
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone().trim());
        }

        return userRepository.save(user);
    }

    // 登录验证
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    // 统一的用户资料更新方法（支持普通用户和管理员）
    public User updateUserProfile(Long userId, UserUpdateDTO updateDTO, boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

        // 更新基本信息（普通用户和管理员都可以）
        if (updateDTO.getUsername() != null &&
                !updateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(updateDTO.getUsername()).isPresent()) {
                throw new RuntimeException("用户名已存在: " + updateDTO.getUsername());
            }
            user.setUsername(updateDTO.getUsername().trim());
        }

        if (updateDTO.getEmail() != null &&
                !updateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(updateDTO.getEmail()).isPresent()) {
                throw new RuntimeException("邮箱已被注册: " + updateDTO.getEmail());
            }
            user.setEmail(updateDTO.getEmail().trim());
        }

        if (updateDTO.getPhone() != null &&
                !updateDTO.getPhone().equals(user.getPhone())) {
            if (userRepository.findByPhone(updateDTO.getPhone()).isPresent()) {
                throw new RuntimeException("手机号已被注册: " + updateDTO.getPhone());
            }
            user.setPhone(updateDTO.getPhone().trim());
        }

        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar().trim());
        }

        // 密码修改逻辑
        if (updateDTO.getNewPassword() != null && !updateDTO.getNewPassword().trim().isEmpty()) {
            changePassword(user, updateDTO, isAdmin);
        }

        // 管理员专用字段（只有管理员可以更新）
        if (isAdmin) {
            if (updateDTO.getRole() != null) {
                user.setRole(updateDTO.getRole());
            }
            if (updateDTO.getPoints() != null) {
                user.setPoints(updateDTO.getPoints());
            }
            if (updateDTO.getLevel() != null) {
                user.setLevel(updateDTO.getLevel());
            }
        }

        return userRepository.save(user);
    }

    // 密码修改逻辑（私有方法）
    private void changePassword(User user, UserUpdateDTO updateDTO, boolean isAdmin) {
        String newPassword = updateDTO.getNewPassword().trim();

        // 验证新密码和确认密码是否一致
        if (updateDTO.getConfirmPassword() == null ||
                !newPassword.equals(updateDTO.getConfirmPassword().trim())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }

        // 验证密码长度和强度
        if (newPassword.length() < 6) {
            throw new RuntimeException("密码长度不能少于6位");
        }

        if (isAdmin) {
            // 管理员可以直接修改密码，不需要原密码
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            // 普通用户需要验证原密码
            if (updateDTO.getOldPassword() == null || updateDTO.getOldPassword().trim().isEmpty()) {
                throw new RuntimeException("原密码不能为空");
            }

            if (!passwordEncoder.matches(updateDTO.getOldPassword().trim(), user.getPassword())) {
                throw new RuntimeException("原密码错误");
            }

            // 新密码不能和原密码相同
            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new RuntimeException("新密码不能与原密码相同");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
        }
    }


    // 根据ID查询用户
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    // 根据用户名查询用户
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 查询用户积分
    public Integer getUserPoints(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));

        return user.getPoints();
    }

}