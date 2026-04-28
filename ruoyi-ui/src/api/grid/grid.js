import request from '@/utils/request'

// 获取网格员任务
export function getGridTask(gridUserId) {
  return request({
    url: '/grid/task/' + gridUserId,
    method: 'get'
  })
}

// 提交检测结果
export function submitResult(feedbackId) {
  return request({
    url: '/grid/submitResult',
    method: 'post',
    data: feedbackId
  })
}
