// 会议相关类型定义
export interface Conference {
  id: number
  title: string
  description: string
  content?: string
  startTime: string
  endTime: string
  location: string
  maxParticipants: number
  registrationCount: number
  categoryId: number // 新增
  categoryName: string
  status: number
  organizer?: string
  coverImage?: string
}

// 创建/更新会议的请求类型
export interface CreateConferenceRequest {
  title: string
  content: string
  startTime: string
  endTime: string
  location: string
  organizer: string
  categoryId: number
  coverImage: string
  status: number
}

// 更新会议的请求类型
export interface UpdateConferenceRequest {
  title: string
  content: string
  startTime: string
  endTime: string
  location: string
  organizer: string
  categoryId: number
  coverImage: string
  status: number
}

export interface ConferenceCategory {
  id: number
  name: string
  description: string
}

export interface ConferenceRegistration {
  conferenceId: number
  realName: string
  phone: string
  email: string
  company: string
  gender: number
  arrivalMethod?: string
  arrivalTrain?: string
  arrivalTime?: string
}

// 行业动态相关类型定义
export interface News {
  newsId: number
  title: string
  summary: string
  content: string
  publishTime: string
  createdTime?: string
  author?: string
  viewCount: number
  sortOrder?: number
  top?: number
  status?: number
  coverImage?: string
}

// 用户相关类型定义
export interface User {
  userId: number
  username: string
  email: string
  phone: string
  realName: string
  company: string
  createTime: string
  updateTime: string
  token?: string
  role?: string
}

// 登录响应类型定义
export interface LoginResponse extends User {
  token: string
  role?: string
}

// API响应类型定义
// 文件: index.ts

export class ApiResponse<T> {
  code: number
  message: string
  data: T

  // 使用构造函数来初始化属性
  constructor(code: number, message: string, data: T) {
    this.code = code
    this.message = message
    this.data = data
  }
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  currentPage: number
}

// 分页参数类型
export interface PageParams {
  page?: number
  size?: number
  categoryId?: number
  keyword?: string
}

// 会议查询参数
export interface ConferenceQueryParams {
  page?: number
  size?: number
  categoryId?: number
  keyword?: string
}

// 新闻查询参数
export interface NewsQueryParams {
  page?: number
  size?: number
  keyword?: string
}

// 空对象类型，用于替代unknown/any
export interface EmptyObject {}

// 会议分类详情类型
export interface ConferenceCategoryDetail {
  id: number
  conferenceId: number
  content: string
}

// 会议分类类型
export interface Category {
  id: number
  name: string
  description: string
  sortOrder: number
}

// 后端原始会议数据类型
export interface RawConference {
  conferenceId: number
  title: string
  description: string
  content?: string
  startTime: string
  endTime: string
  location: string
  maxParticipants: number
  currentParticipants: number
  status: number
  categoryId?: number
  organizer?: string
  coverImage?: string
  createdTime?: string
  category?: Category | null
}

export class SuperAdminUser {
  userId: number = 0
  username: string = ''
  email: string = ''
  phone: string = ''
  realName: string = ''
  company: string = ''
  role: string = ''
  createTime: string = ''
  updateTime: string = ''
}

export class UserBehaviorAnalysis {
  totalViews: number = 0
  totalLikes: number = 0
  totalFavorites: number = 0
  activeUsers: number = 0
}

export class TrendItem {
  date: string = ''
  views: number = 0
  likes: number = 0
  favorites: number = 0
}

export class SystemStats {
  totalUsers: number = 0
  superAdminCount: number = 0
  adminCount: number = 0
  userCount: number = 0
  newsCount: number = 0
  conferenceCount: number = 0
  totalViews: number = 0
  totalLikes: number = 0
  totalFavorites: number = 0
}

export class BehaviorAnalysisParams {
  startDate?: string
  endDate?: string
}

// 新闻权重更新参数
export interface SortOrderRequest {
  sortOrder: number
}

export interface StandardDetail {
  standardName: string
  standardDesc: string
  requirements: string
  process: string
  timeline: string
}

export interface TrainingDetail {
  standardName: string
  standardDesc: string
  requirements: string
  process: string
  timeline: string
}

export interface ToolDetail {
  standardName: string
  standardDesc: string
  requirements: string
  process: string
  timeline: string
}

export interface NewsStats {
  newsId: number
  views: number
  likes: number
  favorites: number
  comments: number
}

export interface NewsTrend {
  date: string
  views: number
  likes: number
  comments: number
}

