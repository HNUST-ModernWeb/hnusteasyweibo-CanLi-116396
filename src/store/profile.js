import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useProfileStore = defineStore('profile', () => {
  const info = ref({
    name: '前端学习者',
    intro: '正在学习 Vue3 全栈开发'
  })

  const update = (data) => {
    info.value = { ...info.value, ...data }
  }

  return { info, update }
}, { persist: true })