exports.launch = function() {
  window.loadingView.loading(10);

  fetch('version.json')
    .then(data => {
      // 加载到 20%
      window.loadingView.loading(20);

      if (data.status === 200) {
        return data.json();
      } else {
        throw data.status;
      }
    })
    .then(json => {
      document.createElement('script').src = json.code;
    })
    .catch(code => {
      alert(`加载页面失败: ${code}`);
    });
};
