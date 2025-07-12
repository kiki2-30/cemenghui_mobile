import { UserService } from '../services/UserService'
import router from '@ohos.router'
import promptAction from '@ohos.promptAction'

@Entry
@Component
struct RegisterPage {
  @State username: string = ''
  @State password: string = ''
  @State email: string = ''
  @State phone: string = ''
  @State realName: string = ''
  @State company: string = ''
  @State loading: boolean = false

  private userService: UserService = new UserService()

  async onRegister(): Promise<void> {
    // 用户名校验
    const usernameTrim = this.username.trim();
    if (!usernameTrim) {
      promptAction.showToast({ message: '用户名不能为空', duration: 2000 })
      return
    }
    if (usernameTrim.length < 3 || usernameTrim.length > 20) {
      promptAction.showToast({ message: '用户名长度必须在 3 到 20 个字符之间', duration: 2000 })
      return
    }
    // 密码校验
    if (!this.password) {
      promptAction.showToast({ message: '密码不能为空', duration: 2000 })
      return
    }
    if (this.password.length < 8 || this.password.length > 30) {
      promptAction.showToast({ message: '密码长度必须在 8 到 30 个字符之间', duration: 2000 })
      return
    }
    if (!(/[A-Za-z]/.test(this.password) && /[0-9]/.test(this.password))) {
      promptAction.showToast({ message: '密码必须至少包含一个字母和一个数字', duration: 2000 })
      return
    }
    if (!/^[-A-Za-z0-9@$!%*?&]+$/.test(this.password)) {
      promptAction.showToast({ message: '密码只能包含字母、数字和 @$!%*?&', duration: 2000 })
      return
    }
    // 邮箱校验
    if (!this.email) {
      promptAction.showToast({ message: '邮箱不能为空', duration: 2000 })
      return
    }
    if (!/^([\w.-]+)@([\w-]+)\.([a-zA-Z]{2,})$/.test(this.email)) {
      promptAction.showToast({ message: '邮箱格式不正确', duration: 2000 })
      return
    }
    // 手机号校验
    if (!this.phone) {
      promptAction.showToast({ message: '手机号不能为空', duration: 2000 })
      return
    }
    if (!/^1[3-9]\d{9}$/.test(this.phone)) {
      promptAction.showToast({ message: '手机号格式不正确', duration: 2000 })
      return
    }
    // 真实姓名校验
    const realNameTrim = this.realName.trim();
    if (!realNameTrim) {
      promptAction.showToast({ message: '真实姓名不能为空', duration: 2000 })
      return
    }
    if (realNameTrim.length < 2 || realNameTrim.length > 20) {
      promptAction.showToast({ message: '真实姓名长度必须在 2 到 20 个字符之间', duration: 2000 })
      return
    }
    // 公司名称 company 可选，无特殊校验
    this.loading = true
    try {
      const res = await this.userService.register({
        username: usernameTrim,
        password: this.password,
        email: this.email,
        phone: this.phone,
        realName: realNameTrim,
        company: this.company
      })
      if (res && res.data) {
        promptAction.showToast({ message: '注册成功，请登录', duration: 2000 })
        router.replaceUrl({ url: 'pages/LoginPage' })
        return
      }
      promptAction.showToast({ message: '注册失败', duration: 2000 })
    } catch (e) {
      promptAction.showToast({ message: '注册异常，请检查网络', duration: 2000 })
    } finally {
      this.loading = false
    }
  }

  build() {
    Column() {
      Text('用户注册')
        .fontSize(22)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 32, top: 80 })

      TextInput({ placeholder: '用户名', text: this.username })
        .width('80%').height(44).margin({ bottom: 12 })
        .onChange((v: string) => this.username = v)
      TextInput({ placeholder: '密码', text: this.password })
        .width('80%').height(44).margin({ bottom: 12 })
        .onChange((v: string) => this.password = v)
      TextInput({ placeholder: '邮箱', text: this.email })
        .width('80%').height(44).margin({ bottom: 12 })
        .onChange((v: string) => this.email = v)
      TextInput({ placeholder: '手机号', text: this.phone })
        .width('80%').height(44).margin({ bottom: 12 })
        .onChange((v: string) => this.phone = v)
      TextInput({ placeholder: '真实姓名', text: this.realName })
        .width('80%').height(44).margin({ bottom: 12 })
        .onChange((v: string) => this.realName = v)
      TextInput({ placeholder: '公司', text: this.company })
        .width('80%').height(44).margin({ bottom: 24 })
        .onChange((v: string) => this.company = v)

      Button(this.loading ? '注册中...' : '注册')
        .width('80%').height(44)
        .fontSize(16)
        .fontColor('#FFFFFF')
        .backgroundColor('#007DFF')
        .borderRadius(22)
        .onClick(() => { if (!this.loading) void this.onRegister() })
      Button('返回登录')
        .width('80%').height(36)
        .fontSize(14)
        .fontColor('#007DFF')
        .backgroundColor('#FFFFFF')
        .borderRadius(18)
        .margin({ top: 12 })
        .onClick(() => router.replaceUrl({ url: 'pages/LoginPage' }))
    }
    .width('100%').height('100%')
    .justifyContent(FlexAlign.Center)
    .alignItems(HorizontalAlign.Center)
    .backgroundColor('#FFFFFF')
  }
} 