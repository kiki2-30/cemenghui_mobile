import { NewsService, NewsEditRequest } from '../services/NewsService'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { ApiResponse, News } from '../types/index'
import { SimpleRichTextEditor } from '../components/SimpleRichTextEditor'

interface NewsEditPageParams {
  mode: 'add' | 'edit'
  newsId?: number
}

@Entry
@Component
struct NewsEditPage {
  @State title: string = ''
  @State summary: string = ''
  @State content: string = ''
  @State coverImage: string = ''
  @State publishTime: string = ''
  @State status: number = 1
  @State loading: boolean = false
  @State isEdit: boolean = false
  @State newsId: number = 0

  private newsService: NewsService = new NewsService()

  aboutToAppear() {
    const params = router.getParams() as NewsEditPageParams
    if (params && params.mode === 'edit' && params.newsId) {
      this.isEdit = true
      this.newsId = params.newsId
      this.loadNewsDetail(params.newsId)
    } else {
      this.isEdit = false
      this.publishTime = this.getNowDateTime()
    }
  }

  async loadNewsDetail(newsId: number) {
    this.loading = true
    try {
      const res = await this.newsService.getNewsDetail(newsId)
      if (res.code === 200 && res.data) {
        this.title = res.data.title || ''
        this.summary = res.data.summary || ''
        this.content = res.data.content || ''
        this.coverImage = res.data.coverImage || ''
        this.publishTime = res.data.publishTime || this.getNowDateTime()
        this.status = typeof res.data.status === 'number' ? res.data.status : 1
      }
    } finally {
      this.loading = false
    }
  }

  async onSubmit() {
    if (!this.title.trim() || !this.summary.trim() || !this.content.trim() || !this.coverImage.trim() || !this.publishTime.trim()) {
      promptAction.showToast({ message: '请填写所有必填项' })
      return
    }
    const req: NewsEditRequest = {
      title: this.title,
      summary: this.summary,
      content: this.content,
      imageUrl: this.coverImage,
      publishTime: this.publishTime,
      status: 1
    }
    this.loading = true
    try {
      let res: ApiResponse<News> | undefined
      if (this.isEdit) {
        res = await this.newsService.editNews(this.newsId, req)
      } else {
        res = await this.newsService.addNews(req)
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

  getNowDateTime(): string {
    const now = new Date()
    const pad = (n: number) => n.toString().padStart(2, '0')
    return `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}T${pad(now.getHours())}:${pad(now.getMinutes())}:00.000+08:00`
  }

  build() {
    Column() {
      Text(this.isEdit ? '编辑行业动态' : '新增行业动态').fontSize(20).fontWeight(FontWeight.Bold).margin({ bottom: 20 })
      TextInput({ placeholder: '标题', text: this.title }).onChange(v => this.title = v).margin({ bottom: 12 })
      TextInput({ placeholder: '摘要', text: this.summary }).onChange(v => this.summary = v).margin({ bottom: 12 })
      
      // 富文本编辑器
      SimpleRichTextEditor({ 
        content: this.content,
        onContentChange: (content: string) => {
          this.content = content
        }
      })
      .margin({ bottom: 12 })
      
      TextInput({ placeholder: '图片URL', text: this.coverImage }).onChange(v => this.coverImage = v).margin({ bottom: 12 })
      TextInput({ placeholder: '发布时间', text: this.publishTime }).onChange(v => this.publishTime = v).margin({ bottom: 12 })
      Button(this.loading ? '提交中...' : (this.isEdit ? '保存修改' : '提交')).width('100%').height(44).fontSize(16).fontColor('#fff').backgroundColor(this.loading ? '#bbb' : '#FF4757').borderRadius(8).onClick(() => { if (!this.loading) this.onSubmit() })
      Button('返回').width('100%').height(40).fontSize(15).fontColor('#333').backgroundColor('#F0F0F0').borderRadius(8).margin({ top: 12 }).onClick(() => router.back())
    }.padding(24).width('100%').height('100%').backgroundColor('#fff')
  }
} 