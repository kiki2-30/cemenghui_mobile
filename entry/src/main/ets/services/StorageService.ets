import preferences from '@ohos.data.preferences'
import { User } from '../types/index'

export class StorageService {
  private static instance: StorageService
  private preferencesHelper: preferences.Preferences | null = null
  private readonly STORE_NAME = 'user_preferences'
  private readonly USER_TOKEN_KEY = 'user_token'
  private readonly USER_INFO_KEY = 'user_info'
  private readonly IS_LOGGED_IN_KEY = 'is_logged_in'
  private readonly USER_ROLE_KEY = 'user_role'

  private constructor() {}

  static getInstance(): StorageService {
    if (!StorageService.instance) {
      StorageService.instance = new StorageService()
    }
    return StorageService.instance
  }

  async init() {
    try {
      const context = getContext(this)
      this.preferencesHelper = await preferences.getPreferences(context, this.STORE_NAME)
    } catch (error) {
      console.error('初始化存储服务失败:', error)
    }
  }

  // 保存用户token
  async saveUserToken(token: string) {
    if (!this.preferencesHelper) return
    try {
      await this.preferencesHelper.put(this.USER_TOKEN_KEY, token)
      await this.preferencesHelper.flush()
    } catch (error) {
      console.error('保存用户token失败:', error)
    }
  }

  // 获取用户token
  async getUserToken(): Promise<string | null> {
    if (!this.preferencesHelper) return null
    try {
      return await this.preferencesHelper.get(this.USER_TOKEN_KEY, '') as string
    } catch (error) {
      console.error('获取用户token失败:', error)
      return null
    }
  }

  // 保存用户信息
  async saveUserInfo(user: User): Promise<void> {
    if (!this.preferencesHelper) return
    try {
      await this.preferencesHelper.put(this.USER_INFO_KEY, JSON.stringify(user))
      if (user.role) {
        await this.preferencesHelper.put(this.USER_ROLE_KEY, user.role)
      }
      await this.preferencesHelper.flush()
    } catch (error) {
      console.error('保存用户信息失败:', error)
    }
  }

  // 获取用户信息
  async getUserInfo(): Promise<User | null> {
    if (!this.preferencesHelper) return null
    try {
      const userInfoStr = await this.preferencesHelper.get(this.USER_INFO_KEY, '') as string
      return userInfoStr ? JSON.parse(userInfoStr) as User : null
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return null
    }
  }

  // 设置登录状态
  async setLoginStatus(isLoggedIn: boolean) {
    if (!this.preferencesHelper) return
    try {
      await this.preferencesHelper.put(this.IS_LOGGED_IN_KEY, isLoggedIn)
      await this.preferencesHelper.flush()
    } catch (error) {
      console.error('设置登录状态失败:', error)
    }
  }

  // 获取登录状态
  async getLoginStatus(): Promise<boolean> {
    if (!this.preferencesHelper) return false
    try {
      return await this.preferencesHelper.get(this.IS_LOGGED_IN_KEY, false) as boolean
    } catch (error) {
      console.error('获取登录状态失败:', error)
      return false
    }
  }

  // 清除所有用户数据
  async clearUserData() {
    if (!this.preferencesHelper) return
    try {
      await this.preferencesHelper.delete(this.USER_TOKEN_KEY)
      await this.preferencesHelper.delete(this.USER_INFO_KEY)
      await this.preferencesHelper.delete(this.IS_LOGGED_IN_KEY)
      await this.preferencesHelper.delete(this.USER_ROLE_KEY)
      await this.preferencesHelper.flush()
    } catch (error) {
      console.error('清除用户数据失败:', error)
    }
  }

  // 检查是否已登录
  async isLoggedIn(): Promise<boolean> {
    const token = await this.getUserToken()
    const loginStatus = await this.getLoginStatus()
    return !!(token && loginStatus)
  }

  async getUserRole(): Promise<string | null> {
    const role = await this.preferencesHelper?.get(this.USER_ROLE_KEY, '') as string
    return role ? role : null
  }
} 