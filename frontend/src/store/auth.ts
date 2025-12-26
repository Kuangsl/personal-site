import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const username = ref<string | null>(localStorage.getItem('username'))

  const setAuth = (newToken: string, name: string) => {
    token.value = newToken
    username.value = name
    localStorage.setItem('token', newToken)
    localStorage.setItem('username', name)
  }

  const logout = () => {
    token.value = null
    username.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  return { token, username, setAuth, logout }
})