import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

/**
 * 用户身份信息
 */
export const useAuthStore = defineStore('auth', () => {
  // 定义状态 (State)
  const token = ref<string | null>(localStorage.getItem('token'))
  const username = ref<string | null>(localStorage.getItem('username'))
  const nickname = ref<string | null>(localStorage.getItem('nickname'))
  const avatarUrl = ref<string | null>(localStorage.getItem('avatarUrl'))
  const email = ref<string | null>(null)
  // 设置身份验证
  const setAuth = (newToken: string, name: string) => {
    token.value = newToken
    username.value = name
    localStorage.setItem('token', newToken)
    localStorage.setItem('username', name)
  }
  // 获取用户信息
  const fetchUserInfo = async () => {
    if (!token.value) return
    try {
      const res = await axios.get('/api/user/info')
      const data = res.data
      // 更新状态
      username.value = data.username
      nickname.value = data.nickname || data.username // 如果没昵称就显示用户名
      avatarUrl.value = data.avatarUrl
      email.value = data.email
      // 顺便更新本地缓存，保证下次刷新时有默认值
      if (data.nickname) localStorage.setItem('nickname', data.nickname)
      if (data.avatarUrl) localStorage.setItem('avatarUrl', data.avatarUrl)
    } catch (e) {
      console.error('获取用户信息失败', e)
    }
  }

  const logout = () => {
    token.value = null
    username.value = null
    nickname.value = null
    avatarUrl.value = null
    email.value = null

    // 清除所有缓存
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('nickname')
    localStorage.removeItem('avatarUrl')
  }

  return {
    token,
    username,
    nickname,
    avatarUrl,
    email,
    setAuth,
    fetchUserInfo,
    logout
  }
})