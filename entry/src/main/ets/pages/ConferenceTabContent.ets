import { ConferenceService } from '../services/ConferenceService'
import { Conference, ConferenceCategory, RawConference } from '../types/index'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'

@Component
export struct ConferenceTabContent {
  @State conferences: Conference[] = []
  @State categories: ConferenceCategory[] = []
  @State selectedCategoryId: number = 0
  @State loading: boolean = false
  @State refreshing: boolean = false
  @State currentPage: number = 0
  @State hasMore: boolean = true
  @State error: string = ''
  private conferenceService: ConferenceService = new ConferenceService()
  private refreshTimer: number = -1

  aboutToAppear(): void {
    this.loadCategories()
    this.loadConferences()
    // 启动定时刷新，每30秒刷新一次
    this.startAutoRefresh()
  }

  aboutToDisappear(): void {
    // 清除定时器
    if (this.refreshTimer !== -1) {
      clearInterval(this.refreshTimer)
      this.refreshTimer = -1
    }
  }

  startAutoRefresh(): void {
    this.refreshTimer = setInterval(() => {
      console.info('[ConferenceTabContent] 自动刷新会议列表')
      this.loadConferences(true)
    }, 30000) // 30秒
  }

  async loadCategories(): Promise<void> {
    try {
      const response = await this.conferenceService.getCategories()
      if (response.code === 200) {
        this.categories = response.data
        this.categories.unshift({ id: 0, name: '全部', description: '所有分类' })
      }
    } catch (error) {
      this.error = '加载分类失败，请检查网络连接'
    }
  }

  async loadConferences(refresh: boolean = false): Promise<void> {
    if (this.loading) return
    
    this.loading = true
    this.error = ''
    if (refresh) {
      this.currentPage = 0
      this.hasMore = true
    }

    try {
      const response = await this.conferenceService.getConferences({
        page: this.currentPage,
        size: 10,
        categoryId: this.selectedCategoryId === 0 ? undefined : this.selectedCategoryId
      })
      
      console.info('[ConferenceTabContent] 会议列表接口返回:', JSON.stringify(response))
      
      if (response.code === 200) {
        const list: Conference[] = (response.data.content as RawConference[]).map(this.toConference)
        console.info('[ConferenceTabContent] 转换后的会议数据:', JSON.stringify(list))
        if (refresh) {
          this.conferences = list
        } else {
          this.conferences = this.conferences.concat(list)
        }
        this.hasMore = this.currentPage < response.data.totalPages - 1
        this.currentPage++
      }
    } catch (error) {
      console.error('加载会议列表失败:', error)
      this.error = '加载会议列表失败，请检查网络连接'
    } finally {
      this.loading = false
      this.refreshing = false
    }
  }

  onCategoryChange(categoryId: number): void {
    this.selectedCategoryId = categoryId
    this.loadConferences(true)
  }

  onRefresh(): void {
    this.refreshing = true
    this.loadConferences(true)
  }

  onLoadMore(): void {
    if (this.hasMore && !this.loading) {
      this.loadConferences()
    }
  }

  onConferenceClick(conference: Conference): void {
    router.pushUrl({
      url: 'pages/ConferenceDetailPage',
      params: { conferenceId: conference.id }
    })
  }

  formatDate(dateString: string): string {
    if (!dateString) return '待定'
    const date = new Date(dateString)
    return `${date.getMonth() + 1}月${date.getDate()}日`
  }

  formatTime(dateString: string): string {
    if (!dateString) return '--:--'
    const date = new Date(dateString)
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
  }

  toConference(item: RawConference): Conference {
    console.info('[ConferenceTabContent] 转换会议数据:', JSON.stringify(item))
    console.info('[ConferenceTabContent] currentParticipants值:', item.currentParticipants, typeof item.currentParticipants)
    
    return {
      id: item.conferenceId,
      title: item.title,
      description: item.description,
      content: item.content,
      startTime: item.startTime,
      endTime: item.endTime,
      location: item.location,
      maxParticipants: item.maxParticipants,
      registrationCount: item.currentParticipants,
      categoryId: item.categoryId ?? 0, // 新增
      categoryName: item.category && item.category.name ? item.category.name : '',
      status: item.status,
      organizer: item.organizer,
      coverImage: item.coverImage
    }
  }

