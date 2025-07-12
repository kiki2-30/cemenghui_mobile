import { NewsService } from '../services/NewsService'
import { News } from '../types/index'
import router from '@ohos.router'
import { StorageService } from '../services/StorageService'
import promptAction from '@ohos.promptAction'
import { NewsTabContent } from './NewsTabContent'

@Entry
@Component
struct NewsPage {
  @State newsList: News[] = []
  @State loading: boolean = false
  @State refreshing: boolean = false
  @State currentPage: number = 0
  @State hasMore: boolean = true
  @State searchKeyword: string = ''
  @State isSearching: boolean = false
  @State error: string = ''
  @State isSuperAdmin: boolean = false
  private newsService: NewsService = new NewsService()

  aboutToAppear(): void {
    StorageService.getInstance().getUserInfo().then(user => {
      this.isSuperAdmin = user?.role === 'SUPER_ADMIN'
      this.loadNews()
    })
  }

  async loadNews(refresh: boolean = false): Promise<void> {
    if (this.loading) return
    this.loading = true
    this.error = ''
    if (refresh) {
      this.currentPage = 0
      this.hasMore = true
    }
    try {
      const response = await this.newsService.getNewsList({
        page: this.currentPage,
        size: 10,
        keyword: this.searchKeyword || undefined
      })
      if (response.code === 200) {
        if (refresh) {
          this.newsList = response.data.content
        } else {
          this.newsList = this.newsList.concat(response.data.content)
        }
        this.hasMore = this.currentPage < response.data.totalPages - 1
        this.currentPage++
      }
    } catch (error) {
      console.error('加载行业动态失败:', error)
      this.error = '加载行业动态失败，请检查网络连接'
    } finally {
      this.loading = false
      this.refreshing = false
    }
  }

  onSearch(): void {
    this.isSearching = true
    this.loadNews(true)
  }

  onRefresh(): void {
    this.refreshing = true
    this.loadNews(true)
  }

  onLoadMore(): void {
    if (this.hasMore && !this.loading) {
      this.loadNews()
    }
  }

  onNewsClick(news: News): void {
    router.pushUrl({
      url: 'pages/NewsDetailPage',
      params: { newsId: news.newsId }
    })
  }

  async onAddNews() {
    router.pushUrl({ url: 'pages/NewsEditPage', params: { mode: 'add' } })
  }

  async onEditNews(newsId: number) {
    router.pushUrl({ url: 'pages/NewsEditPage', params: { mode: 'edit', newsId } })
  }

