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

/**
 * 从 ManagerActivity 分离出的 Activity
 * <p>
 * 负责处理扩展内容的安装
 */
public class InstallActivity extends Activity {
    private static final String TAG = "InstallActivity";

    private static final String RESOURCE_PACK_METADATA_FILENAME = "resourcepack.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        install();
        postInstall();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        install();
        postInstall();
    }

    void install() {
        Intent intent = getIntent();

        preInstall(intent);

        Log.d(TAG, intent.getDataString());
        Toast.makeText(this, "导入扩展中...", Toast.LENGTH_SHORT).show();


        doInstall(intent);

        postInstall();
    }

    /**
     * 处理安装前事务
     *
     * @param intent
     */
    void preInstall(Intent intent) {
        if (intent.getDataString() == null) {
            Log.e(TAG, "intent.getDataString() is null");
            Toast.makeText(this, "非法输入！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 正式的安装工作
     *
     * @param intent
     */
    void doInstall(Intent intent) {
        String installDir = getFilesDir().toString();
        String id = "";
        boolean idUniqueCheck = false;

        try {
            // TODO: 不阻塞主进程
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
                }
                if (!idUniqueCheck) {
                    // ID 不唯一
                    Toast.makeText(this, "扩展" + id + "已存在！暂不支持更新操作。", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 对资源包(resource pack)进行兼容操作
                // 强行转换为扩展以方便读取
                if (parts.length == 2 && parts[1].equals(RESOURCE_PACK_METADATA_FILENAME)) {
                    parts[1] = Constants.EXTENSION_METADATA_FILENAME;
                }

                // 解压缩操作
                Log.d(TAG, "Unzipping " + entry.getName());
                ExtensionFileOutputStream out = new ExtensionFileOutputStream(installDir, entry.getName());
                for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    out.write(c);
                }
                zipInputStream.closeEntry();
                out.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "扩展导入失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        String folder = installDir + File.separator + id;
        Metadata metadata;

        try {
            metadata = ExtensionManager.loadMetadata(folder);
            Log.d(TAG, "Loaded extension: " + metadata.getName());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "扩展打包格式错误！", Toast.LENGTH_SHORT).show();
            return;
        } catch (JsonParseException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "扩展描述文件格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 当 Manager 状态下
        // 将扩展导入至 ExtensionManager
        if (Global.isManagerRunning) {
            ExtensionManager.GetInstance().loadAsync(metadata);
        }

        // 导入成功后提示用户
        String afterTip = "";
        if (Global.isManagerRunning) {
            afterTip = "请刷新模组管理器！";
        } else if (Global.isGameRunning) {
            afterTip = "请重新启动游戏以加载新导入扩展！";
        }
        Toast.makeText(this, "扩展" + id + "导入成功！" + afterTip, Toast.LENGTH_SHORT).show();
    }

    /**
     * 在处理完后快速销毁
     */
    void postInstall() {
        finish();
    }
}
