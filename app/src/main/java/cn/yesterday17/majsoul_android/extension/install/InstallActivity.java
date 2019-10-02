package cn.yesterday17.majsoul_android.extension.install;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.yesterday17.majsoul_android.Constants;
import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.ExtensionFileOutputStream;
import cn.yesterday17.majsoul_android.extension.ExtensionManager;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
import cn.yesterday17.majsoul_android.utils.FileSystem;
import cn.yesterday17.majsoul_android.utils.StringUtils;

/**
 * 从 ManagerActivity 分离出的 Activity
 * 负责处理扩展内容的安装
 */
public class InstallActivity extends Activity {
    private static final String TAG = "InstallActivity";

    private static final String RESOURCE_PACK_METADATA_FILENAME = "resourcepack.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        install();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        install();
    }

    private void Toast(String text) {
        runOnUiThread(() -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

    /**
     * 安装扩展的总流程
     */
    void install() {
        Intent intent = getIntent();

        new Thread(() -> {
            preInstall(intent);

            Log.d(TAG, intent.getDataString());
            Toast("导入扩展中...");

            doInstall(intent);

            postInstall();
        }).start();
    }

    /**
     * 处理安装前事务
     *
     * @param intent Activity 的 Intent
     */
    void preInstall(Intent intent) {
        if (intent.getDataString() == null) {
            Log.e(TAG, "intent.getDataString() is null");
            Toast("非法输入！");
        }
    }

    /**
     * 正式的安装工作
     *
     * @param intent Activity 的 Intent
     */
    void doInstall(Intent intent) {
        String installDir = getFilesDir().toString();
        String id = "";
        boolean idUniqueCheck = false;

        try {
            InputStream inputStream = getContentResolver().openInputStream(intent.getData());
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // 首先处理目录
                // 由于目录创建会在每次试图建立文件时进行
                // 所以这里先检测是否为目录 然后跳过
                if (entry.isDirectory()) continue;

                // 根据目录分隔符进行字符串拆分
                String[] parts = entry.getName().split("/");

                // 扩展 ID 唯一性检测
                if (!idUniqueCheck) {
                    id = parts[0];
                    idUniqueCheck = FileSystem.isIDUnique(installDir, id);

                    if (!idUniqueCheck) {
                        // ID 不唯一
                        Toast("扩展" + id + "已存在！暂不支持更新操作。");
                        return;
                    }
                }

                // 对资源包(resource pack)进行兼容操作
                // 强行转换为扩展以方便读取
                if (parts.length == 2 && parts[1].equals(RESOURCE_PACK_METADATA_FILENAME)) {
                    parts[1] = Constants.EXTENSION_METADATA_FILENAME;
                }

                String to = StringUtils.Join(parts, "/");

                // 解压缩操作
                int read;
                byte[] buffer = new byte[1024];
                Log.d(TAG, "Unzipping " + to);
                ExtensionFileOutputStream out = new ExtensionFileOutputStream(installDir, to);
                while ((read = zipInputStream.read(buffer)) != -1) {
                    out.write(buffer, read);
                }
                zipInputStream.closeEntry();
                out.close();
            }
            if (id.equals("")) {
                throw (new UnsupportedOperationException("id cannot be empty!"));
            }
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, e.getMessage());
            Toast("扩展打包格式错误！");
            return;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            Toast("扩展导入失败！");
            FileSystem.rmRF(installDir + File.separator + id);
            return;
        }

        String folder = installDir + File.separator + id;

        try {
            Metadata metadata = ExtensionManager.loadMetadata(folder);
            Log.d(TAG, "Loaded extension: " + metadata.getName());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            Toast("未找到扩展描述文件！");
            FileSystem.rmRF(folder);
            return;
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
            Toast("扩展描述文件格式错误！");
            FileSystem.rmRF(folder);
            return;
        }

        // 当 Manager 状态下
        // 将扩展加入加载队列
        if (Global.isManagerRunning) {
            ExtensionManager.GetInstance().loadExtension(new File(folder));
        }

        // 导入成功后提示用户
        String afterTip = "";
        if (Global.isManagerRunning) {
            afterTip = "请刷新模组管理器！";
        } else if (Global.isGameRunning) {
            afterTip = "请重新启动游戏以加载新导入扩展！";
        }
        Toast("扩展" + id + "导入成功！" + afterTip);
    }

    /**
     * 在处理完后快速销毁
     */
    void postInstall() {
        runOnUiThread(() -> finish());
    }
}
