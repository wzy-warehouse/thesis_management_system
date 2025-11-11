import { defineStore } from 'pinia'
import { type Ref, ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const username: Ref<string> = ref('')
  const token: Ref<string> = ref('')

  return { username, token }
})
