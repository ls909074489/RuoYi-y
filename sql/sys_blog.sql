/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50714
Source Host           : 127.0.0.1:3306
Source Database       : ry-y

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2019-05-05 14:09:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_blog
-- ----------------------------
DROP TABLE IF EXISTS `sys_blog`;
CREATE TABLE `sys_blog` (
  `blog_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `blog_title` varchar(50) NOT NULL COMMENT '公告标题',
  `blog_type` char(2) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `blog_content` varchar(500) NOT NULL COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `view_count` int(10) DEFAULT NULL,
  `reply_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_blog
-- ----------------------------
INSERT INTO `sys_blog` VALUES ('1', '1111111111111111', '1', '<p>11111111111111111111111111</p>', '0', 'admin', '2019-02-13 11:43:47', '', null, '', '0', '0');
INSERT INTO `sys_blog` VALUES ('2', '2222', '1', '<p>2222222222222222222222</p>', '0', 'admin', '2019-02-13 11:44:07', '', null, '', '0', '0');

-- ----------------------------
-- Table structure for sys_blog_reply
-- ----------------------------
DROP TABLE IF EXISTS `sys_blog_reply`;
CREATE TABLE `sys_blog_reply` (
  `blog_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `reply_content` varchar(500) NOT NULL COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `reply_id` int(4) NOT NULL,
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_blog_reply
-- ----------------------------