  @Builder
  private ConferenceCard(conference: Conference) {
    Column() {
      Row() {
        Column() {
          Text(conference.title || '会议标题')
            .fontSize(16)
            .fontWeight(FontWeight.Bold)
            .fontColor('#333333')
            .maxLines(2)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
          Text(conference.description || '会议描述')
            .fontSize(14)
            .fontColor('#666666')
            .maxLines(2)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
            .margin({ top: 8 })
          
          if (conference.organizer) {
            Text(`主办: ${conference.organizer}`)
              .fontSize(12)
              .fontColor('#666666')
              .margin({ top: 4 })
          }
          
          Row() {
            Text(conference.categoryName || '分类')
              .fontSize(12)
              .fontColor('#007DFF')
              .backgroundColor('#E6F3FF')
              .padding({ left: 8, right: 8, top: 4, bottom: 4 })
              .borderRadius(12)
            Text(`报名人数: ${conference.registrationCount || 0}/${conference.maxParticipants}`)
              .fontSize(12)
              .fontColor('#999999')
              .margin({ left: 12 })
          }
          .margin({ top: 8 })
        }
        .layoutWeight(1)
        Column() {
          Text(this.formatDate(conference.startTime))
            .fontSize(12)
            .fontColor('#999999')
          Text(this.formatTime(conference.startTime))
            .fontSize(14)
            .fontColor('#333333')
            .fontWeight(FontWeight.Bold)
            .margin({ top: 4 })
        }
        .alignItems(HorizontalAlign.End)
      }
      Divider()
        .margin({ top: 12 })
        .color('#E0E0E0')
      // 新增：编辑、删除按钮
      Row() {
        Button('编辑')
          .fontSize(14)
          .fontColor('#fff')
          .backgroundColor('#007DFF')
          .borderRadius(20)
          .onClick(() => {
            router.pushUrl({ url: 'pages/ConferenceEditPage', params: { mode: 'edit', conferenceId: conference.id } })
          })
        Button('删除')
          .fontSize(14)
          .fontColor('#fff')
          .backgroundColor('#FF4757')
          .borderRadius(20)
          .margin({ left: 16 })
          .onClick(() => {
            promptAction.showDialog({
              message: '确定要删除该会议吗？',
              buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
            }).then(result => {
              if (result.index === 1) {
                this.deleteConference(conference.id)
              }
            })
          })
      }
      .margin({ top: 12 })
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
    .borderRadius(8)
    .margin({ left: 16, right: 16, top: 8, bottom: 8 })
    .shadow({ radius: 4, color: '#00000010', offsetX: 0, offsetY: 2 })
    .onClick(() => this.onConferenceClick(conference))
  }

  async deleteConference(conferenceId: number) {
    try {
      const res = await this.conferenceService.deleteConference(conferenceId)
      if (res.code === 200) {
        promptAction.showToast({ message: '删除成功' })
        this.loadConferences(true)
      } else {
        promptAction.showToast({ message: res.message || '删除失败' })
      }
    } catch (e) {
      promptAction.showToast({ message: '删除失败' })
    }
  }

  @Builder
  private ConferenceList() {
    Column() {
      Scroll() {
        Row() {
          ForEach(this.categories, (category: ConferenceCategory) => {
            Button(category.name)
              .fontSize(14)
              .fontColor(this.selectedCategoryId === category.id ? '#FFFFFF' : '#333333')
              .backgroundColor(this.selectedCategoryId === category.id ? '#007DFF' : '#F0F0F0')
              .borderRadius(20)
              .padding({ left: 16, right: 16, top: 8, bottom: 8 })
              .margin({ right: 12 })
              .onClick(() => this.onCategoryChange(category.id))
          })
        }
        .padding({ left: 16, right: 16, top: 12, bottom: 12 })
      }
      .scrollable(ScrollDirection.Horizontal)
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
                  .onClick(() => this.loadConferences(true))
              }
              .width('100%')
              .height(200)
              .justifyContent(FlexAlign.Center)
            }
          } else if (this.conferences.length === 0 && !this.loading) {
            ListItem() {
              Column() {
                Text('📋')
                  .fontSize(48)
                  .fontColor('#CCCCCC')
                Text('暂无会议信息')
                  .fontSize(14)
                  .fontColor('#999999')
                  .margin({ top: 8 })
              }
              .width('100%')
              .height(200)
              .justifyContent(FlexAlign.Center)
            }
          } else {
            ForEach(this.conferences, (conference: Conference) => {
              ListItem() {
                this.ConferenceCard(conference)
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
      // 新增会议按钮
      Row() {
        Button('新增会议')
          .fontSize(16)
          .fontColor('#fff')
          .backgroundColor('#007DFF')
          .borderRadius(20)
          .margin({ left: 16, top: 16, bottom: 8 })
          .onClick(() => {
            router.pushUrl({ url: 'pages/ConferenceEditPage', params: { mode: 'add' } })
          })
      }
      this.ConferenceList()
    }
    .width('100%')
    .height('100%')
  }
} 