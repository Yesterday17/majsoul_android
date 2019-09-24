var XMLHttpRequest_Original = require('plus/xhr.js').XMLHttpRequest;

class XMLHttpRequest {
  open(method, url, async) {}

  setRequestHeader(key, value) {
    //
  }
}
window.XMLHttpRequest = XMLHttpRequest;
