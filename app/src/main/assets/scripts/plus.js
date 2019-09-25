'use strict';

require('plus/xhr.js');
require('plus/fetch.js');
require('plus/extensionFetch.js');

window.Majsoul_Plus = {};

const loadUrl = require('index').loadUrl;
const launch = require('launch').launch;

loadUrl().then(() => {
  var manager = PlatformClass.createClass(
    'cn.yesterday17.majsoul_android.extension.ExtensionManager'
  );
  manager.callWithBack(before => {
    eval(before);
    launch();
    manager.callWithBack(after => {
      eval(after);
    }, 'getAfterGameScripts');
  }, 'getBeforeGameScripts');
});
