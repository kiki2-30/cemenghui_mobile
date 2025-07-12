import { UserService } from '../services/UserService'
import { User } from '../types/index'
import router from '@ohos.router'

interface GeneratedTypeLiteralInterface_1 {
  userId?: number;
}

@Entry
@Component
struct UserDetailPage {
  @State user: User | null = null
  private userService: UserService = new UserService()

  aboutToAppear() {
    const params = router.getParams() as GeneratedTypeLiteralInterface_1
    const userId = params.userId
    if (typeof userId === 'number') {
      this.loadUser(userId)
    }
  }

  async loadUser(userId: number) {
    const res = await this.userService.getUserInfo(userId)
    this.user = res.data
  }

  build() {
    Column() {
      Text('用户详情').fontSize(20).fontWeight(FontWeight.Bold).margin({ bottom: 16 })
      if (this.user) {
        Text('用户名: ' + (this.user.username ?? '-'))
        Text('邮箱: ' + (this.user.email ?? '-'))
        Text('手机号: ' + (this.user.phone ?? '-'))
        Text('真实姓名: ' + (this.user.realName ?? '-'))
        Text('公司: ' + (this.user.company ?? '-'))
        Text('创建时间: ' + (this.user.createTime ?? '-'))
        Text('更新时间: ' + (this.user.updateTime ?? '-'))
      } else {
        Text('加载中...')
      }
      Button('返回').onClick(() => router.back())
    }
    .padding(24)
    .width('100%')
    .height('100%')
    .backgroundColor('#FFF')
  }
} 