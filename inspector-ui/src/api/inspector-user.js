// inspector-ui/src/api/inspector-user.js
import request from '@/utils/request'

// 检测员用户注册
export function inspectorRegister(data) {
    return request({
        url: '/system/inspector/user/register',
        method: 'post',
        data: data
    })
}

// 检测员用户登录
// 监督员登录
export function inspectorLogin(data) {
    return request({
        url: '/system/inspector/login',  // 确保这个路径正确
        method: 'post',
        data
    })
}

// 检测员获取网格列表
export function getInspectorGridList(token) {
    return request({
        url: '/system/inspector/user/grid/list',
        method: 'get',
        params: { token }
    })
}

// 检测员选择网格
export function selectGrid(data) {
    return request({
        url: '/system/inspector/user/select/grid',
        method: 'post',
        data: data
    })
}

// 检测员提交空气质量反馈
export function submitAqiFeedback(data) {
    return request({
        url: '/system/inspector/user/feedback/submit',
        method: 'post',
        data: data
    })
}

// 检测员获取反馈历史
export function getInspectorFeedbackHistory(token) {
    return request({
        url: '/system/inspector/user/feedback/history',
        method: 'get',
        params: {token}
    })
}

