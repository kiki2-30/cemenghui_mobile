import { ConferenceService } from '../services/ConferenceService'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { StandardDetail, ApiResponse } from '../types/index'

interface StandardDetailEditPageParams {
  mode: 'add' | 'edit'
  conferenceId: number
}

@Entry
@Component
struct StandardDetailEditPage {
  @State standardName: string = ''
  @State standardDesc: string = ''
  @State requirements: string = ''
  @State process: string = ''
  @State timeline: string = ''
  @State loading: boolean = false
  @State isEdit: boolean = false
  @State conferenceId: number = 0

  private conferenceService: ConferenceService = new ConferenceService()

  aboutToAppear() {
    const params = router.getParams() as StandardDetailEditPageParams
    if (params && params.mode === 'edit' && params.conferenceId) {
      this.isEdit = true
      this.conferenceId = params.conferenceId
      this.loadDetail(params.conferenceId)
    } else if (params && params.mode === 'add' && params.conferenceId) {
      this.isEdit = false
      this.conferenceId = params.conferenceId
    }
  }

  async loadDetail(conferenceId: number) {
    this.loading = true
    try {
      const res = await this.conferenceService.getStandardDetail(conferenceId)
      if (res.code === 200 && res.data) {
        this.standardName = res.data.standardName || ''
        this.standardDesc = res.data.standardDesc || ''
        this.requirements = res.data.requirements || ''
        this.process = res.data.process || ''
        this.timeline = res.data.timeline || ''
      }
    } finally {
      this.loading = false
    }
  }

  async onSubmit() {
    if (!this.standardName.trim() || !this.standardDesc.trim() || !this.requirements.trim() || !this.process.trim() || !this.timeline.trim()) {
      promptAction.showToast({ message: '请填写所有必填项' })
      return
    }
    const req: StandardDetail = {
      standardName: this.standardName,
      standardDesc: this.standardDesc,
      requirements: this.requirements,
      process: this.process,
      timeline: this.timeline
    }
    this.loading = true
    try {
      let res: ApiResponse<StandardDetail> | undefined
      if (this.isEdit) {
        res = await this.conferenceService.updateStandardDetail(this.conferenceId, req)
      } else {
        res = await this.conferenceService.createStandardDetail(this.conferenceId, req)
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
      Text(this.isEdit ? '编辑标准定制' : '新增标准定制').fontSize(20).fontWeight(FontWeight.Bold).margin({ bottom: 20 })
      TextInput({ placeholder: '标准名称', text: this.standardName }).onChange(v => this.standardName = v).margin({ bottom: 12 })
      TextInput({ placeholder: '标准描述', text: this.standardDesc }).onChange(v => this.standardDesc = v).margin({ bottom: 12 })
      TextInput({ placeholder: '标准要求', text: this.requirements }).onChange(v => this.requirements = v).margin({ bottom: 12 })
      TextInput({ placeholder: '制定流程', text: this.process }).onChange(v => this.process = v).margin({ bottom: 12 })
      TextInput({ placeholder: '时间安排', text: this.timeline }).onChange(v => this.timeline = v).margin({ bottom: 12 })
      Button(this.loading ? '提交中...' : (this.isEdit ? '保存修改' : '提交')).width('100%').height(44).fontSize(16).fontColor('#fff').backgroundColor(this.loading ? '#bbb' : '#007DFF').borderRadius(8).onClick(() => { if (!this.loading) this.onSubmit() })
      Button('返回').width('100%').height(40).fontSize(15).fontColor('#333').backgroundColor('#F0F0F0').borderRadius(8).margin({ top: 12 }).onClick(() => router.back())
    }.padding(24).width('100%').height('100%').backgroundColor('#fff')
  }
} 