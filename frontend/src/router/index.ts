import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import HomeView from '../views/HomeView.vue'
import Profile from '../views/Profile.vue'
import { useAuthStore } from '../stores/auth'
import VideoView from '../views/VideoView.vue'
import NullView from '../views/NullView.vue'
import NotFound from '../views/NotFound.vue'


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
        }, {
          path: '/videos',
          name: 'videos',
          component: VideoView
        }, {
          path: '/articles',
          name: 'articles',
          component: NullView
        }, {
          path: '/manga',
          name: 'manga',
          component: NullView
        }, {
          path: '/novels',
          name: 'novels',
          component: NullView
        }, {
          path: '/photos',
          name: 'photos',
          component: NullView
        }, {
          path: '/games',
          name: 'games',
          component: NullView
        }, {
          path: '/cloud',
          name: 'cloud',
          component: NullView
        }, {
          path: '/repository',
          name: 'repository',
          component: NullView
        }, {
          path: '/profile',
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
      component: NotFound
    }
  ]
})

// 全局路由守卫
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