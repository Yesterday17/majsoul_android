function Platform(_class) {
  this.platform = PlatformClass.createClass(Platform.prefix + '.' + _class);
}

Platform.prototype.send = function(funcName) {
  return new Promise((resolve, reject) => {
    this.platform.callWithBack(response => {
      var resp = JSON.parse(response);
      if (resp.err) {
        reject(resp.err);
      } else {
        resolve(resp.data);
      }
    }, funcName);
  });
};

Platform.prefix = 'cn.yesterday17.majsoul_android';

window.Platform = Platform;
