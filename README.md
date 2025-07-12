# 合作平台移动端应用

基于鸿蒙HarmonyOS开发的移动端合作平台应用，包含会议注册子系统和行业动态子系统。

## 功能特性

### 会议注册子系统
- ✅ 合作列表分类浏览（会议研讨、标准定制、技术培训、工具研发、公益行动）
- ✅ 会议详情查看
- ✅ 参会回执填写（包含详细字段）
- ✅ 各类合作详情查看
- ✅ 下拉刷新和上拉加载更多
- ✅ 用户注册与登录

### 行业动态子系统
- ✅ 行业动态列表展示
- ✅ 模糊搜索功能
- ✅ 下拉刷新和上拉加载更多
- ✅ 动态详情展示（支持富文本）
- ✅ 用户交互（点赞、收藏、浏览记录）
- ✅ 用户行为分析

## 项目结构

```
entry/src/main/ets/
├── pages/                    # 页面组件
│   ├── Index.ets            # 主页面（底部导航）
│   ├── ConferencePage.ets   # 会议注册页面
│   ├── NewsPage.ets         # 行业动态页面
│   ├── ConferenceDetailPage.ets  # 会议详情页面
│   └── NewsDetailPage.ets   # 行业动态详情页面
├── services/                 # 服务层
│   ├── HttpService.ets      # HTTP请求服务
│   ├── UserService.ets      # 用户服务
│   ├── ConferenceService.ets # 会议服务
│   ├── NewsService.ets      # 行业动态服务
│   └── StorageService.ets   # 本地存储服务
└── entryability/
    └── EntryAbility.ets     # 应用入口
```

## 技术栈

- **框架**: HarmonyOS ArkTS
- **UI组件**: ArkUI
- **网络请求**: @ohos.net.http
- **本地存储**: @ohos.data.preferences
- **路由**: @ohos.router

## 接口文档

### 已实现的接口
- 用户管理：注册、登录、信息管理
- 会议管理：分类、列表、详情、注册
- 行业动态：列表、详情、用户交互

### 需要补充的接口

#### 1. 用户权限管理
```javascript
// 用户角色枚举
enum UserRole {
  NORMAL_USER = 'NORMAL_USER',      // 普通用户
  ADMIN = 'ADMIN',                  // 管理员
  SUPER_ADMIN = 'SUPER_ADMIN'       // 超级管理员
}

// 获取用户角色
GET /user/{userId}/role

// 检查用户权限
GET /user/{userId}/permissions
```

#### 2. 会议注册扩展字段
```javascript
// 扩展会议注册接口
POST /api/conferences/register
{
  "conferenceId": 1,
  "name": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "company": "科技有限公司",
  "position": "工程师",
  "gender": "男",                    // 新增：性别
  "arrivalMethod": "高铁",           // 新增：到达方式
  "arrivalTrain": "G101",           // 新增：到达车次
  "arrivalTime": "2024-06-01 09:00" // 新增：到达时间
}
```

#### 3. 微信登录接口
```javascript
// 微信登录
POST /user/wechat-login
{
  "code": "微信授权码",
  "encryptedData": "加密数据",
  "iv": "加密向量"
}

// 响应
{
  "code": 200,
  "message": "success!",
  "data": {
    "token": "jwt_token",
    "userInfo": {
      "userId": 1,
      "username": "wechat_user",
      "realName": "张三",
      "avatar": "头像URL"
    }
  }
}
```

#### 4. 公益行动小程序跳转
```javascript
// 获取小程序信息
GET /api/conferences/{conferenceId}/miniprogram-info

// 响应
{
  "code": 200,
  "message": "success!",
  "data": {
    "appId": "小程序AppID",
    "path": "跳转路径",
    "extraData": "额外参数"
  }
}
```

#### 5. 用户行为分析接口
```javascript
// 获取用户行为统计（超级管理员权限）
GET /api/analytics/user-behavior?startDate=2024-06-01&endDate=2024-06-30

// 获取会议注册统计
GET /api/analytics/conference-stats?conferenceId=1&startDate=2024-06-01&endDate=2024-06-30

// 获取行业动态统计
GET /api/analytics/news-stats?newsId=1&startDate=2024-06-01&endDate=2024-06-30
```

## 安装和运行

1. 确保已安装DevEco Studio
2. 克隆项目到本地
3. 在DevEco Studio中打开项目
4. 配置API服务器地址（在services目录下的各个服务类中）
5. 运行项目

## 配置说明

### API服务器配置
在以下文件中修改`baseUrl`：
- `services/UserService.ets`
- `services/ConferenceService.ets`
- `services/NewsService.ets`

```typescript
private baseUrl: string = 'http://your-api-domain.com' // 替换为实际API地址
```

### 网络权限配置
确保在`module.json5`中配置了网络权限：

```json
{
  "requestPermissions": [
    {
      "name": "ohos.permission.INTERNET"
    }
  ]
}
```

## 开发说明

### 添加新页面
1. 在`pages`目录下创建新的页面文件
2. 在`main_pages.json`中添加页面路由
3. 使用`router.pushUrl()`进行页面跳转

### 添加新接口
1. 在对应的服务类中添加新方法
2. 在页面中调用服务方法
3. 处理响应数据和错误

### 样式定制
- 使用ArkUI的样式系统
- 支持响应式布局
- 遵循鸿蒙设计规范

## 注意事项

1. 确保API服务器正常运行
2. 网络请求需要配置正确的权限
3. 本地存储需要初始化
4. 用户登录状态需要正确管理
5. 错误处理要完善

## 后续优化

1. 添加图片上传功能
2. 实现消息推送
3. 添加离线缓存
4. 优化性能
5. 添加单元测试
6. 完善错误处理
7. 添加国际化支持 