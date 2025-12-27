<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { NResult, NButton } from 'naive-ui'
import { Telescope, ArrowBack } from '@vicons/ionicons5'

const router = useRouter()

// 语录库：保留你喜欢的随机文案
const quotes = [
    "生活总归带点荒谬，你访问的页面就像薛定谔的猫，它可能还没被写出来。",
    "这里是互联网的尽头，没有代码，只有无尽的虚无。",
    "错误 404：我们翻遍了 CPU 的缓存，甚至去隔壁服务器看了看，真的没找到。",
    "页面可能去火星种土豆了，暂时无法与地球建立连接。",
    "其实页面一直都在，只是以你目前的权限等级，还无法观测到它。",
    "可能是你的引力波太强，把这个页面扭曲到了另一个维度。",
    "这个页面正在进行量子纠缠，目前处于“存在”与“不存在”的叠加态。",
    "嘿，你是不是迷路了？还是说...你在寻找传说中的 One Piece？",
    "服务器君表示：它对这个 URL 没有任何印象，并向你扔了一个异常。",
    "生活就像 HTTP 状态码，除了 200 OK，偶尔总得面对几个 404。",
]

const currentQuote = ref('')

onMounted(() => {
    const index = Math.floor(Math.random() * quotes.length)
    currentQuote.value = quotes[index]!
})
</script>

<template>
    <div class="not-found-container">
        <div class="content-wrapper">
            <n-result status="404" title="404 资源不存在" :description="currentQuote" size="huge">
                <template #footer>
                    <div class="action-box">
                        <n-button type="primary" size="large" round @click="router.push('/')">
                            <template #icon>
                                <Telescope />
                            </template>
                            返回地球
                        </n-button>
                        <n-button secondary size="large" round @click="router.go(-1)">
                            <template #icon>
                                <ArrowBack />
                            </template>
                            撤回一步
                        </n-button>
                    </div>
                </template>
            </n-result>
        </div>
    </div>
</template>

<style scoped>
.not-found-container {
    /* 🔥 关键修复：强制高度撑满可视区域，减去顶部导航栏的大概高度 */
    min-height: calc(100vh);
    width: 100%;

    /* Flex 居中三件套 */
    display: flex;
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */

    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    padding: 20px;
    /* 防止小屏幕贴边 */
    box-sizing: border-box;
}

.content-wrapper {
    max-width: 600px;
    width: 100%;
    text-align: center;
    padding: 40px;
    background: rgba(255, 255, 255, 0.45);
    backdrop-filter: blur(12px);
    border-radius: 24px;
    box-shadow: 0 8px 32px rgba(31, 38, 135, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.6);

    /* 简单的入场动画 */
    animation: popIn 0.6s cubic-bezier(0.18, 0.89, 0.32, 1.28);
}

.action-box {
    display: flex;
    justify-content: center;
    gap: 16px;
    margin-top: 24px;
}

@keyframes popIn {
    from {
        opacity: 0;
        transform: scale(0.9) translateY(20px);
    }

    to {
        opacity: 1;
        transform: scale(1) translateY(0);
    }
}
</style>