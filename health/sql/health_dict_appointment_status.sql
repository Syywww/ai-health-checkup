-- ============================================================================
-- 体检预约管理系统 · 预约状态字典
-- 说明：为预约记录状态提供字典（0待确认 1已确认 2已完成 3已取消）。
-- 执行：mysql -u root -p 库名 < health_dict_appointment_status.sql
-- ============================================================================

USE `health`;

INSERT INTO sys_dict_type VALUES(13, '预约状态', 'appointment_status', '0', 'admin', sysdate(), '', null, '预约记录状态列表');

INSERT INTO sys_dict_data VALUES(40, 1, '待确认', '0', 'appointment_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
INSERT INTO sys_dict_data VALUES(41, 2, '已确认', '1', 'appointment_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
INSERT INTO sys_dict_data VALUES(42, 3, '已完成', '2', 'appointment_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
INSERT INTO sys_dict_data VALUES(43, 4, '已取消', '3', 'appointment_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '');