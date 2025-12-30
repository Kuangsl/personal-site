<script setup lang="ts">

import { ref, reactive } from 'vue'
import { useMessage, NCard, NTabs, NTabPane, NForm, NFormItem, NInput, NButton } from 'naive-ui'
import axios from 'axios'
import { useAuthStore } from '../stores/auth' // 引入我们之前写的 store

/**
 * 使用 Naive UI 的消息提示
 */
const message = useMessage()
// 使用 Pinia store
const authStore = useAuthStore()

// 当前模式是登录'login'还是注册'register'
const activeTab = ref('login')
const loading = ref(false)

// 表单数据
const formModel = reactive({
  username: '',
  password: '',
  confirmPassword: '' // 注册时多一个确认密码
})
// 定义触发器
const emit = defineEmits(['success'])
// 处理提交 (登录或注册)
const handleSubmit = async () => {
  // 1. 基本校验
  if (!formModel.username || !formModel.password) {
    message.warning('请输入用户名和密码')
    return
  }
  if (activeTab.value === 'register') {
    if (formModel.password !== formModel.confirmPassword) {
      message.error('两次输入的密码不一致')
      return
    }
  }
  loading.value = true
  try {
    // 根据模式决定调用哪个接口
    const url = activeTab.value === 'login' ? '/api/auth/login' : '/api/auth/register'
    // 发送请求
    const response = await axios.post(url, {
      username: formModel.username,
      password: formModel.password
    })

    // 处理成功响应
    if (activeTab.value === 'login') {
      // 登录成功：拿到 token 和 username，存入 store
      const { token, username } = response.data
      authStore.setAuth(token, username)
      await authStore.fetchUserInfo()
      message.success('登录成功，欢迎回来！')
      emit('success')
    } else {
      // 注册成功：切换到登录 Tab，让用户登录
      message.success('注册成功，请登录')
      activeTab.value = 'login'
      formModel.password = ''
      formModel.confirmPassword = ''
    }

  } catch (error: any) {
    // 处理失败响应:获取后端返回的错误信息，如果没有则显示默认信息
    const errorMsg = error.response?.data || '操作失败，请检查网络或联系管理员'
    message.error(typeof errorMsg === 'string' ? errorMsg : JSON.stringify(errorMsg))
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <n-card class="auth-card" @keyup.enter="handleSubmit">
    <n-tabs v-model:value="activeTab" size="large" justify-content="space-evenly" animated>
      <!-- 登录 -->
      <n-tab-pane name="login" tab="登 录">
        <n-form size="large" class="login-form">
          <n-form-item label="用户名">
            <n-input v-model:value="formModel.username" placeholder="请输入用户名" active-bar-bar />
          </n-form-item>
          <n-form-item label="密码">
            <n-input v-model:value="formModel.password" type="password" show-password-on="mousedown"
              placeholder="请输入密码" />
          </n-form-item>
          <n-button type="primary" block size="large" :loading="loading" @click="handleSubmit">
            立即登录
          </n-button>
        </n-form>
      </n-tab-pane>
      <!-- 注册 -->
      <n-tab-pane name="register" tab="注 册">
        <n-form size="large" class="login-form">
          <n-form-item label="用户名">
            <n-input v-model:value="formModel.username" placeholder="设置一个用户名" />
          </n-form-item>
          <n-form-item label="密码">
            <n-input v-model:value="formModel.password" type="password" show-password-on="mousedown"
              placeholder="设置密码 (建议复杂一点)" />
          </n-form-item>
          <n-form-item label="确认密码">
            <n-input v-model:value="formModel.confirmPassword" type="password" show-password-on="mousedown"
              placeholder="请再次输入密码" />
          </n-form-item>
          <n-button type="success" block size="large" :loading="loading" @click="handleSubmit">
            确认注册
          </n-button>
        </n-form>
      </n-tab-pane>
    </n-tabs>
  </n-card>
</template>

<style scoped>
.auth-card {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-form {
  margin-top: 20px;
  padding: 0 10px 20px;
}
</style>