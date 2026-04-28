import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// 引入 node 的 path 模块，用于处理路径
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      // 配置别名：@ 代表 src 目录
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/system': {
        target: 'http://localhost:8080',
        changeOrigin: true
        // 不需要 rewrite，因为路径就是 /system/city/provinces
      }
    }
  }
})