import { ConferenceService } from '../services/ConferenceService'
import { Conference, ConferenceRegistration, RawConference } from '../types/index'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { StorageService } from '../services/StorageService'
import type { ConferenceCategoryDetail } from '../types/index'
import type { StandardDetail } from '../types/index'
import type { ToolDetail, TrainingDetail } from '../types/index'


interface ConferenceParams {
  conferenceId?: number
}

@Entry
@Component
struct ConferenceDetailPage {
  @State conference: Conference | null = null
  @State loading: boolean = true
  @State showRegistrationForm: boolean = false
  @State isRegistered: boolean = false
  @State registrationData: ConferenceRegistration = {
    conferenceId: 0,
    realName: '',
    phone: '',
    email: '',
    company: '',
    gender: 1,
    arrivalMethod: '',
    arrivalTrain: '',
    arrivalTime: ''
  }
  @State categoryDetail: StandardDetail | null = null
  
  private conferenceId: number = 0
  private conferenceService: ConferenceService = new ConferenceService()
  private dialogController!: CustomDialogController

  aboutToAppear(): void {
    this.dialogController = new CustomDialogController({
      builder: RegistrationDialog({
        registrationData: this.registrationData,
        onSubmit: (): void => { void this.submitRegistration(); },
        onCancel: (): void => this.dialogController.close()
      }),
      alignment: DialogAlignment.Center,
      autoCancel: false
    })
    const params = router.getParams()
    console.info('[ConferenceDetailPage] params:', JSON.stringify(params))
    let conferenceId = (params as ConferenceParams).conferenceId
    console.info('[ConferenceDetailPage] parsed conferenceId:', conferenceId, typeof conferenceId)
    if (typeof conferenceId === 'string') {
      conferenceId = parseInt(conferenceId)
    }
    if (typeof conferenceId === 'number' && !isNaN(conferenceId)) {
      this.conferenceId = conferenceId
      this.registrationData.conferenceId = this.conferenceId
    } else {
      promptAction.showToast({ message: '无效的会议ID', duration: 2000 })
      this.loading = false
      return
    }
    this.loadConferenceDetail()
    this.loadConferenceDetail(); // 每次页面显示都刷新详情
    // 新增：自动获取当前用户手机号并检查报名状态
    void StorageService.getInstance().getUserInfo().then(user => {
      if (user && user.phone) {
        this.registrationData.phone = user.phone
        void this.checkRegistration(user.phone)
      }
    })
  }

