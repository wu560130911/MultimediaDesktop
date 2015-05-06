/*
Navicat MySQL Data Transfer

Source Server         : mysql.wms.studio
Source Server Version : 50528
Source Host           : mysql.wms.studio:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2015-05-06 14:36:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_application`
-- ----------------------------
DROP TABLE IF EXISTS `tb_application`;
CREATE TABLE `tb_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `auditing_statu` bit(1) NOT NULL,
  `description` longtext,
  `icon_cls` varchar(50) DEFAULT NULL,
  `module` varchar(200) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `role` varchar(11) DEFAULT NULL,
  `tip` varchar(250) DEFAULT NULL,
  `type_group` varchar(255) DEFAULT NULL,
  `use_count` bigint(20) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9j0su3f8d18jo7upntdbamq08` (`user_id`),
  CONSTRAINT `FK_9j0su3f8d18jo7upntdbamq08` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_application
-- ----------------------------
INSERT INTO `tb_application` VALUES ('1', '2015-04-09 14:07:39', '', '提供各种应用，可以管理自己的应用和上传开发的应用', 'appManager_icon', 'Wdesktop.app.bootstrap.AppManagerBootStrap', '应用市场', '用户', '上传、安装、管理应用', '管理', '4', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('2', '2015-04-09 14:14:30', '', '提供各种应用，可以管理自己的应用和上传开发的应用', 'appManager_icon', 'Wdesktop.app.bootstrap.AppManagerBootStrap', '应用市场', '用户', '上传、安装、管理应用', '管理', '0', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('3', '2015-04-11 17:19:37', '', '简单的系统监控应用，使用各种报表', 'cpu-shortcut', 'Wdesktop.desktop.widget.SystemStatus', '系统状态', '用户', '监控系统状态', '管理', '3', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('4', '2015-04-11 17:30:54', '', '提供壁纸上传、壁纸设置、主题设置功能。\n包含海量壁纸，赶快安装，上传你的壁纸，找到属于你的壁纸吧', 'wallpaper_cls', 'Wdesktop.module.WallpaperModule', '壁纸管理', '用户', '壁纸设置和上传', '管理', '2', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('5', '2015-04-13 10:46:52', '', '音乐播放器，播放您想听的音乐', 'music_main', 'Wdesktop.desktop.widget.AudioWindow', '音乐播放器', '用户', '播放你想听的音乐', '音乐', '4', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('6', '2015-04-13 11:01:46', '', '播放您想看的视频，需要您的浏览器支持', 'video_play_main', 'Wdesktop.desktop.widget.VideoWindow', '视频播放器', '用户', '播放您想看的视频', '音乐', '3', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('7', '2015-04-13 16:46:33', '', '列出系统的所有电影，查找您想要的电影', 'video_list_desktop', 'Wdesktop.app.bootstrap.MovieListAppBootStrap', '电影列表', '用户', '电影列表，查找电影', '视频', '3', '0.01', 'wu560130911');
INSERT INTO `tb_application` VALUES ('8', '2015-04-13 16:48:29', '', '上传您的电影，与大家共同分享', 'video_add_desktop', 'Wdesktop.app.bootstrap.MovieAddAppBootStrap', '电影上传', '用户', '上传您的电影，与大家分享', '视频', '2', '0.01', 'wu560130911');
INSERT INTO `tb_application` VALUES ('9', '2015-04-16 14:16:06', '', '电影审核，审核用户上传的电影，本应用为管理员应用', 'check_movie', 'Wdesktop.app.bootstrap.CheckMovieListAppBootStrap', '电影审核', '管理员', '审核用户上传的电影', '视频', '1', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('10', '2015-04-17 15:25:39', '', '分享音乐，快来上传您的音乐吧', 'music_add_desktop', 'Wdesktop.app.bootstrap.MusicAppBootStrap', '音乐上传', '用户', '快来上传您的音乐吧', '音乐', '1', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('11', '2015-04-17 17:52:16', '', '寻找属于你的音乐', 'music_list_desktop', 'Wdesktop.app.bootstrap.MusicListAppBootStrap', '音乐列表', '用户', '寻找属于你的音乐', '音乐', '2', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('12', '2015-04-18 11:00:12', '', '审核用户分享的音乐，管理员应用', 'check_music_desktop', 'Wdesktop.app.bootstrap.CheckMusicListAppBootStrap', '音乐审核', '管理员', '审核音乐', '音乐', '1', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('13', '2015-04-18 11:28:29', '', '管理用户信息、用户日志等', 'user_manage_desktop', 'Wdesktop.app.bootstrap.UserManagerBootStrap', '个人管理', '用户', '管理个人信息', '管理', '1', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('14', '2015-04-24 10:31:10', '', '查看系统的日志', 'system_log_desktop', 'Wdesktop.app.bootstrap.SystemLogBootStrap', '系统日志', '管理员', '查看系统的日志', '管理', '1', '0.0.1', 'wu560130911');
INSERT INTO `tb_application` VALUES ('15', '2015-04-26 09:28:44', '', '音乐播放器（新版）', 'music_main', 'Wdesktop.desktop.widget.AudioPlayerWindow', '音乐播放器（新版）', '用户', '音乐播放器（新版）', '音乐', '1', '1.0.0', 'wu560130911');

-- ----------------------------
-- Table structure for `tb_buildin_application`
-- ----------------------------
DROP TABLE IF EXISTS `tb_buildin_application`;
CREATE TABLE `tb_buildin_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `uninstall` bit(1) DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fi2twwg5tvxbgi8rf821xndwb` (`application_id`),
  CONSTRAINT `FK_fi2twwg5tvxbgi8rf821xndwb` FOREIGN KEY (`application_id`) REFERENCES `tb_application` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_buildin_application
-- ----------------------------
INSERT INTO `tb_buildin_application` VALUES ('1', '2015-04-17 10:44:51', '', '3');
INSERT INTO `tb_buildin_application` VALUES ('2', '2015-04-17 10:44:56', '', '4');
INSERT INTO `tb_buildin_application` VALUES ('3', '2015-04-17 10:45:01', '', '5');
INSERT INTO `tb_buildin_application` VALUES ('4', '2015-04-17 10:45:01', '', '6');
INSERT INTO `tb_buildin_application` VALUES ('5', '2015-04-17 10:45:38', '', '1');

-- ----------------------------
-- Table structure for `tb_credit`
-- ----------------------------
DROP TABLE IF EXISTS `tb_credit`;
CREATE TABLE `tb_credit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `change_time` datetime DEFAULT NULL,
  `credit_num` int(11) NOT NULL,
  `credit_type` varchar(255) DEFAULT NULL,
  `descriptions` longtext NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5u2n86ga0nad8s5w9ny8d83jl` (`user_id`),
  CONSTRAINT `FK_5u2n86ga0nad8s5w9ny8d83jl` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_credit
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_login_ip`
-- ----------------------------
DROP TABLE IF EXISTS `tb_login_ip`;
CREATE TABLE `tb_login_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(255) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `login_type` varchar(255) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iibde02yus0bvhaolt4h3n76r` (`user_id`),
  CONSTRAINT `FK_iibde02yus0bvhaolt4h3n76r` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_login_ip
-- ----------------------------
INSERT INTO `tb_login_ip` VALUES ('1', '127.0.0.1', '2015-04-08 16:11:18', '注册', 'wu560130911');

-- ----------------------------
-- Table structure for `tb_moive`
-- ----------------------------
DROP TABLE IF EXISTS `tb_moive`;
CREATE TABLE `tb_moive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actor` varchar(100) NOT NULL,
  `author` varchar(50) NOT NULL,
  `description` longtext,
  `duration` int(11) NOT NULL,
  `filename` varchar(300) DEFAULT NULL,
  `madetime` date DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `auditing_statu` bit(1) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9f0q2jsxekvjo7rxu88v5uw0p` (`user_id`),
  CONSTRAINT `FK_9f0q2jsxekvjo7rxu88v5uw0p` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_moive
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_movie_playlist`
-- ----------------------------
DROP TABLE IF EXISTS `tb_movie_playlist`;
CREATE TABLE `tb_movie_playlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `score` double NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `movie_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4lsx4rvc8rs7lq9pnv265l5qc` (`user_id`),
  KEY `FK_kbxt9tfa1dejb17k2o2omrn2m` (`movie_id`),
  CONSTRAINT `FK_4lsx4rvc8rs7lq9pnv265l5qc` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
  CONSTRAINT `FK_kbxt9tfa1dejb17k2o2omrn2m` FOREIGN KEY (`movie_id`) REFERENCES `tb_moive` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_movie_playlist
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_music`
-- ----------------------------
DROP TABLE IF EXISTS `tb_music`;
CREATE TABLE `tb_music` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `duration` int(11) NOT NULL,
  `singer` varchar(50) NOT NULL,
  `size` bigint(20) NOT NULL,
  `time` datetime DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `year` int(11) NOT NULL,
  `auditing_statu` bit(1) DEFAULT NULL,
  `filename` varchar(300) NOT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jm53pg9qg1uwvmvlx4i4mqrg6` (`user_id`),
  CONSTRAINT `FK_jm53pg9qg1uwvmvlx4i4mqrg6` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_music
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_music_playlist`
-- ----------------------------
DROP TABLE IF EXISTS `tb_music_playlist`;
CREATE TABLE `tb_music_playlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `score` double NOT NULL,
  `music_id` bigint(20) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cvu9raknisydry4muxyymj06h` (`music_id`),
  KEY `FK_ge5lrues7bd088klaw54l8n5r` (`user_id`),
  CONSTRAINT `FK_cvu9raknisydry4muxyymj06h` FOREIGN KEY (`music_id`) REFERENCES `tb_music` (`id`),
  CONSTRAINT `FK_ge5lrues7bd088klaw54l8n5r` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_music_playlist
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_notebook`
-- ----------------------------
DROP TABLE IF EXISTS `tb_notebook`;
CREATE TABLE `tb_notebook` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `mood` varchar(30) NOT NULL,
  `notedate` date DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  `weather` varchar(30) NOT NULL,
  `writetime` datetime DEFAULT NULL,
  `uid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_shks93lkj36jao4auvg5hpglc` (`uid`),
  CONSTRAINT `FK_shks93lkj36jao4auvg5hpglc` FOREIGN KEY (`uid`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_notebook
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_system_log`
-- ----------------------------
DROP TABLE IF EXISTS `tb_system_log`;
CREATE TABLE `tb_system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_path` longtext,
  `lever` varchar(10) DEFAULT NULL,
  `log_date_time` datetime DEFAULT NULL,
  `log_from` varchar(200) DEFAULT NULL,
  `message` longtext,
  `thread_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105258 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_system_log
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` varchar(20) NOT NULL,
  `credit` bigint(20) NOT NULL,
  `disable` bit(1) NOT NULL,
  `disable_reason` varchar(255) DEFAULT NULL,
  `email` varchar(30) NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `name` varchar(10) NOT NULL,
  `password` varchar(80) NOT NULL,
  `register_time` datetime DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `v_status` bit(1) NOT NULL,
  `wallpaper_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_770ne3o1tqedboka2b0hg1dim` (`wallpaper_id`),
  CONSTRAINT `FK_770ne3o1tqedboka2b0hg1dim` FOREIGN KEY (`wallpaper_id`) REFERENCES `tb_wallpaper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('wu560130911', '0', '', '正常', '560130911@163.com', '2015-05-06 13:53:20', '管理员', '0c098745003cab92740ff3c9baa4a4ad0c601fce', '2015-04-08 16:11:18', '管理员', '3aea7571f06e679f', '', '1');

-- ----------------------------
-- Table structure for `tb_user_application`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_application`;
CREATE TABLE `tb_user_application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `score` double NOT NULL,
  `app_id` bigint(20) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `desktop_icon` bit(1) DEFAULT NULL,
  `quick_start` bit(1) DEFAULT NULL,
  `start_menu` bit(1) DEFAULT NULL,
  `uninstall` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b7yex4b9fipdnh50h87xlxhfy` (`app_id`),
  KEY `FK_m1pyyqj8cxe7ny04d3h0fcfye` (`user_id`),
  CONSTRAINT `FK_b7yex4b9fipdnh50h87xlxhfy` FOREIGN KEY (`app_id`) REFERENCES `tb_application` (`id`),
  CONSTRAINT `FK_m1pyyqj8cxe7ny04d3h0fcfye` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user_application
-- ----------------------------
INSERT INTO `tb_user_application` VALUES ('1', '2015-04-11 13:51:37', '0', '1', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('10', '2015-04-13 17:06:28', '0', '7', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('14', '2015-04-16 14:21:31', '0', '9', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('18', '2015-04-16 20:27:28', '0', '8', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('24', '2015-04-17 17:35:47', '0', '10', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('25', '2015-04-17 17:35:56', '0', '5', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('26', '2015-04-17 17:52:33', '0', '11', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('27', '2015-04-18 11:00:55', '0', '12', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('28', '2015-04-18 11:29:26', '0', '13', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('29', '2015-04-24 10:32:04', '0', '14', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('30', '2015-04-24 11:16:36', '0', '3', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('31', '2015-04-24 11:16:42', '0', '4', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('32', '2015-04-24 11:16:55', '0', '6', 'wu560130911', '', '', '', '');
INSERT INTO `tb_user_application` VALUES ('33', '2015-04-26 09:28:59', '0', '15', 'wu560130911', '', '', '', '');

-- ----------------------------
-- Table structure for `tb_wallpaper`
-- ----------------------------
DROP TABLE IF EXISTS `tb_wallpaper`;
CREATE TABLE `tb_wallpaper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `add_date` datetime DEFAULT NULL,
  `is_default` bit(1) NOT NULL,
  `path` varchar(250) DEFAULT NULL,
  `wallpaper_type` varchar(255) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_kjm681u1t8jqecrg3cwcp63s2` (`user_id`),
  CONSTRAINT `FK_kjm681u1t8jqecrg3cwcp63s2` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_wallpaper
-- ----------------------------
INSERT INTO `tb_wallpaper` VALUES ('1', '2015-04-28 11:13:05', '', 'resources/resources/wallpapers/fresh-morning.jpg', '风景', 'wu560130911');
