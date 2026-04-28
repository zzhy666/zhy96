-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员', '3', '1', 'table', 'system/table/index', 1, 0, 'C', '0', '0', 'system:table:list', '#', 'admin', sysdate(), '', null, '网格人员菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:table:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:table:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:table:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:table:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('网格人员导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:table:export',       '#', 'admin', sysdate(), '', null, '');