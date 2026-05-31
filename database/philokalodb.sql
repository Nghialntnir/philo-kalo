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