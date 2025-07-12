import router from '@ohos.router'
// 修正点1: 使用最简单的默认导入
import promptAction from '@ohos.promptAction'
import { SuperAdminService, BehaviorAnalysisResponse, SystemStatsResponse, TrendResponse, TrendPoint } from '../services/SuperAdminService'
import { SuperAdminUser, ApiResponse, NewsStats, NewsTrend } from '../types/index'
import { NewsService } from '../services/NewsService'
import { ConferenceService } from '../services/ConferenceService'
// 修正News类型导入
import { News } from '../types/index'

interface NewsTrendCount {
  date: string;
  count: number;
}

// 兼容后端多字段的类型声明
interface CompatibleNewsStats {
  newsId: number
  views?: number
  likes?: number
  favorites?: number
  comments?: number
  viewCount?: number
  likeCount?: number
  favoriteCount?: number
  commentCount?: number
}


@Entry
@Component
struct SuperAdminPage {
  // @State 变量的初始化非常棒，这是防止闪退的关键
  @State activeTab: number = 0
  @State loading: boolean = false
  @State users: SuperAdminUser[] = []
  @State behavior: BehaviorAnalysisResponse = {
    viewTrend: [],
    likeTrend: [],
    favoriteTrend: [],
    behaviorStats: { totalViews: 0, totalLikes: 0, totalFavorites: 0 },
    userActivity: { totalUsers: 0, activeUsers: 0, activityRate: 0 }
  }
  @State stats: SystemStatsResponse = {
    userStats: { totalUsers: 0, superAdmins: 0, admins: 0, normalUsers: 0 },
    contentStats: { totalNews: 0, totalConferences: 0 },
    behaviorStats: { totalViews: 0, totalLikes: 0, totalFavorites: 0 }
  }
  @State trend: TrendResponse = {
    viewTrend: [],
    likeTrend: [],
    favoriteTrend: []
  }
  @State error: string = ''
  @State newsCount: number = 0
  @State conferenceCount: number = 0
  @State userCount: number = 0
  @State totalViews: number = 0
  @State totalLikes: number = 0
  @State totalComments: number = 0
  @State newsTrend: NewsTrend[] = []
  @State singleNewsStats: NewsStats | null = null
  @State batchNewsStats: NewsStats[] = []
  @State newsIdInput: string = ''

  private adminService: SuperAdminService = new SuperAdminService()
  private newsService: NewsService = new NewsService()
  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear() {
    this.loadTabData(0)
  }

  async loadTabData(tab: number): Promise<void> {
    this.activeTab = tab
    this.loading = true
    this.error = ''
    try {
      if (tab === 0) {
        const res = await this.adminService.getAllUsers()
        if (res.data) { // 增加一个判断，防止null覆盖初始值
          this.users = res.data
        }
      } else if (tab === 1) {
        // 新闻总数
        const newsRes = await this.newsService.getNewsList({ page: 0, size: 1 })
        this.newsCount = newsRes.data?.totalElements ?? 0
        // 会议总数
        const confRes = await this.conferenceService.getConferences({ page: 0, size: 1 })
        this.conferenceCount = confRes.data?.totalElements ?? 0
        // 总用户数
        const userRes = await this.adminService.getAllUsers()
        this.userCount = userRes.data?.length ?? 0
        // 总浏览/点赞/评论
        const allNewsRes = await this.newsService.getNewsList({ page: 0, size: 1000 })
        const newsIds: number[] = allNewsRes.data?.content.map((n: News) => n.newsId) ?? []
        let statsRes: ApiResponse<NewsStats[]> = new ApiResponse<NewsStats[]>(200, '', [])
        if (newsIds.length > 0) {
          statsRes = await this.newsService.getBatchNewsStats(newsIds)
        }
        this.totalViews = statsRes.data.reduce((sum: number, s: NewsStats) => sum + (s.views ?? 0), 0)
        this.totalLikes = statsRes.data.reduce((sum: number, s: NewsStats) => sum + (s.likes ?? 0), 0)
        this.totalComments = statsRes.data.reduce((sum: number, s: NewsStats) => sum + (s.comments ?? 0), 0)
        // 单条新闻统计演示（取第一条新闻）
        if (newsIds.length > 0) {
          const singleRes = await this.newsService.getNewsStats(newsIds[0])
          this.singleNewsStats = singleRes.data ?? null
        } else {
          this.singleNewsStats = null
        }
        // 批量新闻统计演示（取前3条新闻）
        if (newsIds.length > 0) {
          const batchRes = await this.newsService.getBatchNewsStats(newsIds.slice(0, 3))
          this.batchNewsStats = batchRes.data ?? []
        } else {
          this.batchNewsStats = []
        }
        // 新闻浏览趋势
        const trendRes = await this.newsService.getNewsBehaviorTrend('day')
        this.newsTrend = trendRes.data?.slice(-7) ?? []
      } else if (tab === 2) {
        const res = await this.adminService.getSystemStats()
        if (res.data) {
          this.stats = res.data
        }
      }
    } catch (e) {
      this.error = '数据加载失败，请重试'
      console.error(`[SuperAdminPage] Load data error: ${JSON.stringify(e)}`)
    }
    this.loading = false
  }

