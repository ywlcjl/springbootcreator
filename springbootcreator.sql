/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : localhost:3306
 Source Schema         : springbootcreator

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 15/12/2025 19:27:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '盐值',
  `is_root` tinyint(1) NULL DEFAULT 0 COMMENT '是否最高管理员',
  `admin_permission` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '权限字符集1|2|3',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态:0停用,1启用',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '5db8acbe682222b48e40302bf6788bd98a44c47a', 'faac63cfedfd475c', 1, '1|2|3', 1, '1000-01-01 00:00:00', '2025-12-15 14:25:09', '2014-08-10 15:49:21');
INSERT INTO `admin` VALUES (2, 'test', 'bab8bf89410ab72df7e00100dd8d04789fb1228d', 'f666e250631e41ac', 0, '2|3', 1, '1000-01-01 00:00:00', '2025-12-15 14:59:37', '2025-12-03 10:50:24');

-- ----------------------------
-- Table structure for admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '权限名称',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '权限描述',
  `is_protected` tinyint NULL DEFAULT 0 COMMENT '受保护权限,不能被删除',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态:0无效,1有效',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin_permission
-- ----------------------------
INSERT INTO `admin_permission` VALUES (1, '系统权限', '后台系统设置, 用户管理, 日志管理', 1, 1, '2025-12-14 23:53:04', '2012-03-26 11:00:55');
INSERT INTO `admin_permission` VALUES (2, '文章权限', '文章管理, 分类管理', 1, 1, '2025-12-15 12:20:02', '2012-03-26 11:01:03');
INSERT INTO `admin_permission` VALUES (3, '图片管理', '图片管理', 1, 1, '2025-12-15 12:20:05', '2025-12-15 12:19:09');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '作者名称',
  `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '来源',
  `cover_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '封面图片',
  `desc_txt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章描述',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `hop_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '跳转链接',
  `top` tinyint NULL DEFAULT 0,
  `category_id` int NULL DEFAULT 0 COMMENT '分类id',
  `admin_id` bigint NULL DEFAULT 0 COMMENT '管理员id',
  `status` tinyint NULL DEFAULT 0,
  `post_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 'Springboot Creator通用CMS后台系统文章示例页', 'SpringbootCreator作者', '', '', '这是一款能够让你快速搭建一个Java的Web Api应用，各类后台管理系统原型，CMS文章网站系统等网络应用。', '&lt;p&gt;Springboot Creator是一款由Java Springboot框架编写的通用CMS后台管理系统，该系统能够让你快速搭建一个Java的Web Api应用，各类后台管理系统原型，CMS文章网站系统等网络应用。&lt;/p&gt;&lt;p&gt;它使用了以下框架和插件功能：&lt;/p&gt;&lt;p&gt;&lt;b&gt;后端技术：&lt;/b&gt;&lt;/p&gt;&lt;p&gt;Springboot Web框架，MySQL 8.0数据库，Mybatis-plus数据库操作库，Freemarker视图模板引擎。&lt;/p&gt;&lt;p&gt;&lt;b&gt;前端技术：&lt;/b&gt;&lt;/p&gt;&lt;p&gt;Bootstrap5, JQuery3, Summernote富文本编辑器。&lt;/p&gt;&lt;p&gt;&lt;b&gt;核心功能：&lt;/b&gt;&lt;/p&gt;&lt;p&gt;用户管理，权限管理，文章管理，分类管理，图片管理，系统日志，&lt;/p&gt;&lt;p&gt;&lt;b&gt;功能展示：&lt;/b&gt;&lt;/p&gt;&lt;p style=\"text-align: left;\"&gt;&lt;a href=\"/uploads/attachs/2025/12/15/5961ea27-630a-44aa-bf97-061b09512979.png\" target=\"_blank\"&gt;&lt;img src=\"/uploads/attachs/2025/12/15/5961ea27-630a-44aa-bf97-061b09512979_thumb.png\"&gt;&lt;/a&gt;&lt;/p&gt;&lt;p style=\"text-align: center; \"&gt;&lt;/p&gt;&lt;div style=\"text-align: center;\"&gt;（点击查看大图）&lt;/div&gt;&lt;div style=\"text-align: left;\"&gt;&lt;br&gt;&lt;/div&gt;&lt;p&gt;&lt;/p&gt;&lt;p style=\"text-align: left;\"&gt;&lt;a href=\"/uploads/attachs/2025/12/15/6374b98b-3274-41b2-9810-aedf2188383c.png\" target=\"_blank\"&gt;&lt;img src=\"/uploads/attachs/2025/12/15/6374b98b-3274-41b2-9810-aedf2188383c_thumb.png\"&gt;&lt;/a&gt;&lt;/p&gt;&lt;p style=\"text-align: left;\"&gt;&lt;a href=\"/uploads/attachs/2025/12/15/bcda6b99-ae40-43cb-a6d6-495cef71e59d.png\" target=\"_blank\"&gt;&lt;img src=\"/uploads/attachs/2025/12/15/bcda6b99-ae40-43cb-a6d6-495cef71e59d_thumb.png\"&gt;&lt;/a&gt;&lt;/p&gt;&lt;p style=\"text-align: left;\"&gt;&lt;a href=\"/uploads/attachs/2025/12/15/6e83e995-46e0-432c-87db-d549f14bc03a.png\" target=\"_blank\"&gt;&lt;img src=\"/uploads/attachs/2025/12/15/6e83e995-46e0-432c-87db-d549f14bc03a_thumb.png\"&gt;&lt;/a&gt;&lt;/p&gt;&lt;p&gt;&lt;/p&gt;&lt;div style=\"text-align: left;\"&gt;&lt;a href=\"/uploads/attachs/2025/12/15/beef0737-7949-4d48-827c-75f34bff059b.png\" target=\"_blank\"&gt;&lt;img src=\"/uploads/attachs/2025/12/15/beef0737-7949-4d48-827c-75f34bff059b_thumb.png\"&gt;&lt;/a&gt;&lt;/div&gt;&lt;br&gt;&lt;br&gt;&lt;p&gt;&lt;/p&gt;&lt;p&gt;注意：Springboot Creator代码仅供测试学习，请勿用于生产环境，如果系统有任何问题，请自行负责。&lt;br&gt;&lt;br&gt;&lt;/p&gt;', '', 1, 1, 1, 1, '2025-12-15 10:00:00', '2025-12-15 19:19:18', '2025-12-15 15:58:35');

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `parent_id` int NULL DEFAULT 0 COMMENT '父分类id',
  `hop_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '跳转链接',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 0,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_category
