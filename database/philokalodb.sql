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