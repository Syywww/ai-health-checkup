-- ============================================================================
-- 体检预约管理系统 · 业务菜单 + 业务字典 脚本
-- 说明：ry_20260417.sql 只含若依系统菜单/字典，以下补齐预约、会员、AI 业务
--       菜单与 health_type/health_sex 业务字典。admin 为超级管理员，前端菜单树
--       走 selectMenuTreeAll，无需 sys_role_menu 关联即可全部可见。
-- 执行：mysql -u root -p 库名 < health_business_menu_dict.sql
-- ============================================================================

USE `health`;

-- ----------------------------------------------------------------------------
-- 一、业务菜单（sys_menu）
--    目录 M（menu_type='M'） / 菜单 C（menu_type='C'） / 按钮 F（menu_type='F'）
--    列顺序与 ry_20260417.sql 一致：
--    menu_id, menu_name, parent_id, order_num, path, component, query, route_name,
--    is_frame, is_cache, menu_type, visible, status, perms, icon, create_by,
--    create_time, update_by, update_time, remark
-- ----------------------------------------------------------------------------

-- ===== 预约管理（一级目录） =====
insert into sys_menu values('2000', '预约管理', '0', '1', 'reservation', NULL, '', '', 1, 0, 'M', '0', '0', '', 'date', 'admin', sysdate(), '', null, '预约管理目录');

-- 检查项管理
insert into sys_menu values('2001', '检查项管理', '2000', '1', 'checkitem', 'reservation/checkitem/index', '', '', 1, 0, 'C', '0', '0', 'reservation:checkitem:list', 'checkbox', 'admin', sysdate(), '', null, '检查项管理菜单');
insert into sys_menu values('2002', '检查项查询', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkitem:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '检查项新增', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkitem:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '检查项修改', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkitem:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2005', '检查项删除', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkitem:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2006', '检查项导出', '2001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkitem:export', '#', 'admin', sysdate(), '', null, '');

