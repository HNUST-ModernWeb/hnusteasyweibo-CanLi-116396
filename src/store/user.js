import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref({})
  const isLogin = ref(false)

  const login = (data) => {
    userInfo.value = data
    isLogin.value = true
  }

  const logout = () => {
    userInfo.value = {}
    isLogin.value = false
  }

  return {
    userInfo,
    isLogin,
    login,
    logout
  }
}, {
  persist: true
})