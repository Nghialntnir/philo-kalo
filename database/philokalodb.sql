-- Host: localhost
-- Create day: 27/5/2026
-- Generation Time: 27-5-2026 at 13:21
-- Author: Nghia Le

CREATE DATABASE IF NOT EXISTS `philokalodb`;
USE `philokalodb`;
-- -----------------------------------------------------------------------------------------------------------------
CREATE TABLE `user` (
    `id`          char(36) PRIMARY KEY DEFAULT (UUID()),
    `email`       varchar(255) UNIQUE NOT NULL,
	`username`    varchar(50) UNIQUE NOT NULL,        -- để @tag
    `password`    varchar(255) NOT NULL,
    `full_name`   varchar(255),
    `avatar_url`  TEXT,
    `bio`         TEXT,
    `is_artist`   BOOLEAN DEFAULT 0,
    `is_active`   BOOLEAN DEFAULT 1,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- -----------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `role`(
	`id`			char(36) PRIMARY KEY DEFAULT (UUID()),
    `name`			varchar(50) UNIQUE NOT NULL,
    `description` 	TEXT,
    `created_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- --------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_role`(
	`user_id` 		char(36) NOT NULL,
    `role_id` 		char(36) NOT NULL,
    `assigned_by` 	char(36) NOT NULL, -- Nhận biết người gán quyền cho user này
    `assigned_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,-- Gán quyền lúc nào
    `created_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Cập nhật quyền lúc nào
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) 	ON DELETE CASCADE, -- Xóa user thì xóa user_role
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)	 	ON DELETE NO ACTION, -- Xóa user_role trước khi xóa role
    FOREIGN KEY (`assigned_by`) REFERENCES `user`(`id`) ON DELETE CASCADE, -- Nếu xóa ng gán quyền thì phải cảnh báo
	PRIMARY KEY (`user_id`, `role_id`) -- 1 user chỉ có 1 role 1 lần
);
-- --------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `permission`(
	`id` 			char(36) PRIMARY KEY DEFAULT (UUID()),
    `name` 			varchar(50) UNIQUE NOT NULL,
    `description` 	TEXT,
    `created_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- --------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `role_permission`(
	`role_id` 		char(36) NOT NULL,
    `permission_id` char(36) NOT NULL,
    `created_at` 	DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) 			ON DELETE CASCADE, -- Xóa role thì xóa role_permission
	FOREIGN KEY (`permission_id`) REFERENCES `permission`(`id`) ON DELETE NO ACTION, -- Xóa role_permission trước thì mới xóa role
	PRIMARY KEY (`role_id`, `permission_id`)
);
-- --------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `invalidated_token`(
	`id` 		char(36) NOT NULL,
	`expiry_time` DATETIME
);
-- --------------------------------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `artwork` (
    `id`              char(36) PRIMARY KEY DEFAULT (UUID()),
    `artist_id`       char(36) NOT NULL,
    `title`           varchar(255) NOT NULL,
    `description`     TEXT,
    `medium`          varchar(100),        -- sơn dầu, acrylic, digital...
    `year_created`    YEAR,
    `status`          ENUM('draft','published','archived','auction') DEFAULT 'draft',
    `is_for_sale`     BOOLEAN DEFAULT 0,
    `price`           DECIMAL(15,2),
    `currency`        char(3) DEFAULT 'VND',
    `view_count`      INT UNSIGNED DEFAULT 0,
    `like_count`      INT UNSIGNED DEFAULT 0,
    `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`artist_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `artwork_image` (
    `id`              char(36) PRIMARY KEY DEFAULT (UUID()),
    `artwork_id`      char(36) NOT NULL,
    `is_primary`      BOOLEAN DEFAULT 0,       -- ảnh đại diện của bộ
    `original_url`    TEXT NOT NULL,            -- file gốc (full res, dùng khi download)
    `full_url`        TEXT,                     -- ~2000px (lightbox/zoom)
    `medium_url`      TEXT,                     -- ~800px (detail page)
    `thumb_url`       TEXT NOT NULL,            -- ~400px (grid list, ưu tiên lazy load)
    `blur_hash`       varchar(100),             -- ← placeholder mờ khi ảnh chưa load
    `width`           INT UNSIGNED,             -- width ảnh gốc (px)
    `height`          INT UNSIGNED,             -- height ảnh gốc (px)
    `file_size_kb`    INT UNSIGNED,
    `format`          varchar(10),              -- jpg, png, webp
    `sort_order`      TINYINT UNSIGNED DEFAULT 0,
    `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`artwork_id`) REFERENCES `artwork`(`id`) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `category` (
    `id`        char(36) PRIMARY KEY DEFAULT (UUID()),
    `name`      varchar(100) UNIQUE NOT NULL,
    `slug`      varchar(100) UNIQUE NOT NULL,
    `parent_id` char(36) DEFAULT NULL,
    FOREIGN KEY (`parent_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS `artwork_category` (
    `artwork_id`    char(36) NOT NULL,
    `category_id`  char(36) NOT NULL,
    PRIMARY KEY (`artwork_id`, `category_id`),
    FOREIGN KEY (`artwork_id`)   REFERENCES `artwork`(`id`)   ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)  ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `tag` (
    `id`    char(36) PRIMARY KEY DEFAULT (UUID()),
    `name`  varchar(50) UNIQUE NOT NULL,
    `slug`  varchar(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS `artwork_tag` (
    `artwork_id` char(36) NOT NULL,
    `tag_id`     char(36) NOT NULL,
    PRIMARY KEY (`artwork_id`, `tag_id`),
    FOREIGN KEY (`artwork_id`) REFERENCES `artwork`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`)     REFERENCES `tag`(`id`)    ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS `artwork_like` (
    `user_id`       char(36) NOT NULL,
    `artwork_id`    char(36) NOT NULL,
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`, `artwork_id`),
    FOREIGN KEY (`user_id`)    REFERENCES `user`(`id`)    ON DELETE CASCADE,
    FOREIGN KEY (`artwork_id`) REFERENCES `artwork`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `artwork_comment` (
    `id`            char(36) PRIMARY KEY DEFAULT (UUID()),
    `artwork_id`    char(36) NOT NULL,
    `user_id`       char(36) NOT NULL,
    `parent_id`     char(36) DEFAULT NULL,   -- reply comment
    `content`       TEXT NOT NULL,
    `is_hidden`     BOOLEAN DEFAULT 0,
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`artwork_id`) REFERENCES `artwork`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`)    REFERENCES `user`(`id`)   ON DELETE CASCADE,
    FOREIGN KEY (`parent_id`)  REFERENCES `artwork_comment`(`id`) ON DELETE SET NULL
);