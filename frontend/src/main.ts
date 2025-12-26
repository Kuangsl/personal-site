import { createApp } from 'vue'
import { createPinia } from 'pinia' // 必须引入 Pinia
// import './style.css' // 如果你不需要默认的样式可以注释掉
import App from './App.vue'

const app = createApp(App)

// 1. 创建并挂载 Pinia 实例 (用于管理登录状态)
const pinia = createPinia()
app.use(pinia)

// 注意：我们暂时不使用 router，先确保基本功能跑通
// app.use(router)

app.mount('#app')