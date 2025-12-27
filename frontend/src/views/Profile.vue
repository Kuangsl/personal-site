<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import {
    NCard, NAvatar, NTabs, NTabPane,
    NButton, NInput, NForm, NFormItem, useMessage, NUpload, type UploadCustomRequestOptions
} from 'naive-ui'
import { useAuthStore } from '../stores/auth'
// 👇 1. 修复：User 改为 Person
import { Person, Pencil } from '@vicons/ionicons5'
import axios from 'axios'

const authStore = useAuthStore()
const message = useMessage()

// 表单数据
const profileForm = reactive({
    nickname: '',
    email: '',
    avatarUrl: ''
})


// 挂载时拉取最新数据，确保刷新不丢失
onMounted(async () => {
    // 先尝试从后端拉取最新数据
    await authStore.fetchUserInfo()

    // 然后填入表单
    profileForm.nickname = authStore.nickname || authStore.username || ''
    profileForm.email = authStore.email || ''
    profileForm.avatarUrl = authStore.avatarUrl || ''
})

// 自定义上传逻辑
const customRequest = async ({ file, onFinish, onError }: UploadCustomRequestOptions) => {
    const formData = new FormData()
    formData.append('file', file.file as File)

    try {
        message.loading('正在上传...')
        // 调用后端上传接口
        const res = await axios.post('/api/upload/avatar', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        })
        // 后端现在会返回新 URL
        const newAvatarUrl = res.data
        // 更新表单显示
        profileForm.avatarUrl = newAvatarUrl

        // 更新全局 Store
        authStore.avatarUrl = newAvatarUrl

        // 顺便保存到用户资料里
        await handleUpdateProfile()
        message.success('头像修改成功')
        onFinish()
    } catch (e) {
        message.error('上传失败')
        onError()
    }
}

// 更新资料
const handleUpdateProfile = async () => {
    try {
        message.loading('正在保存...')
        await axios.post('/api/user/update', {
            nickname: profileForm.nickname,
            avatarUrl: profileForm.avatarUrl, // 确保把当前头像也带上
            email: profileForm.email
        })
        // 如果修改了昵称，也要更新 store
        authStore.username = profileForm.nickname
        await authStore.fetchUserInfo()
        message.success('资料已保存')
    } catch (e) {
        message.success('资料保存失败!')
        console.error(e)
    }
}
</script>
<template>
    <div class="profile-container">
        <div class="profile-content">
            <n-card class="profile-side">
                <div class="avatar-wrapper">
                    <n-upload action="/api/upload/avatar" :custom-request="customRequest" :show-file-list="false"
                        accept="image/*">
                        <div class="avatar-hover-mask">
                            <n-avatar :size="120" :src="profileForm.avatarUrl || undefined" class="user-avatar">
                                <n-icon v-if="!profileForm.avatarUrl">
                                    <Person />
                                </n-icon>
                            </n-avatar>
                            <div class="edit-hint"><n-icon>
                                    <Pencil />
                                </n-icon> 换头像</div>
                        </div>
                    </n-upload>

                    <h2 class="username">{{ authStore.username || '未登录用户' }}</h2>
                </div>
            </n-card>

            <n-card class="profile-main">
                <n-tabs type="line" animated>
                    <n-tab-pane name="info" tab="基本资料">
                        <n-form label-placement="left" label-width="80">
                            <n-form-item label="昵称">
                                <n-input v-model:value="profileForm.nickname" placeholder="请输入昵称" />
                            </n-form-item>
                            <n-button type="primary" @click="handleUpdateProfile">保存修改</n-button>
                        </n-form>
                    </n-tab-pane>
                </n-tabs>
            </n-card>
        </div>
    </div>
</template>

<style scoped>
.profile-container {
    max-width: 1000px;
    margin: 40px auto;
    padding: 0 20px;
}

.profile-content {
    display: flex;
    gap: 24px;
    flex-wrap: wrap;
}

/* 左侧样式 */
.profile-side {
    flex: 1;
    min-width: 300px;
    text-align: center;
    border-radius: 12px;
}

.avatar-wrapper {
    margin-bottom: 24px;
}

.user-avatar {
    border: 4px solid #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 16px;
}

.username {
    margin: 8px 0;
    color: #333;
}

.bio {
    color: #666;
    font-style: italic;
    margin-top: 20px;
}

/* 右侧样式 */
.profile-main {
    flex: 2;
    min-width: 400px;
    border-radius: 12px;
}

.tab-action {
    margin-top: 24px;
    display: flex;
    justify-content: flex-end;
}

.profile-container {
    max-width: 1000px;
    margin: 40px auto;
    padding: 0 20px;
}

.profile-content {
    display: flex;
    gap: 24px;
}

.profile-side {
    flex: 1;
    text-align: center;
}

.profile-main {
    flex: 2;
}

/* 头像悬停效果 */
.avatar-wrapper {
    position: relative;
    display: inline-block;
    cursor: pointer;
}

.user-avatar {
    display: block;
}

.edit-hint {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(79, 250, 73, 0.5);
    color: #fff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.avatar-hover-mask:hover .edit-hint {
    opacity: 1;
}
</style>