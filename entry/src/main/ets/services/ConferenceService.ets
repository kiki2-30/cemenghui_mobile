import { HttpService } from './HttpService'
import { 
  ApiResponse, 
  Conference, 
  ConferenceCategory, 
  ConferenceRegistration, 
  ConferenceQueryParams,
  PageResponse,
  EmptyObject,
  RawConference,
  StandardDetail,
  TrainingDetail,
  ToolDetail,
  CreateConferenceRequest,
  UpdateConferenceRequest
} from '../types/index'

export class ConferenceService {
  private httpService: HttpService = new HttpService()
  private baseUrl: string = 'http://172.22.113.34:8088' // 用主机实际IP和正确端口

  // 获取会议分类列表
  async getCategories(): Promise<ApiResponse<ConferenceCategory[]>> {
    return await this.httpService.get<ConferenceCategory[]>(`${this.baseUrl}/api/conferences/categories`)
  }

  // 获取会议列表（分页、筛选、搜索）
  async getConferences(params: ConferenceQueryParams): Promise<ApiResponse<PageResponse<RawConference>>> {
    const queryParams = new Array<string>()
    if (params.page !== undefined) queryParams.push(`page=${params.page}`)
    if (params.size !== undefined) queryParams.push(`size=${params.size}`)
    if (params.categoryId !== undefined) queryParams.push(`categoryId=${params.categoryId}`)
    if (params.keyword) queryParams.push(`keyword=${params.keyword}`)
    const queryString = queryParams.length > 0 ? `?${queryParams.join('&')}` : ''
    return await this.httpService.get<PageResponse<RawConference>>(`${this.baseUrl}/api/conferences${queryString}`)
  }

  // 获取会议详情
  async getConferenceDetail(conferenceId: number): Promise<ApiResponse<RawConference>> {
    return await this.httpService.get<RawConference>(`${this.baseUrl}/api/conferences/${conferenceId}`)
  }

  // 获取热门会议
  async getHotConferences(limit: number = 5): Promise<ApiResponse<Conference[]>> {
    return await this.httpService.get<Conference[]>(`${this.baseUrl}/api/conferences/hot?limit=${limit}`)
  }

  // 提交会议注册
  async registerConference(registrationData: ConferenceRegistration): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.post<EmptyObject, ConferenceRegistration>(`${this.baseUrl}/api/conferences/register`, registrationData)
  }

  // 检查是否已注册
  async checkRegistration(conferenceId: number, phone: string): Promise<ApiResponse<boolean>> {
    return await this.httpService.get<boolean>(`${this.baseUrl}/api/conferences/${conferenceId}/check-registration?phone=${phone}`)
  }

  // 获取标准定制详情
  async getStandardDetail(conferenceId: number): Promise<ApiResponse<StandardDetail | null>> {
    return await this.httpService.get<StandardDetail | null>(`${this.baseUrl}/api/conferences/${conferenceId}/standard-detail`)
  }

  // 新增标准定制详情
  async createStandardDetail(conferenceId: number, data: StandardDetail): Promise<ApiResponse<StandardDetail>> {
    return await this.httpService.post<StandardDetail, StandardDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/standard-detail`, data)
  }
  // 修改标准定制详情
  async updateStandardDetail(conferenceId: number, data: StandardDetail): Promise<ApiResponse<StandardDetail>> {
    return await this.httpService.put<StandardDetail, StandardDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/standard-detail`, data)
  }
  // 删除标准定制详情
  async deleteStandardDetail(conferenceId: number): Promise<ApiResponse<EmptyObject>> {
    return await this.httpService.delete<EmptyObject>(`${this.baseUrl}/api/conferences/${conferenceId}/standard-detail`)
  }

  // 获取技术培训详情
  async getTrainingDetail(conferenceId: number): Promise<ApiResponse<TrainingDetail | null>> {
    return await this.httpService.get<TrainingDetail | null>(`${this.baseUrl}/api/conferences/${conferenceId}/training-detail`)
  }
  // 新增技术培训详情
  async createTrainingDetail(conferenceId: number, data: TrainingDetail): Promise<ApiResponse<TrainingDetail>> {
    return await this.httpService.post<TrainingDetail, TrainingDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/training-detail`, data)
  }
  // 修改技术培训详情
  async updateTrainingDetail(conferenceId: number, data: TrainingDetail): Promise<ApiResponse<TrainingDetail>> {
    return await this.httpService.put<TrainingDetail, TrainingDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/training-detail`, data)
  }

  // 获取工具研发详情
  async getToolDetail(conferenceId: number): Promise<ApiResponse<ToolDetail | null>> {
    return await this.httpService.get<ToolDetail | null>(`${this.baseUrl}/api/conferences/${conferenceId}/tool-detail`)
  }
  // 新增工具研发详情
  async createToolDetail(conferenceId: number, data: ToolDetail): Promise<ApiResponse<ToolDetail>> {
    return await this.httpService.post<ToolDetail, ToolDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/tool-detail`, data)
  }
  // 修改工具研发详情
  async updateToolDetail(conferenceId: number, data: ToolDetail): Promise<ApiResponse<ToolDetail>> {
    return await this.httpService.put<ToolDetail, ToolDetail>(`${this.baseUrl}/api/conferences/${conferenceId}/tool-detail`, data)
  }

  // 新增会议
  async createConference(data: CreateConferenceRequest): Promise<ApiResponse<number>> {
    return await this.httpService.post<number, CreateConferenceRequest>(`${this.baseUrl}/api/conferences`, data)
  }

  // 编辑会议
  async updateConference(conferenceId: number, data: UpdateConferenceRequest): Promise<ApiResponse<null>> {
    return await this.httpService.put<null, UpdateConferenceRequest>(`${this.baseUrl}/api/conferences/${conferenceId}`, data)
  }

  // 删除会议
  async deleteConference(conferenceId: number): Promise<ApiResponse<null>> {
    return await this.httpService.delete<null>(`${this.baseUrl}/api/conferences/${conferenceId}`)
  }
} 