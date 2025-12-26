import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import router from './router' // 引入路由

const app = createApp(App)

const pinia = createPinia()
app.use(pinia) // 1. 先挂载 Pinia (因为 router 里面可能用到 store)
app.use(router) // 2. 再挂载 Router

app.mount('#app')