import { HttpService } from './HttpService'
import { 
  ApiResponse, 
  News, 
  NewsQueryParams,
  PageResponse,
  EmptyObject,
  SortOrderRequest,
  NewsTrend,
  NewsStats
} from '../types/index'

// 定义具体的接口类型
interface LikeStatus {
  liked: boolean
}

interface FavoriteStatus {
  favorited: boolean
}

interface BatchNewsStatsRequest {
  newsIds: number[]
}

export interface NewsEditRequest {
  title: string
  summary: string
  content: string
  imageUrl: string
  publishTime: string
  author?: string
  status: number // 只用上架/下架
  top?: boolean
}

export interface NewsStatusRequest {
  status: number
}

export interface NewsTopRequest {
  top: number
}

export class NewsService {
  private httpService: HttpService = new HttpService()
  private baseUrl: string = 'http://172.22.113.34:8088' // 用主机实际IP和正确端口

  // 获取行业动态列表
  async getNewsList(params: NewsQueryParams): Promise<ApiResponse<PageResponse<News>>> {
    const queryParams = new Array<string>()
    if (params.page !== undefined) queryParams.push(`page=${params.page}`)
    if (params.size !== undefined) queryParams.push(`size=${params.size}`)
    if (params.keyword) queryParams.push(`keyword=${params.keyword}`)

    const queryString = queryParams.length > 0 ? `?${queryParams.join('&')}` : ''
    return await this.httpService.get<PageResponse<News>>(`${this.baseUrl}/api/news/list${queryString}`)
  }

  // 获取行业动态详情
  async getNewsDetail(newsId: number): Promise<ApiResponse<News>> {
    return await this.httpService.get<News>(`${this.baseUrl}/api/news/${newsId}`)
  }

  // 记录浏览
  async recordView(newsId: number, userId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject>(`${this.baseUrl}/api/news/${newsId}/view?userId=${userId}`)
  }

  // 点赞
  async likeNews(newsId: number, userId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject>(`${this.baseUrl}/api/news/${newsId}/like?userId=${userId}`)
  }

  // 取消点赞
  async unlikeNews(newsId: number, userId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject>(`${this.baseUrl}/api/news/${newsId}/unlike?userId=${userId}`)
  }

  // 收藏
  async favoriteNews(newsId: number, userId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject>(`${this.baseUrl}/api/news/${newsId}/favorite?userId=${userId}`)
  }

  // 取消收藏
  async unfavoriteNews(newsId: number, userId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject>(`${this.baseUrl}/api/news/${newsId}/unfavorite?userId=${userId}`)
  }

  // 查询点赞状态
  async getLikeStatus(newsId: number, userId: number): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/api/news/${newsId}/like-status?userId=${userId}`)
  }

  // 查询收藏状态
  async getFavoriteStatus(newsId: number, userId: number): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/api/news/${newsId}/favorite-status?userId=${userId}`)
  }

  // 获取统计数据
  async getNewsStats(newsId: number): Promise<ApiResponse<NewsStats>> {
    return await this.httpService.get<NewsStats>(`${this.baseUrl}/api/news/${newsId}/stats`)
  }

  // 批量获取统计数据
  async getBatchNewsStats(newsIds: number[]): Promise<ApiResponse<NewsStats[]>> {
    const requestData: BatchNewsStatsRequest = { newsIds: newsIds }
    return await this.httpService.post<NewsStats[], BatchNewsStatsRequest>(`${this.baseUrl}/api/news/stats`, requestData)
  }

  // 新增行业动态
  async addNews(newsData: NewsEditRequest): Promise<ApiResponse<News>> {
    return await this.httpService.post<News, NewsEditRequest>(`${this.baseUrl}/api/news`, newsData)
  }

  // 编辑行业动态
  async editNews(newsId: number, newsData: NewsEditRequest): Promise<ApiResponse<News>> {
    return await this.httpService.put<News, NewsEditRequest>(`${this.baseUrl}/api/news/${newsId}`, newsData)
  }

  // 删除行业动态
  async deleteNews(newsId: number): Promise<ApiResponse<boolean>> {
    return await this.httpService.delete<boolean>(`${this.baseUrl}/api/news/${newsId}`)
  }

  // 上下架行业动态
  async updateNewsStatus(newsId: number, status: number): Promise<ApiResponse<News>> {
    const req: NewsStatusRequest = { status }
    return await this.httpService.put<News, NewsStatusRequest>(`${this.baseUrl}/api/news/${newsId}/status`, req)
  }

  // 置顶行业动态
  async topNews(newsId: number, top: number): Promise<ApiResponse<News>> {
    const req: NewsTopRequest = { top }
    return await this.httpService.put<News, NewsTopRequest>(`${this.baseUrl}/api/news/${newsId}/top`, req)
  }

  // 更新新闻权重排序
  async updateSortOrder(newsId: number, sortOrder: number): Promise<ApiResponse<EmptyObject>> {
    const req: SortOrderRequest = { sortOrder }
    return await this.httpService.put<EmptyObject, SortOrderRequest>(
      `${this.baseUrl}/api/news/${newsId}/sort-order`,
      req
    )
  }

  // 获取新闻行为趋势
  async getNewsBehaviorTrend(type: string = 'day'): Promise<ApiResponse<NewsTrend[]>> {
    return await this.httpService.get<NewsTrend[]>(`${this.baseUrl}/api/news/behavior/trend?type=${type}`)
  }
} 