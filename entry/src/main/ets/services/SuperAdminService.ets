import { HttpService } from './HttpService'
import { StorageService } from './StorageService'
import { UserService } from './UserService'
import {
  SuperAdminUser,
  ApiResponse
} from '../types/index'

// --- 类型定义区 ---

// 用 class 明确声明 headers
export class RequestHeaders {
  Authorization: string;
  constructor(token: string) {
    this.Authorization = `Bearer ${token}`;
  }
}

// 新增2: 为用户行为分析的函数参数创建接口
export interface BehaviorAnalysisParams {
  startDate?: string;
  endDate?: string;
}

// (您原有的其他接口定义保持不变)
export interface TrendPointData {
  date?: string
  count?: number
}
export interface TrendPoint {
  date: string
  count: number
}
export interface BehaviorStatsData {
  totalViews?: number
  totalLikes?: number
  totalFavorites?: number
}
export interface BehaviorStats {
  totalViews: number
  totalLikes: number
  totalFavorites: number
}
export interface UserActivityData {
  totalUsers?: number
  activeUsers?: number
  activityRate?: number
}
export interface UserActivity {
  totalUsers: number
  activeUsers: number
  activityRate: number
}
export interface BehaviorAnalysisResponseData {
  viewTrend?: TrendPointData[]
  likeTrend?: TrendPointData[]
  favoriteTrend?: TrendPointData[]
  behaviorStats?: BehaviorStatsData
  userActivity?: UserActivityData
}
export interface BehaviorAnalysisResponse {
  viewTrend: TrendPoint[]
  likeTrend: TrendPoint[]
  favoriteTrend: TrendPoint[]
  behaviorStats: BehaviorStats
  userActivity: UserActivity
}
export interface SystemStatsUserStatsData {
  totalUsers?: number
  superAdmins?: number
  admins?: number
  normalUsers?: number
}
export interface SystemStatsUserStats {
  totalUsers: number
  superAdmins: number
  admins: number
  normalUsers: number
}
export interface SystemStatsContentStatsData {
  totalNews?: number
  totalConferences?: number
}
export interface SystemStatsContentStats {
  totalNews: number
  totalConferences: number
}
export interface SystemStatsResponseData {
  userStats?: SystemStatsUserStatsData
  contentStats?: SystemStatsContentStatsData
  behaviorStats?: BehaviorStatsData
}
export interface SystemStatsResponse {
  userStats: SystemStatsUserStats
  contentStats: SystemStatsContentStats
  behaviorStats: BehaviorStats
}
export interface TrendResponseData {
  viewTrend?: TrendPointData[]
  likeTrend?: TrendPointData[]
  favoriteTrend?: TrendPointData[]
}
export interface TrendResponse {
  viewTrend: TrendPoint[]
  likeTrend: TrendPoint[]
  favoriteTrend: TrendPoint[]
}

export class SuperAdminService {
  private httpService: HttpService = new HttpService()
  private baseUrl: string = 'http://172.22.113.34:8088'
  public userService: UserService = new UserService()

  private async getToken(): Promise<string> {
    return (await StorageService.getInstance().getUserToken()) || ''
  }

  // 获取所有用户
  async getAllUsers(): Promise<ApiResponse<SuperAdminUser[]>> {
    const token = await this.getToken()
    const headers = new RequestHeaders(token)
    return await this.httpService.get<SuperAdminUser[]>(
      `${this.baseUrl}/api/super-admin/users`,
      headers
    )
  }

  // 用户行为分析
  async getUserBehaviorAnalysis(params?: BehaviorAnalysisParams): Promise<ApiResponse<BehaviorAnalysisResponse>> {
    const token = await this.getToken()
    let query = ''
    if (params) {
      const arr: string[] = []
      if (params.startDate) arr.push(`startDate=${params.startDate}`)
      if (params.endDate) arr.push(`endDate=${params.endDate}`)
      if (arr.length > 0) query = '?' + arr.join('&')
    }
    const headers = new RequestHeaders(token)
    return await this.httpService.get<BehaviorAnalysisResponse>(
      `${this.baseUrl}/api/super-admin/user-behavior/analysis${query}`,
      headers
    )
  }

  // 系统统计
  async getSystemStats(): Promise<ApiResponse<SystemStatsResponse>> {
    const token = await this.getToken()
    const headers = new RequestHeaders(token)
    return await this.httpService.get<SystemStatsResponse>(
      `${this.baseUrl}/api/super-admin/system/stats`,
      headers
    )
  }

  async getBehaviorTrend(days?: number): Promise<ApiResponse<TrendResponse>> {
    const token = await this.getToken()
    const query = days ? `?days=${days}` : ''
    const headers = new RequestHeaders(token)
    return await this.httpService.get<TrendResponse>(
      `${this.baseUrl}/api/super-admin/user-behavior/trend${query}`,
      headers
    )
  }

  // 删除用户（带token）
  async deleteUser(userId: number): Promise<ApiResponse<boolean>> {
    const token = await this.getToken()
    const headers = new RequestHeaders(token)
    return await this.httpService.delete<boolean>(`${this.baseUrl}/user/${userId}`, headers)
  }
}