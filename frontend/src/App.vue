<script setup lang="ts">
  // 引入必要的 Provider
  import { NConfigProvider, NGlobalStyle, NMessageProvider, NResult, NButton } from 'naive-ui'
  // 引入状态 store
  import { useAuthStore } from './stores/auth'
  // 引入我们刚才写好的漂亮登录卡片
  import LoginCard from './components/LoginCard.vue'
  
  const authStore = useAuthStore()
  
  const handleLogout = () => {
    authStore.logout()
    // 登出后，authStore.token 变为 null，页面会自动跳回登录卡片
  }
  </script>
  
  <template>
    <n-config-provider>
      <n-message-provider>
        <n-global-style />
  
        <div class="app-container">
          <div v-if="!authStore.token" class="auth-container">
            <LoginCard />
          </div>
  
          <div v-else class="main-container">
            <n-result
              status="success"
              title="登录成功"
              :description="'你好，尊贵的已登录用户：' + authStore.username"
              size="huge"
            >
              <template #footer>
                <n-button type="error" @click="handleLogout">退出登录</n-button>
              </template>
            </n-result>
          </div>
        </div>
  
      </n-message-provider>
    </n-config-provider>
  </template>
  
  <style>
  /* 确保占满全屏，去掉默认边距 */
  body, html, #app {
    height: 100%;
    margin: 0;
    padding: 0;
  }
  
  .app-container {
    height: 100%;
    width: 100%;
  }
  
  /* 登录页容器 */
  .auth-container {
    height: 100%;
  }
  
  /* 主页容器 */
  .main-container {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #e6f7ff;
  }
  </style>