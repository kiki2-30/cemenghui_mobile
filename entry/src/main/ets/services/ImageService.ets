import http from '@ohos.net.http'
import image from '@ohos.multimedia.image'
import fs from '@ohos.file.fs'
import fileIO from '@ohos.fileio'

export class ImageService {
  private static instance: ImageService
  private cacheDir: string = ''

  private constructor() {
    // 初始化缓存目录
    this.initCacheDir()
  }

  static getInstance(): ImageService {
    if (!ImageService.instance) {
      ImageService.instance = new ImageService()
    }
    return ImageService.instance
  }

  private async initCacheDir() {
    try {
      const context = getContext()
      this.cacheDir = context.filesDir + '/image_cache'
      // 确保缓存目录存在
      try {
        await fs.access(this.cacheDir)
      } catch {
        await fs.mkdir(this.cacheDir)
      }
    } catch (error) {
      console.error('初始化图片缓存目录失败:', error)
    }
  }

  /**
   * 下载图片并处理防盗链问题
   * @param imageUrl 图片URL
   * @returns 本地图片路径或原始URL
   */
  async downloadImageWithHeaders(imageUrl: string): Promise<string> {
    if (!imageUrl || !imageUrl.trim()) {
      return ''
    }

    try {
      // 生成缓存文件名
      const urlHash = this.hashCode(imageUrl)
      const cacheFile = this.cacheDir + '/' + urlHash + '.jpg'
      
      // 检查缓存
      try {
        await fs.access(cacheFile)
        console.info('使用缓存的图片:', cacheFile)
        return cacheFile
      } catch {
        // 缓存不存在，需要下载
      }

      // 创建HTTP请求，添加浏览器标准请求头
      const httpRequest = http.createHttp()
      
      // 设置请求头，模拟浏览器请求
      const headers: Record<string, string> = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
        'Accept': 'image/webp,image/apng,image/*,*/*;q=0.8',
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
        'Accept-Encoding': 'gzip, deflate, br',
        'Connection': 'keep-alive',
        'Referer': 'https://www.google.com/',
        'Sec-Fetch-Dest': 'image',
        'Sec-Fetch-Mode': 'no-cors',
        'Sec-Fetch-Site': 'cross-site',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache'
      }

      console.info('开始下载图片:', imageUrl)
      console.info('使用请求头:', JSON.stringify(headers))

      const response = await httpRequest.request(imageUrl, {
        method: http.RequestMethod.GET,
        header: headers,
        readTimeout: 30000,
        connectTimeout: 10000
      })

      if (response.responseCode === 200) {
        // 保存到缓存
        await fileIO.writeFile(cacheFile, response.result as ArrayBuffer)
        console.info('图片下载成功，已缓存:', cacheFile)
        return cacheFile
      } else {
        console.error('图片下载失败，响应码:', response.responseCode)
        // 如果下载失败，返回原始URL让系统尝试
        return imageUrl
      }
    } catch (error) {
      console.error('图片下载异常:', error)
      // 异常情况下返回原始URL
      return imageUrl
    }
  }

  /**
   * 简单的字符串哈希函数
   */
  private hashCode(str: string): string {
    let hash = 0
    if (str.length === 0) return hash.toString()
    for (let i = 0; i < str.length; i++) {
      const char = str.charCodeAt(i)
      hash = ((hash << 5) - hash) + char
      hash = hash & hash // 转换为32位整数
    }
    return Math.abs(hash).toString()
  }

  /**
   * 清理图片缓存
   */
  async clearCache(): Promise<void> {
    try {
      if (this.cacheDir) {
        const files = await fs.listFile(this.cacheDir)
        for (const file of files) {
          await fs.unlink(this.cacheDir + '/' + file)
        }
        console.info('图片缓存已清理')
      }
    } catch (error) {
      console.error('清理图片缓存失败:', error)
    }
  }
} 