-- 检查组管理
insert into sys_menu values('2011', '检查组管理', '2000', '2', 'checkgroup', 'reservation/checkgroup/index', '', '', 1, 0, 'C', '0', '0', 'reservation:checkgroup:list', 'list', 'admin', sysdate(), '', null, '检查组管理菜单');
insert into sys_menu values('2012', '检查组查询', '2011', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkgroup:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2013', '检查组新增', '2011', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkgroup:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2014', '检查组修改', '2011', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkgroup:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2015', '检查组删除', '2011', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkgroup:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2016', '检查组导出', '2011', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:checkgroup:export', '#', 'admin', sysdate(), '', null, '');

-- 套餐管理
insert into sys_menu values('2021', '套餐管理', '2000', '3', 'setmeal', 'reservation/setmeal/index', '', '', 1, 0, 'C', '0', '0', 'reservation:setmeal:list', 'form', 'admin', sysdate(), '', null, '套餐管理菜单');
insert into sys_menu values('2022', '套餐查询', '2021', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setmeal:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2023', '套餐新增', '2021', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setmeal:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2024', '套餐修改', '2021', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setmeal:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2025', '套餐删除', '2021', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setmeal:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2026', '套餐导出', '2021', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setmeal:export', '#', 'admin', sysdate(), '', null, '');

-- 预约设置
insert into sys_menu values('2031', '预约设置', '2000', '4', 'setting', 'reservation/setting/index', '', '', 1, 0, 'C', '0', '0', 'reservation:setting:list', 'edit', 'admin', sysdate(), '', null, '预约设置菜单');
insert into sys_menu values('2032', '预约设置查询', '2031', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2033', '预约设置新增', '2031', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2034', '预约设置修改', '2031', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2035', '预约设置删除', '2031', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2036', '预约设置导出', '2031', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2037', '预约设置导入', '2031', '6', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:setting:import', '#', 'admin', sysdate(), '', null, '');

-- 预约记录
insert into sys_menu values('2041', '预约记录', '2000', '5', 'appointment', 'reservation/appointment/index', '', '', 1, 0, 'C', '0', '0', 'reservation:appointment:list', 'time', 'admin', sysdate(), '', null, '预约记录菜单');
insert into sys_menu values('2042', '预约记录查询', '2041', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:appointment:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2043', '预约记录新增', '2041', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:appointment:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2044', '预约记录修改', '2041', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:appointment:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2045', '预约记录删除', '2041', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:appointment:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2046', '预约记录导出', '2041', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'reservation:appointment:export', '#', 'admin', sysdate(), '', null, '');

-- ===== 会员管理（一级目录） =====
insert into sys_menu values('2100', '会员管理', '0', '2', 'member', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', null, '会员管理目录');

-- 会员信息
insert into sys_menu values('2101', '会员信息', '2100', '1', 'index', 'member/index', '', '', 1, 0, 'C', '0', '0', 'member:member:list', 'user', 'admin', sysdate(), '', null, '会员信息菜单');
insert into sys_menu values('2102', '会员查询', '2101', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'member:member:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2103', '会员新增', '2101', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'member:member:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2104', '会员修改', '2101', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'member:member:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2105', '会员删除', '2101', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'member:member:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2106', '会员导出', '2101', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'member:member:export', '#', 'admin', sysdate(), '', null, '');

-- 体检记录录入
insert into sys_menu values('2111', '体检记录', '2100', '2', 'upload', 'member/upload', '', '', 1, 0, 'C', '0', '0', 'member:examination:list', 'documentation', 'admin', sysdate(), '', null, '体检记录菜单');
insert into sys_menu values('2112', '体检记录查询', '2111', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'member:examination:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2113', '体检记录新增', '2111', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'member:examination:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2114', '体检记录修改', '2111', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'member:examination:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2115', '体检记录删除', '2111', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'member:examination:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2116', '体检记录导出', '2111', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'member:examination:export', '#', 'admin', sysdate(), '', null, '');

-- 体检统计
insert into sys_menu values('2121', '体检统计', '2100', '3', 'statistics', 'member/statistics', '', '', 1, 0, 'C', '0', '0', 'member:statistics:list', 'chart', 'admin', sysdate(), '', null, '体检统计菜单');

-- ===== AI 助手（一级目录） =====
insert into sys_menu values('2200', 'AI 助手', '0', '3', 'ai', NULL, '', '', 1, 0, 'M', '0', '0', '', 'message', 'admin', sysdate(), '', null, 'AI 助手目录');

-- AI 对话
insert into sys_menu values('2201', 'AI 对话', '2200', '1', 'chat', 'ai/chat', '', '', 1, 0, 'C', '0', '0', 'ai:chat:list', 'message', 'admin', sysdate(), '', null, 'AI 对话菜单');

-- ----------------------------------------------------------------------------
-- 二、普通角色（role_id=2）授权：若使用非 admin 账号演示，需关联业务菜单。
--    admin 为超级管理员可忽略本段。
-- ----------------------------------------------------------------------------
insert into sys_role_menu values('2', '2000');
insert into sys_role_menu values('2', '2001');
insert into sys_role_menu values('2', '2002');
insert into sys_role_menu values('2', '2003');
insert into sys_role_menu values('2', '2004');
insert into sys_role_menu values('2', '2005');
insert into sys_role_menu values('2', '2006');
insert into sys_role_menu values('2', '2011');
insert into sys_role_menu values('2', '2012');
insert into sys_role_menu values('2', '2013');
insert into sys_role_menu values('2', '2014');
insert into sys_role_menu values('2', '2015');
insert into sys_role_menu values('2', '2016');
insert into sys_role_menu values('2', '2021');
insert into sys_role_menu values('2', '2022');
insert into sys_role_menu values('2', '2023');
insert into sys_role_menu values('2', '2024');
insert into sys_role_menu values('2', '2025');
insert into sys_role_menu values('2', '2026');
insert into sys_role_menu values('2', '2031');
insert into sys_role_menu values('2', '2032');
insert into sys_role_menu values('2', '2033');
insert into sys_role_menu values('2', '2034');
insert into sys_role_menu values('2', '2035');
insert into sys_role_menu values('2', '2036');
insert into sys_role_menu values('2', '2037');
insert into sys_role_menu values('2', '2041');
insert into sys_role_menu values('2', '2042');
insert into sys_role_menu values('2', '2043');
insert into sys_role_menu values('2', '2044');
insert into sys_role_menu values('2', '2045');
insert into sys_role_menu values('2', '2046');
insert into sys_role_menu values('2', '2100');
insert into sys_role_menu values('2', '2101');
insert into sys_role_menu values('2', '2102');
insert into sys_role_menu values('2', '2103');
insert into sys_role_menu values('2', '2104');
insert into sys_role_menu values('2', '2105');
insert into sys_role_menu values('2', '2106');
insert into sys_role_menu values('2', '2111');
insert into sys_role_menu values('2', '2112');
insert into sys_role_menu values('2', '2113');
insert into sys_role_menu values('2', '2114');
insert into sys_role_menu values('2', '2115');
insert into sys_role_menu values('2', '2116');
insert into sys_role_menu values('2', '2121');
insert into sys_role_menu values('2', '2200');
insert into sys_role_menu values('2', '2201');

-- ----------------------------------------------------------------------------
-- 三、业务字典（sys_dict_type + sys_dict_data）
--     health_type：检查项类型（1 检查 / 2 检验）
--     health_sex ：检查项/检查组适用性别（0 不限 / 1 男 / 2 女）
-- ----------------------------------------------------------------------------
insert into sys_dict_type values(11, '检查类型', 'health_type', '0', 'admin', sysdate(), '', null, '检查类型列表');
insert into sys_dict_type values(12, '适用性别', 'health_sex', '0', 'admin', sysdate(), '', null, '适用性别列表');

insert into sys_dict_data values(30, 1, '检查', '1', 'health_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '检查类型');
insert into sys_dict_data values(31, 2, '检验', '2', 'health_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '检验类型');
insert into sys_dict_data values(32, 1, '不限', '0', 'health_sex', '', 'info', 'Y', '0', 'admin', sysdate(), '', null, '不限性别');
insert into sys_dict_data values(33, 2, '男', '1', 'health_sex', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '适用男');
insert into sys_dict_data values(34, 3, '女', '2', 'health_sex', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '适用女');