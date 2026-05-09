<template>
  <nav>
    <router-link to="/">首页</router-link>
    <router-link to="/publish">发布</router-link>
    <router-link to="/profile">
      我的
      <span v-if="notice.count > 0" class="badge">{{ notice.count }}</span>
    </router-link>
    <button class="theme" @click="theme.darkMode = !theme.darkMode">
      {{ theme.darkMode ? '☀️' : '🌙' }}
    </button>
    <button v-if="!user.isLogin" class="login-btn" @click="$router.push('/login')">
      登录
    </button>
    <button v-if="user.isLogin" class="logout" @click="user.logout()">退出</button>
  </nav>
</template>

<script setup>
import { useUserStore } from '../store/user'
import { useThemeStore } from '../store/theme'
import { useNoticeStore } from '../store/notice'
const user = useUserStore()
const theme = useThemeStore()
const notice = useNoticeStore()
</script>

<style scoped>
nav {
  background: #4263eb;
  padding: 16px;
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}
nav a, nav button {
  color: white;
  text-decoration: none;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
}
.badge {
  background: red;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  margin-left: 4px;
}
</style>