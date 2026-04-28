-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准', '3', '1', 'bz', 'system/bz/index', 1, 0, 'C', '0', '0', 'system:bz:list', '#', 'admin', sysdate(), '', null, '空气质量指数(AQI)级别标准菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:bz:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:bz:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:bz:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:bz:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('空气质量指数(AQI)级别标准导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:bz:export',       '#', 'admin', sysdate(), '', null, '');