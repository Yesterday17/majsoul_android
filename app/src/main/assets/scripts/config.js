'use strict';

function genTips(content) {
  return [`${content}`, `${content}·`, `${content}··`, `${content}···`];
}

class loadingView {
  constructor() {
    this.bridge = PlatformClass.createClass(
      'cn.yesterday17.majsoul_android.game.SplashDialog'
    );
  }

  setFontColor(value) {
    this.bridge.call('setFontColor', value);
  }

  setTips(value) {
    this.bridge.call('setTips', value);
  }

  loading(value) {
    switch (value) {
      case 10:
        this.setTips(genTips('加载 HTML '));
        break;
      case 20:
        this.setTips(genTips('加载 version.json 中'));
        break;
      case 30:
        this.setTips(genTips('执行 load0 中'));
        break;
      case 40:
        this.setTips(genTips('加载 resversion.json 中'));
        break;
      case 70:
        this.setTips(genTips('加载 config.json 中'));
        break;
      case 80:
        this.setTips(genTips('加载 liqi.json 中'));
        break;
      case 85:
        this.setTips(genTips('执行 app.NetAgent.init 中'));
        break;
      case 100:
        this.setTips(genTips('加载完成'));
        break;
    }
    this.bridge.call('loading', value);
  }
}

window.loadingView = new loadingView();
window.ConchRenderType = 6;
