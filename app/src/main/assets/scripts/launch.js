exports.launch = function() {
  window.loadingView.loading(10);

  window.LayaExpose.send('version')
    .then(version => {
      return window.LayaExpose.send('resVersion');
    })
    .then(resversion => {
      window.resversion = JSON.stringify(resversion);

      // 加载到 20%
      window.loadingView.loading(20);

      return window.LayaExpose.send('code');
    })
    .then(code => {
      document.createElement('script').text = code;
    })
    .catch(e => {
      alert(`错误：${e}。请尝试重新启动游戏！`);
    });
};
