// 文件: HttpService.ets
import http from '@ohos.net.http'
// 确保 ApiResponse 是一个 class，并且有构造函数
import { ApiResponse, EmptyObject } from '../types/index'
import { StorageService } from './StorageService'
import { RequestHeaders } from './SuperAdminService'

interface LoginData {
  username?: string;
  password?: string;
}

// 修改点1：改造为泛型接口，避免使用 any
interface RequestLogInfo<D> {
  url: string;
  method: string;
  headers: Record<string, string>;
  data?: D;
}

export class HttpService {
  private timeout: number = 10000

  // GET请求
  async get<T = EmptyObject>(url: string, headers?: RequestHeaders): Promise<ApiResponse<T>> {
    return await this.request<T>(http.RequestMethod.GET, url, undefined, headers)
  }

  // POST请求
  async post<T = EmptyObject, D = EmptyObject>(url: string, data?: D, headers?: RequestHeaders): Promise<ApiResponse<T>> {
    // (登录日志部分无需修改)
    return await this.request<T, D>(http.RequestMethod.POST, url, data, headers)
  }

  // PUT请求
  async put<T = EmptyObject, D = EmptyObject>(url: string, data?: D, headers?: RequestHeaders): Promise<ApiResponse<T>> {
    return await this.request<T, D>(http.RequestMethod.PUT, url, data, headers)
  }

  // DELETE请求
  async delete<T = EmptyObject>(url: string, headers?: RequestHeaders): Promise<ApiResponse<T>> {
    return await this.request<T>(http.RequestMethod.DELETE, url, undefined, headers)
  }

  // 通用请求方法
  private async request<T = EmptyObject, D = EmptyObject>(
    method: http.RequestMethod,
    url: string,
    data?: D,
    headers?: RequestHeaders
  ): Promise<ApiResponse<T>> {
    const httpRequest = http.createHttp()
    try {
      const requestHeaders: Record<string, string> = {
        'Content-Type': 'application/json'
      }
      if (headers) {
        requestHeaders['Authorization'] = headers.Authorization
      }
      const token = await StorageService.getInstance().getUserToken()
      if (token) {
        requestHeaders['Authorization'] = `Bearer ${token}`
      }

      const logInfo: RequestLogInfo<D> = { url, method: method.toString(), headers: requestHeaders, data };
      console.info('[HttpService] 请求参数:', logInfo)

      const options: http.HttpRequestOptions = {
        method: method,
        header: requestHeaders,
        extraData: data ? JSON.stringify(data) : undefined,
        connectTimeout: this.timeout,
        readTimeout: this.timeout
      };

      const response = await httpRequest.request(url, options)

      if (response.responseCode === http.ResponseCode.OK) {
        const responseText = response.result.toString()
        // 假设 ApiResponse 是 class，可以直接返回解析后的 JSON
        return JSON.parse(responseText) as ApiResponse<T>
      } else {
        const errorMessage = `HTTP ${response.responseCode}: ${response.result.toString()}`
        console.error('[HttpService] HTTP错误:', errorMessage)
        // 修改点2：使用 ApiResponse 的构造函数创建错误实例，避免 unknown
        // **前提：您的 ApiResponse 是一个有 constructor(code, message, data) 的 class**
        return new ApiResponse(-1, errorMessage, null as T);
      }
    } catch (error) {
      const errorMessage = (error as Error).message || '未知错误'
      console.error('[HttpService] 请求异常:', errorMessage)
      // 修改点3：同样使用 ApiResponse 的构造函数创建异常实例
      return new ApiResponse(-1, errorMessage, null as T);
    } finally {
      httpRequest.destroy()
    }
  }
}