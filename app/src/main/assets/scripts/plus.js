'use strict';
const fetch = require('lib/fetch.js').fetch;
const loadUrl = require('index').loadUrl;
const launch = require('launch').launch;

loadUrl().then(() => {
  // 验证结果: LayaNative 中 XMLHTTPRequest 不受 CORS 限制
  // fetch('https://api.bilibili.com/x/web-interface/archive/stat?aid=2')
  //   .then(data => data.text())
  //   .then(text => {
  //     console.log('test-fetch');
  //     console.log(text);
  //   })
  //   .catch(e => {
  //     console.error(e);
  //   });
  launch();
});
