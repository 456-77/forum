package org.example.forum_platform.dto;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String phone;

    public String getUsername() {
        return username;
    }

    public CharSequence getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
