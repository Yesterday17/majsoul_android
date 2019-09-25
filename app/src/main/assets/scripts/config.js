'use strict';

class loadingView {
  constructor() {
    this.bridge = PlatformClass.createClass(
      'cn.yesterday17.majsoul_android.game.SplashDialog'
    );
  }

  loading(value) {
    this.bridge.call('loading', value);

    if (value === 100) {
      delete window.loadingView;
    }
  }
}

window.loadingView = new loadingView();
window.ConchRenderType = 6;
