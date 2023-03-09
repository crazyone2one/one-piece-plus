import { defineStore } from 'pinia'
import { ref } from 'vue'

interface IUserState {
  id: string
  token: string
  username: string
  authorities: string[]
}
export const useUserStore = defineStore(
  'userStore',
  () => {
    const user = ref<IUserState>({
      id: '',
      username: '',
      token: '',
      authorities: [],
    })
    /**
     * 获取token值
     * @returns token
     */
    const getToken = (): string => {
      return user.value.token
    }
    const getSessionUser = (): IUserState => {
      return user.value
    }
    /**
     * 重置用户信息
     */
    const resetAuthStore = (): void => {
      user.value.id = ''
      user.value.username = ''
      user.value.token = ''
      user.value.authorities = []
    }
    const logout = () => {
      resetAuthStore()
    }
    return { user, getToken, getSessionUser, resetAuthStore, logout }
  },
  {
    persist: true,
  }
)
