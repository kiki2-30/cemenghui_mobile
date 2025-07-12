import { ConferenceService } from '../services/ConferenceService'
import { CreateConferenceRequest, UpdateConferenceRequest, ApiResponse, EmptyObject } from '../types/index'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'

interface ConferenceEditPageParams {
  mode: 'add' | 'edit'
  conferenceId?: number
}

@Entry
@Component
struct ConferenceEditPage {
  @State title: string = ''
  @State content: string = ''
  @State startTime: string = ''
  @State endTime: string = ''
  @State location: string = ''
  @State organizer: string = ''
  @State categoryId: number = 1
  @State coverImage: string = ''
  @State status: number = 1
  @State loading: boolean = false
  @State isEdit: boolean = false
  @State conferenceId: number = 0

  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear() {
    const params = router.getParams() as ConferenceEditPageParams
    if (params && params.mode === 'edit' && params.conferenceId) {
      this.isEdit = true
      this.conferenceId = params.conferenceId
      this.loadConferenceDetail(params.conferenceId)
    } else {
      this.isEdit = false
    }
  }

  async loadConferenceDetail(conferenceId: number) {
    this.loading = true
    try {
      const res = await this.conferenceService.getConferenceDetail(conferenceId)
      if (res.code === 200 && res.data) {
        this.title = res.data.title || ''
        this.content = res.data.content || ''
        this.startTime = res.data.startTime || ''
        this.endTime = res.data.endTime || ''
        this.location = res.data.location || ''
        this.organizer = res.data.organizer || ''
        this.categoryId = res.data.categoryId || 1
        this.coverImage = res.data.coverImage || ''
        this.status = typeof res.data.status === 'number' ? res.data.status : 1
      }
    } finally {
      this.loading = false
    }
  }

  async onSubmit() {
    if (!this.title.trim() || !this.content.trim() || !this.startTime.trim() || !this.endTime.trim() || !this.location.trim() || !this.organizer.trim() || !this.coverImage.trim()) {
      promptAction.showToast({ message: '请填写所有必填项' })
      return
    }
    this.loading = true
    try {
      let res: ApiResponse<number> | ApiResponse<null> | undefined;
      if (this.isEdit) {
        const updateData: UpdateConferenceRequest = {
          title: this.title,
          content: this.content,
          startTime: this.startTime,
          endTime: this.endTime,
          location: this.location,
          organizer: this.organizer,
          categoryId: this.categoryId,
          coverImage: this.coverImage,
          status: this.status
        }
        res = await this.conferenceService.updateConference(this.conferenceId, updateData)
      } else {
        const createData: CreateConferenceRequest = {
          title: this.title,
          content: this.content,
          startTime: this.startTime,
          endTime: this.endTime,
          location: this.location,
          organizer: this.organizer,
          categoryId: this.categoryId,
          coverImage: this.coverImage,
          status: this.status
        }
        res = await this.conferenceService.createConference(createData)
      }
      if (res && res.code === 200) {
        promptAction.showToast({ message: this.isEdit ? '修改成功' : '新增成功' })
        router.back()
      } else {
        promptAction.showToast({ message: res?.message || '操作失败' })
      }
    } finally {
      this.loading = false
    }
  }

  build() {
    Column() {
      Text(this.isEdit ? '编辑会议' : '新增会议').fontSize(20).fontWeight(FontWeight.Bold).margin({ bottom: 20 })
      TextInput({ placeholder: '标题', text: this.title }).onChange(v => this.title = v).margin({ bottom: 12 })
      TextInput({ placeholder: '内容', text: this.content }).onChange(v => this.content = v).margin({ bottom: 12 })
      TextInput({ placeholder: '开始时间 2024-08-01T09:00:00', text: this.startTime }).onChange(v => this.startTime = v).margin({ bottom: 12 })
      TextInput({ placeholder: '结束时间 2024-08-01T17:00:00', text: this.endTime }).onChange(v => this.endTime = v).margin({ bottom: 12 })
      TextInput({ placeholder: '地点', text: this.location }).onChange(v => this.location = v).margin({ bottom: 12 })
      TextInput({ placeholder: '主办单位', text: this.organizer }).onChange(v => this.organizer = v).margin({ bottom: 12 })
      TextInput({ placeholder: '分类ID（1-标准定制 2-技术培训 3-工具研发 4-公益活动）', text: this.categoryId.toString() }).onChange(v => this.categoryId = parseInt(v) || 1).margin({ bottom: 12 })
      TextInput({ placeholder: '封面图片URL', text: this.coverImage }).onChange(v => this.coverImage = v).margin({ bottom: 12 })
      TextInput({ placeholder: '状态（1-上架 0-下架）', text: this.status.toString() }).onChange(v => this.status = parseInt(v) || 1).margin({ bottom: 12 })
      Button(this.loading ? '提交中...' : (this.isEdit ? '保存修改' : '提交')).width('100%').height(44).fontSize(16).fontColor('#fff').backgroundColor(this.loading ? '#bbb' : '#007DFF').borderRadius(8).onClick(() => { if (!this.loading) this.onSubmit() })
      Button('返回').width('100%').height(40).fontSize(15).fontColor('#333').backgroundColor('#F0F0F0').borderRadius(8).margin({ top: 12 }).onClick(() => router.back())
    }.padding(24).width('100%').height('100%').backgroundColor('#fff')
  }
} 