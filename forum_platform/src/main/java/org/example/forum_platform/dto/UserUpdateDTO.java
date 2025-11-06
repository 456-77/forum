package org.example.forum_platform.dto;

public class UserUpdateDTO {
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String oldPassword;     // 新增：普通用户修改密码需要原密码
    private String newPassword;     // 新增：新密码
    private String confirmPassword; // 新增：确认密码    // 新增：普通用户修改密码需要原密码
    private String role;      // 新增：管理员专用
    private Integer points;   // 新增：管理员专用
    private Integer level;    // 新增：管理员专用

    // getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}