import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8888', // 后端地址
        changeOrigin: true,
        // 如果后端接口就是 /api/auth/... 则不需要 rewrite
        // 如果后端是 /auth/... 但前端请求 /api/auth/... 则需要 rewrite
      }
    }
  }
})