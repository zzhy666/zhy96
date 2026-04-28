import request from '@/utils/request'

// 查询检查记录提交列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询检查记录提交详细
export function getRecord(recordId) {
  return request({
    url: '/system/record/' + recordId,
    method: 'get'
  })
}

// 新增检查记录提交
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改检查记录提交
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除检查记录提交
export function delRecord(recordId) {
  return request({
    url: '/system/record/' + recordId,
    method: 'delete'
  })
}
export function dispatchInspector(data) {
  return request({
    url: '/system/record/dispatch',  // 根据实际情况调整路径
    method: 'post',
    data: data
  })
}
