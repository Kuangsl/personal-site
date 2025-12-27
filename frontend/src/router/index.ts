import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import HomeView from '../views/HomeView.vue'
import Profile from '../views/Profile.vue'
import { useAuthStore } from '../stores/auth'


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: MainLayout, // 父路由加载布局（导航栏）
      children: [
        {
          path: '', // 空路径表示默认子路由
          name: 'Home',
          component: HomeView
        },
        {
          path: 'profile',
          name: 'Profile',
          component: Profile,
          meta: { requiresAuth: true },
        }
      ]
    },
    {
      // 匹配所有路径，正则 (.*)* 表示捕获任意字符
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('../views/NotFound.vue')
    }
  ]
})

// 👇 3. 核心逻辑：全局路由守卫
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore() // 在这里调用 store 是安全的

  // 判断逻辑：如果目标路由需要认证 (requiresAuth) 且 用户没有 Token
  if (to.meta.requiresAuth && !authStore.token) {
    // 强制跳转回主页
    next('/')

  } else {
    // 否则放行
    next()
  }
})

export default router