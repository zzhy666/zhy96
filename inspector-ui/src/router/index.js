import { createRouter, createWebHashHistory } from 'vue-router'

// 导入组件
import Login from '../views/Login.vue'
import Regist from '../views/Regist.vue'
import SelectGrid from '../views/SelectGrid.vue'
import AqiFeedback from '../views/AqiFeedback.vue'
import FeedbackHistory from '../views/FeedbackHistory.vue'

const routes = [
  {
    path: '/regist',
    name: 'Regist',
    component: Regist
  },
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  // 4.3 选择网格地址页
  {
    path: '/select-grid',
    name: 'SelectGrid',
    component: SelectGrid,
    meta: {
      title: '选择网格地址',
      requiresAuth: true
    }
  },
  // 4.3 AQI反馈页
  {
    path: '/aqi-feedback',
    name: 'AqiFeedback',
    component: AqiFeedback,
    meta: {
      requiresAuth: true,
      title: '空气质量监督反馈'
    }
  },
  // 4.4 历史反馈列表页
  {
    path: '/feedback-history',
    name: 'FeedbackHistory',
    component: FeedbackHistory,
    meta: {
      requiresAuth: true,
      title: '我的历史反馈'
    }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('inspector_token')

  if (to.meta.requiresAuth && !isAuthenticated) {
    // 需要登录但未登录，重定向到登录页
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    // 已登录但访问登录页，重定向到首页
    next('/select-grid')
  } else {
    next()
  }
})
export default router