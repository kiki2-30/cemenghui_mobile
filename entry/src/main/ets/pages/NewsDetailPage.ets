import { NewsService } from '../services/NewsService'
import { News } from '../types/index'
import router from '@ohos.router'
import { StorageService } from '../services/StorageService'
import { RichTextView } from '../components/RichTextView'

interface NewsParams {
  newsId?: number
}

interface CompatibleNewsStats {
  views?: number;
  viewCount?: number;
  likes?: number;
  likeCount?: number;
  favorites?: number;
  favoriteCount?: number;
}

@Entry
@Component
struct NewsDetailPage {
  @State news: News | null = null
  @State loading: boolean = true
  @State isLiked: boolean = false
  @State isFavorited: boolean = false
  @State likeCount: number = 0
  @State favoriteCount: number = 0
  @State viewCount: number = 0
  
  private newsId: number = 0
  private newsService: NewsService = new NewsService()
  private userId: number = 0 // 动态获取用户ID

  aboutToAppear(): void {
    const params = router.getParams()
    if (typeof params === 'object' && params !== null) {
      const newsId = (params as NewsParams).newsId
      if (typeof newsId === 'number') {
        this.newsId = newsId
      }
    }
    StorageService.getInstance().getUserInfo().then(user => {
      this.userId = user?.userId ?? 0
      this.loadNewsDetail()
      this.loadNewsStats()
    })
  }

  async loadNewsDetail(): Promise<void> {
    try {
      const response = await this.newsService.getNewsDetail(this.newsId)
      if (response.code === 200) {
        this.news = response.data
        await this.newsService.recordView(this.newsId, this.userId)
        await this.loadInteractionStatus()
      }
    } catch (error) {
      console.error('加载动态详情失败:', error)
    } finally {
      this.loading = false
    }
  }

  async loadNewsStats(): Promise<void> {
    try {
      const res = await this.newsService.getNewsStats(this.newsId)
      if (res.code === 200 && res.data) {
        const raw = res.data as CompatibleNewsStats;
        this.viewCount = raw.views ?? raw.viewCount ?? 0
        this.likeCount = raw.likes ?? raw.likeCount ?? 0
        this.favoriteCount = raw.favorites ?? raw.favoriteCount ?? 0
      }
    } catch (error) {
      console.error('获取统计数据失败:', error)
    }
  }

  async loadInteractionStatus(): Promise<void> {
    if (!this.userId) {
      this.isLiked = false
      this.isFavorited = false
      return
    }
    try {
      // 适配后端接口直接返回true/false
      const likeResponse = await this.newsService.getLikeStatus(this.newsId, this.userId)
      const favoriteResponse = await this.newsService.getFavoriteStatus(this.newsId, this.userId)
      if (likeResponse.code === 200) {
        this.isLiked = likeResponse.data === true
      }
      if (favoriteResponse.code === 200) {
        this.isFavorited = favoriteResponse.data === true
      }
    } catch (error) {
      console.error('获取交互状态失败:', error)
    }
  }

  async onLikeClick(): Promise<void> {
    // 判断未登录自动跳转到登录页
    const isLogin = await StorageService.getInstance().isLoggedIn()
    if (!isLogin) {
      router.pushUrl({ url: 'pages/LoginPage' })
      return
    }
    try {
      if (this.isLiked) {
        const response = await this.newsService.unlikeNews(this.newsId, this.userId)
        if (response.code === 200) {
          this.isLiked = false
          this.likeCount = Math.max(0, this.likeCount - 1)
        }
      } else {
        const response = await this.newsService.likeNews(this.newsId, this.userId)
        if (response.code === 200) {
          this.isLiked = true
          this.likeCount++
        }
      }
    } catch (error) {
      console.error('点赞操作失败:', error)
    }
  }

  async onFavoriteClick(): Promise<void> {
    // 判断未登录自动跳转到登录页
    const isLogin = await StorageService.getInstance().isLoggedIn()
    if (!isLogin) {
      router.pushUrl({ url: 'pages/LoginPage' })
      return
    }
    try {
      if (this.isFavorited) {
        const response = await this.newsService.unfavoriteNews(this.newsId, this.userId)
        if (response.code === 200) {
          this.isFavorited = false
          this.favoriteCount = Math.max(0, this.favoriteCount - 1)
        }
      } else {
        const response = await this.newsService.favoriteNews(this.newsId, this.userId)
        if (response.code === 200) {
          this.isFavorited = true
          this.favoriteCount++
        }
      }
    } catch (error) {
      console.error('收藏操作失败:', error)
    }
  }

