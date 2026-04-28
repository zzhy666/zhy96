<template>
  <div class="page-container">
    <div class="form-card">
      <h2 class="page-title">空气质量监督信息反馈</h2>

      <!-- 地址信息展示 -->
      <div class="address-info-box">
        <div class="info-item">
          <span class="info-label">省份</span>
          <span class="info-value">{{ gridInfo.provinceName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">城市</span>
          <span class="info-value">{{ gridInfo.cityName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">详细地址</span>
          <span class="info-value">{{ gridInfo.detailAddress }}</span>
        </div>
      </div>
      
      <el-form 
        ref="aqiFormRef" 
        :model="aqiForm" 
        :rules="aqiFormRules" 
        label-position="top"
        style="margin-top: 20px;"
      >
        <!-- 预估AQI等级 -->
        <el-form-item label="预估AQI等级" prop="aqiLevelId" required>
          <div class="aqi-card-list">
            <div 
              v-for="level in aqiLevelList" 
              :key="level.aqiLevelId"
              class="aqi-card-item"
              :class="{ 'aqi-card-active': aqiForm.aqiLevelId === level.aqiLevelId }"
              @click="handleAqiLevelChange(level.aqiLevelId)"
            >
              <div class="aqi-level-name">{{ level.levelName }}</div>
              <div class="aqi-level-range">{{ level.aqiRange }}</div>
            </div>
          </div>
        </el-form-item>

        <!-- 空气质量描述 -->
        <el-form-item label="空气质量描述" prop="airDescription" required>
          <el-input
            v-model="aqiForm.airDescription"
            placeholder="请描述观测到的空气质量状况（如：空气清新、有雾霾等）"
            type="textarea"
            :rows="4"
          />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleSubmitFeedback" 
            class="btn-primary"
          >
            提交反馈
          </el-button>
        </el-form-item>

        <!-- 返回按钮 -->
        <el-form-item>
          <el-button @click="handleGoBack" class="btn-default">
            返回重新选择
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
// 修改导入，重命名API函数
import {submitFeedback, submitFeedback as apiSubmitFeedback} from '@/api/feedback'
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const aqiFormRef = ref()

const gridInfo = reactive({})
const aqiForm = reactive({
  provinceId: '',
  cityId: '',
  detailAddress: '',
  aqiLevelId: '',
  aqiLevelName: '',
  airDescription: ''
})

const aqiLevelList = ref([
  { aqiLevelId: 1, levelName: '一级优', aqiRange: '0-50', color: '#00e400' },
  { aqiLevelId: 2, levelName: '二级良', aqiRange: '51-100', color: '#ffff00' },
  { aqiLevelId: 3, levelName: '三级轻度污染', aqiRange: '101-150', color: '#ff7e00' },
  { aqiLevelId: 4, levelName: '四级中度污染', aqiRange: '151-200', color: '#ff0000' },
  { aqiLevelId: 5, levelName: '五级重度污染', aqiRange: '201-300', color: '#99004c' },
  { aqiLevelId: 6, levelName: '六级严重污染', aqiRange: '>300', color: '#7e0023' }
])

const aqiFormRules = reactive({
  aqiLevelId: [{ required: true, message: '请选择预估AQI等级', trigger: 'change' }],
  airDescription: [
    { required: true, message: '请填写描述信息', trigger: 'blur' },
    { min: 5, message: '描述信息不能少于5个字符', trigger: 'blur' }
  ]
})

onMounted(() => {
  const gridInfoStr = sessionStorage.getItem('selected_grid_info')
  if (!gridInfoStr) {
    ElMessage.warning('请先选择网格地址')
    router.push('/select-grid')
    return
  }
  const gridData = JSON.parse(gridInfoStr)
  Object.assign(gridInfo, gridData)
  Object.assign(aqiForm, {
    provinceId: gridData.provinceId,
    cityId: gridData.cityId,
    detailAddress: gridData.detailAddress
  })
})

const handleAqiLevelChange = (levelId) => {
  aqiForm.aqiLevelId = levelId
  aqiForm.aqiLevelName = aqiLevelList.value.find(item => item.aqiLevelId === levelId)?.levelName || ''
}
  const getCurrentUser = () => {
    // 1. 检查所有可能的存储位置和键名
    const localStorageUser = localStorage.getItem('userInfo');
    const sessionStorageUser = sessionStorage.getItem('userInfo');
    const token = localStorage.getItem('token') || sessionStorage.getItem('token'); // 有时用户信息在token中

    console.log('【调试】localStorage userInfo:', localStorageUser);
    console.log('【调试】sessionStorage userInfo:', sessionStorageUser);
    console.log('【调试】token:', token);

    // 2. 优先尝试解析最常见的存储位置
    let userInfo = {};
    try {
      if (localStorageUser) {
        userInfo = JSON.parse(localStorageUser);
      } else if (sessionStorageUser) {
        userInfo = JSON.parse(sessionStorageUser);
      }
    } catch (e) {
      console.error('解析用户信息失败:', e);
    }

    console.log('【调试】解析后的 userInfo 对象:', userInfo);
    console.log('【调试】userInfo 的所有键:', Object.keys(userInfo));

    // 3. 检查可能包含检测员ID的字段
    // 常见的字段名有：userId, id, inspectorId, user_id 等
    const possibleIdKeys = ['userId', 'id', 'inspectorId', 'user_id', 'inspector_id'];
    const possibleNameKeys = ['userName', 'name', 'nickName', 'username', 'inspectorName'];

    for (const key of possibleIdKeys) {
      if (userInfo[key] !== undefined) {
        console.log(`找到ID字段 "${key}":`, userInfo[key]);
      }
    }
    for (const key of possibleNameKeys) {
      if (userInfo[key] !== undefined) {
        console.log(`找到姓名字段 "${key}":`, userInfo[key]);
      }
    }

    return userInfo;
  };
// ✅ 修改函数名，避免冲突
const handleSubmitFeedback = async () => {
  try {
    // 1. 设置临时的检测员ID和姓名
    const inspectorId = 1;  // 临时使用固定值
    const inspectorName = "系统管理员";  // 临时使用固定值
    // 验证表单
    const currentUser = getCurrentUser();
    console.log('准备提交，当前用户对象:', currentUser);

    if (!aqiForm.aqiLevelId) {
      ElMessage.warning('请选择预估AQI等级')
      return
    }

    if (!aqiForm.airDescription || aqiForm.airDescription.trim().length < 5) {
      ElMessage.warning('请填写至少5个字符的空气质量描述')
      return
    }

    // 准备提交数据
    const formData = {
      provinceId: gridInfo.provinceId,
      provinceName: gridInfo.provinceName,
      cityId: gridInfo.cityId,
      cityName: gridInfo.cityName,
      detailAddress: gridInfo.detailAddress,
      aqiLevelId: aqiForm.aqiLevelId,
      aqiLevelName: aqiForm.aqiLevelName,
      airDescription: aqiForm.airDescription.trim(),
      inspectorId: inspectorId,     // 必须包含
      inspectorName: inspectorName

    };console.log('提交的数据：', formData);

    // ✅ 调用重命名后的API函数
    const response = await apiSubmitFeedback(formData)

    console.log('API响应:', response)

    if (response.code === 200) {
      ElMessage.success('反馈提交成功！')

      // 清空表单
      aqiForm.aqiLevelId = ''
      aqiForm.aqiLevelName = ''
      aqiForm.airDescription = ''

      // 跳转到历史页面
      setTimeout(() => {
        router.push('/feedback-history')
      }, 1500)
    } else {
      ElMessage.error('提交失败：' + (response.msg || '未知错误'))
    }
  } catch (error) {
    console.error('提交出错:', error)
    ElMessage.error('网络错误，请稍后重试')
  }
}

const handleGoBack = () => {
  router.push('/select-grid')
}
</script>

<style scoped>
/* 核心响应式布局 */
.page-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px 16px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-card {
  width: 100%;
  max-width: 600px; /* 电脑端最大宽度限制 */
  background: #fff;
  border-radius: 8px;
  padding: 32px 28px;
  box-sizing: border-box;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.page-title {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 20px 0;
}

/* 地址信息展示 */
.address-info-box {
  background-color: #f9fafc;
  border-radius: 6px;
  padding: 16px;
}
.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
}
.info-item:last-child {
  margin-bottom: 0;
}
.info-label {
  color: #909399;
}
.info-value {
  color: #303133;
  font-weight: 500;
  text-align: right;
  flex: 1;
  margin-left: 10px;
}

/* AQI卡片选择器 */
.aqi-card-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.aqi-card-item {
  width: calc(50% - 5px);
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}
.aqi-card-active {
  border-color: #409EFF;
  background-color: #ecf5ff;
  box-shadow: 0 0 0 1px #409EFF inset;
}
.aqi-level-name {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}
.aqi-level-range {
  font-size: 12px;
  color: #909399;
}

/* 按钮样式 */
.btn-primary {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 22px;
}
.btn-default {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 22px;
  border: 1px solid #dcdfe6;
}

/* 手机端适配 */
@media screen and (max-width: 576px) {
  .form-card {
    padding: 24px 20px;
    box-shadow: none;
  }
  .page-title {
    font-size: 18px;
  }
  .aqi-card-item {
    padding: 10px;
  }
}
</style>