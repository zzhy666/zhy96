import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listInspector(query) {
  return request({
    url: '/system/inspector/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getInspector(id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addInspector(data) {
  return request({
    url: '/system/inspector',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateInspector(data) {
  return request({
    url: '/system/inspector',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delInspector(id) {
  return request({
    url: '/system/inspector/' + id,
    method: 'delete'
  })
}
