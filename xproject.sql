/*
Navicat MySQL Data Transfer

Source Server         : cmp-172.16.39.153
Source Server Version : 50618
Source Host           : 172.16.39.153:3306
Source Database       : cloudpm

Target Server Type    : MYSQL
Target Server Version : 50618
File Encoding         : 65001

Date: 2017-08-24 18:19:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_resource
-- ----------------------------
DROP TABLE IF EXISTS `admin_resource`;
CREATE TABLE `admin_resource` (
  `RESOURCE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `RESOURCE_NAME` varchar(100) NOT NULL COMMENT '资源名称',
  `PARENT_RESOURCE_ID` bigint(20) NOT NULL COMMENT '父级资源id',
  `PERMISSION_EXPRESSION` varchar(100) DEFAULT '' COMMENT '资源代码',
  `RESOURCE_TYPE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '资源类型：0-系统资源,1-普通资源',
  `RESOURCE_ICON` varchar(50) DEFAULT NULL COMMENT '资源菜单的ICON(font-awesome类名)',
  `ACTION_TYPE` tinyint(4) NOT NULL DEFAULT '1' COMMENT '功能类型：0-菜单，1-按钮',
  `RESOURCE_URL` varchar(500) DEFAULT '' COMMENT '资源url',
  `SIBLINGS_INDEX` int(11) NOT NULL COMMENT '兄弟节点间的排序号,asc排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `CREATE_BY` bigint(20) NOT NULL COMMENT '创建者,用户表id',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '更新者,用户表的id',
  PRIMARY KEY (`RESOURCE_ID`),
  UNIQUE KEY `uk_admin_resource_name` (`RESOURCE_NAME`,`PARENT_RESOURCE_ID`),
  UNIQUE KEY `uk_admin_permission_expression` (`PERMISSION_EXPRESSION`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='后台管理-资源表,如菜单';

-- ----------------------------
-- Records of admin_resource
-- ----------------------------
INSERT INTO `admin_resource` VALUES ('1', '后台管理系统', '0', null, '0', 'fa-circle-o', '0', null, '1', '2015-06-09 21:21:07', '1', null, null);
INSERT INTO `admin_resource` VALUES ('2', '系统管理', '1', null, '0', 'fa-laptop', '0', null, '1', '2015-06-09 23:39:33', '1', null, null);
INSERT INTO `admin_resource` VALUES ('3', '用户管理', '2', null, '0', 'fa-user-circle-o', '0', '/admin/user/index.html', '1', '2015-06-10 00:05:13', '1', null, null);
INSERT INTO `admin_resource` VALUES ('4', '角色管理', '2', null, '0', 'fa-id-card-o', '0', '/admin/role/index.html', '2', '2015-06-10 00:13:19', '1', '2017-04-27 19:51:47', '1');
INSERT INTO `admin_resource` VALUES ('5', '资源管理', '2', null, '0', 'fa-list-alt', '0', '/admin/resource/index.html', '3', '2015-06-10 10:07:01', '1', '2017-08-16 17:46:01', '1');
INSERT INTO `admin_resource` VALUES ('6', '系统设置', '2', null, '0', 'fa-gear', '0', null, '4', '2016-03-05 15:26:33', '1', null, null);
INSERT INTO `admin_resource` VALUES ('7', '缓存管理', '6', null, '0', 'fa-circle-o', '0', '/admin/syssettings/cachelist', '1', '2016-03-05 15:28:13', '1', null, null);
INSERT INTO `admin_resource` VALUES ('8', '参数配置', '6', null, '0', 'fa-circle-o', '0', '/admin/syssettings/paramlist', '2', '2016-03-05 15:29:48', '1', null, null);

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `ROLE_CODE` varchar(100) NOT NULL COMMENT '角色代码,由字母、下划线组成',
  `ROLE_TYPE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '角色类型：0-系统角色,1-普通角色',
  `DESCRIPTION` varchar(200) NOT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `CREATE_BY` bigint(20) NOT NULL COMMENT '创建者,用户表的id',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最近更新时间',
  `UPDATE_BY` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE KEY `ROLE_NAME` (`ROLE_NAME`),
  UNIQUE KEY `ROLE_CODE` (`ROLE_CODE`),
  UNIQUE KEY `uk_admin_role_name` (`ROLE_NAME`),
  UNIQUE KEY `uk_admin_role_code` (`ROLE_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='后台管理-角色表';

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '系统管理员角色', 'system_admin', '0', '系统管理员角色', '2016-03-21 12:30:09', '1', null, null);

-- ----------------------------
-- Table structure for admin_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_resource`;
CREATE TABLE `admin_role_resource` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色id',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源id',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `CREATE_BY` bigint(20) NOT NULL COMMENT '创建者,用户表id',
  PRIMARY KEY (`ROLE_ID`,`RESOURCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理-角色.资源关系表';

-- ----------------------------
-- Records of admin_role_resource
-- ----------------------------
INSERT INTO `admin_role_resource` VALUES ('1', '1', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '2', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '3', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '4', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '5', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '6', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '7', '2016-03-17 14:08:45', '1');
INSERT INTO `admin_role_resource` VALUES ('1', '8', '2016-03-17 14:08:45', '1');

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `USER_NAME` varchar(32) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(32) NOT NULL COMMENT '密码',
  `USER_TYPE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型：0-系统用户,1-普通用户',
  `REAL_NAME` varchar(32) NOT NULL COMMENT '昵称',
  `MOBILE_PHONE` varchar(11) NOT NULL COMMENT '手机号码',
  `EMAIL` varchar(100) NOT NULL COMMENT '电子邮箱',
  `STATUS` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户状态:0-禁用,1-启用',
  `USER_ICON` varchar(255) DEFAULT '/resources/images/default-user-icon.png' COMMENT '用户头像',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `CREATE_BY` bigint(20) NOT NULL COMMENT '创建者,用户表的id',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '更新者,用户表的id',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最后登录时间',
  `LOGIN_TIMES` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `uk_admin_user_name` (`USER_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='后台管理-用户表';

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('1', 'admin', 'fb67cbc5e4eed4d69edde797e15ea16e', '0', '系统管理员', '15112345678', 'xadmin@qq.com', '1', '/img/admin/usericon/75cafd8717b74726bd79c224c73d74d5.png', '2016-02-23 13:47:49', '1', '2017-08-16 16:57:55', '1', '2017-08-24 18:18:55', '200');

-- ----------------------------
-- Table structure for admin_user_access_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_access_log`;
CREATE TABLE `admin_user_access_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `title` varchar(100) NOT NULL COMMENT '日志标题',
  `uri` varchar(512) NOT NULL COMMENT '请求URI',
  `method` varchar(10) NOT NULL COMMENT '请求方法:GET/POST/PUT/DELETE/INPUT',
  `request_header` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `request_contenttype` varchar(50) DEFAULT NULL,
  `request_parameter` varchar(2000) DEFAULT NULL COMMENT '请求参数',
  `access_user_id` bigint(20) NOT NULL COMMENT '操作用户ID',
  `access_time` datetime NOT NULL COMMENT '操作时间',
  `client_ipaddr` varchar(50) DEFAULT NULL COMMENT '客户端IP地址',
  `server_ipaddr` varchar(50) DEFAULT NULL COMMENT '访问的服务器IP:端口号',
  `process_time1` bigint(20) DEFAULT NULL COMMENT '控制器方法的执行时长(毫秒)',
  `process_time2` bigint(20) DEFAULT NULL COMMENT '控制器方法执行完毕到页面渲染时长(毫秒)',
  `logging_completed` tinyint(1) NOT NULL COMMENT '日志记录是否结束,1-是0-否',
  `asyn_request` tinyint(1) NOT NULL COMMENT '请求是否是异步的,1-是0-否',
  `response_contenttype` varchar(50) DEFAULT NULL,
  `response_result` text,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_user_access_log
-- ----------------------------
INSERT INTO `admin_user_access_log` VALUES ('21', '用户登录', '/xproject-admin/login/submit', 'POST', null, 'application/json;charset=UTF-8', '{\"parameter\":{},\"body\":{\"userName\":\"xadmin\"}}', '1', '2017-05-22 14:41:53', '172.16.18.137', '127.0.0.1:8081', '1058', '42', '1', '1', 'application/json;charset=UTF-8', '{\"success\":true,\"code\":null,\"message\":\"登录成功!\",\"data\":null}', '2017-05-22 14:41:54');
INSERT INTO `admin_user_access_log` VALUES ('22', '用户登录', '/xproject-admin/login/submit', 'POST', null, 'application/json;charset=UTF-8', '{\"parameter\":{},\"body\":{\"userName\":\"xadmin\"}}', '1', '2017-05-22 14:42:23', '172.16.18.137', '127.0.0.1:8081', '108', '7', '1', '1', 'application/json;charset=UTF-8', '{\"success\":true,\"code\":null,\"message\":\"登录成功!\",\"data\":null}', '2017-05-22 14:42:23');
INSERT INTO `admin_user_access_log` VALUES ('23', '用户登录', '/xproject-admin/login/submit', 'POST', null, 'application/json;charset=utf-8', '{\"parameter\":{},\"body\":{\"userName\":\"xadmin\"}}', '1', '2017-05-22 14:42:59', '172.16.18.137', '127.0.0.1:8081', '82', '13', '1', '1', 'application/json;charset=UTF-8', '{\"success\":true,\"code\":null,\"message\":\"登录成功!\",\"data\":null}', '2017-05-22 14:42:59');

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户id',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色id',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `CREATE_BY` bigint(20) NOT NULL COMMENT '创建者,用户表id',
  PRIMARY KEY (`USER_ID`,`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理-用户角色关系表';

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('1', '1', '2016-02-29 22:12:07', '1');
