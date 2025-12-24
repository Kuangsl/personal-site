<script setup lang="ts">
import { ref } from 'vue'
// 引入 Naive UI 的组件
import { NConfigProvider, NGlobalStyle, NCard, NForm, NFormItem, NInput, NButton, NSpace } from 'naive-ui'

// 定义表单数据变量
const formValue = ref({
  username: '',
  password: ''
})

const handleLogin = () => {
  console.log("点击了登录", formValue.value)
  // 这里以后会写发送给后端的代码
}
</script>

<template>
  <!-- NConfigProvider 用于配置全局主题，这里先默认 -->
  <n-config-provider>
    <n-global-style />
    
    <!-- 外层容器，全屏居中 -->
    <div class="login-container">
      
      <!-- 登录卡片 -->
      <n-card title="个人空间 · 登录" style="width: 400px; box-shadow: 0 0 10px rgba(0,0,0,0.1)">
        <n-form>
          <n-form-item label="用户名">
            <n-input v-model:value="formValue.username" placeholder="请输入用户名/邮箱" />
          </n-form-item>
          
          <n-form-item label="密码">
            <n-input 
              v-model:value="formValue.password" 
              type="password" 
              show-password-on="click"
              placeholder="请输入密码" 
            />
          </n-form-item>
          
          <n-space justify="end">
            <n-button attr-type="button">注册</n-button>
            <n-button type="primary" @click="handleLogin">登录</n-button>
          </n-space>
        </n-form>
      </n-card>
      
    </div>
  </n-config-provider>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  height: 100vh;           /* 占满视口高度 */
  background-color: #f0f2f5; /* 浅灰背景 */
}
</style>
