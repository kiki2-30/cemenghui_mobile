import { StorageService } from '../services/StorageService'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { ConferenceTabContent } from './ConferenceTabContent'
import { NewsTabContent } from './NewsTabContent'

@Entry
@Component
struct Index {
  @State currentIndex: number = 0
  @State isLogin: boolean = false
  @State userRole: string = ''
  private tabsController: TabsController = new TabsController()

  aboutToAppear() {
    // 先初始化StorageService，确保本地存储可用
    void StorageService.getInstance().init().then(() => {
      StorageService.getInstance().isLoggedIn().then(isLogin => {
        console.info('[Index] 登录状态:', isLogin)
        this.isLogin = isLogin
        if (isLogin) {
          StorageService.getInstance().getUserRole().then(role => {
            this.userRole = role ?? ''
          })
        } else {
          this.userRole = ''
        }
      })
    })
  }

  build() {
    Column() {
      // 顶部标题栏+我的/登录按钮
      Row() {
        Text('合作平台')
          .fontSize(20)
          .fontWeight(FontWeight.Bold)
          .fontColor('#333333')
        Blank()
        // 只有超级管理员才显示超级管理按钮
        if ((this.userRole ?? '').toUpperCase() === 'SUPER_ADMIN') {
          Button('超级管理')
            .fontSize(14)
            .backgroundColor('#FF4757')
            .fontColor('#fff')
            .borderRadius(16)
            .padding({ left: 12, right: 12, top: 4, bottom: 4 })
            .onClick(() => router.pushUrl({ url: 'pages/SuperAdminPage' }))
        }
        Button(this.isLogin ? '我的' : '登录/注册')
          .fontSize(14)
          .backgroundColor('#007DFF')
          .fontColor('#fff')
          .borderRadius(16)
          .padding({ left: 12, right: 12, top: 4, bottom: 4 })
          .onClick(() => {
            if (!this.isLogin) {
              router.pushUrl({ url: 'pages/LoginPage' })
            } else {
              router.pushUrl({ url: 'pages/ProfilePage' })
            }
          })
        // WebView测试入口按钮
        Button('WebView测试')
          .fontSize(14)
          .backgroundColor('#00B386')
          .fontColor('#fff')
          .borderRadius(16)
          .padding({ left: 12, right: 12, top: 4, bottom: 4 })
          .margin({ left: 8 })
          .onClick(() => router.pushUrl({ url: 'pages/WebViewTestPage' }))
      }
      .width('100%')
      .height(56)
      .padding({ left: 16, right: 16 })
      .backgroundColor('#FFFFFF')
      .justifyContent(FlexAlign.SpaceBetween)
      .alignItems(VerticalAlign.Center)

      // 主要内容区域
      Tabs({ barPosition: BarPosition.End, controller: this.tabsController }) {
        // 会议注册模块
        TabContent() {
          ConferenceTabContent()
        }
        .tabBar(this.TabBuilder('会议注册', 0))

        // 行业动态模块
        TabContent() {
          NewsTabContent()
        }
        .tabBar(this.TabBuilder('行业动态', 1))
      }
      .onChange((index: number) => {
        this.currentIndex = index
      })
      .backgroundColor('#F5F5F5')
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F5F5F5')
  }

  @Builder TabBuilder(title: string, index: number) {
    Column() {
      Text(title)
        .fontSize(14)
        .fontColor(this.currentIndex === index ? '#007DFF' : '#666666')
        .fontWeight(this.currentIndex === index ? FontWeight.Bold : FontWeight.Normal)
    }
    .width('100%')
    .height(50)
    .justifyContent(FlexAlign.Center)
    .backgroundColor('#FFFFFF')
  }
}