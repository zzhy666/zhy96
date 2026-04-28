import request from '@/utils/request'

// 查询空气质量指数(AQI)级别标准列表
export function listBz(query) {
  return request({
    url: '/system/bz/list',
    method: 'get',
    params: query
  })
}

// 查询空气质量指数(AQI)级别标准详细
export function getBz(levelId) {
  return request({
    url: '/system/bz/' + levelId,
    method: 'get'
  })
}

// 新增空气质量指数(AQI)级别标准
export function addBz(data) {
  return request({
    url: '/system/bz',
    method: 'post',
    data: data
  })
}

// 修改空气质量指数(AQI)级别标准
export function updateBz(data) {
  return request({
    url: '/system/bz',
    method: 'put',
    data: data
  })
}

// 删除空气质量指数(AQI)级别标准
export function delBz(levelId) {
  return request({
    url: '/system/bz/' + levelId,
    method: 'delete'
  })
}
