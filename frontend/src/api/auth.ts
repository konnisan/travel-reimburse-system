import request from '@/request'

export interface LoginData {
  username: string
  password: string
}

export interface UserInfo {
  id: string
  username: string
  displayName: string
  roles: string[]
  permissions: string[]
}

export interface LoginResult {
  accessToken: string
  tokenType?: string
  expiresIn?: number
  user: UserInfo
}

export function login(data: LoginData) {
  return request.post<LoginResult>('/auth/login', data, { loading: true })
}

export function logout() {
  return request.post<void>('/auth/logout')
}

export function getCurrentUser() {
  return request.get<UserInfo>('/auth/me')
}
