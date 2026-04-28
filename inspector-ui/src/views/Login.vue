<template>
  <el-container class="login-container">
    <el-main>
      <el-row justify="center" align="middle" class="full-height">
        <el-col :xs="22" :sm="18" :md="12" :lg="8" :xl="6">
          <div class="login-card">
            <!-- Logo区域 -->
            <div class="logo-section">
              <img src="../assets/logo.png" alt="logo" class="logo-img" />
            </div>

            <!-- 标题区域 -->
            <div class="title-section">
              <h3>东软环保公众监督平台</h3>
              <h4>公众监督员端</h4>
            </div>

            <!-- 表单区域 -->
            <div class="form-section">
              <el-form-item label="手机号:" class="input-item">
                <el-input
                    v-model="Phone"
                    placeholder="请输入手机号"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="密码:" class="input-item">
                <el-input
                    v-model="Password"
                    type="password"
                    placeholder="请输入密码"
                    size="large"
                />
              </el-form-item>

              <!-- 记住我选项 -->
              <div class="remember-section">
                <el-checkbox v-model="form.rememberMe">记住密码</el-checkbox>
              </div>

              <div class="action-section">
                <el-link underline="never" @click="goRegist" class="regist-link">
                  注册
                </el-link>
                <el-button type="primary" @click="signIn" size="large" class="login-btn">
                  登录
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup>
import {reactive, ref, onMounted} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 使用封装好的API
import { inspectorLogin } from '@/api/inspector-user'

const Password = ref('')
const Phone = ref('')
const router = useRouter()
const form = reactive({
  phone: '',
  password: '',
  rememberMe: false
})
const goRegist = () => {
  router.push('/regist')
}
// 页面加载时检查是否已记住账号
onMounted(() => {
  const savedPhone = localStorage.getItem('inspector_phone')
  const savedPassword = localStorage.getItem('inspector_password')
  if (savedPhone && savedPassword) {
    Phone.value = savedPhone
    Password.value = savedPassword
    form.rememberMe = true
  }
})

const signIn = async() => {
  if (!Phone.value) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!Password.value) {
    ElMessage.warning('请输入密码')
    return
  }

  if (Phone.value.length !== 11) {
    ElMessage.warning('手机号格式不正确')
    return
  }

  try {
    console.log('=== 登录请求 ===');
    console.log('手机号:', Phone.value);
    console.log('密码:', Password.value);
    console.log('密码长度:', Password.value.length);

    // 使用封装好的API调用
    const response = await inspectorLogin({
      phone: Phone.value,
      password: Password.value
    })
    console.log('登录响应:', response);
    if (response && response.code === 200) {
      const {token, userInfo} = response.data

      // 保存token和用户信息
      localStorage.setItem('inspector_token', token)
      localStorage.setItem('inspector_info', JSON.stringify(userInfo))

      // 如果选择记住我，保存手机号和密码
      if (form.rememberMe) {
        localStorage.setItem('inspector_phone', Phone.value)
        localStorage.setItem('inspector_password', Password.value)
      } else {
        localStorage.removeItem('inspector_phone')
        localStorage.removeItem('inspector_password')
      }

      ElMessage.success('登录成功')
      // 跳转到网格选择页面
      router.push('/select-grid')
    } else {
      console.log('登录失败:', response?.msg);
      ElMessage.error(response.msg || '账号或密码不正确')
    }
  } catch (error) {
    console.error('登录错误:', error)
    if (error.response) {
      // 请求已发送，服务器响应状态码不在2xx范围内
      ElMessage.error(error.response.data?.msg || '登录失败')
    } else if (error.request) {
      // 请求已发送但没有收到响应
      ElMessage.error('网络请求失败，请检查后端服务')
    } else {
      // 请求配置出错
      ElMessage.error('请求配置错误: ' + error.message)
    }
  }
  // 在登录按钮点击事件中添加日志
  console.log('前端发送的密码:', JSON.stringify(Password.value));
  console.log('密码长度:', Password.value.length);
  console.log('每个字符的ASCII码:');
  let pwd = Password.value;
  for (let i = 0; i < pwd.length; i++) {
    console.log(`  [${i}]: '${pwd[i]}' (${pwd.charCodeAt(i)})`);
  }


}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background-color: #f1f1f1;
  display: flex;
}

.full-height {
  width: 100%;
  min-height: 100vh;
}

.login-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  margin-bottom: 20px;
}

.logo-section {
  text-align: center;
  margin-bottom: 24px;
}

.logo-img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.title-section {
  text-align: center;
  margin-bottom: 32px;
}

.title-section h3 {
  font-size: 24px;
  margin-bottom: 8px;
  color: #333;
}

.title-section h4 {
  font-size: 16px;
  color: #666;
}

.form-section {
  width: 100%;
}

.input-item {
  margin-bottom: 20px;
}

.remember-section {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.remember-section .el-checkbox {
  color: #666;
}

.input-item :deep(.el-form-item__label) {
  font-size: 16px;
  color: #333;
  padding-right: 12px;
}

.input-item :deep(.el-input__inner) {
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
}

.action-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.regist-link {
  font-size: 16px;
  color: #409eff;
}

.login-btn {
  width: 120px;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .login-card {
    padding: 24px;
    margin: 16px;
  }

  .title-section h3 {
    font-size: 20px;
  }

  .title-section h4 {
    font-size: 14px;
  }

  .input-item :deep(.el-form-item__label) {
    font-size: 14px;
  }

  .input-item :deep(.el-input__inner) {
    height: 44px;
    font-size: 14px;
  }

  .regist-link {
    font-size: 14px;
  }

  .login-btn {
    width: 100px;
    height: 44px;
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .action-section {
    flex-direction: column;
    gap: 16px;
  }

  .login-btn {
    width: 100%;
  }
}
</style>