import router from '@ohos.router'
import promptAction from '@ohos.promptAction'
import { UserService, UpdateUserData } from '../services/UserService'
import { User } from '../types/index'

// 在新版本的SDK中，这些UI枚举通常是全局可用的，无需手动导入。
// 如果您的IDE仍然提示找不到，请使用IDE的快速修复功能（通常是Alt+Enter）自动导入。

interface UserEditPageParams {
  userId?: number;
}

// 用class声明，避免结构化类型
class UserUpdateData {
  username: string = '';
  password?: string;
  email: string = '';
  phone: string = '';
  realName: string = '';
  company: string = '';
}

@Entry
@Component
struct UserEditPage {
  @State user: User | null = null
  @State username: string = ''
  @State password: string = ''
  @State email: string = ''
  @State phone: string = ''
  @State realName: string = ''
  @State company: string = ''
  @State isLoading: boolean = true

  private userService: UserService = new UserService()

  aboutToAppear() {
    const params = router.getParams() as UserEditPageParams
    const userId = params.userId
    if (typeof userId === 'number') {
      this.loadUser(userId)
    } else {
      promptAction.showToast({ message: '用户ID无效或格式不正确' })
      router.back()
    }
  }

  async loadUser(userId: number) {
    this.isLoading = true
    try {
      const res = await this.userService.getUserInfo(userId)
      if (res && res.code === 200 && res.data) {
        this.user = res.data
        this.username = res.data.username ?? ''
        this.email = res.data.email ?? ''
        this.phone = res.data.phone ?? ''
        this.realName = res.data.realName ?? ''
        this.company = res.data.company ?? ''
      } else {
        promptAction.showToast({ message: res?.message || '获取用户信息失败' })
      }
    } catch (e) {
      promptAction.showToast({ message: '加载用户信息异常' })
    } finally {
      this.isLoading = false
    }
  }

  async onSave() {
    if (!this.username.trim() || !this.email.trim() || !this.phone.trim() || !this.realName.trim()) {
      promptAction.showToast({ message: '请填写所有必填项' })
      return
    }
    const userId = this.user?.userId
    if (!userId) return

    // 必须用class实例，逐字段赋值
    const updateData = new UpdateUserData();
    updateData.username = this.username;
    updateData.email = this.email;
    updateData.phone = this.phone;
    updateData.realName = this.realName;
    updateData.company = this.company;
    if (this.password.trim()) {
      updateData.password = this.password;
    }

    try {
      const res = await this.userService.updateUser(userId, updateData)
      if (res && res.code === 200) {
        promptAction.showToast({ message: '修改成功' })
        router.back()
      } else {
        promptAction.showToast({ message: `修改失败: ${res?.message || '未知错误'}` })
      }
    } catch (e) {
      promptAction.showToast({ message: '修改异常' })
    }
  }

  // @Builder 方法用于构建可复用的UI片段
  @Builder
  private FormContent() {
    Column({ space: 16 }) {
      TextInput({ placeholder: '用户名', text: this.username }).onChange((v) => this.username = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
      TextInput({ placeholder: '新密码 (留空不修改)', text: this.password }).type(InputType.Password).onChange((v) => this.password = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
      TextInput({ placeholder: '邮箱', text: this.email }).type(InputType.Email).onChange((v) => this.email = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
      TextInput({ placeholder: '手机号', text: this.phone }).type(InputType.PhoneNumber).onChange((v) => this.phone = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
      TextInput({ placeholder: '真实姓名', text: this.realName }).onChange((v) => this.realName = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
      TextInput({ placeholder: '公司', text: this.company }).onChange((v) => this.company = v)
        .height(48).backgroundColor('#F7F7F7').borderRadius(8).padding({ left: 12, right: 12 })
    }
    .width('100%')
  }

  build() {
    Column() {
      // 导航栏
      Row() {
        Button('←')
          .width(40).height(40).fontSize(20).fontColor(Color.Black)
          .backgroundColor(Color.Transparent).type(ButtonType.Circle)
          .onClick(() => router.back())
        Text('编辑用户')
          .fontSize(20).fontWeight(FontWeight.Bold).margin({ left: 8 })
      }
      .width('100%').height(56).padding({ left: 16, right: 16 })
      .justifyContent(FlexAlign.Start).alignItems(VerticalAlign.Center)

      // 主体内容
      Scroll() {
        Column() {
          if (this.isLoading) {
            LoadingProgress().width(40).margin({ top: 100 })
          } else {
            this.FormContent()
          }
        }
        .padding(16)
      }.layoutWeight(1)

      // 底部按钮
      Row({ space: 12 }) {
        Button('取消')
          .layoutWeight(1).height(48).type(ButtonType.Normal).backgroundColor('#EFEFEF')
          .fontColor(Color.Black)
          .onClick(() => router.back())

        Button('保存').onClick(() => this.onSave())
          .layoutWeight(1).height(48).type(ButtonType.Capsule)
          .enabled(!this.isLoading)
      }.width('100%').padding(16)
    }
    .width('100%').height('100%').backgroundColor(Color.White)
  }
}
