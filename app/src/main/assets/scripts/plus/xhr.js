var XMLHttpRequest_Original = require('plus/xhr_ori.js').XMLHttpRequest;

// class XMLHttpRequest {
//   open(method, url, async) {}

//   setRequestHeader(key, value) {
//     //
//   }
// }

window.XMLHttpRequest = XMLHttpRequest_Original;