-- ----------------------------
INSERT INTO `article_category` VALUES (1, '默认分类', 0, '', 0, 1, '2015-12-18 16:38:37', '2012-05-28 13:24:20');

-- ----------------------------
-- Table structure for attach
-- ----------------------------
DROP TABLE IF EXISTS `attach`;
CREATE TABLE `attach`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
  `orig_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上传前文件名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件路径',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'png|jpg|jpeg|gif',
  `article_id` bigint NULL DEFAULT 0,
  `admin_id` bigint NULL DEFAULT 0,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attach
-- ----------------------------
INSERT INTO `attach` VALUES (1, '5961ea27-630a-44aa-bf97-061b09512979.png', 'example1.png', 'uploads/attachs/2025/12/15/5961ea27-630a-44aa-bf97-061b09512979.png', 'png', 1, 1, '2025-12-15 18:06:06', '2025-12-15 16:00:24');
INSERT INTO `attach` VALUES (2, '6374b98b-3274-41b2-9810-aedf2188383c.png', 'example2.png', 'uploads/attachs/2025/12/15/6374b98b-3274-41b2-9810-aedf2188383c.png', 'png', 1, 1, '2025-12-15 16:56:04', '2025-12-15 16:00:27');
INSERT INTO `attach` VALUES (3, 'bcda6b99-ae40-43cb-a6d6-495cef71e59d.png', 'example3.png', 'uploads/attachs/2025/12/15/bcda6b99-ae40-43cb-a6d6-495cef71e59d.png', 'png', 1, 1, '2025-12-15 16:56:10', '2025-12-15 16:00:29');
INSERT INTO `attach` VALUES (4, '6e83e995-46e0-432c-87db-d549f14bc03a.png', 'example4.png', 'uploads/attachs/2025/12/15/6e83e995-46e0-432c-87db-d549f14bc03a.png', 'png', 1, 1, '2025-12-15 16:56:16', '2025-12-15 16:00:33');
INSERT INTO `attach` VALUES (5, 'beef0737-7949-4d48-827c-75f34bff059b.png', 'example5.png', 'uploads/attachs/2025/12/15/beef0737-7949-4d48-827c-75f34bff059b.png', 'png', 1, 1, '2025-12-15 16:56:21', '2025-12-15 16:00:35');

-- ----------------------------
-- Table structure for syslog
-- ----------------------------
DROP TABLE IF EXISTS `syslog`;
CREATE TABLE `syslog`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL COMMENT '类型',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志内容',
  `admin_id` bigint NOT NULL DEFAULT 0 COMMENT '管理员ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of syslog
-- ----------------------------
INSERT INTO `syslog` VALUES (1, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 16:03:23', '2025-12-15 15:22:46');
INSERT INTO `syslog` VALUES (2, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 16:03:24', '2025-12-15 15:24:01');
INSERT INTO `syslog` VALUES (3, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 16:03:25', '2025-12-15 15:24:31');
INSERT INTO `syslog` VALUES (4, 1, '登录系统', '登录系统', 2, 'test', '0:0:0:0:0:0:0:1', '2025-12-15 16:03:26', '2025-12-15 15:45:59');
INSERT INTO `syslog` VALUES (5, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 16:03:35', '2025-12-15 15:49:04');
INSERT INTO `syslog` VALUES (6, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 16:54:55', '2025-12-15 16:54:55');
INSERT INTO `syslog` VALUES (7, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 17:03:53', '2025-12-15 17:03:52');
INSERT INTO `syslog` VALUES (8, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 17:50:06', '2025-12-15 17:50:06');
INSERT INTO `syslog` VALUES (9, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 18:05:57', '2025-12-15 18:05:56');
INSERT INTO `syslog` VALUES (10, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 18:55:40', '2025-12-15 18:55:39');
INSERT INTO `syslog` VALUES (11, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 19:09:18', '2025-12-15 19:09:17');
INSERT INTO `syslog` VALUES (12, 1, '登录系统', '登录系统', 1, 'admin', '0:0:0:0:0:0:0:1', '2025-12-15 19:19:03', '2025-12-15 19:19:03');

SET FOREIGN_KEY_CHECKS = 1;
