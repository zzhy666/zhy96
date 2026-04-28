import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCity(query) {
  return request({
    url: '/system/city/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCity(id) {
  return request({
    url: '/system/city/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCity(data) {
  return request({
    url: '/system/city',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCity(data) {
  return request({
    url: '/system/city',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCity(id) {
  return request({
    url: '/system/city/' + id,
    method: 'delete'
  })
}
export function getProvinces() {
  return request({
    url: '/system/city/provinces',  // 注意：路径变了！
    method: 'get'
  })
}

// 根据省份ID获取城市
export function getCities(provinceId) {
  return request({
    url: `/system/city/cities/${provinceId}`,  // 注意：路径变了！
    method: 'get'
  })
}
