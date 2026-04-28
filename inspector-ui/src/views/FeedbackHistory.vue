<template>
  <div class="page-container">
    <div class="content-wrapper">
      <!-- 页面头部 -->
      <div class="page-header">
        <h2 class="page-title">我的历史反馈</h2>
        <el-button type="primary" @click="goToAddFeedback" class="add-btn">
          新增反馈
        </el-button>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-box">
        <el-icon class="is-loading" style="font-size: 30px; color: #409EFF;"><Loading /></el-icon>
        <p style="color: #909399; margin-top: 10px;">加载中...</p>
      </div>

      <!-- 列表区域 -->
      <div v-else class="list-container">
        <!-- 空状态 -->
        <el-empty
          v-if="historyList.length === 0"
          description="暂无反馈数据"
          style=" padding: 60px 0;"
        />

        <!-- 电脑端：表格展示（优化列宽+收紧宽度） -->
        <el-table
          :data="historyList"
          style="width: 100%"
          stripe
          :cell-style="{ padding: '12px 0' }"
          :header-cell-style="{ padding: '14px 0', backgroundColor: '#fafafa' }"
          class="pc-table"
          v-if="!isMobile"
        >
          <!-- 精准控制每一列宽度，避免自动均分导致过宽 -->
          <el-table-column prop="feedbackTime" label="反馈时间" width="180" align="center" />
          <el-table-column label="地区" width="140" :align="center">
            <template #default="scope">
              {{ scope.row.provinceName }} {{ scope.row.cityName }}
            </template>
          </el-table-column>
          <el-table-column prop="aqiLevelName" label="预估AQI等级" width="130" align="center">
            <template #default="scope">
              <el-tag :type="getAqiTagType(scope.row.aqiLevelId)" size="small">
                {{ scope.row.aqiLevelName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="detailAddress" label="详细地址" min-width="180" show-overflow-tooltip />
          <el-table-column prop="airDescription" label="描述" min-width="180" show-overflow-tooltip />
        </el-table>

        <!-- 手机端：卡片流展示 -->
        <div class="history-card-list" v-if="isMobile">
          <div
            v-for="item in historyList"
            :key="item.feedbackId"
            class="history-card-item"
          >
            <div class="card-header">
              <span class="aqi-tag" :class="'aqi-tag-' + item.aqiLevelId">
                {{ item.aqiLevelName }}
              </span>
              <span class="feedback-time">{{ item.feedbackTime }}</span>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="info-label">📍 地区</span>
                <span class="info-text">{{ item.provinceName }} {{ item.cityName }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">🏠 详细地址</span>
                <span class="info-text">{{ item.detailAddress }}</span>
              </div>
              <div class="info-row" v-if="item.airDescription">
                <span class="info-label">💬 描述</span>
                <span class="info-text">{{ item.airDescription }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Loading } from '@element-plus/icons-vue'
import { getFeedbackHistory } from '@/api/feedback'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)

// 判断是否为手机端
const isMobile = computed(() => {
  return window.innerWidth <= 576
})
const historyList = ref([])
// 模拟数据
onMounted(async () => {
  loading.value = true
  try {
    // 调用API获取历史数据
    const response = await getFeedbackHistory()
    if (response.code === 200) {
      // 将接口返回的真实数据赋值给 historyList
      historyList.value = response.data
      // 确保数据按时间倒序排列（最近的在前）
      historyList.value.sort((a, b) => new Date(b.feedbackTime) - new Date(a.feedbackTime))
    } else {
      ElMessage.error('加载历史记录失败：' + response.msg)
    }
  } catch (error) {
    console.error('获取历史反馈列表失败:', error)
    ElMessage.error('网络错误，加载历史记录失败')
  } finally {
    loading.value = false
  }
})
// ✅ 修改函数名，避免冲突
const handleSubmitFeedback = async () => {
  try {
    // 验证表单
    const currentUser = getCurrentUser();
    console.log('准备提交，当前用户对象:', currentUser);

    // --- 新增：从currentUser中提取检测员信息 ---
    // 根据您之前调试打印的字段名，调整这里的取值逻辑
    // 例如，如果控制台显示找到的字段是 userId 和 userName，则改为:
    // const inspectorId = currentUser.userId || 1;
    // const inspectorName = currentUser.userName || '匿名用户';
    const inspectorId = currentUser.userId || currentUser.id || currentUser.inspectorId || 1;
    const inspectorName = currentUser.userName || currentUser.name || currentUser.nickName || '匿名用户';

    console.log('最终使用的检测员ID:', inspectorId);
    console.log('最终使用的检测员姓名:', inspectorName);
    // --- 新增结束 ---

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
      // --- 关键修改：加入检测员信息 ---
      inspectorId: inspectorId,     // 动态赋值，而非固定值
      inspectorName: inspectorName  // 动态赋值，而非固定值
    };
    console.log('提交的数据：', formData);

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

// 获取AQI标签类型
const getAqiTagType = (levelId) => {
  const typeMap = {
    1: 'success',
    2: 'warning',
    3: 'warning',
    4: 'danger',
    5: 'danger',
    6: 'danger'
  }
  return typeMap[levelId] || 'info'
}


const goToAddFeedback = () => {
  router.push('/select-grid')
}
</script>

<style scoped>
/* 核心优化：收紧最大宽度，避免宽屏上过度拉伸 */
.page-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 30px 16px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

/* 关键优化：从1000px收紧到800px，宽屏上更紧凑 */
.content-wrapper {
  width: 100%;
  max-width: 800px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.add-btn {
  border-radius: 20px;
  padding: 0 20px;
}

.loading-box {
  text-align: center;
  padding: 60px 0;
}

.list-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 电脑端表格样式优化 */
.pc-table {
  font-size: 14px;
}

/* 手机端卡片列表样式 */
.history-card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-card-item {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.aqi-tag {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
}
.aqi-tag-1 { background-color: #00e400; }
.aqi-tag-2 { background-color: #ffcc00; color: #333; }
.aqi-tag-3 { background-color: #ff7e00; }
.aqi-tag-4 { background-color: #ff0000; }
.aqi-tag-5 { background-color: #99004c; }
.aqi-tag-6 { background-color: #7e0023; }

.feedback-time {
  font-size: 12px;
  color: #909399;
}

.card-body {
  font-size: 14px;
}

.info-row {
  display: flex;
  margin-bottom: 8px;
  line-height: 1.5;
}
.info-row:last-child {
  margin-bottom: 0;
}
.info-label {
  color: #909399;
  width: 80px;
  flex-shrink: 0;
}
.info-text {
  color: #303133;
  flex: 1;
}

/* 手机端适配 */
@media screen and (max-width: 576px) {
  .page-container {
    padding: 16px;
  }
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  .add-btn {
    width: 100%;
  }
  .list-container {
    padding: 0;
    background: transparent;
    box-shadow: none;
  }
  .page-title {
    font-size: 18px;
  }
}
</style>