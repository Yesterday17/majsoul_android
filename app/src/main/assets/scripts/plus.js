require('plus/platform.js');
require('plus/xhr.js');
require('lib/fetch.js');
require('plus/extensionFetch.js');
var scripts = require('plus/getScripts.js');
var Babel = require('lib/babel.min.js').Babel;

window.Majsoul_Plus = {};

var loadUrl = require('index').loadUrl;
var launch = require('launch').launch;

loadUrl().then(() => {
  Promise.all([scripts.getBeforeGameScripts(), scripts.getAfterGameScripts()])
    .then(arr =>
      arr.map(
        code =>
          Babel.transform(code, {
            sourceType: 'script',
            minified: true,
            presets: ['es2015', 'es2016', 'es2017']
          }).code
      )
    )
    .then(es5 => {
      eval(es5[0]);
      launch();
      eval(
        'Majsoul_Plus["after-game"] = setInterval(function(){if(window.loadingView){return;}' +
          es5[1] +
          'clearInterval(Majsoul_Plus["after-game"])}, 500)'
      );
    })
    .catch(e => {
      console.error(e);
    });
});
