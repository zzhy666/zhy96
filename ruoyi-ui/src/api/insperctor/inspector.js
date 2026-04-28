import request from '@/utils/request'

// 查询人员列表
export function listInspector(query) {
  return request({
    url: '/system/inspector/list',
    method: 'get',
    params: query
  })
}

// 查询人员详细
export function getInspector(id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'get'
  })
}

// 新增人员
export function addInspector(data) {
  return request({
    url: '/system/inspector',
    method: 'post',
    data: data
  })
}

// 修改人员
export function updateInspector(data) {
  return request({
    url: '/system/inspector',
    method: 'put',
    data: data
  })
}

// 删除人员
export function delInspector(id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'delete'
  })
}
// 检测员登录
export function inspectorLogin(data) {
  return request({
    url: '/system/inspector/login',
    method: 'post',
    data: data
  })
}

// 检测员注册
export function inspectorRegister(data) {
  return request({
    url: '/system/inspector/register',
    method: 'post',
    data: data
  })
}

// 获取网格列表
export function getGridList(params) {
  return request({
    url: '/system/inspector/grid/list',
    method: 'get',
    params: params
  })
}

// 提交空气质量反馈
export function submitAqiFeedback(data) {
  return request({
    url: '/system/feedback/submit',
    method: 'post',
    data: data
  })
}

// 获取反馈历史
export function getFeedbackHistory(params) {
  return request({
    url: '/system/feedback/history',
    method: 'get',
    params: params
  })
}
