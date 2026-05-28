import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { getCurrentUser, login, logout, type LoginData, type LoginResult, type UserInfo } from '@/api/auth'

const TOKEN_KEY = 'VE_ACCESS_TOKEN'
const USER_KEY = 'VE_USER_INFO'

const readStoredUser = () => {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) return null
  try {
    return JSON.parse(raw) as UserInfo
  } catch {
    localStorage.removeItem(USER_KEY)
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const user = ref<UserInfo | null>(readStoredUser())

  const isAuthenticated = computed(() => Boolean(token.value && user.value))
  const permissions = computed(() => user.value?.permissions ?? [])
  const roles = computed(() => user.value?.roles ?? [])

  const setSession = (session: LoginResult) => {
    token.value = session.accessToken
    user.value = session.user
    localStorage.setItem(TOKEN_KEY, session.accessToken)
    localStorage.setItem(USER_KEY, JSON.stringify(session.user))
  }

  const clearSession = () => {
    token.value = ''
    user.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
    localStorage.removeItem('VETOKEN')
    localStorage.removeItem('token')
  }

  const signIn = async (data: LoginData) => {
    const session = await login(data)
    setSession(session)
  }

  const signOut = async () => {
    try {
      await logout()
    } catch (error) {
      console.warn(error)
    } finally {
      clearSession()
    }
  }

  const hydrateUser = async () => {
    if (!token.value) return
    try {
      const currentUser = await getCurrentUser()
      user.value = currentUser
      localStorage.setItem(USER_KEY, JSON.stringify(currentUser))
    } catch (error) {
      console.warn(error)
      clearSession()
    }
  }

  const hasPermission = (permission: string) => permissions.value.includes(permission)
  const hasAnyPermission = (data: string[]) => data.some((permission) => hasPermission(permission))
  const hasRole = (role: string) => roles.value.includes(role)

  return {
    token,
    user,
    roles,
    permissions,
    isAuthenticated,
    setSession,
    clearSession,
    signIn,
    signOut,
    hydrateUser,
    hasPermission,
    hasAnyPermission,
    hasRole
  }
})
