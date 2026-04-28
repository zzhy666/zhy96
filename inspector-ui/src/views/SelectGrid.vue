<template>
  <div class="page-container">
    <div class="form-card">
      <h2 class="page-title">选择网格地址</h2>

      <el-form
          ref="gridFormRef"
          :model="gridForm"
          :rules="gridFormRules"
          label-position="top"
          size="default"
      >
        <!-- 所在省下拉框 -->
        <el-form-item label="所在省" prop="provinceId" required>
          <el-select
              v-model="gridForm.provinceId"
              placeholder="请选择省份"
              style="width: 100%"
              @change="handleProvinceSelect"
              :loading="loadingProvince"
          >
            <el-option
                v-for="province in provinceList"
                :key="province.id"
                :label="province.name"
                :value="province.id"
            />
          </el-select>
        </el-form-item>

        <!-- 所在市下拉框 -->
        <el-form-item label="所在市" prop="cityId" required>
          <el-select
              v-model="gridForm.cityId"
              placeholder="请先选择省份"
              style="width: 100%"
              :disabled="!gridForm.provinceId"
              :loading="loadingCity"
          >
            <el-option
                v-for="city in cityList"
                :key="city.id"
                :label="city.name"
                :value="city.id"
            />
          </el-select>
        </el-form-item>

        <!-- 具体观测地址 -->
        <el-form-item label="详细地址" prop="detailAddress" required>
          <el-input
              v-model="gridForm.detailAddress"
              placeholder="请填写本人所观测的具体地址"
              type="textarea"
              :rows="3"
          />
        </el-form-item>

        <!-- 下一步按钮 -->
        <el-form-item>
          <el-button
              type="primary"
              @click="handleNextStep"
              class="btn-primary"
              :loading="submitting"
          >
            下一步
          </el-button>
        </el-form-item>

        <!-- 历史记录跳转 -->
        <el-form-item>
          <el-button @click="goToHistoryList" class="btn-default">
            查看我的历史反馈
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const gridFormRef = ref()

// 数据定义
const gridForm = reactive({
  provinceId: '',
  provinceName: '',
  cityId: '',
  cityName: '',
  detailAddress: ''
})

// 省份和城市列表
const provinceList = ref([])
const cityList = ref([])

// 加载状态
const loadingProvince = ref(false)
const loadingCity = ref(false)
const submitting = ref(false)

// 表单验证规则
const gridFormRules = reactive({
  provinceId: [{ required: true, message: '请选择所在省份', trigger: 'change' }],
  cityId: [{ required: true, message: '请选择所在城市', trigger: 'change' }],
  detailAddress: [{ required: true, message: '请填写详细地址', trigger: 'blur' }]
})

// 从sessionStorage恢复已选地址
const loadFromSession = () => {
  const savedData = sessionStorage.getItem('selected_grid_info')
  if (savedData) {
    try {
      const parsed = JSON.parse(savedData)
      Object.assign(gridForm, parsed)

      // 如果已选择省份，加载该省份的城市
      if (gridForm.provinceId) {
        loadCities(gridForm.provinceId)
      }
    } catch (error) {
      console.error('加载sessionStorage数据失败:', error)
    }
  }
}

// 从数据库获取省份数据
const loadProvinces = async () => {
  try {
    loadingProvince.value = true
    // 这里调用后端接口获取所有省份
    // 假设接口返回格式: [{id: 1, name: '北京市'}, {id: 2, name: '辽宁省'}, ...]
    const response = await fetch('/system/city/provinces')
    if (response.ok) {
      const data = await response.json()
      // 修改2：正确处理返回的数据格式
      if (data.code === 200) {
        // 转换数据格式：从 {id, name} 到 {id, name}（保持不变）
        provinceList.value = data.data || data
        console.log('省份数据已加载:', provinceList.value)
      } else {
        throw new Error(data.msg || '获取省份数据失败')
      }
    } else {
      throw new Error('获取省份数据失败')
    }
  } catch (error) {
    console.error('加载省份数据失败:', error)
    ElMessage.error('加载省份数据失败，请刷新重试')
  } finally {
    loadingProvince.value = false
  }
}

// 根据省份ID加载城市数据
const loadCities = async (provinceId) => {
  if (!provinceId) {
    cityList.value = []
    return
  }

  try {
    loadingCity.value = true
    // 找到选中的省份名称
    const selectedProvince = provinceList.value.find(p => p.id === provinceId)
    if (selectedProvince) {
      gridForm.provinceName = selectedProvince.name
    }

    // 修改1：使用正确的接口路径
    const response = await fetch(`/system/city/cities/${provinceId}`)
    if (response.ok) {
      const data = await response.json()
      // 修改2：正确处理返回的数据格式
      if (data.code === 200) {
        // 转换数据格式：从 {id, name} 到 {id, name}（保持不变）
        cityList.value = data.data || data
        console.log('城市数据已加载:', cityList.value)

        // 如果之前选择了城市，确保城市在列表中
        if (gridForm.cityId) {
          const cityExists = cityList.value.some(city => city.id === gridForm.cityId)
          if (!cityExists) {
            gridForm.cityId = ''
            gridForm.cityName = ''
          } else {
            const selectedCity = cityList.value.find(city => city.id === gridForm.cityId)
            if (selectedCity) {
              gridForm.cityName = selectedCity.name
            }
          }
        }
      } else {
        throw new Error(data.msg || '获取城市数据失败')
      }
    } else {
      throw new Error('获取城市数据失败')
    }
  } catch (error) {
    console.error('加载城市数据失败:', error)
    ElMessage.error('加载城市数据失败')
    cityList.value = []
  } finally {
    loadingCity.value = false
  }
}

// 事件处理
onMounted(async () => {
  // 先加载省份数据
  await loadProvinces()

  // 从sessionStorage恢复已选地址
  loadFromSession()
})

// 选择省份事件
const handleProvinceSelect = (provinceId) => {
  // 清空城市选择
  gridForm.cityId = ''
  gridForm.cityName = ''
  cityList.value = []

  // 加载该省份的城市
  if (provinceId) {
    loadCities(provinceId)
  }
}

// 下一步
const handleNextStep = async () => {
  const valid = await gridFormRef.value.validate().catch(() => false)
  if (!valid) return

  // 设置城市名称
  if (gridForm.cityId) {
    const selectedCity = cityList.value.find(item => item.id === gridForm.cityId)
    if (selectedCity) {
      gridForm.cityName = selectedCity.name
    }
  }

  // 保存到sessionStorage
  sessionStorage.setItem('selected_grid_info', JSON.stringify(gridForm))

  // 跳转到反馈页面
  router.push('/aqi-feedback')
}

// 查看历史记录
const goToHistoryList = () => {
  router.push('/feedback-history')
}
</script>

<style scoped>
/* 核心修复：电脑端限制最大宽度，居中显示，不再撑满屏幕 */
.page-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px 16px;
  box-sizing: border-box;
  /* 电脑端垂直居中 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-card {
  width: 100%;
  /* 关键：电脑端最大宽度限制，不会无限变宽 */
  max-width: 500px;
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
  margin: 0 0 28px 0;
}

/* 按钮样式：电脑端不会无限拉宽，手机端自动适配 */
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

/* 手机端适配：小屏优化 */
@media screen and (max-width: 576px) {
  .form-card {
    padding: 24px 20px;
    box-shadow: none;
  }
  .page-title {
    font-size: 18px;
  }
}
</style>