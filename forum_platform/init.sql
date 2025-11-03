-- ==============================
-- 社区论坛系统数据库初始化脚本
-- Database: forum_db
-- ==============================

-- 1️⃣ 创建数据库
CREATE DATABASE IF NOT EXISTS forum_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE forum_db;

-- ==============================
-- 2️⃣ 用户表
-- ==============================
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar_url VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',  -- USER / MODERATOR / ADMIN
    points INT DEFAULT 0,
    level INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- ==============================
-- 3️⃣ 板块表
-- ==============================
CREATE TABLE IF NOT EXISTS boards (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    moderator_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (moderator_id) REFERENCES users(id) ON DELETE SET NULL
    );

-- ==============================
-- 4️⃣ 帖子表
-- ==============================
CREATE TABLE IF NOT EXISTS posts (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(200) NOT NULL,
    content TEXT,
    image_url VARCHAR(255),
    author_id BIGINT NOT NULL,
    board_id BIGINT NOT NULL,
    likes INT DEFAULT 0,
    dislikes INT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
    );

-- ==============================
-- 5️⃣ 评论表
-- ==============================
CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        content TEXT NOT NULL,
                                        author_id BIGINT NOT NULL,
                                        post_id BIGINT NOT NULL,
                                        parent_id BIGINT DEFAULT NULL, -- 回复的上级评论
                                        likes INT DEFAULT 0,
                                        dislikes INT DEFAULT 0,
                                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE SET NULL
    );

-- ==============================
-- 6️⃣ 通知表
-- ==============================
CREATE TABLE IF NOT EXISTS notifications (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             receiver_id BIGINT NOT NULL,
                                             sender_id BIGINT,
                                             type VARCHAR(50),             -- reply / like / system
    content TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE SET NULL
    );

-- ==============================
-- 7️⃣ 用户等级表
-- ==============================
CREATE TABLE IF NOT EXISTS user_levels (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           level INT NOT NULL UNIQUE,
                                           min_points INT NOT NULL,
                                           max_points INT NOT NULL,
                                           privilege_description VARCHAR(255)
    );

-- ==============================
-- 8️⃣ 初始化基础数据
-- ==============================

-- 管理员账户（密码：admin123，经 BCrypt 加密）
INSERT INTO users (username, password, email, role, points, level)
VALUES ('admin', '$2a$10$E8YVuMCZq5M2BvHbmyy3xOe4XnsvS5C/bkE/0lVX83vqtfp13Mgzu', 'admin@forum.com', 'ADMIN', 9999, 5);

-- 默认板块
INSERT INTO boards (name, description) VALUES
                                           ('生活闲聊', '分享日常趣事'),
                                           ('兴趣爱好', '讨论兴趣话题'),
                                           ('社区活动', '发布线下活动'),
                                           ('房产信息', '房屋出租买卖讨论');

-- 等级规则
INSERT INTO user_levels (level, min_points, max_points, privilege_description) VALUES
                                                                                   (1, 0, 99, '基础用户'),
                                                                                   (2, 100, 499, '活跃用户'),
                                                                                   (3, 500, 999, '核心成员'),
                                                                                   (4, 1000, 4999, '版主候选'),
                                                                                   (5, 5000, 999999, '超级用户');
