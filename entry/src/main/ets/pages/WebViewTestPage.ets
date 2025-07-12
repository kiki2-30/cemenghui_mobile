import web_webview from '@ohos.web.webview'
import router from '@ohos.router'

interface WebViewTestPageParams {
  url?: string
}

@Entry
@Component
export struct WebViewTestPage {
  private webController: web_webview.WebviewController = new web_webview.WebviewController()
  @State webUrl: string = 'http://www.chinagongyi.com.cn/'

  aboutToAppear() {
    // 只设置url，不自动调用loadWebPage，避免WebviewController未绑定Web组件时报错
    const params = router.getParams() as WebViewTestPageParams
    if (params && params.url) {
      this.webUrl = params.url
    }
  }

  loadWebPage() {
    if (this.webUrl.trim()) {
      this.webController.loadUrl(this.webUrl)
    }
  }

  build() {
    Column() {
      Text('网页展示').fontSize(18).fontWeight(FontWeight.Bold).margin({ bottom: 16 })
      // 输入框
      Row() {
        TextInput({ 
          placeholder: '请输入网页URL', 
          text: this.webUrl 
        })
          .width('70%')
          .height(40)
          .onChange((value: string) => {
            this.webUrl = value
          })
        Button('打开网页')
          .width('25%')
          .height(40)
          .margin({ left: 8 })
          .onClick(() => this.loadWebPage())
      }
      .margin({ bottom: 16 })

      // WebView显示区域，初始直接用src属性
      Web({ src: this.webUrl, controller: this.webController })
        .width('100%')
        .height(500)
        .backgroundColor('#fff')
    }
    .padding(24)
    .width('100%')
    .height('100%')
    .backgroundColor('#fff')
  }
} 