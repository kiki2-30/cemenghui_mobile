import { StorageService } from '../services/StorageService'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { User } from '../types/index'

@Entry
@Component
struct ProfilePage {
  @State user: User | null = null

  aboutToAppear() {
    void StorageService.getInstance().getUserInfo().then(userInfo => {
      this.user = userInfo
    })
  }

  async onLogout() {
    await StorageService.getInstance().clearUserData()
    promptAction.showToast({ message: '已退出登录', duration: 1500 })
    router.replaceUrl({ url: 'pages/Index' })
  }

  build() {
    Column() {
      Text('个人中心')
        .fontSize(22)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 32, top: 80 })

      if (this.user) {
        Text(`用户名：${this.user.username}`)
          .fontSize(18)
          .fontColor('#333')
          .margin({ bottom: 16 })
        Text(`邮箱：${this.user.email}`)
          .fontSize(16)
          .fontColor('#666')
          .margin({ bottom: 16 })
        Text(`手机号：${this.user.phone}`)
          .fontSize(16)
          .fontColor('#666')
          .margin({ bottom: 32 })
      } else {
        Text('未获取到用户信息')
          .fontSize(16)
          .fontColor('#999')
          .margin({ bottom: 32 })
      }

      Button('退出登录')
        .width('80%')
        .height(48)
        .fontSize(16)
        .fontColor('#FFFFFF')
        .backgroundColor('#FF4757')
        .borderRadius(24)
        .onClick(() => { void this.onLogout() })
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center)
    .alignItems(HorizontalAlign.Center)
    .backgroundColor('#FFFFFF')
  }
} 