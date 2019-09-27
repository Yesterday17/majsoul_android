const extensionFetch = id => {
  return (input, init) => {
    alert(
      id +
        '正在尝试调用 fetchSelf！\n雀魂+ 不支持 雀魂 Plus 的该属性，请联系作者修改。'
    );
    return fetch(`majsoul_plus/extension/${id}/${input}`, init);
  };
};

window.extensionFetch = extensionFetch;