  async onDeleteNews(newsId: number) {
    const result: promptAction.ShowDialogSuccessResponse = await promptAction.showDialog({
      message: '确定要删除该动态吗？',
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      const res = await this.newsService.deleteNews(newsId)
      if (res.code === 200) {
        promptAction.showToast({ message: '删除成功' })
        this.loadNews(true)
      } else {
        promptAction.showToast({ message: res.message || '删除失败' })
      }
    }
  }

  async onToggleStatus(news: News) {
    const newStatus = news.status === 1 ? 0 : 1
    const result: promptAction.ShowDialogSuccessResponse = await promptAction.showDialog({
      message: `确定要${newStatus === 1 ? '上架' : '下架'}该动态吗？`,
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      const res = await this.newsService.updateNewsStatus(news.newsId, newStatus)
      if (res.code === 200) {
        promptAction.showToast({ message: `${newStatus === 1 ? '上架' : '下架'}成功` })
        this.loadNews(true)
      } else {
        promptAction.showToast({ message: res.message || '操作失败' })
      }
    }
  }

  async onTopNews(news: News) {
    const newTop = news.top ? 0 : 1
    const result: promptAction.ShowDialogSuccessResponse = await promptAction.showDialog({
      message: `确定要${newTop === 1 ? '置顶' : '取消置顶'}该动态吗？`,
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      const res = await this.newsService.topNews(news.newsId, newTop)
      if (res.code === 200) {
        promptAction.showToast({ message: `${newTop === 1 ? '置顶' : '取消置顶'}成功` })
        this.loadNews(true)
      } else {
        promptAction.showToast({ message: res.message || '操作失败' })
      }
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return '未知时间'
    const date = new Date(dateString)
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
  }

  formatTime(dateString: string): string {
    if (!dateString) return '--:--'
    const date = new Date(dateString)
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
  }

  @Builder
  private NewsCard(news: News) {
    Column() {
      Row() {
        Column() {
          Text(news.title || '动态标题')
            .fontSize(16)
            .fontWeight(FontWeight.Bold)
            .fontColor('#333333')
            .maxLines(2)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
          Text(news.summary || '动态摘要')
            .fontSize(14)
            .fontColor('#666666')
            .maxLines(3)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
            .margin({ top: 8 })
          Row() {
            Text(this.formatDate(news.publishTime))
              .fontSize(12)
              .fontColor('#999999')
            Text(this.formatTime(news.publishTime))
              .fontSize(12)
              .fontColor('#999999')
              .margin({ left: 12 })
          }
          .margin({ top: 8 })
        }
        .layoutWeight(1)
        if (this.isSuperAdmin) {
          Column() {
            Button('编辑').fontSize(13).width(60).height(28).margin({ bottom: 4 }).onClick(() => this.onEditNews(news.newsId))
            Button('删除').fontSize(13).width(60).height(28).margin({ bottom: 4 }).onClick(() => this.onDeleteNews(news.newsId))
            Button(news.status === 1 ? '下架' : '上架').fontSize(13).width(60).height(28).margin({ bottom: 4 }).onClick(() => this.onToggleStatus(news))
            Button(news.top ? '取消置顶' : '置顶').fontSize(13).width(60).height(28).onClick(() => this.onTopNews(news))
          }
        }
      }
      Divider()
        .margin({ top: 12 })
        .color('#E0E0E0')
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
    .borderRadius(8)
    .margin({ left: 16, right: 16, top: 8, bottom: 8 })
    .shadow({ radius: 4, color: '#00000010', offsetX: 0, offsetY: 2 })
    .onClick(() => this.onNewsClick(news))
  }

  @Builder
  private NewsList() {
    Column() {
      // 顶部测试跳转按钮
      Row() {
        Button('测试跳转')
          .fontSize(15)
          .fontColor('#fff')
          .backgroundColor('#00B386')
          .borderRadius(20)
          .width(120)
          .height(40)
          .margin({ left: 16, top: 12, bottom: 4 })
          .onClick(() => router.pushUrl({ url: 'pages/TestPage' }))
      }
      if (this.isSuperAdmin) {
        Row() {
          Button('新增动态')
            .fontSize(15)
            .fontColor('#fff')
            .backgroundColor('#FF4757')
            .borderRadius(20)
            .width(120)
            .height(40)
            .margin({ left: 16, top: 12, bottom: 4 })
            .onClick(() => this.onAddNews())
        }
      }
      Row() {
        TextInput({ placeholder: '搜索行业动态...', text: this.searchKeyword })
          .width('100%')
          .height(40)
          .backgroundColor('#F5F5F5')
          .borderRadius(20)
          .padding({ left: 16, right: 16 })
          .onChange((value: string) => {
            this.searchKeyword = value
          })
          .onSubmit(() => this.onSearch())
        Button('搜索')
          .fontSize(14)
          .fontColor('#FFFFFF')
          .backgroundColor('#007DFF')
          .borderRadius(20)
          .width(60)
          .height(40)
          .margin({ left: 12 })
          .onClick(() => this.onSearch())
      }
      .padding(16)
      .backgroundColor('#FFFFFF')
      Refresh({ refreshing: $$this.refreshing, offset: 120, friction: 100 }) {
        List() {
          if (this.error) {
            ListItem() {
              Column() {
                Text('⚠️')
                  .fontSize(48)
                  .fontColor('#FFA500')
                Text(this.error)
                  .fontSize(14)
                  .fontColor('#666666')
                  .margin({ top: 8 })
                Button('重试')
                  .fontSize(14)
                  .fontColor('#FFFFFF')
                  .backgroundColor('#007DFF')
                  .borderRadius(20)
                  .padding({ left: 16, right: 16, top: 8, bottom: 8 })
                  .margin({ top: 16 })
                  .onClick(() => this.loadNews(true))
              }
              .width('100%')
              .height(200)
              .justifyContent(FlexAlign.Center)
            }
          } else if (this.newsList.length === 0 && !this.loading) {
            ListItem() {
              Column() {
                Text('📰')
                  .fontSize(48)
                  .fontColor('#CCCCCC')
                Text('暂无行业动态')
                  .fontSize(14)
                  .fontColor('#999999')
                  .margin({ top: 8 })
              }
              .width('100%')
              .height(200)
              .justifyContent(FlexAlign.Center)
            }
          } else {
            ForEach(this.newsList, (news: News) => {
              ListItem() {
                this.NewsCard(news)
              }
            })
          }
          if (this.loading) {
            ListItem() {
              Row() {
                LoadingProgress()
                  .width(24)
                  .height(24)
                Text('加载中...')
                  .fontSize(14)
                  .fontColor('#666666')
                  .margin({ left: 8 })
              }
              .width('100%')
              .height(60)
              .justifyContent(FlexAlign.Center)
            }
          }
        }
        .width('100%')
        .height('100%')
        .backgroundColor('#F5F5F5')
        .onReachEnd(() => this.onLoadMore())
      }
    }
    .width('100%')
    .height('100%')
  }

  build() {
    Column() {
      NewsTabContent()
    }
    .width('100%')
    .height('100%')
  }
} 