var platform = new Platform('extension.ExtensionManager');

exports.getBeforeGameScripts = function() {
  return platform.send('getBeforeGameScripts');
};

exports.getAfterGameScripts = function() {
  return platform.send('getAfterGameScripts');
};
