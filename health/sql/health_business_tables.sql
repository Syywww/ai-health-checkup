-- ============================================================================
-- 体检预约管理系统 · 业务表建表脚本
-- 说明：ry_20260417.sql 只含若依系统表（sys_*/gen_*），以下 12 张业务表缺失，
--       补齐后系统才能正常启动与演示。与 ry_20260417.sql 顺序执行即可。
-- 执行：mysql -u root -p 库名 < health_business_tables.sql
-- 字符集/引擎与若依系统表保持一致：utf8mb4 / InnoDB。
-- ============================================================================

USE `health`;

-- ----------------------------------------------------------------------------
-- 会员表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `name`        varchar(50)  DEFAULT NULL COMMENT '会员姓名',
  `gender`      varchar(10)  DEFAULT NULL COMMENT '性别(男/女)',
  `birthday`    date         DEFAULT NULL COMMENT '出生日期',
  `phone`       varchar(20)  DEFAULT NULL COMMENT '手机号',
  `id_card`     varchar(20)  DEFAULT NULL COMMENT '身份证号',
  `address`     varchar(255) DEFAULT NULL COMMENT '地址',
  `status`      varchar(10)  DEFAULT '0' COMMENT '状态(0正常 1停用)',
  `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_member_phone`   (`phone`),
  KEY `idx_member_id_card` (`id_card`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='会员信息表';

-- ----------------------------------------------------------------------------
-- 体检记录表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `examination_record`;
CREATE TABLE `examination_record` (
  `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `member_id`       bigint(20)  DEFAULT NULL COMMENT '会员ID',
  `setmeal_id`      bigint(20)  DEFAULT NULL COMMENT '套餐ID',
  `examination_date` date        DEFAULT NULL COMMENT '体检日期',
  `doctor`          varchar(50) DEFAULT NULL COMMENT '体检医生',
  `result_summary`  varchar(2000) DEFAULT NULL COMMENT '结果小结',
  `status`          varchar(10) DEFAULT '0' COMMENT '状态(0待出 1已出 2异常)',
  `attachment`      varchar(255) DEFAULT NULL COMMENT '报告附件',
  `create_by`       varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`     datetime    DEFAULT NULL COMMENT '创建时间',
  `update_by`       varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`     datetime    DEFAULT NULL COMMENT '更新时间',
  `remark`          varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_exam_record_member` (`member_id`),
  KEY `idx_exam_record_setmeal` (`setmeal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='体检记录表';

-- ----------------------------------------------------------------------------
-- 体检明细表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `examination_detail`;
CREATE TABLE `examination_detail` (
  `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `record_id`       bigint(20) DEFAULT NULL COMMENT '体检记录ID',
  `checkitem_id`    bigint(20) DEFAULT NULL COMMENT '检查项ID',
  `result_value`    varchar(255) DEFAULT NULL COMMENT '结果值',
  `unit`            varchar(50) DEFAULT NULL COMMENT '单位',
  `reference_range` varchar(255) DEFAULT NULL COMMENT '参考范围',
  `status`          varchar(10) DEFAULT '0' COMMENT '状态',
  `create_by`       varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`     datetime   DEFAULT NULL COMMENT '创建时间',
  `update_by`       varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`     datetime   DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_exam_detail_record`   (`record_id`),
  KEY `idx_exam_detail_checkitem` (`checkitem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='体检明细表';

-- ----------------------------------------------------------------------------
-- 预约记录表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `member_id`        bigint(20) DEFAULT NULL COMMENT '会员ID',
  `setmeal_id`       bigint(20) DEFAULT NULL COMMENT '套餐ID',
  `appointment_date` date       DEFAULT NULL COMMENT '预约日期',
  `appointment_time` varchar(50) DEFAULT NULL COMMENT '预约时段',
  `status`           varchar(10) DEFAULT '0' COMMENT '状态(0待确认 1已确认 2已完成 3已取消)',
  `source`           varchar(20) DEFAULT NULL COMMENT '预约来源',
  `remark`           varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by`        varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time`      datetime   DEFAULT NULL COMMENT '创建时间',
  `update_by`        varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time`      datetime   DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_appointment_member` (`member_id`),
  KEY `idx_appointment_date` (`appointment_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='预约记录表';

-- ----------------------------------------------------------------------------
-- 预约设置表（放号）
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `appointment_setting`;
CREATE TABLE `appointment_setting` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设置ID',
  `setting_date` date      DEFAULT NULL COMMENT '放号日期',
  `max_count`   bigint(20) DEFAULT NULL COMMENT '当日可预约数',
  `reserved_count` bigint(20) DEFAULT 0 COMMENT '已预约人数',
  `create_by`   varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime   DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime   DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_setting_date` (`setting_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='预约设置表';

-- ----------------------------------------------------------------------------
-- 检查组表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `t_checkgroup`;
CREATE TABLE `t_checkgroup` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '检查组ID',
  `code`        varchar(50)  DEFAULT NULL COMMENT '项目编码',
  `name`        varchar(100) DEFAULT NULL COMMENT '项目名称',
  `help_code`   varchar(50)  DEFAULT NULL COMMENT '助记码',
  `sex`         varchar(10)  DEFAULT NULL COMMENT '适用性别(男/女/通用)',
  `attention`   varchar(500) DEFAULT NULL COMMENT '注意事项',
  `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_checkgroup_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='检查组表';

-- ----------------------------------------------------------------------------
-- 检查项表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `t_checkitem`;
CREATE TABLE `t_checkitem` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '检查项ID',
  `code`        varchar(50)  DEFAULT NULL COMMENT '项目编码',
  `name`        varchar(100) DEFAULT NULL COMMENT '项目名称',
  `sex`         varchar(10)  DEFAULT NULL COMMENT '适用性别',
  `age`         varchar(50)  DEFAULT NULL COMMENT '适用年龄段(如18-60)',
  `price`       bigint(20)   DEFAULT 0 COMMENT '价格',
  `type`        varchar(50)  DEFAULT NULL COMMENT '类型',
  `attention`   varchar(500) DEFAULT NULL COMMENT '注意事项',
  `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_checkitem_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='检查项表';

-- ----------------------------------------------------------------------------
-- 套餐表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `t_setmeal`;
CREATE TABLE `t_setmeal` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `name`        varchar(100) DEFAULT NULL COMMENT '套餐名称',
  `code`        varchar(50)  DEFAULT NULL COMMENT '套餐编码',
  `help_code`   varchar(50)  DEFAULT NULL COMMENT '助记码',
  `sex`         varchar(10)  DEFAULT NULL COMMENT '适用性别',
  `age`         varchar(50)  DEFAULT NULL COMMENT '适用年龄段',
  `price`       bigint(20)   DEFAULT 0 COMMENT '价格',
  `attention`   varchar(500) DEFAULT NULL COMMENT '注意事项',
  `img`         varchar(255) DEFAULT NULL COMMENT '套餐图片',
  `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_setmeal_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='体检套餐表';

-- ----------------------------------------------------------------------------
-- 检查组-检查项关联表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `t_checkgroup_checkitem`;
CREATE TABLE `t_checkgroup_checkitem` (
  `checkgroup_id` bigint(20) NOT NULL COMMENT '检查组ID',
  `checkitem_id`  bigint(20) NOT NULL COMMENT '检查项ID',
  PRIMARY KEY (`checkgroup_id`, `checkitem_id`),
  KEY `idx_cg_ci_checkitem` (`checkitem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检查组与检查项关联表';

-- ----------------------------------------------------------------------------
-- 套餐-检查组关联表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `t_setmeal_checkgroup`;
CREATE TABLE `t_setmeal_checkgroup` (
  `setmeal_id`   bigint(20) NOT NULL COMMENT '套餐ID',
  `checkgroup_id` bigint(20) NOT NULL COMMENT '检查组ID',
  PRIMARY KEY (`setmeal_id`, `checkgroup_id`),
  KEY `idx_sm_cg_checkgroup` (`checkgroup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐与检查组关联表';

-- ----------------------------------------------------------------------------
-- AI 会话表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `ai_conversation`;
CREATE TABLE `ai_conversation` (
  `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `title`       varchar(100) DEFAULT NULL COMMENT '会话标题',
  `user_id`     bigint(20)  DEFAULT NULL COMMENT '用户ID',
  `model`       varchar(100) DEFAULT NULL COMMENT '使用的模型',
  `status`      int(1)      DEFAULT 0 COMMENT '状态(0正常 1删除)',
  `create_by`   varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_ai_conv_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='AI 对话会话表';

-- ----------------------------------------------------------------------------
-- AI 消息表
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS `ai_message`;
CREATE TABLE `ai_message` (
  `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `conversation_id` bigint(20)  DEFAULT NULL COMMENT '会话ID',
  `role`            varchar(20) DEFAULT NULL COMMENT '角色(user/assistant)',
  `content`         text        COMMENT '消息内容',
  `tokens`          int(11)     DEFAULT 0 COMMENT 'Token数',
  `create_time`     datetime    DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_ai_msg_conv` (`conversation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='AI 对话消息表';

-- ============================================================================
-- 提示：本文件为全新建库脚本，已包含最新列结构。
-- 若数据库按旧版本已建过库，请另执行 health_upgrade_appointment_setting.sql
-- （为 appointment_setting 增加 reserved_count 列），无需重复执行本文件。
-- ============================================================================