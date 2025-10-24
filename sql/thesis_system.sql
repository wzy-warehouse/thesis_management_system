/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : thesis_system

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 24/10/2025 19:36:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for citation_format
-- ----------------------------
DROP TABLE IF EXISTS `citation_format`;
CREATE TABLE `citation_format`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '格式名称（如 APA）',
  `template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板（如 {author}.{title}[J]...）',
  `is_default` tinyint NOT NULL COMMENT '是否默认（1 - 是，0 - 否）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '引用格式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of citation_format
-- ----------------------------

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹名称',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父文件夹 ID（0 为根目录）',
  `create_user` bigint NOT NULL COMMENT '创建人 ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_folder_create_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `fk_folder_create_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件夹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of folder
-- ----------------------------

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '论文标题',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `publish_time` date NULL DEFAULT NULL COMMENT '发表时间',
  `journal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发表期刊',
  `journal_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '期刊类型（如 SCI / 核心）',
  `research_direction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '研究方向',
  `innovation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '创新点',
  `deficiency` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '不足',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件存储路径',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小（字节）',
  `file_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件 MD5 哈希（去重）',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键词（逗号分隔）',
  `create_user` bigint NOT NULL COMMENT '上传人 ID（关联 user 表）',
  `create_time` datetime NOT NULL COMMENT '上传时间',
  `update_time` datetime NOT NULL COMMENT '最后编辑时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0 - 正常，1 - 回收站）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_paper_create_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `fk_paper_create_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '论文表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------

-- ----------------------------
-- Table structure for paper_folder
-- ----------------------------
DROP TABLE IF EXISTS `paper_folder`;
CREATE TABLE `paper_folder`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `paper_id` bigint NOT NULL COMMENT '论文 ID',
  `folder_id` bigint NOT NULL COMMENT '文件夹 ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_paper_folder_paper`(`paper_id` ASC) USING BTREE,
  INDEX `fk_paper_folder_folder`(`folder_id` ASC) USING BTREE,
  CONSTRAINT `fk_paper_folder_folder` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_paper_folder_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '论文-文件夹关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper_folder
-- ----------------------------

-- ----------------------------
-- Table structure for recycle_bin
-- ----------------------------
DROP TABLE IF EXISTS `recycle_bin`;
CREATE TABLE `recycle_bin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `paper_id` bigint NOT NULL COMMENT '论文 ID',
  `delete_time` datetime NOT NULL COMMENT '删除时间（用于定时清理）',
  `delete_user` bigint NOT NULL COMMENT '删除人 ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_recycle_bin_paper`(`paper_id` ASC) USING BTREE,
  INDEX `fk_recycle_bin_user`(`delete_user` ASC) USING BTREE,
  CONSTRAINT `fk_recycle_bin_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_recycle_bin_user` FOREIGN KEY (`delete_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回收站表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recycle_bin
-- ----------------------------

-- ----------------------------
-- Table structure for share
-- ----------------------------
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `paper_id` bigint NOT NULL COMMENT '论文 ID',
  `share_token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分享唯一标识（UUID）',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间（null 为永久）',
  `permission` tinyint NOT NULL COMMENT '权限（1 - 仅查看，2 - 允许下载）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_share_token`(`share_token` ASC) USING BTREE,
  INDEX `fk_share_paper`(`paper_id` ASC) USING BTREE,
  CONSTRAINT `fk_share_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分享表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of share
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键（自增）',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密密码（BCrypt）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$zzmowtAgnv6y34QICOLswuH9HOIVlKQ4Fvvu/PHOKrzY5CkJA3HFC', '2025-10-24 19:35:43', '2025-10-24 19:35:51');

SET FOREIGN_KEY_CHECKS = 1;
