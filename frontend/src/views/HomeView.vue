<script setup lang="ts">
import { useRouter } from 'vue-router'
import { NGrid, NGridItem, NCard, NIcon, NButton, NSpace, NCarousel } from 'naive-ui'
import {
    FilmOutline, BookOutline, ImagesOutline, GameControllerOutline,
    CloudOutline, CodeSlashOutline, ArrowForwardOutline
} from '@vicons/ionicons5'

const router = useRouter()

// 功能模块配置
const features = [
    { title: '影视中心', desc: '高清电影、剧集收藏', icon: FilmOutline, path: '/movies', color: '#70c0e8' },
    { title: '文章笔记', desc: '技术教程、随笔记录', icon: BookOutline, path: '/articles', color: '#63e2b7' },
    { title: '精彩相册', desc: '记录生活每一刻', icon: ImagesOutline, path: '/photos', color: '#f2c97d' },
    { title: '娱乐游戏', desc: '休闲小游戏集合', icon: GameControllerOutline, path: '/games', color: '#e88080' },
    { title: '私有网盘', desc: '文件存储与分享', icon: CloudOutline, path: '/drive', color: '#8a8af2' },
    { title: '代码仓库', desc: 'Web IDE 在线编程', icon: CodeSlashOutline, path: '/code', color: '#333333' }
]

const goTo = (path: string) => router.push(path)
</script>

<template>
    <div class="home-container">
        <div class="hero-section">
            <div class="hero-content">
                <h1 class="animate-title">Welcome to Kuang Space</h1>
                <p class="subtitle">探索技术，记录生活，分享创意</p>
                <div class="hero-actions">
                    <n-button type="primary" size="large" round class="start-btn" @click="goTo('/articles')">
                        开始探索
                        <template #icon><n-icon>
                                <ArrowForwardOutline />
                            </n-icon></template>
                    </n-button>
                    <n-button ghost size="large" round class="github-btn" tag="a"
                        href="https://github.com/Kuangsl/personal-site" target="_blank">
                        GitHub
                    </n-button>
                </div>
            </div>
            <div class="bg-circle circle-1"></div>
            <div class="bg-circle circle-2"></div>
        </div>

        <div class="features-container">
            <n-grid x-gap="24" y-gap="24" cols="1 600:2 900:3" responsive="screen">
                <n-grid-item v-for="feature in features" :key="feature.title">
                    <n-card hoverable class="feature-card" @click="goTo(feature.path)">
                        <template #header>
                            <div class="card-header">
                                <n-icon :size="32" :color="feature.color" class="card-icon">
                                    <component :is="feature.icon" />
                                </n-icon>
                                <span class="card-title">{{ feature.title }}</span>
                            </div>
                        </template>
                        <p class="card-desc">{{ feature.desc }}</p>
                    </n-card>
                </n-grid-item>
            </n-grid>
        </div>
    </div>
</template>

<style scoped>
.home-container {
    min-height: 100%;
}

/* --- Hero Section 样式 --- */
.hero-section {
    position: relative;
    height: 450px;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    text-align: center;
}

.hero-content {
    z-index: 10;
    padding: 20px;
}

.animate-title {
    font-size: 4rem;
    height: 80px;
    margin: 0 0 16px 0;
    background: linear-gradient(to right, #0683ff, #21be13);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    font-weight: 800;
}

.subtitle {
    font-size: 1.5rem;
    color: #5d6d7e;
    margin-bottom: 30px;
}

.hero-actions {
    display: flex;
    gap: 16px;
    justify-content: center;
}

/* 装饰性背景圆 */
.bg-circle {
    position: absolute;
    border-radius: 50%;
    filter: blur(60px);
    opacity: 0.6;
}

.circle-1 {
    width: 300px;
    height: 300px;
    background: #a1c4fd;
    top: -50px;
    left: -50px;
}

.circle-2 {
    width: 400px;
    height: 400px;
    background: #c2e9fb;
    bottom: -100px;
    right: -50px;
}

/* --- Features Section 样式 --- */
.features-container {
    max-width: 1200px;
    margin: -60px auto 0;
    /* 向上重叠一点 Hero */
    padding: 0 24px 48px;
    position: relative;
    z-index: 20;
}

.feature-card {
    border-radius: 16px;
    cursor: pointer;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    height: 100%;
    border: none;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.feature-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.card-header {
    display: flex;
    align-items: center;
    gap: 12px;
}

.card-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #2c3e50;
}

.card-desc {
    color: #7f8c8d;
    margin: 0;
    font-size: 0.95rem;
}
</style>