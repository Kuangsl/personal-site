<script setup lang="ts">
import { ref, h } from 'vue'
import { useRouter } from 'vue-router'
import {
    NLayout, NLayoutHeader, NLayoutContent, NMenu, NButton, NSpace,
    NDropdown, NAvatar, NModal, NBackTop, NIcon, useMessage
} from 'naive-ui'
// 👇 关键修改：把 LanguageHs 改为 Language
import { PersonCircleOutline, LogOutOutline, Language } from '@vicons/ionicons5'
import { useAuthStore } from '../stores/auth'
import LoginCard from '../components/LoginCard.vue'


const authStore = useAuthStore()// 状态管理
const router = useRouter()
const showLoginModal = ref(false) // 控制登录弹窗
const message = useMessage()// 初始化 message

// 2. 导航菜单配置
const menuOptions = [
    { label: '主页', key: 'home', path: '/' },
    { label: '影视', key: 'movies', path: '/movies' },
    { label: '文章', key: 'articles', path: '/articles' },
    { label: '漫画', key: 'manga', path: '/manga' },
    { label: '小说', key: 'novels', path: '/novels' },
    { label: '照片', key: 'photos', path: '/photos' },
    { label: '游戏', key: 'games', path: '/games' },
    { label: '网盘', key: 'drive', path: '/drive' },
    { label: '代码', key: 'code', path: '/code' },
]

const activeKey = ref('home')

// 菜单点击跳转
const handleMenuUpdate = (key: string) => {
    activeKey.value = key
    const route = menuOptions.find(o => o.key === key)
    if (route) router.push(route.path)
}

// 3. 用户头像下拉菜单
const userOptions = [
    { label: '个人中心', key: 'profile', icon: () => h(NIcon, null, { default: () => h(PersonCircleOutline) }) },
    { label: '退出登录', key: 'logout', icon: () => h(NIcon, null, { default: () => h(LogOutOutline) }) }
]

const handleUserSelect = (key: string) => {
    if (key === 'logout') {
        authStore.logout()
        message.success('已登出')
        // 跳转回主页
        router.push('/')
    } else if (key === 'profile') {
        router.push('/profile')
    }
}

// 4. 登录成功回调
const onLoginSuccess = () => {
    showLoginModal.value = false
    // 这里不需要跳转，状态改变后 Navbar 会自动变成头像
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
            <n-space align="center" size="large">
                <n-button text style="font-size: 20px">
                    <n-icon>
                        <Language />
                    </n-icon>
                </n-button>
            </n-space>
            <div class="logo" @click="router.push('/')">
                <span style="font-size: 1.5rem; font-weight: bold; color: #18a058;">KuangSL</span>
            </div>

            <div class="nav-menu">
                <n-menu mode="horizontal" :value="activeKey" :options="menuOptions" @update:value="handleMenuUpdate" />
            </div>

            <n-space align="center" size="large">
                <n-button text style="font-size: 20px">
                    <n-icon>
                        <LanguageHs />
                    </n-icon>
                </n-button>

                <div v-if="authStore.token">
                    <n-dropdown trigger="hover" :options="userOptions" @select="handleUserSelect">
                        <n-space align="center" style="cursor: pointer;">
                            <n-avatar round size="small"
                                :src="authStore.avatarUrl || 'https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg'" />
                            <span>{{ authStore.username }}</span>
                        </n-space>
                    </n-dropdown>
                </div>

                <div v-else>
                    <n-button type="primary" round @click="showLoginModal = true">
                        登录 / 注册
                    </n-button>
                </div>
            </n-space>
        </n-layout-header>

        <n-layout-content style="min-height: calc(100vh - 64px); background-color: #f7f9fc;">
            <router-view v-slot="{ Component }">
                <transition name="fade" mode="out-in">
                    <component :is="Component" />
                </transition>
            </router-view>
        </n-layout-content>

        <n-back-top :right="40" :bottom="40" />

        <n-modal v-model:show="showLoginModal">
            <LoginCard @success="onLoginSuccess" style="width: 400px" />
        </n-modal>

    </n-layout>
</template>

<style scoped>
.logo {
    cursor: pointer;
    display: flex;
    align-items: center;
}

.nav-menu {
    flex: 1;
    display: flex;
    justify-content: center;
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
</style>