  // 新增：详情页专用的转换方法
  toConference(item: RawConference): Conference {
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
      categoryName: '',
      status: item.status,
      organizer: item.organizer,
      coverImage: item.coverImage
    }
  }

  async loadConferenceDetail(): Promise<void> {
    try {
      console.info('[ConferenceDetailPage] 请求会议详情ID:', this.conferenceId)
      const response = await this.conferenceService.getConferenceDetail(this.conferenceId)
      console.info('[ConferenceDetailPage] 详情接口返回:', JSON.stringify(response))
      if (response.code === 200) {
        this.conference = this.toConference(response.data as RawConference)
        // 分类详情接口调用
        const category = this.conference.categoryName
        if (category === '标准定制') {
          const detailRes = await this.conferenceService.getStandardDetail(this.conferenceId)
          if (detailRes.code === 200 && detailRes.data) this.categoryDetail = detailRes.data as StandardDetail
          else this.categoryDetail = null
        } else {
          this.categoryDetail = null
        }
      }
    } catch (error) {
      console.error('加载会议详情失败:', error)
    } finally {
      this.loading = false
    }
  }

  // 修改：checkRegistration方法支持传入手机号
  async checkRegistration(phone?: string): Promise<void> {
    const checkPhone = phone || this.registrationData.phone
    if (!checkPhone) return
    
    try {
      const response = await this.conferenceService.checkRegistration(this.conferenceId, checkPhone)
      if (response.code === 200) {
        this.isRegistered = response.data
      }
    } catch (error) {
      console.error('检查注册状态失败:', error)
    }
  }

  async submitRegistration(): Promise<void> {
    if (!this.validateForm()) return
    
    try {
      const response = await this.conferenceService.registerConference(this.registrationData)
      
      if (response.code === 200) {
        promptAction.showToast({
          message: '报名成功，您已成功报名本次会议！',
          duration: 2000
        })
        this.dialogController.close()
        // 新增：报名成功后自动刷新报名状态
        void this.checkRegistration(this.registrationData.phone)
      }
    } catch (error) {
      console.error('提交注册失败:', error)
      promptAction.showToast({
        message: '报名失败，请稍后重试',
        duration: 2000
      })
    }
  }

  validateForm(): boolean {
    if (!this.registrationData.realName.trim()) {
      promptAction.showToast({
        message: '请输入姓名',
        duration: 2000
      })
      return false
    }
    if (!this.registrationData.phone.trim()) {
      promptAction.showToast({
        message: '请输入手机号码',
        duration: 2000
      })
      return false
    }
    if (!this.registrationData.email.trim()) {
      promptAction.showToast({
        message: '请输入邮箱',
        duration: 2000
      })
      return false
    }
    if (!this.registrationData.company.trim()) {
      promptAction.showToast({
        message: '请输入单位',
        duration: 2000
      })
      return false
    }
    return true
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
        
        Text('会议详情')
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
      } else if (!this.conference) {
        Column() {
          Text('会议不存在或已被删除')
            .fontSize(18)
            .fontColor('#FF4757')
            .margin({ top: 80 })
        }
        .width('100%')
        .height('100%')
        .justifyContent(FlexAlign.Center)
      } else {
        Scroll() {
          Column() {
            // 会议基本信息
            this.ConferenceInfo()
            // 详情区块渲染逻辑
            if (this.conference && this.conference.categoryId === 1) {
              StandardDetailSection({ conferenceId: this.conferenceId })
            } else if (this.conference && this.conference.categoryId === 2) {
              TrainingDetailSection({ conferenceId: this.conferenceId })
            } else if (this.conference && this.conference.categoryId === 3) {
              ToolDetailSection({ conferenceId: this.conferenceId })
            } else if (this.conference && this.conference.categoryId === 4) {
              Button('打开公益网站')
                .fontSize(16)
                .fontColor('#fff')
                .backgroundColor('#07C160')
                .borderRadius(20)
                .onClick(() => {
                  router.pushUrl({
                    url: 'pages/WebViewTestPage',
                    params: { url: 'http://www.chinagongyi.com.cn/' }
                  })
                })
            } else {
              Text(`未匹配到详情区块，当前类型ID：${this.conference?.categoryId}`)
            }
            // 会议详细内容
            this.ConferenceContent()
            // 注册按钮
            this.RegistrationButton()
          }
        }
        .width('100%')
        .height('100%')
        .backgroundColor('#F5F5F5')
      }
    }
    .width('100%')
    .height('100%')
  }

  @Builder ConferenceInfo() {
    Column() {
      Text(this.conference!.title)
        .fontSize(20)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 16 })
      
      Row() {
        Column() {
          Text('时间')
            .fontSize(12)
            .fontColor('#999999')
          Text(this.formatDateTime(this.conference!.startTime))
            .fontSize(14)
            .fontColor('#333333')
            .fontWeight(FontWeight.Bold)
            .margin({ top: 4 })
        }
        .layoutWeight(1)
        
        Column() {
          Text('地点')
            .fontSize(12)
            .fontColor('#999999')
          Text(this.conference!.location)
            .fontSize(14)
            .fontColor('#333333')
            .fontWeight(FontWeight.Bold)
            .margin({ top: 4 })
        }
        .layoutWeight(1)
      }
      .margin({ bottom: 16 })
      
      if (this.conference!.organizer) {
        Text(`主办单位: ${this.conference!.organizer}`)
          .fontSize(14)
          .fontColor('#666666')
          .margin({ bottom: 16 })
      }
      
      Row() {
        Text(this.conference!.categoryName)
          .fontSize(12)
          .fontColor('#007DFF')
          .backgroundColor('#E6F3FF')
          .padding({ left: 8, right: 8, top: 4, bottom: 4 })
          .borderRadius(12)
        
        Text(`报名人数: ${this.conference!.registrationCount}/${this.conference!.maxParticipants}`)
          .fontSize(12)
          .fontColor('#999999')
          .margin({ left: 12 })
      }
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
    .margin({ bottom: 8 })
  }

  @Builder ConferenceContent() {
    Column() {
      Text('会议详情')
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 12 })
      // 优先展示分类详情
      if (this.categoryDetail) {
        // 展示内容时只用standardName、standardDesc、requirements、process、timeline字段
        Column() {
          Text(`标准名称：${this.categoryDetail.standardName}`).fontSize(14).fontColor('#333')
          Text(`标准描述：${this.categoryDetail.standardDesc}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`标准要求：${this.categoryDetail.requirements}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`制定流程：${this.categoryDetail.process}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`时间安排：${this.categoryDetail.timeline}`).fontSize(14).fontColor('#333').margin({ top: 4 })
        }
        .margin({ bottom: 8 })
      } else {
        Text(this.conference!.content || this.conference!.description)
          .fontSize(14)
          .fontColor('#666666')
          .lineHeight(20)
      }
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
    .margin({ bottom: 8 })
  }

  @Builder RegistrationButton() {
    Column() {
      if (this.isRegistered) {
        Button('已注册')
          .width('100%')
          .height(48)
          .fontSize(16)
          .fontColor('#FFFFFF')
          .backgroundColor('#999999')
          .borderRadius(24)
          .enabled(false)
      } else {
        Button('立即报名')
          .width('100%')
          .height(48)
          .fontSize(16)
          .fontColor('#FFFFFF')
          .backgroundColor('#007DFF')
          .borderRadius(24)
          .onClick(() => {
            void StorageService.getInstance().isLoggedIn().then(isLogin => {
              if (!isLogin) {
                router.pushUrl({ url: 'pages/LoginPage' })
              } else {
                this.dialogController.open()
              }
            })
          })
      }
    }
    .padding(16)
    .backgroundColor('#FFFFFF')
  }

  formatDateTime(dateString: string): string {
    const date = new Date(dateString)
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
  }
}

// 注册表单弹窗
@CustomDialog
struct RegistrationDialog {
  @State registrationData: ConferenceRegistration = {
    conferenceId: 0,
    realName: '',
    phone: '',
    email: '',
    company: '',
    gender: 1,
    arrivalMethod: '',
    arrivalTrain: '',
    arrivalTime: ''
  }
  private onSubmit: () => void = () => {}
  private onCancel: () => void = () => {}
  controller: CustomDialogController = new CustomDialogController({
    builder: RegistrationDialog({
      registrationData: this.registrationData,
      onSubmit: this.onSubmit,
      onCancel: this.onCancel
    }),
    alignment: DialogAlignment.Center,
    autoCancel: false
  })

  build() {
    Column() {
      Text('会议报名')
        .fontSize(18)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 20 })

      Scroll() {
        Column() {
          // 姓名
          TextInput({ placeholder: '请输入姓名', text: this.registrationData.realName })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.realName = value
            })

          // 手机号码
          TextInput({ placeholder: '请输入手机号码', text: this.registrationData.phone })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.phone = value
            })

          // 邮箱
          TextInput({ placeholder: '请输入邮箱', text: this.registrationData.email })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.email = value
            })

          // 单位
          TextInput({ placeholder: '请输入单位', text: this.registrationData.company })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.company = value
            })

          // 性别
          Row() {
            Text('性别')
              .fontSize(14)
              .fontColor('#333333')
              .margin({ right: 16 })
            
            Radio({ value: '1', group: 'gender' })
              .checked(this.registrationData.gender === 1)
              .onChange((isChecked: boolean) => {
                if (isChecked) this.registrationData.gender = 1
              })
            Text('男')
              .fontSize(14)
              .fontColor('#333333')
              .margin({ right: 16 })
            
            Radio({ value: '2', group: 'gender' })
              .checked(this.registrationData.gender === 2)
              .onChange((isChecked: boolean) => {
                if (isChecked) this.registrationData.gender = 2
              })
            Text('女')
              .fontSize(14)
              .fontColor('#333333')
          }
          .margin({ bottom: 16 })

          // 到达方式
          TextInput({ placeholder: '请输入到达方式', text: this.registrationData.arrivalMethod || '' })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.arrivalMethod = value
            })

          // 到达车次
          TextInput({ placeholder: '请输入到达车次', text: this.registrationData.arrivalTrain || '' })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.arrivalTrain = value
            })

          // 到达时间
          TextInput({ placeholder: '请输入到达时间', text: this.registrationData.arrivalTime || '' })
            .width('100%')
            .height(48)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .padding({ left: 16, right: 16 })
            .margin({ bottom: 16 })
            .onChange((value: string) => {
              this.registrationData.arrivalTime = value
            })
        }
      }
      .height(400)

      Row() {
        Button('取消')
          .width('45%')
          .height(48)
          .fontSize(16)
          .fontColor('#666666')
          .backgroundColor('#F0F0F0')
          .borderRadius(24)
          .onClick(() => this.onCancel())
        
        Button('提交')
          .width('45%')
          .height(48)
          .fontSize(16)
          .fontColor('#FFFFFF')
          .backgroundColor('#007DFF')
          .borderRadius(24)
          .margin({ left: '10%' })
          .onClick(() => this.onSubmit())
      }
      .margin({ top: 20 })
    }
    .width('90%')
    .padding(24)
    .backgroundColor('#FFFFFF')
    .borderRadius(16)
  }
} 

@Component
struct StandardDetailSection {
  @Prop conferenceId: number
  @State detail: StandardDetail | null = null
  @State loading: boolean = false
  @State isSuperAdmin: boolean = false
  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear(): void {
    this.refresh()
    StorageService.getInstance().getUserInfo().then(user => {
      this.isSuperAdmin = user?.role === 'superadmin' || user?.role === 'SUPER_ADMIN'
    })
  }

  async refresh() {
    this.loading = true
    try {
      const res = await this.conferenceService.getStandardDetail(this.conferenceId)
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
      message: '确定要删除该标准定制内容吗？',
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      this.loading = true
      try {
        const res = await this.conferenceService.deleteStandardDetail(this.conferenceId)
        if (res.code === 200) {
          promptAction.showToast({ message: '删除成功' })
          await this.refresh()
        } else {
          promptAction.showToast({ message: res.message || '删除失败' })
        }
      } catch (e) {
        promptAction.showToast({ message: '删除失败' })
      } finally {
        this.loading = false
      }
    }
  }

  build() {
    Column() {
      Text('标准定制详情')
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 12 })
      if (this.loading) {
        Text('加载中...').fontSize(14).fontColor('#999999')
      } else if (!this.detail) {
        Text('暂无标准定制内容').fontSize(14).fontColor('#999999')
        if (this.isSuperAdmin) {
          Button('新增')
            .fontSize(14)
            .fontColor('#fff')
            .backgroundColor('#007DFF')
            .borderRadius(20)
            .margin({ top: 12 })
            .onClick(() => {
              router.pushUrl({ url: 'pages/StandardDetailEditPage', params: { mode: 'add', conferenceId: this.conferenceId } })
            })
        }
      } else {
        // 内容展示
        Column() {
          Text(`标准名称：${this.detail.standardName}`).fontSize(14).fontColor('#333')
          Text(`标准描述：${this.detail.standardDesc}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`标准要求：${this.detail.requirements}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`制定流程：${this.detail.process}`).fontSize(14).fontColor('#333').margin({ top: 4 })
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
                router.pushUrl({ url: 'pages/StandardDetailEditPage', params: { mode: 'edit', conferenceId: this.conferenceId } })
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

@Component
struct ToolDetailSection {
  @Prop conferenceId: number
  @State detail: ToolDetail | null = null
  @State loading: boolean = false
  @State isSuperAdmin: boolean = false
  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear(): void {
    this.refresh()
    StorageService.getInstance().getUserInfo().then(user => {
      this.isSuperAdmin = user?.role === 'superadmin' || user?.role === 'SUPER_ADMIN'
    })
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

@Component
struct TrainingDetailSection {
  @Prop conferenceId: number
  @State detail: TrainingDetail | null = null
  @State loading: boolean = false
  @State isSuperAdmin: boolean = false
  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear(): void {
    this.refresh()
    StorageService.getInstance().getUserInfo().then(user => {
      this.isSuperAdmin = user?.role === 'superadmin' || user?.role === 'SUPER_ADMIN'
    })
  }

  async refresh() {
    this.loading = true
    try {
      const res = await this.conferenceService.getTrainingDetail(this.conferenceId)
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
      message: '确定要删除该技术培训内容吗？',
      buttons: [ { text: '取消', color: '#666666' }, { text: '确定', color: '#FF4757' } ]
    })
    if (result.index === 1) {
      this.loading = true
      try {
        // 这里假设有deleteTrainingDetail接口，如无可补充
        // await this.conferenceService.deleteTrainingDetail(this.conferenceId)
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
      Text('技术培训详情')
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 12 })
      if (this.loading) {
        Text('加载中...').fontSize(14).fontColor('#999999')
      } else if (!this.detail) {
        Text('暂无技术培训内容').fontSize(14).fontColor('#999999')
        if (this.isSuperAdmin) {
          Button('新增')
            .fontSize(14)
            .fontColor('#fff')
            .backgroundColor('#007DFF')
            .borderRadius(20)
            .margin({ top: 12 })
            .onClick(() => {
              router.pushUrl({ url: 'pages/TrainingDetailEditPage', params: { mode: 'add', conferenceId: this.conferenceId } })
            })
        }
      } else {
        // 内容展示
        Column() {
          Text(`培训名称：${this.detail.standardName}`).fontSize(14).fontColor('#333')
          Text(`培训描述：${this.detail.standardDesc}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`培训要求：${this.detail.requirements}`).fontSize(14).fontColor('#333').margin({ top: 4 })
          Text(`培训流程：${this.detail.process}`).fontSize(14).fontColor('#333').margin({ top: 4 })
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
                router.pushUrl({ url: 'pages/TrainingDetailEditPage', params: { mode: 'edit', conferenceId: this.conferenceId } })
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