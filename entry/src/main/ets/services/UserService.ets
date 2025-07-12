import { HttpService } from './HttpService'
import { User, LoginResponse, ApiResponse } from '../types/index'

interface RegisterUserData {
  username: string
  password: string
  email: string
  phone: string
  realName: string
  company: string
}

interface LoginCredentials {
  username: string
  password: string
}

export class UpdateUserData {
  username: string = '';
  password?: string;
  email: string = '';
  phone: string = '';
  realName: string = '';
  company: string = '';
}

interface WechatLoginRequest {
  code: string
}

interface UniqueCheckResponse {
  unique: boolean
}

export class UserService {
  private httpService: HttpService = new HttpService()
  private baseUrl: string = 'http://172.22.113.34:8088' // 用主机实际IP和正确端口

  // 用户注册
  async register(userData: RegisterUserData): Promise<ApiResponse<User>> {
    return await this.httpService.post<User, RegisterUserData>(`${this.baseUrl}/user/register`, userData)
  }

  // 用户登录
  async login(credentials: LoginCredentials): Promise<ApiResponse<LoginResponse>> {
    console.info('[UserService] 登录请求参数:', {
      username: credentials.username,
      password: '*'.repeat(credentials.password.length),
      passwordLength: credentials.password.length
    })
    console.info('[UserService] 密码是否为空:', credentials.password === '')
    console.info('[UserService] 密码是否包含空格:', credentials.password.includes(' '))
    console.info('[UserService] 密码trim后长度:', credentials.password.trim().length)
    
    return await this.httpService.post<LoginResponse, LoginCredentials>(`${this.baseUrl}/user/login`, credentials)
  }

  // 获取用户信息
  async getUserInfo(userId: number): Promise<ApiResponse<User>> {
    return await this.httpService.get<User>(`${this.baseUrl}/user/${userId}`)
  }

  // 根据用户名获取用户信息
  async getUserByUsername(username: string): Promise<ApiResponse<User>> {
    return await this.httpService.get<User>(`${this.baseUrl}/user/username/${username}`)
  }

  // 更新用户信息
  async updateUser(userId: number, userData: UpdateUserData): Promise<ApiResponse<User>> {
    return await this.httpService.put<User, UpdateUserData>(`${this.baseUrl}/user/${userId}`, userData)
  }

  // 删除用户
  async deleteUser(userId: number): Promise<ApiResponse<boolean>> {
    return await this.httpService.delete<boolean>(`${this.baseUrl}/user/${userId}`)
  }

  // 检查用户名唯一性
  async checkUsername(username: string): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/user/check-username/${username}`)
  }

  // 检查邮箱唯一性
  async checkEmail(email: string): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/user/check-email/${email}`)
  }

  // 检查手机号唯一性
  async checkPhone(phone: string): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/user/check-phone/${phone}`)
  }

  // 微信登录（需要补充接口）
  async wechatLogin(code: string) {
    const req: WechatLoginRequest = { code }
    return await this.httpService.post<User, WechatLoginRequest>(`${this.baseUrl}/user/wechat-login`, req)
  }
}