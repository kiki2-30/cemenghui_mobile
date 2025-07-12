import { UserService } from '../services/UserService'
import { StorageService } from '../services/StorageService'
import { LoginResponse } from '../types/index'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'

@Entry
@Component
struct LoginPage {
  @State username: string = ''
  @State password: string = ''
  @State loading: boolean = false

  private userService: UserService = new UserService()

  async onLogin(): Promise<void> {
    if (!this.username.trim() || !this.password.trim()) {
      promptAction.showToast({ message: '请输入账号和密码', duration: 2000 })
      return
    }
    this.loading = true
    try {
      console.info('[LoginPage] 开始登录，用户名:', this.username)
      console.info('[LoginPage] 密码长度:', this.password.length)
      console.info('[LoginPage] 密码内容:', '*'.repeat(this.password.length))
      console.info('[LoginPage] 密码是否为空:', this.password === '')
      console.info('[LoginPage] 密码是否包含空格:', this.password.includes(' '))
      console.info('[LoginPage] 密码trim后长度:', this.password.trim().length)
      console.info('[LoginPage] 发送登录请求参数:', { username: this.username, password: this.password })
      
      const res = await this.userService.login({ username: this.username, password: this.password })
      console.info('[LoginPage] 登录响应:', res)
      
      if (res && res.data && res.data.token) {
        const loginData: LoginResponse = res.data
        console.info('[LoginPage] 登录数据:', loginData)
        console.info('[LoginPage] 登录数据role字段:', loginData.role)
        await StorageService.getInstance().saveUserToken(loginData.token)
        await StorageService.getInstance().saveUserInfo(loginData)
        const savedRole = await StorageService.getInstance().getUserRole()
        console.info('[LoginPage] 存储后userRole:', savedRole)
        await StorageService.getInstance().setLoginStatus(true)
        promptAction.showToast({ message: '登录成功', duration: 1500 })
        router.replaceUrl({ url: 'pages/Index' })
        return
      }
      promptAction.showToast({ message: '登录失败，账号或密码错误', duration: 2000 })
    } catch (e) {
      console.error('[LoginPage] 登录异常:', e)
      promptAction.showToast({ message: '登录失败，请检查网络', duration: 2000 })
    } finally {
      this.loading = false
    }
  }

  build() {
    Column() {
      Text('用户登录')
        .fontSize(22)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 32, top: 80 })

      TextInput({ placeholder: '请输入账号', text: this.username })
        .width('80%')
        .height(48)
        .backgroundColor('#F5F5F5')
        .borderRadius(8)
        .padding({ left: 16, right: 16 })
        .margin({ bottom: 20 })
        .onChange((v: string) => this.username = v)

      TextInput({ placeholder: '请输入密码', text: this.password })
        .width('80%')
        .height(48)
        .backgroundColor('#F5F5F5')
        .borderRadius(8)
        .padding({ left: 16, right: 16 })
        .margin({ bottom: 32 })
        .onChange((v: string) => this.password = v)

      Button(this.loading ? '登录中...' : '登录')
        .width('80%')
        .height(48)
        .fontSize(16)
        .fontColor('#FFFFFF')
        .backgroundColor('#007DFF')
        .borderRadius(24)
        .onClick(() => { if (!this.loading) void this.onLogin() })
      Button('去注册')
        .width('80%')
        .height(36)
        .fontSize(14)
        .fontColor('#007DFF')
        .backgroundColor('#FFFFFF')
        .borderRadius(18)
        .margin({ top: 12 })
        .onClick(() => router.replaceUrl({ url: 'pages/RegisterPage' }))
    }
    .width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center)
    .alignItems(HorizontalAlign.Center)
    .backgroundColor('#FFFFFF')
  }
} 