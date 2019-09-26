var manager = PlatformClass.createClass(
  'cn.yesterday17.majsoul_android.extension.ExtensionManager'
);

function getScripts(name) {
  return new Promise(function(resolve, reject) {
    manager.callWithBack(code => {
      resolve(code);
    }, name);
  });
}

exports.getBeforeGameScripts = function() {
  return getScripts('getBeforeGameScripts');
};

exports.getAfterGameScripts = function() {
  return getScripts('getAfterGameScripts');
};
