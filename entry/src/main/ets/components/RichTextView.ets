import web_webview from '@ohos.web.webview'

interface RichTextStyle {
  fontSize?: number
  fontColor?: string
  lineHeight?: number
  padding?: number
}

@Component
export struct RichTextView {
  @Prop content: string = ''
  @Prop style?: RichTextStyle
  
  private webController: web_webview.WebviewController = new web_webview.WebviewController()
  
  // 将HTML内容转换为适合WebView显示的格式
  private getFormattedHtml(): string {
    const defaultStyle: RichTextStyle = {
      fontSize: this.style?.fontSize || 14,
      fontColor: this.style?.fontColor || '#333333',
      lineHeight: this.style?.lineHeight || 1.5,
      padding: this.style?.padding || 16
    }
    
    return `
      <!DOCTYPE html>
      <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            font-size: ${defaultStyle.fontSize}px;
            color: ${defaultStyle.fontColor};
            line-height: ${defaultStyle.lineHeight};
            padding: ${defaultStyle.padding}px;
            margin: 0;
            background-color: transparent;
          }
          h1, h2, h3, h4, h5, h6 {
            margin: 16px 0 8px 0;
            font-weight: bold;
          }
          h1 { font-size: 24px; }
          h2 { font-size: 20px; }
          h3 { font-size: 18px; }
          h4 { font-size: 16px; }
          h5 { font-size: 14px; }
          h6 { font-size: 12px; }
          p {
            margin: 8px 0;
            text-align: justify;
          }
          img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            margin: 8px 0;
          }
          a {
            color: #007DFF;
            text-decoration: none;
          }
          blockquote {
            border-left: 4px solid #007DFF;
            padding-left: 16px;
            margin: 16px 0;
            background-color: #F5F5F5;
            padding: 12px;
            border-radius: 4px;
          }
          code {
            background-color: #F5F5F5;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Courier New', monospace;
          }
          pre {
            background-color: #F5F5F5;
            padding: 12px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 8px 0;
          }
          ul, ol {
            padding-left: 20px;
            margin: 8px 0;
          }
          li {
            margin: 4px 0;
          }
          table {
            width: 100%;
            border-collapse: collapse;
            margin: 8px 0;
          }
          th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
          }
          th {
            background-color: #F5F5F5;
            font-weight: bold;
          }
          .highlight {
            background-color: #FFF3CD;
            padding: 2px 4px;
            border-radius: 4px;
          }
          .text-center { text-align: center; }
          .text-right { text-align: right; }
          .text-left { text-align: left; }
        </style>
      </head>
      <body>
        ${this.content}
      </body>
      </html>
    `
  }
  
  build() {
    Web({ src: this.getFormattedHtml(), controller: this.webController })
      .width('100%')
      .height('100%')
      .backgroundColor('transparent')
      .onPageBegin((event) => {
        console.info('富文本页面开始加载')
      })
      .onPageEnd((event) => {
        console.info('富文本页面加载完成')
      })
      .onErrorReceive((event) => {
        console.error('富文本加载错误:', event.error)
      })
  }
} 