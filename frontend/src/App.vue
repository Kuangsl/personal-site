<script setup lang="ts">
  import { NConfigProvider, NGlobalStyle, NMessageProvider } from 'naive-ui'
  import { useAuthStore } from './store/auth'
  import Login from './views/Login.vue'
  
  const authStore = useAuthStore()
  </script>
  
  <template>
    <n-config-provider>
      <n-message-provider>
        <n-global-style />
        
        <div v-if="!authStore.token">
          <Login />
        </div>
        
        <div v-else class="main-content">
          <h1>你好, {{ authStore.username }}!</h1>
          <p>后端 API 端口: 8888</p>
          <n-button @click="authStore.logout">退出登录</n-button>
        </div>
        
      </n-message-provider>
    </n-config-provider>
  </template>
  
  <style>
  .main-content {
    padding: 40px;
    text-align: center;
  }
  </style>