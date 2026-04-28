<template>
  <el-container class="regist-container">
    <el-main>
      <el-row justify="center" align="middle" class="full-height">
        <el-col :xs="22" :sm="18" :md="12" :lg="8" :xl="6">
          <div class="regist-card">
            <!-- 头部区域 -->
            <div class="header-section">
              <el-link underline="never" @click="goBack" class="back-link">
                返回
              </el-link>
              <h2 class="page-title">注册</h2>
            </div>
            
            <!-- Logo区域 -->
            <div class="logo-section">
              <img src="../assets/logo.png" alt="logo" class="logo-img" />
            </div>
            
            <!-- 表单区域 -->
            <div class="form-section">
              <el-form-item label="手机号" class="input-item">
                <el-input 
                  v-model="phone" 
                  placeholder="请输入手机号" 
                  size="large"
                  :class="{ 'is-error': phoneError }"
                />
                <div v-if="phoneError" class="error-message">请输入有效的手机号</div>
              </el-form-item>
              
              <el-form-item label="真实姓名" class="input-item">
                <el-input 
                  v-model="name" 
                  placeholder="请输入姓名" 
                  size="large"
                  :class="{ 'is-error': nameError }"
                />
                <div v-if="nameError" class="error-message">请输入真实姓名</div>
              </el-form-item>
              
              <el-form-item label="性别" class="input-item">
                <el-radio-group v-model="sex" size="large">
                  <el-radio value="男">男</el-radio>
                  <el-radio value="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
              
              <el-form-item label="密码" class="input-item">
                <el-input 
                  v-model="password" 
                  type="password" 
                  placeholder="请输入密码" 
                  size="large"
                  :class="{ 'is-error': passwordError }"
                />
                <div v-if="passwordError" class="error-message">请输入密码</div>
              </el-form-item>
              
              <el-form-item label="确认密码" class="input-item">
                <el-input 
                  v-model="repassword"
                  type="password" 
                  placeholder="再次输入密码" 
                  size="large"
                  :class="{ 'is-error': repasswordError }"
                />
                <div v-if="repasswordError" class="error-message">两次密码不一致</div>
              </el-form-item>
              
              <el-button 
                type="primary" 
                @click="regist" 
                size="large" 
                class="regist-btn"
                :loading="isLoading"
              >
                注册
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const phone = ref('')
const name = ref('')
const sex = ref('男')
const password = ref('')
const repassword = ref('')
const router = useRouter()
const phoneError = ref(false)
const nameError = ref(false)
const passwordError = ref(false)
const repasswordError = ref(false)
const isLoading = ref(false)

const goBack = () => {
  router.push('/')
}

const regist = async () => { 
  let hasError = false
  phoneError.value = false
  nameError.value = false
  passwordError.value = false
  repasswordError.value = false

  if (!phone.value) {
    phoneError.value = true
    hasError = true
  }
  if (!name.value) {
    nameError.value = true
    hasError = true
  }
  if (!password.value) {
    passwordError.value = true
    hasError = true
  }
  if (!repassword.value) {
    repasswordError.value = true
    hasError = true
  }
  if (password.value !== repassword.value) {
    repasswordError.value = true
    hasError = true
  }
  
  if (hasError) {
    ElMessage.warning('请完善信息')
    return
  }
  
  try {
    isLoading.value = true
    const res = await axios.post("http://localhost:8080/system/inspector/register",{
      name: name.value,
      phone: phone.value,
      password: password.value
    })
    
    if(res.data.code === 200){
      ElMessage.success('注册成功,转至登录页')
      console.log('新用户信息:', res.data.data)
      router.push('/')
    } else {
      ElMessage.error(res.data.message || '注册失败')
    }
  } catch (error) {
    console.error('注册错误:', error)
    if (error.response && error.response.data) {
      // 尝试显示后端返回的具体错误信息
      ElMessage.error(error.response.data.msg || '注册失败')
    } else {
      ElMessage.error('网络请求失败，请检查后端服务')
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.regist-container {
  min-height: 100vh;
  background-color: #f1f1f1;
  display: flex;
}

.full-height {
  width: 100%;
  min-height: 100vh;
}

.regist-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  margin-bottom: 20px;
}

.header-section {
  position: relative;
  text-align: center;
  margin-bottom: 24px;
}

.back-link {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #409eff;
}

.page-title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.logo-section {
  text-align: center;
  margin-bottom: 32px;
}

.logo-img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.form-section {
  width: 100%;
}

.input-item {
  margin-bottom: 24px;
  position: relative;
}

.input-item :deep(.el-form-item__label) {
  font-size: 16px;
  color: #333;
  padding-right: 12px;
}

.input-item :deep(.el-input__wrapper) {
  height: 48px;
  border-radius: 8px;
}

/* 错误状态样式 */
.input-item :deep(.el-input.is-error .el-input__wrapper) {
  border-color: #f56c6c;
  box-shadow: 0 0 0 1px #f56c6c inset !important;
}

.error-message {
  color: #f56c6c;
  font-size: 14px;
  line-height: 1.2;
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 8px;
}

.regist-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  margin-top: 16px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .regist-card {
    padding: 24px;
    margin: 16px;
  }
  
  .back-link {
    font-size: 14px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .input-item :deep(.el-form-item__label) {
    font-size: 14px;
  }
  
  .input-item :deep(.el-input__wrapper) {
    height: 44px;
    border-radius: 8px;
  }
  
  .error-message {
    font-size: 12px;
  }
  
  .regist-btn {
    height: 44px;
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .back-link {
    left: -8px;
  }
}
</style>
