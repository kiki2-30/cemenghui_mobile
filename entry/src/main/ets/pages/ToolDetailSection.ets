import { ToolDetail } from '../types/index'
import { ConferenceService } from '../services/ConferenceService'
import promptAction from '@ohos.promptAction'
import router from '@ohos.router'

@Component
struct ToolDetailSection {
  @Prop conferenceId: number
  @State detail: ToolDetail | null = null
  @State loading: boolean = false
  @State isSuperAdmin: boolean = false
  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear(): void {
    this.refresh()
    // 这里可根据实际项目获取用户权限
    // StorageService.getInstance().getUserInfo().then(user => {
    //   this.isSuperAdmin = user?.role === 'superadmin' || user?.role === 'SUPER_ADMIN'
    // })
  }

  async refresh() {
    this.loading = true
    try {
      const res = await this.conferenceService.getToolDetail(this.conferenceId)
      if (res.code === 200 && res.data) {
        this.detail = {
          standardName: res.data.standardName ?? '',
          standardDesc: res.data.standardDesc ?? '',
          requirements: res.data.requirements ?? '',
          process: res.data.process ?? '',
          timeline: res.data.timeline ?? ''
        }
      } else {
        this.detail = null
      }
    } catch (e) {
      this.detail = null
    } finally {
      this.loading = false
    }
  }

  async onDelete() {
    const result = await promptAction.showDialog({
      message: '确定要删除该工具研发内容吗？',
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      this.loading = true
      try {
        // 这里假设有deleteToolDetail接口，如无可补充
        // await this.conferenceService.deleteToolDetail(this.conferenceId)
        promptAction.showToast({ message: '删除成功（请补充接口实现）' })
        await this.refresh()
      } catch (e) {
        promptAction.showToast({ message: '删除失败' })
      } finally {
        this.loading = false
      }
    }
  }

  build() {
    Column() {
      Text('工具研发详情')
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 12 })
      if (this.loading) {
        Text('加载中...').fontSize(14).fontColor('#999999')
      } else if (!this.detail) {
        Text('暂无工具研发内容').fontSize(14).fontColor('#999999')
        if (this.isSuperAdmin) {
          Button('新增')
            .fontSize(14)
            .fontColor('#fff')
            .backgroundColor('#007DFF')
            .borderRadius(20)
            .margin({ top: 12 })
            .onClick(() => {
              router.pushUrl({ url: 'pages/ToolDetailEditPage', params: { mode: 'add', conferenceId: this.conferenceId } })
            })
        }
      } else {
        // 内容展示
        Column() {
          Text(`工具名称：${this.detail.standardName}`).fontSize(14).fontColor('#333')
          Text(`工具描述：${this.detail.standardDesc}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`工具要求：${this.detail.requirements}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`研发流程：${this.detail.process}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`时间安排：${this.detail.timeline}`).fontSize(14).fontColor('#333').margin({ top: 4 })
        }
        .margin({ bottom: 8 })
        if (this.isSuperAdmin) {
          Row() {
            Button('编辑')
              .fontSize(14)
              .fontColor('#fff')
              .backgroundColor('#007DFF')
              .borderRadius(20)
              .onClick(() => {
                router.pushUrl({ url: 'pages/ToolDetailEditPage', params: { mode: 'edit', conferenceId: this.conferenceId } })
              })
            Button('删除')
              .fontSize(14)
              .fontColor('#fff')
              .backgroundColor('#FF4757')
              .borderRadius(20)
              .margin({ left: 16 })
              .onClick(() => {
                void this.onDelete()
              })
          }
          .margin({ top: 12 })
        }
      }
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
    .borderRadius(8)
    .margin({ bottom: 8 })
  }
} 