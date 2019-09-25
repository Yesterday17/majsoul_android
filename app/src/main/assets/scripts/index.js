'use strict';
var __awaiter =
  (this && this.__awaiter) ||
  function(thisArg, _arguments, P, generator) {
    function adopt(value) {
      return value instanceof P
        ? value
        : new P(function(resolve) {
            resolve(value);
          });
    }
    return new (P || (P = Promise))(function(resolve, reject) {
      function fulfilled(value) {
        try {
          step(generator.next(value));
        } catch (e) {
          reject(e);
        }
      }
      function rejected(value) {
        try {
          step(generator['throw'](value));
        } catch (e) {
          reject(e);
        }
      }
      function step(result) {
        result.done
          ? resolve(result.value)
          : adopt(result.value).then(fulfilled, rejected);
      }
      step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
  };
Object.defineProperty(exports, '__esModule', { value: true });

window._conchInfo = { version: '2.1.3.1' };
var _inline = !conchConfig.localizable;
console.log('======================================================  ');
console.log('             LAYA CONCH            ');
console.log('     runtimeversion:' + conchConfig.getRuntimeVersion());
console.log('          jsversion:' + window._conchInfo.version);
console.log('             isplug:' + conchConfig.getIsPlug());
console.log('======================================================');
var asyncs = require('async');

function initFreeType() {
  return __awaiter(this, void 0, void 0, function*() {
    let bRet = false;
    let sTempFontPath = conch.getCachePath() + '/runtimeFont/';
    if (!fs_exists(sTempFontPath)) {
      fs_mkdir(sTempFontPath);
    }
    sTempFontPath += 'layabox.ttf';
    bRet = conch.initFreeTypeDefaultFontFromFile(sTempFontPath);
    if (bRet == false) {
      const assetFontData = conch.readFileFromAsset('font/layabox.ttf', 'raw');
      if (assetFontData) {
        fs_writeFileSync(sTempFontPath, assetFontData);
        bRet = conch.initFreeTypeDefaultFontFromFile(sTempFontPath);
      }
    }
    if (!bRet) {
      const fSystemVersion = navigator.sv;
      if (fSystemVersion >= 2.0 && fSystemVersion < 5.0) {
        bRet = conch.initFreeTypeDefaultFontFromFile(
          '/system/fonts/DFHEIA5A.ttf'
        );
        if (bRet == false) {
          bRet = conch.initFreeTypeDefaultFontFromFile(
            '/system/fonts/DroidSansFallback.ttf'
          );
        }
      } else if (fSystemVersion >= 5.0 && fSystemVersion < 6.0) {
        const vDefaultStrings = [];
        vDefaultStrings.push('/system/fonts/NotoSansHans-Regular.otf');
        vDefaultStrings.push('/system/fonts/Roboto-Regular.ttf');
        bRet = conch.initFreeTypeDefaultFontFromFile(vDefaultStrings.join('|'));
      } else if (fSystemVersion >= 6.0 && fSystemVersion < 7.0) {
        const vDefaultStrings = [];
        vDefaultStrings.push('/system/fonts/NotoSansSC-Regular.otf');
        vDefaultStrings.push('/system/fonts/Roboto-Regular.ttf');
        bRet = conch.initFreeTypeDefaultFontFromFile(vDefaultStrings.join('|'));
      } else if (fSystemVersion >= 7.0 && fSystemVersion < 8.0) {
        bRet = false;
      }
    }
    if (bRet == false) {
      console.log('[index] 字体初始化失败，即将从网络上下载字体');
      let data = yield asyncs.downloadSync(
        location.fullpath + '/font/simhei.ttf',
        true,
        null
      );
      if (!data) {
        data = yield asyncs.downloadSync(
          'http://runtime.layabox.com/font/simhei.ttf',
          true,
          null
        );
      }
      if (!data) {
        alert('字体下载失败！');
        return;
      }
      fs_writeFileSync(sTempFontPath, data);
      bRet = conch.initFreeTypeDefaultFontFromFile(sTempFontPath);
    }
    if (!bRet) {
      console.log('[index] 字体初始化失败');
    }
  });
}

function setOrientation(s) {
  const nameToVal = {
    landscape: 0,
    portrait: 1,
    user: 2,
    behind: 3,
    sensor: 4,
    nosensor: 5,
    sensor_landscape: 6,
    sensor_portrait: 7,
    reverse_landscape: 8,
    reverse_portrait: 9,
    full_sensor: 10
  };
  var nOri = (function(name) {
    try {
      var n = nameToVal[name];
      return n || 0;
    } catch (e) {
      return 0;
    }
  })(s);
  conchConfig.setScreenOrientation(nOri);
}

function initHtml(data) {
  const tag = new RegExp('<(/|)([\\w.]+)(?: +|)?([^>]*?)(/|)>', 'g');
  let res;
  let attrs;
  const scripts = [];
  const all = [];
  let lastTag = {};
  let lastIndex = 0;
  const _comments = new RegExp('<!--[\\s\\S]*?-->', 'g');
  const _blank = new RegExp('^[^\\S]*|[^\\S]*$', 'g');
  const _attributes = new RegExp('(\\w+)\\s*=\\s*("[^"]*"|\'[^\']*\')', 'g');
  data = data.replace('<script>', '<!--');
  data = data.replace('</script>', '-->');
  data = data.replace(_comments, '');
  let layaMeta = null;
  const supportedTag = ['html', 'title', 'head', 'body', 'script', 'meta'];
  while ((res = tag.exec(data))) {
    let temp = {};
    temp.tagName = res[2];
    if (res[3]) {
      temp.attributes = {};
      while ((attrs = _attributes.exec(res[3])) != null) {
        temp.attributes[attrs[1]] = attrs[2].substring(1, attrs[2].length - 1);
      }
    }
    if (temp.tagName === 'meta' && temp.attributes.name === 'laya')
      layaMeta = temp.attributes;
    if (!supportedTag.includes(temp.tagName)) {
      alert(
        `LayaPlayer不支持的标签：\n${res[0]}\n\n(具体请参考LayaBox网站文档)`
      );
    }
    if (!res[1] && !res[4]) temp.type = -1;
    else if (!res[1] && res[4]) temp.type = 0;
    else if (res[1] && !res[4]) temp.type = 1;
    else {
      alert(`解析标签出错</${temp.tagName}/>`);
      return;
    }
    temp.index = res.index;
    all.push(temp);
    if (temp.tagName == 'script' || temp.tagName == 'meta') {
      if (temp.type == -1) {
        if (lastTag.tagName == temp.tagName && lastTag.type == -1) {
          if (temp.tagName == 'meta') {
            scripts.push(temp);
          } else {
            alert(
              `解析标签${temp.tagName}出错,连续两个script标签可能是 脚本中包含<script>`
            );
            return;
          }
        }
        scripts.push(temp);
      } else if (temp.type == 1) {
        if (lastTag.tagName == temp.tagName && lastTag.type == -1) {
          let text = data.substring(lastIndex, temp.index);
          text = text.replace(_blank, '');
          if (text) {
            temp = scripts.pop();
            temp.type = 1;
            temp.attributes = temp.attributes || {};
            temp.attributes.text = text;
            scripts.push(temp);
          }
        }
      } else {
        if (temp.attributes) scripts.push(temp);
      }
    }
    lastIndex = res.index + res[0].length;
    lastTag = temp;
  }
  const chkjs =
    layaMeta && layaMeta.layajsprotect && layaMeta.layajsprotect == 'true';
  for (const d in scripts) {
    const one = scripts[d];
    if (chkjs) {
      if (!one.attributes) continue;
      if (one.tagName === 'script' && one.attributes.loader != 'laya') continue;
    }
    const t = document.createElement(one.tagName);
    t.onerror = () => {
      if (window['onLayaInitError']) {
        window['onLayaInitError']('Load script error');
      }
    };
    for (const attr in one.attributes) {
      t[attr] = one.attributes[attr];
    }
    document.head.appendChild(t);
  }
  if (layaMeta) setOrientation(layaMeta.screenorientation);
  const onloadScipt = document.createElement('script');
  onloadScipt.text = 'window.onload&&window.onload()';
}

function loadUrl(url) {
  return __awaiter(this, void 0, void 0, function*() {
    function updateDcc() {
      return __awaiter(this, void 0, void 0, function*() {
        cache.setResourceID('appurl', urlpath);
        const curassets = cache.getResourceID('netassetsid');
        const assetsidStr = yield asyncs.downloadSync(
          urlpath + 'update/assetsid.txt?rand=' + Math.random() * Date.now(),
          false,
          null
        );
        console.log(`[index] assetsid old: ${curassets} new: ${assetsidStr}`);
        if (!assetsidStr) {
          if (curassets && curassets != '') {
            if (window['onLayaInitError']) {
              isDccOk = false;
              window['onLayaInitError']('Update DCC get assetsid error');
            }
          }
        } else {
          if (curassets != assetsidStr) {
            console.log('[index] Need update');
            let txtdcc = '';
            const bindcc = yield asyncs.downloadSync(
              urlpath + 'update/filetable.bin?' + assetsidStr,
              true,
              null
            );
            if (!bindcc || !(bindcc instanceof ArrayBuffer)) {
              txtdcc = yield asyncs.downloadSync(
                urlpath + 'update/filetable.txt?' + assetsidStr,
                false,
                null
              );
            } else {
              if (bindcc.byteLength % 8 != 0) {
                console.log('[index] filetable.bin 长度非法');
              } else {
                const v = new Uint32Array(bindcc);
                if (v[0] != 0xffeeddcc || v[1] != 1) {
                  console.log('[index] dcc.bin file err!');
                } else {
                  if (v[2] == 0x00ffffff) {
                    const stp = (4 + 8) / 2;
                    const md5int = new Uint8Array(v.buffer, 16, 32);
                    const so = String.fromCharCode.apply(null, md5int);
                    console.log('[index] so=' + so);
                    console.log('[index] netid=' + assetsidStr);
                    if (so == assetsidStr) {
                      for (let ii = stp, isz = v.length / 2; ii < isz; ii++)
                        txtdcc +=
                          v[ii * 2].toString(16) +
                          ' ' +
                          v[ii * 2 + 1].toString(16) +
                          '\n';
                    }
                  } else {
                    console.log('[index] old format');
                    for (let ii = 1, isz = v.length / 2; ii < isz; ii++)
                      txtdcc +=
                        v[ii * 2].toString(16) +
                        ' ' +
                        v[ii * 2 + 1].toString(16) +
                        '\n';
                  }
                }
              }
            }
            if (txtdcc && txtdcc.length > 0) {
              console.log(`[index] save txtdcc(length): ${txtdcc.length}`);
              cache.saveFileTable(txtdcc);
              window.appcache = cache = new AppCache(urlpath);
              cache.setResourceID('netassetsid', assetsidStr);
            } else {
              if (window['onLayaInitError']) {
                isDccOk = false;
                window['onLayaInitError']('Update DCC get filetable error');
              }
            }
          }
        }
      });
    }
    if (url === 'http://stand.alone.version/index.html') _inline = false;
    if (!_inline) url = 'http://stand.alone.version/index.html';
    console.log('[index] loadURL:' + url);
    if (history.length <= 0) history._push(url);
    if (url.length < 2) return;
    location.setHref(url);
    const urlpath = location.fullpath + '/';
    let cache = (window.appcache = new AppCache(urlpath));
    document.loadCookie();
    yield initFreeType();
    try {
      require('config');
    } catch (e) {
      console.error(e);
    }
    let isDccOk = true;
    if (_inline) {
      yield updateDcc();
      if (!isDccOk) {
        console.log('[index] Failed to init dcc');
        return;
      }
    }
    let data = yield asyncs.loadText(url);
    for (let retry = 0; retry < 3; retry++) {
      data = yield asyncs.loadText(url);
      if (data) break;
    }
    if (!data) {
      alert('网络异常，请检查您的网络或与开发商联系。');

      data = cache.loadCachedURL(url);
      if (!data || data.length <= 0)
        if (window['onLayaInitError']) {
          window['onLayaInitError']('Load start url error');
        }
      return;
    }
    if (url.endsWith('.js')) {
      window.eval(
        data +
          ('\n        window.onload&&window.onload();\n        //@ sourceURL=' +
            url +
            '\n        ')
      );
    } else {
      initHtml(data);
    }
  });
}

window.document.addEventListener('keydown', function(e) {
  switch (e.keyCode) {
    case 115:
      simAndroidDeviceLosted();
      break;
    case 116:
      reloadJS(true);
      break;
    case 117:
      history.back();
      break;
    case 118:
      break;
    case 119:
      break;
    case 120:
      gc();
      break;
  }
});

window.loadConchUrl = loadUrl;
window['updateByZip'] = function(url, onEvent, onEnd) {
  var cachePath = conch.getCachePath();
  var localfile = cachePath + url.substr(url.lastIndexOf('/'));
  downloadBigFile(
    url,
    localfile,
    function(total, now, speed) {
      onEvent('downloading', Math.floor((now / total) * 100), null);
      return false;
    },
    function(curlret, httpret) {
      if (curlret != 0 || httpret < 200 || httpret >= 300) {
        onEvent('downloadError');
      } else {
        onEvent('downloadOK');
        var zip = new ZipFile();
        if (zip.setSrc(localfile)) {
          zip.forEach(function(id, name, dir, sz) {
            if (!dir) {
              var buf = zip.readFile(id);
              var fid = window.appcache.hashstr('/' + name);
              if (window.appcache.updateFile(fid, 0, buf, false)) {
                onEvent('updating', null, name);
              } else {
                onEvent('updateError', null, name);
              }
            }
          });
          zip.close();
          if (onEnd) onEnd(localfile);
        } else {
          console.log('[index] Failed to set zip src');
          onEvent('unknownError');
        }
      }
    },
    10,
    100000000
  );
};

exports.loadUrl = () => loadUrl(conch.presetUrl);
