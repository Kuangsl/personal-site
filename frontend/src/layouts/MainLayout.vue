<script setup lang="ts">
import { ref, h } from 'vue'
import { useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent, NMenu, NButton, NSpace,
  NDropdown, NAvatar, NModal, NBackTop, NIcon, useMessage, NSplit
} from 'naive-ui'
import { PersonCircleOutline, LogOutOutline } from '@vicons/ionicons5'
import { useAuthStore } from '../stores/auth'
import LoginCard from '../components/LoginCard.vue'

const authStore = useAuthStore()// 状态管理
const router = useRouter()// 路由跳转
const showLoginModal = ref(false) // 控制登录弹窗
const message = useMessage()// 初始化 message

// 导航菜单配置
const menuOptions = [
  { label: '主页', key: 'home', path: '/' },
  { label: '影视', key: 'videos', path: '/videos' },
  { label: '文章', key: 'articles', path: '/articles' },
  { label: '漫画', key: 'manga', path: '/manga' },
  { label: '小说', key: 'novels', path: '/novels' },
  { label: '照片', key: 'photos', path: '/photos' },
  { label: '游戏', key: 'games', path: '/games' },
  { label: '网盘', key: 'cloud', path: '/cloud' },
  { label: '代码', key: 'repository', path: '/repository' }
]

const activeKey = ref('home')

// 菜单点击跳转
const handleMenuUpdate = (key: string) => {
  activeKey.value = key
  const route = menuOptions.find(o => o.key === key)
  if (route) router.push(route.path)
}

// 用户头像下拉菜单
const userOptions = [
  { label: '个人中心', key: 'profile', icon: () => h(NIcon, null, { default: () => h(PersonCircleOutline) }) },
  { label: '退出登录', key: 'logout', icon: () => h(NIcon, null, { default: () => h(LogOutOutline) }) }
]

const handleUserSelect = (key: string) => {
  if (key === 'profile') {
    router.push('/profile')
  } else if (key === 'logout') {
    authStore.logout()
    message.success('已登出')
    router.push('/')// 跳转回主页
  }
}

// 4. 登录成功回调
const onLoginSuccess = () => {
  showLoginModal.value = false
}

// 样式：吸顶
const headerStyle = {
  position: 'sticky',
  top: 0,
  zIndex: 1000,
  height: '64px',
  padding: '0 24px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
  background: 'rgba(255, 255, 255, 0.95)', // 稍微有点透明度
  backdropFilter: 'blur(10px)'
} as const
</script>

<template>
  <n-layout position="absolute">
    <n-layout-header :style="headerStyle" bordered>
      <!-- logo -->
      <div @click="router.push('/')">
        <span class="logo">KuangSL</span>
      </div>
      <!-- 菜单 -->
      <div> <n-menu responsive mode="horizontal" :value="activeKey" :options="menuOptions"
          @update:value="handleMenuUpdate" />
      </div>

      <!-- 图标 -->
      <div v-if="authStore.token" style="width:max-content;">
        <n-dropdown trigger="hover" :options="userOptions" @select="handleUserSelect">
          <n-space align="center" style="cursor: pointer;">
            <n-avatar round size="large" :src="authStore.avatarUrl || '/uploads/default.png'" />
            <span>{{ authStore.nickname || authStore.username }}</span>
          </n-space>
        </n-dropdown>
      </div>
      <div v-else>
        <n-button type="primary" round @click="showLoginModal = true">
          登录 / 注册
        </n-button>
      </div>

    </n-layout-header>
    <!-- 内容 -->
    <n-layout-content class="content" style="">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </n-layout-content>
    <!-- 返回顶部按钮 -->
    <n-back-top :right="20" :bottom="20" style="z-index: 100" />
    <!-- 登入弹窗 -->
    <n-modal v-model:show="showLoginModal">
      <LoginCard @success="onLoginSuccess" style="width: 400px" />
    </n-modal>

  </n-layout>
</template>

<style scoped>
* {
  font-size: medium
}

/* 简单的淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.logo {
  cursor: pointer;
  display: flex;
  align-items: center;
  flex-shrink: 0;
  font-size: 1.5rem;
  font-weight: bold;
  color: #18a058;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: bold;
  color: #18a058;
}

/* 中间菜单区域占据剩余空间 */
.nav-menu {
  flex: 1 1 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 0;
  /* 允许收缩，避免溢出 */
}

.content {
  min-height: calc(100vh - 64px);
  background-color: #f7f9fc;
}
</style>