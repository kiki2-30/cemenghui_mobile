@Component
export struct SimpleRichTextEditor {
  @Prop content: string = ''
  // 移除 @Prop 装饰的函数参数，改为普通的事件回调属性
  onContentChange: (content: string) => void = () => {}
  
  @State currentContent: string = ''
  
  aboutToAppear() {
    this.currentContent = this.content
  }
  
  build() {
    Column() {
      Text('富文本编辑器')
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#333333')
        .margin({ bottom: 8 })
      
      // 工具栏
      Row() {
        Button('粗体')
          .fontSize(12)
          .backgroundColor('#f0f0f0')
          .fontColor('#333')
          .borderRadius(4)
          .padding({ left: 8, right: 8, top: 4, bottom: 4 })
          .onClick(() => this.insertTag('<b>', '</b>'))
        
        Button('斜体')
          .fontSize(12)
          .backgroundColor('#f0f0f0')
          .fontColor('#333')
          .borderRadius(4)
          .padding({ left: 8, right: 8, top: 4, bottom: 4 })
          .margin({ left: 8 })
          .onClick(() => this.insertTag('<i>', '</i>'))
        
        Button('标题')
          .fontSize(12)
          .backgroundColor('#f0f0f0')
          .fontColor('#333')
          .borderRadius(4)
          .padding({ left: 8, right: 8, top: 4, bottom: 4 })
          .margin({ left: 8 })
          .onClick(() => this.insertTag('<h3>', '</h3>'))
        
        Button('列表')
          .fontSize(12)
          .backgroundColor('#f0f0f0')
          .fontColor('#333')
          .borderRadius(4)
          .padding({ left: 8, right: 8, top: 4, bottom: 4 })
          .margin({ left: 8 })
          .onClick(() => this.insertTag('<ul><li>', '</li></ul>'))
      }
      .margin({ bottom: 12 })
      
      // 文本编辑区域
      TextArea({ 
        placeholder: '请输入内容...', 
        text: this.currentContent 
      })
        .width('100%')
        .height(200)
        .fontSize(14)
        .fontColor('#333333')
        .backgroundColor('#ffffff')
        .borderRadius(8)
        .border({ width: 1, color: '#ddd' })
        .onChange((value: string) => {
          this.currentContent = value
          this.onContentChange(value)
        })
      
      // 预览区域
      if (this.currentContent) {
        Text('预览:')
          .fontSize(14)
          .fontWeight(FontWeight.Bold)
          .fontColor('#333333')
          .margin({ top: 16, bottom: 8 })
        
        Text(this.currentContent)
          .fontSize(14)
          .fontColor('#666666')
          .lineHeight(20)
          .backgroundColor('#f9f9f9')
          .padding(12)
          .borderRadius(8)
          .width('100%')
      }
    }
    .width('100%')
    .backgroundColor('#ffffff')
  }
  
  // 插入HTML标签
  private insertTag(startTag: string, endTag: string) {
    // 这里可以实现简单的标签插入逻辑
    // 由于ArkTS的限制，我们使用简单的文本编辑
    this.currentContent += ` ${startTag}文本${endTag} `
    this.onContentChange(this.currentContent)
  }
} 