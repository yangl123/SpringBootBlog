/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50162
Source Host           : localhost:3306
Source Database       : db_blogs

Target Server Type    : MYSQL
Target Server Version : 50162
File Encoding         : 65001

Date: 2017-12-11 23:53:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `categoryId` int(50) DEFAULT NULL,
  `content` longtext COLLATE utf8_bin,
  `subtime` datetime DEFAULT NULL,
  `tages` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `thumbUrl` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `viewCount` int(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('16', 'test索引', '7', 0x3C703EE4BDA0E5A5BDEFBC8CE79599E68385EFBC8CE4BDA0E5A5BDEFBC8CE585BBE4BA863C2F703E3C703EE68891E698AFE4B880E4B8AAE5A4A7E59D8FE89B8B3C2F703E3C703EE4BDA0E5A5BD3C2F703E, '2017-12-11 23:26:14', 'java,php,', '/CommonFrame/images/97730734131042728cab51b925051d15.jpg', '7');

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES ('7', '心情');
INSERT INTO `tb_category` VALUES ('8', 'ing干');
INSERT INTO `tb_category` VALUES ('9', '技术');
INSERT INTO `tb_category` VALUES ('10', '博客');
INSERT INTO `tb_category` VALUES ('11', '日记');
INSERT INTO `tb_category` VALUES ('12', '卡的');
INSERT INTO `tb_category` VALUES ('13', '隐私');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  `userName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'eWFuZyxsaXUsY29t', 'yangl', '乐乐', null, null);
INSERT INTO `tb_user` VALUES ('2', 'eWFuZyxsaXUsY29t', 'liuq', '宝宝', null, null);
