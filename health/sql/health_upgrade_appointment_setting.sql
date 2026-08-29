-- ============================================================================
-- 体检预约管理系统 · 增量升级脚本
-- 适用场景：数据库已按旧版 health_business_tables.sql 建库，需升级到最新结构。
-- 本次变更：appointment_setting 增加"已预约人数"列（预约设置日历化的基础）。
-- 执行：mysql -u root -p 库名 < health_upgrade_appointment_setting.sql
-- ============================================================================

USE `health`;

ALTER TABLE `appointment_setting`
  ADD COLUMN `reserved_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '已预约人数' AFTER `max_count`;