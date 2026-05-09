import { defineStore } from 'pinia'

export const useNoticeStore = defineStore('notice', {
  state: () => ({
    count: 0
  }),
  actions: {
    add(msg) {
      this.count++
    },
    // ✅ 清除小红点
    clear() {
      this.count = 0
    }
  }
})