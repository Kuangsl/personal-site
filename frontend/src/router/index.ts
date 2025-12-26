import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth' // 引入你的 Pinia store
import Login from '../views/Login.vue'

// 定义路由表
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        name: 'Dashboard',
        // 这里只是个临时主页，之后你可以替换成真正的 Dashboard 组件
        component: () => import('../components/HelloWorld.vue'), 
        meta: { requiresAuth: true } // 标记需要登录
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 🔥 路由守卫：防止未登录直接访问主页
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    
    // 如果要去的地方需要登录，且目前没有 token
    if (to.meta.requiresAuth && !authStore.token) {
        next('/login') // 强制踢回登录页
    } else {
        next() // 放行
    }
})

export default router