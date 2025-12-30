import { createApp } from 'vue'
import { createPinia } from 'pinia' // 必须引入 Pinia
import axios from 'axios' // 👈 引入 axios
import './style.css' // 如果你不需要默认的样式可以注释掉
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth' // 👈 引入 store

const app = createApp(App)

// 创建并挂载 Pinia 实例 (用于管理登录状态)
const pinia = createPinia()
app.use(pinia)
app.use(router)


axios.interceptors.request.use(config => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }
  return config
})

// 处理 401 token 过期自动登出
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      router.push('/')
    }
    return Promise.reject(error)
  }
)
// 挂载路由
app.mount('#app')