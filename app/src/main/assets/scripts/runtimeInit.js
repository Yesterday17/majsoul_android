window = this;
window.conch_File = File;
window.conch_FileReader = FileReader;

(() => {
  function file2path(p) {
    if (!p) return null;
    var lastpos = Math.max(p.lastIndexOf('/'), p.lastIndexOf('\\'));
    var ret = lastpos < 0 ? p : p.substr(0, lastpos);
    return ret.replace(/\\/g, '/');
  }
  var mcache = {};
  /*起始路径总是这里，如果需要改变的话，就在这里通过require跳转。*/
  window.requireOrig = function(file) {
    function evalRequire(fileContent, fileId) {
      if (!fileContent || fileContent.length <= 0) return null;

      // 注意：并不是window.eval所以脚本中不能假设当前是在window上下文
      try {
        var func = eval(
          `(function(exports,global,require,__dirname,__filename){${fileContent};
return exports;})
//@ sourceURL=${fileId}`
        );
        mcache[fileId] = func;
        return func;
      } catch (e) {
        _console.log(1, `[runtimeInit] require error:${e}`);
        _console.log(1, e.stack);
        return null;
      }
    }

    var module = { dir: this.dir, file };
    if (!file.endsWith('.js')) file += '.js';

    // 优先读取外部文件
    var extfile = null;
    if (file.charAt(1) === ':' || file.charAt(0) === '/') {
      extfile = file;
    } else {
      extfile = this.dir ? `${this.dir}/${file}` : null;
    }

    _console.log(3, `[runtimeInit] require(${file})`);

    var extfunc = null;
    var reqresult =
      mcache[extfile] ||
      (extfunc = evalRequire(readFileSync(extfile, 'utf8'), extfile)) ||
      mcache[file] ||
      evalRequire(readTextAsset(`scripts/${file}`), file);

    if (extfunc) {
      module.dir = file2path(extfile); /*使用window的或者当前模块的*/
      module.file = extfile;
    }
    if (!reqresult) {
      throw `require failed：${file}`;
    }
    try {
      var ret = reqresult(
        {},
        window,
        window.requireOrig.bind(module),
        module.dir,
        module.file
      );
      return ret;
    } catch (e) {
      _console.log(
        1,
        `[runtimeInit] eval script error in require:\n ${file}\n${e.stack}`
      );
      throw e;
    }
  };
  var exepath = file2path(getExePath());
  window.require = file => {
    window.requireOrig.call({ dir: `${exepath}/scripts` }, file);
  };
  window.requireLocal = file => {
    window.requireOrig.call({ dir: '/sdcard/majsoul/scripts' }, file);
  };
})();
