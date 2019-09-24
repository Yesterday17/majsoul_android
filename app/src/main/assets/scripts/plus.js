'use strict';
const loadUrl = require('index').loadUrl;
const launch = require('launch').launch;

loadUrl().then(() => {
  launch();
});
