'use strict';

function genTips(content) {
  return [`${content}`, `${content}·`, `${content}··`, `${content}···`];
}

class loadingView {
  constructor() {
    this.sOS = conchConfig.getOS();
    this.bridge = PlatformClass.createClass('cn.yesterday17.majsoul_android.game.SplashDialog');
  }

  get showTextInfo() {
    return this._showTextInfo;
  }

  set showTextInfo(value) {
    this._showTextInfo = value;
    if (this.bridge) {
      this.bridge.call('showTextInfo', value);
    }
  }

  bgColor(value) {
    if (this.bridge) {
      this.bridge.call('bgColor', value);
    }
  }

  setFontColor(value) {
    if (this.bridge) {
      this.bridge.call('setFontColor', value);
    }
  }

  setTips(value) {
    if (this.bridge) {
      this.bridge.call('setTips', value);
    }
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
    if (this.bridge) {
      this.bridge.call('loading', value);
    }
  }

  hideLoadingView() {
    this.bridge.call('hideSplash');
  }
}

window.loadingView = new loadingView();
if (window.loadingView) {
  window.loadingView.bgColor('#000000'); //设置背景颜色
  window.loadingView.setFontColor('#E8AF71'); //设置字体颜色
  window.loadingView.setTips(genTips('初始化中')); //设置tips数组，会随机出现
}
window.ConchRenderType = 6;
