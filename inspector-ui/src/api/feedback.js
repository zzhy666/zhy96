// src/api/feedback.js
import request from '@/utils/request'


// 查询空气质量用户反馈列表
export function listFeedback(query) {
    return request({
        url: '/system/feedback/list',
        method: 'get',
        params: query
    })
}

// 查询空气质量用户反馈详细
export function getFeedback(feedbackId) {
    return request({
        url: '/system/feedback/' + feedbackId,
        method: 'get'
    })
}

// 新增空气质量用户反馈
export function addFeedback(data) {
    return request({
        url: '/system/feedback',
        method: 'post',
        data: data
    })
}
// 提交新的空气质量反馈
export function submitFeedback(data) {
    const token = localStorage.getItem('token');

    // 设置请求头，携带Token
    const headers = {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return request({
        url: '/anon/feedback'   ,
        method: 'post',
        data: data, // 将包含地址、AQI等级、描述的数据对象传给后端
        headers: headers
    })
}

// 获取当前用户的历史反馈列表
export function getFeedbackHistory(params = {}) {  // 添加默认值
    return request({
        url: '/anon/feedback/list',  // 使用匿名接口路径
        method: 'get',
        params: params || {}  // 确保 params 不会为 undefined
    })
}