var platform = new Platform('majsoul.LayaExpose');

exports.launch = function() {
  window.loadingView.loading(10);

  platform
    .send('version')
    .then(version => {
      // 加载到 20%
      window.loadingView.loading(20);

      return platform.send('code');
    })
    .then(code => {
      document.createElement('script').text = code;
    })
    .catch(e => {
      alert(`错误：${e}。请尝试重新启动游戏！`);
    });
};
