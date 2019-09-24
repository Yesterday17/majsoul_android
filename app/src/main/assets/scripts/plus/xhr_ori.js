var __extends =
  (this && this.__extends) ||
  (function() {
    var extendStatics =
      Object.setPrototypeOf ||
      ({ __proto__: [] } instanceof Array &&
        function(d, b) {
          d.__proto__ = b;
        }) ||
      function(d, b) {
        for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
      };
    return function(d, b) {
      extendStatics(d, b);
      function __() {
        this.constructor = d;
      }
      d.prototype =
        b === null
          ? Object.create(b)
          : ((__.prototype = b.prototype), new __());
    };
  })();

var XMLHttpRequest = (function(_super) {
  __extends(XMLHttpRequest, _super);
  function XMLHttpRequest() {
    var _this = _super.call(this) || this;
    _this._hasReqHeader = false;
    _this.withCredentials = false;
    _this.setResponseHeader = function(name, value) {
      this._head = value;
    };
    _this.xhr = new _XMLHttpRequest();
    _this._readyState = 0;
    _this._responseText = _this._response = _this._responseType = _this._url =
      '';
    _this._responseType = 'text';
    _this._method = 'GET';
    _this.xhr._t = _this;
    _this.xhr.set_onreadystatechange(function(r) {
      var _t = this._t;
      if (r == 1) {
        _t._readyState = 1;
      }
      if (_t._onrchgcb) {
        var e = new Event('readystatechange');
        e.target = _t;
        _t._onrchgcb(e);
      }
      var ev;
      if (_t._status == 200) {
        ev = new Event('load');
        ev.target = _t;
        _t.dispatchEvent(ev);
      } else if (_t._status == 404) {
        ev = new Event('error');
        ev.target = _t;
        _t.dispatchEvent(ev);
      }
    });
    return _this;
  }
  XMLHttpRequest.prototype.setRequestHeader = function(name, value) {
    this.xhr.setRequestHeader(name, value);
    this._hasReqHeader = true;
  };
  XMLHttpRequest.prototype.getAllResponseHeaders = function() {
    return this._head;
  };
  Object.defineProperty(XMLHttpRequest.prototype, 'responseText', {
    get: function() {
      return this._responseText;
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'response', {
    get: function() {
      return this._response;
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'responseType', {
    get: function() {
      return this._responseType;
    },
    set: function(type) {
      this._responseType = type;
      if (type == 'blob') {
        this.xhr.responseTypeCode = 4;
      } else if (type == 'arraybuffer') {
        this.xhr.responseTypeCode = 5;
      } else {
        this.xhr.responseTypeCode = 1;
      }
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'url', {
    get: function() {
      return this._url;
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'async', {
    get: function() {
      return this._async;
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'readyState', {
    get: function() {
      return this._readyState;
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'status', {
    get: function() {
      return this._status;
    },
    enumerable: true,
    configurable: true
  });
  XMLHttpRequest.prototype._loadsus = function() {
    var e = new Event('load');
    e.target = this;
    this._onloadcb(e);
  };
  Object.defineProperty(XMLHttpRequest.prototype, 'onreadystatechange', {
    get: function() {
      return this._onrchgcb;
    },
    set: function(listen) {
      this._onrchgcb = listen;
      if (listen == null) return;
      if (this._readyState != 0) {
        var e = new Event('readystatechange');
        e.target = this;
        this._onrchgcb(e);
      }
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(XMLHttpRequest.prototype, 'onload', {
    get: function() {
      return this._onloadcb;
    },
    set: function(listen) {
      this._onloadcb = listen;
      if (listen == null) return;
      if (this._readyState == 4 && this._status == 200) {
        this._loadsus();
      }
    },
    enumerable: true,
    configurable: true
  });
  XMLHttpRequest.prototype.getResponseHeader = function() {
    return this._head;
  };
  XMLHttpRequest.prototype.open = function(type, url, async) {
    console.log('xhr.' + type + ' url=' + url);
    if (!url) return;
    type = type.toUpperCase();
    async = true;
    url = location.resolve(url);
    this._method = type === 'POST' ? 'POST' : 'GET';
    this._url = url;
    this._async =
      async == null || async == undefined || async == true ? true : false;
    this.xhr._open(this._method, this._url, this._async);
  };
  XMLHttpRequest.prototype.overrideMimeType = function(mime) {
    if (this._responseType == 'text' || this._responseText == '')
      this._responseType = 'arraybuffer';
    this.xhr.mimeType = '1';
  };
  XMLHttpRequest.prototype.send = function(body) {
    if (body) {
      if (
        body instanceof ArrayBuffer ||
        ArrayBuffer.isView(body) ||
        body instanceof DataView
      )
        this._responseType = 'arraybuffer';
      else if (body instanceof Object) {
        body = JSON.stringify(body);
      }
    }
    this.xhr._t = this;
    var onPostLoad = function(buf, strbuf) {
      var _t = this._t;
      if (_t.responseType == 'arraybuffer') {
        _t._response = buf;
        _t._responseText = strbuf;
      } else {
        _t._response = _t._responseText = buf;
      }
      _t._readyState = 4;
      _t._status = 200;
      _t.xhr._changeState(4);
      if (_t._onloadcb) {
        _t._loadsus();
      }
      onPostLoad.ref = onPostError.ref = null;
    };
    var onPostError = function(e1, e2) {
      var _t = this._t;
      _t._readyState = 4;
      _t._status = 404;
      _t.xhr._changeState(4);
      if (_t.onerror) {
        var ev = new Event('error');
        ev.target = _t;
        ev['ecode1'] = e1;
        ev['ecode2'] = e2;
        _t.onerror(ev);
      }
      onPostLoad.ref = onPostError.ref = null;
    };
    if (this._method == 'POST' && body) {
      onPostLoad.ref = onPostError.ref = this.xhr;
      this.xhr.setPostCB(onPostLoad, onPostError);
      this.xhr.postData(this.url, body);
    } else if (this._hasReqHeader) {
      onPostLoad.ref = onPostError.ref = this.xhr;
      this.xhr.setPostCB(onPostLoad, onPostError);
      this.xhr.getData(this.url);
    } else {
      var file = new conch_File(this.url);
      var fileRead = new FileReader();
      fileRead.sync = !this.async;
      if (this._responseType == 'text' || this._responseType == 'TEXT') {
        fileRead.responseType = 0;
      } else if (this._responseType == 'arraybuffer') {
        fileRead.responseType = 1;
      } else {
        console.log(
          'XMLhttpRequest 暂不支持的类型 responseType=' + this.responseType
        );
      }
      fileRead._t = this;
      fileRead.onload = function() {
        var _t = this._t;
        if (_t._responseType == 'arraybuffer') {
          _t._response = this.result;
        } else {
          _t._response = _t._responseText = this.result;
          if (_t._responseType == 'json') {
            _t._response = JSON.parse(this.result);
          }
        }
        if (_t.xhr.mimeType) {
          var u8arr = new Uint8Array(_t._response);
          var strret = '';
          u8arr.forEach(function(v, i, arr) {
            if (v >= 0x80) {
              strret += String.fromCharCode(0xf700 | v);
            } else if (v == 0) {
              strret += '\0';
            } else {
              strret += String.fromCharCode(v);
            }
          });
          _t._responseText = strret;
        }
        _t._readyState = 4;
        _t._status = 200;
        _t.xhr._changeState(4);
        if (_t._onloadcb) {
          _t._loadsus();
        }
        fileRead.onload = null;
        fileRead.onerror = null;
      };
      fileRead.onerror = function() {
        var _t = this._t;
        _t._readyState = 4;
        _t._status = 404;
        _t.xhr._changeState(4);
        if (_t.onerror) {
          var ev = new Event('error');
          ev.target = _t;
          _t.onerror(ev);
        }
        fileRead.onload = null;
        fileRead.onerror = null;
      };
      if (this.onerror) {
        fileRead.setIgnoreError(true);
      }
      if (this.responseType == 'arraybuffer') fileRead.readAsArrayBuffer(file);
      else fileRead.readAsText(file);
    }
  };
  return XMLHttpRequest;
})(EventTarget);

exports.XMLHttpRequest = XMLHttpReques;