  async onDeleteUser(userId: number) {
    try {
      const result: promptAction.ShowDialogSuccessResponse = await promptAction.showDialog({
        message: '确定要删除该用户吗？',
        buttons: [
          { text: '取消', color: '#666666' },
          { text: '确定', color: '#FF4757' }
        ]
      });

      if (result.index === 1) {
        // 调用带token的删除接口
        const res = await this.adminService.deleteUser(userId)
        if (res && res.code === 200) {
          await promptAction.showToast({ message: '删除成功' });
          this.loadTabData(this.activeTab);
        } else {
          await promptAction.showToast({ message: res?.message || '删除失败' });
        }
      }
    } catch (error) {
      await promptAction.showToast({ message: '操作异常' });
    }
  }

  // 新增：新闻统计查询方法（兼容后端多种字段名）
  async querySingleNewsStats(): Promise<void> {
    const id = parseInt(this.newsIdInput.trim())
    if (!isNaN(id)) {
      const res = await this.newsService.getNewsStats(id)
      if (res.code === 200 && res.data) {
        const raw: CompatibleNewsStats = res.data as CompatibleNewsStats
        this.singleNewsStats = {
          newsId: raw.newsId,
          views: raw.views ?? raw.viewCount ?? 0,
          likes: raw.likes ?? raw.likeCount ?? 0,
          favorites: raw.favorites ?? raw.favoriteCount ?? 0,
          comments: raw.comments ?? raw.commentCount ?? 0
        }
      } else {
        this.singleNewsStats = null
      }
    } else {
      this.singleNewsStats = null
    }
  }
  async queryBatchNewsStats(): Promise<void> {
    const ids = this.newsIdInput.split(',').map(s => parseInt(s.trim())).filter(n => !isNaN(n))
    if (ids.length > 0) {
      const res = await this.newsService.getBatchNewsStats(ids)
      if (res.code === 200 && Array.isArray(res.data)) {
        this.batchNewsStats = (res.data as CompatibleNewsStats[]).map((raw) => ({
          newsId: raw.newsId,
          views: raw.views ?? raw.viewCount ?? 0,
          likes: raw.likes ?? raw.likeCount ?? 0,
          favorites: raw.favorites ?? raw.favoriteCount ?? 0,
          // 后端无comments字段时，前端显示为0
          comments: raw.comments ?? raw.commentCount ?? 0
        }) as NewsStats)
      } else {
        this.batchNewsStats = []
      }
    } else {
      this.batchNewsStats = []
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
        Text('超级管理员中心')
          .fontSize(20)
          .fontWeight(FontWeight.Bold)
          .fontColor('#FF4757')
          .margin({ left: 16 })
      }
      .width('100%')
      .height(56)
      .padding({ left: 16, right: 16 })
      .backgroundColor('#FFFFFF')
      .justifyContent(FlexAlign.Start)
      .alignItems(VerticalAlign.Center)

      // Tab切换
      Row({ space: 8 }) {
        Button('用户管理')
          .width('30%')
          .height(40)
          .backgroundColor(this.activeTab === 0 ? '#FF4757' : '#F0F0F0')
          .fontColor(this.activeTab === 0 ? '#fff' : '#333')
          .borderRadius(8)
          .onClick(() => this.loadTabData(0))
        Button('行为分析')
          .width('30%')
          .height(40)
          .backgroundColor(this.activeTab === 1 ? '#FF4757' : '#F0F0F0')
          .fontColor(this.activeTab === 1 ? '#fff' : '#333')
          .borderRadius(8)
          .onClick(() => this.loadTabData(1))
        Button('系统统计')
          .width('30%')
          .height(40)
          .backgroundColor(this.activeTab === 2 ? '#FF4757' : '#F0F0F0')
          .fontColor(this.activeTab === 2 ? '#fff' : '#333')
          .borderRadius(8)
          .onClick(() => this.loadTabData(2))
      }
      .width('100%')
      .justifyContent(FlexAlign.SpaceEvenly)
      .padding({ left: 16, right: 16 })
      .margin({ top: 8, bottom: 8 })


      // 内容区
      if (this.loading) {
        Column() {
          LoadingProgress().width(40).height(40)
          Text('加载中...').fontSize(14).fontColor('#666666').margin({ top: 16 })
        }
        .width('100%').justifyContent(FlexAlign.Center).alignItems(HorizontalAlign.Center).margin({ top: 100 })
      } else if (this.error) {
        Column() {
          Text(this.error).fontSize(16).fontColor('#FF4757').margin({ top: 40 })
        }
        .width('100%').justifyContent(FlexAlign.Center).alignItems(HorizontalAlign.Center)
      } else if (this.activeTab === 0) {
        Scroll() {
          Column({ space: 8 }) {
            ForEach(this.users, (user: SuperAdminUser) => {
              Row() {
                Text(user.username || '-').width('20%')
                Text(user.email || '-').layoutWeight(1)
                Text(user.role || '-').width('20%')
                Button('删除').height(30).onClick(() => this.onDeleteUser(user.userId))
              }.padding(8).backgroundColor(Color.White).borderRadius(8).margin({bottom: 8})
            })
          }.padding(16)
        }
      } else if (this.activeTab === 1) {
        // 行为分析UI
        Column() {
          // 内容统计卡片区
          Row({ space: 12 }) {
            Column() {
              Text('新闻总数').fontSize(12).fontColor('#999')
              Text(this.newsCount.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('45%')
            Column() {
              Text('会议总数').fontSize(12).fontColor('#999')
              Text(this.conferenceCount.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('45%')
          }.margin({ bottom: 16 })
          Row({ space: 12 }) {
            Column() {
              Text('总用户').fontSize(12).fontColor('#999')
              Text(this.userCount.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('28%')
            Column() {
              Text('总浏览').fontSize(12).fontColor('#999')
              Text(this.totalViews.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('28%')
            Column() {
              Text('总点赞').fontSize(12).fontColor('#999')
              Text(this.totalLikes.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('28%')
          }.margin({ bottom: 12 })
          Row({ space: 12 }) {
            Column() {
              Text('总评论').fontSize(12).fontColor('#999')
              Text(this.totalComments.toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('28%')
          }.margin({ bottom: 20 })
          // 新增：新闻ID输入与查询
          Row() {
            TextInput({ placeholder: '输入新闻ID,如1或1,2,3', text: this.newsIdInput })
              .width('60%').height(36).fontSize(15).backgroundColor('#f5f5f5').borderRadius(6).margin({ right: 8 })
              .onChange((val: string) => { this.newsIdInput = val })
            Button('查询单条新闻统计').onClick(() => this.querySingleNewsStats()).width('18%').height(36).fontSize(14)
            Button('查询多条新闻统计').onClick(() => this.queryBatchNewsStats()).width('18%').height(36).fontSize(14).margin({ left: 4 })
          }.margin({ top: 12, bottom: 8 })
          // 新增：单条新闻统计演示
          Text('单条新闻统计演示').fontSize(15).fontWeight(FontWeight.Bold).margin({ top: 16, bottom: 4 })
          if (this.singleNewsStats) {
            Text(`新闻ID:${this.singleNewsStats.newsId} 浏览:${this.singleNewsStats.views} 点赞:${this.singleNewsStats.likes} 收藏:${this.singleNewsStats.favorites} 评论:${this.singleNewsStats.comments}`)
              .fontSize(13).fontColor('#333').margin({ bottom: 4 })
          } else {
            Text('无新闻数据').fontSize(13).fontColor('#999').margin({ bottom: 4 })
          }
          // 新增：批量新闻统计演示
          Text('批量新闻统计演示').fontSize(15).fontWeight(FontWeight.Bold).margin({ top: 12, bottom: 4 })
          if (this.batchNewsStats.length > 0) {
            Column() {
              ForEach(this.batchNewsStats, (item: NewsStats) => {
                Text(`新闻ID:${item.newsId} 浏览:${item.views} 点赞:${item.likes} 收藏:${item.favorites} 评论:${item.comments}`)
                  .fontSize(13).fontColor('#666').margin({ bottom: 2 })
              })
            }
          } else {
            Text('无批量新闻数据').fontSize(13).fontColor('#999').margin({ bottom: 4 })
          }
          // 趋势图区
          Text('最近7天新闻趋势').fontSize(16).fontWeight(FontWeight.Bold).margin({ bottom: 8 })
          if (this.newsTrend.length === 0) {
            Text('暂无趋势数据').fontSize(14).fontColor('#999').margin({ top: 20 })
          } else {
            Column() {
              ForEach(this.newsTrend, (item: NewsTrendCount) => {
                Text(`${item.date} 浏览:${item.count ?? 0}`)
                  .fontSize(13).fontColor('#666').margin({ bottom: 4 })
              })
            }
          }
        }.padding(16)
      } else if (this.activeTab === 2) {
        // 系统统计UI
        Column() {
          Text('内容统计').fontSize(16).fontWeight(FontWeight.Bold).margin({ bottom: 12 })
          Row({ space: 12 }) {
            Column() {
              Text('新闻总数').fontSize(12).fontColor('#999')
              Text((this.stats.contentStats?.totalNews ?? 0).toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('45%')
            Column() {
              Text('会议总数').fontSize(12).fontColor('#999')
              Text((this.stats.contentStats?.totalConferences ?? 0).toString()).fontSize(18).fontWeight(FontWeight.Bold)
            }.padding(8).backgroundColor('#fff').borderRadius(8).width('45%')
          }.margin({ bottom: 20 })

          // 会议详细统计（预留结构，后续可接入会议列表和批量统计接口）
          Text('会议详细统计').fontSize(15).fontWeight(FontWeight.Bold).margin({ top: 12, bottom: 8 })
          // 这里可遍历会议列表，展示会议名称、ID及其新闻统计
          // ForEach(this.conferenceStats, (conf) => { ... })
          Text('（后续可展示会议名称、ID及其新闻统计数据）').fontSize(13).fontColor('#999').margin({ top: 8 })
        }.padding(16)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F5F5F5')
  }
}
