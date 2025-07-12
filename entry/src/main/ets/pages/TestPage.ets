import router from '@ohos.router'
import { ConferenceService } from '../services/ConferenceService'
import { RawConference } from '../types/index'

@Entry
@Component
struct TestPage {
  @State apiResponse: string = ''
  @State loading: boolean = false

  private conferenceService: ConferenceService = new ConferenceService()

  async testConferenceAPI(): Promise<void> {
    this.loading = true
    this.apiResponse = ''
    
    try {
      console.info('[TestPage] 开始测试会议API')
      
      // 测试获取会议列表
      const response = await this.conferenceService.getConferences({
        page: 0,
        size: 5
      })
      
      console.info('[TestPage] API响应:', JSON.stringify(response))
      
      if (response.code === 200) {
        const conferences = response.data.content as RawConference[]
        console.info('[TestPage] 会议数据:', JSON.stringify(conferences))
        
        // 检查每个会议的currentParticipants字段
        let analysisResult = '数据验证结果:\n\n'
        conferences.forEach((conference, index) => {
          console.info(`[TestPage] 会议${index + 1} - currentParticipants:`, conference.currentParticipants, typeof conference.currentParticipants)
          analysisResult += `会议${index + 1}: ${conference.title}\n`
          analysisResult += `  - currentParticipants: ${conference.currentParticipants} (${typeof conference.currentParticipants})\n`
          analysisResult += `  - maxParticipants: ${conference.maxParticipants}\n`
          analysisResult += `  - 报名比例: ${conference.currentParticipants}/${conference.maxParticipants}\n\n`
        })
        
        this.apiResponse = `API调用成功！\n\n响应码: ${response.code}\n会议数量: ${conferences.length}\n\n${analysisResult}\n\n原始数据:\n${JSON.stringify(conferences, null, 2)}`
      } else {
        this.apiResponse = `API调用失败！\n响应码: ${response.code}\n错误信息: ${response.message}`
      }
    } catch (error) {
      console.error('[TestPage] API测试失败:', error)
      this.apiResponse = `API测试失败！\n错误信息: ${error}`
    } finally {
      this.loading = false
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
        
        Text('API测试页面')
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

      // 测试按钮
      Button('测试会议API')
        .width('90%')
        .height(50)
        .fontSize(16)
        .fontColor('#FFFFFF')
        .backgroundColor('#007DFF')
        .borderRadius(8)
        .margin({ top: 20 })
        .onClick(() => this.testConferenceAPI())

      // 加载指示器
      if (this.loading) {
        Row() {
          LoadingProgress()
            .width(24)
            .height(24)
          Text('测试中...')
            .fontSize(14)
            .fontColor('#666666')
            .margin({ left: 8 })
        }
        .margin({ top: 16 })
      }

      // 结果显示
      if (this.apiResponse) {
        Scroll() {
          Text(this.apiResponse)
            .fontSize(12)
            .fontColor('#333333')
            .textAlign(TextAlign.Start)
            .width('100%')
        }
        .width('90%')
        .height('70%')
        .backgroundColor('#F5F5F5')
        .borderRadius(8)
        .padding(16)
        .margin({ top: 20 })
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#FFFFFF')
  }
} 