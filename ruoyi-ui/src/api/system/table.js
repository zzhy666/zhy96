import request from '@/utils/request'

// 查询网格人员列表
export function listTable(query) {
  return request({
    url: '/system/table/list',
    method: 'get',
    params: query
  })
}

// 查询网格人员详细
export function getTable(wgId) {
  return request({
    url: '/system/table/' + wgId,
    method: 'get'
  })
}

// 新增网格人员
export function addTable(data) {
  return request({
    url: '/system/table',
    method: 'post',
    data: data
  })
}

// 修改网格人员
export function updateTable(data) {
  return request({
    url: '/system/table',
    method: 'put',
    data: data
  })
}

// 删除网格人员
export function delTable(wgId) {
  return request({
    url: '/system/table/' + wgId,
    method: 'delete'
  })
}
