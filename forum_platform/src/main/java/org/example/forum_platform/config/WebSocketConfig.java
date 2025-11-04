package org.example.forum_platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new org.example.forum_platform.controller.NotificationWebSocket(), "/ws/notifications")
                .setAllowedOrigins("*"); // 允许前端跨域访问
    }
}