  build() {
    Column() {
      // 顶部导航栏
      Row() {
        Button('←')
          .width(40)
          .height(40)
          .fontSize(18)
          .fontColor('#333333')
          .backgroundColor('#FFFFFF')
          .borderRadius(20)
          .onClick(() => router.back())
        Text('动态详情')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor('#333333')
          .margin({ left: 16 })
      }
      .width('100%')
      .height(56)
      .padding({ left: 16, right: 16 })
      .backgroundColor('#FFFFFF')
      .justifyContent(FlexAlign.Start)
      .alignItems(VerticalAlign.Center)
      if (this.loading) {
        Column() {
          LoadingProgress()
            .width(40)
            .height(40)
          Text('加载中...')
            .fontSize(14)
            .fontColor('#666666')
            .margin({ top: 16 })
        }
        .width('100%')
        .height('100%')
        .justifyContent(FlexAlign.Center)
      } else if (this.news) {
        Scroll() {
          Column() {
            // 标题
            Text(this.news?.title ?? '无标题')
              .fontSize(20)
              .fontWeight(FontWeight.Bold)
              .fontColor('#333333')
              .margin({ bottom: 16 })
            // 作者、时间、浏览量
            Row() {
              Text(this.news?.publishTime ? this.formatDate(this.news.publishTime) : '未知时间')
                .fontSize(12)
                .fontColor('#999999')
              Text(`作者: ${(this.news?.author?.toString() ?? '未知')}`)
                .fontSize(12)
                .fontColor('#999999')
                .margin({ left: 16 })
              Text(`浏览: ${this.viewCount}`)
                .fontSize(12)
                .fontColor('#999999')
                .margin({ left: 16 })
            }
            .margin({ bottom: 20 })
            // 封面图片
            if (this.news?.coverImage && typeof this.news.coverImage === 'string' && this.news.coverImage.trim().length > 0) {
              Image(this.news.coverImage.trim())
                .width('100%')
                .height(180)
                .objectFit(ImageFit.Cover)
                .borderRadius(12)
                .margin({ bottom: 20 })
            }
            // 交互按钮（上移到图片下方）
            Row() {
              Button(this.isLiked ? '❤️' : '🤍')
                .width(80)
                .height(40)
                .fontSize(16)
                .fontColor(this.isLiked ? '#FF4757' : '#666666')
                .backgroundColor('#F0F0F0')
                .borderRadius(20)
                .onClick(() => this.onLikeClick())
              Text((this.likeCount ?? 0).toString())
                .fontSize(14)
                .fontColor('#666666')
                .margin({ left: 8, right: 16 })
              Button(this.isFavorited ? '⭐' : '☆')
                .width(80)
                .height(40)
                .fontSize(16)
                .fontColor(this.isFavorited ? '#FFA500' : '#666666')
                .backgroundColor('#F0F0F0')
                .borderRadius(20)
                .onClick(() => this.onFavoriteClick())
              Text((this.favoriteCount ?? 0).toString())
                .fontSize(14)
                .fontColor('#666666')
                .margin({ left: 8 })
            }
            .justifyContent(FlexAlign.Center)
            .margin({ bottom: 20 })
            // 富文本内容
            if (this.news?.content) {
              RichTextView({
                content: this.news.content,
                style: {
                  fontSize: 15,
                  fontColor: '#333333',
                  lineHeight: 1.5,
                  padding: 0
                }
              })
              .width('100%')
              .height('auto')
              .margin({ bottom: 20 })
            }
          }
          .padding(16)
        }
        .width('100%')
        .height('100%')
        .backgroundColor('#FFFFFF')
      }
    }
    .width('100%')
    .height('100%')
  }

  formatDate(dateString: string): string {
    if (!dateString) return '未知时间'
    const date = new Date(dateString)
    if (isNaN(date.getTime())) return '未知时间'
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
  }
} 