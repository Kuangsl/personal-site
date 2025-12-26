<script setup lang="ts">
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { useMessage } from 'naive-ui'
    import axios from 'axios'
    import { useAuthStore } from '../stores/auth'
    
    const formValue = ref({ username: '', password: '' })
    const loading = ref(false)
    const router = useRouter()
    const message = useMessage()
    const authStore = useAuthStore()
    
    const handleLogin = async () => {
      if (!formValue.value.username || !formValue.value.password) {
        message.warning('请填写完整信息')
        return
      }
      loading.value = true
      try {
        // 这里的 /api/auth/login 需要在后端 AuthController 中实现
        const response = await axios.post('/api/auth/login', formValue.value)
        const { token, username } = response.data
        
        authStore.setAuth(token, username)
        message.success('欢迎回来')
        router.push('/') // 登录成功跳转首页
      } catch (error: any) {
        message.error(error.response?.data?.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
    </script>
    
    <template>
      <div class="login-container">
        <n-card title="管理系统登录" style="width: 380px">
          <n-form>
            <n-form-item label="账号">
              <n-input v-model:value="formValue.username" placeholder="输入用户名" />
            </n-form-item>
            <n-form-item label="密码">
              <n-input 
                v-model:value="formValue.password" 
                type="password" 
                show-password-on="mousedown" 
                placeholder="输入密码"
              />
            </n-form-item>
            <n-button type="primary" block :loading="loading" @click="handleLogin">
              进入系统
            </n-button>
          </n-form>
        </n-card>
      </div>
    </template>
    
    <style scoped>
    .login-container {
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #f5f7f9;
    }
    </style>