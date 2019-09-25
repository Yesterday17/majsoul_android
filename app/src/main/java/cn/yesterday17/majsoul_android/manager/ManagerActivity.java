package cn.yesterday17.majsoul_android.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.R;
import cn.yesterday17.majsoul_android.extension.ExtensionManager;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
import cn.yesterday17.majsoul_android.game.GameActivity;
import cn.yesterday17.majsoul_android.manager.views.AboutFragment;
import cn.yesterday17.majsoul_android.manager.views.ExtensionFragment;
import cn.yesterday17.majsoul_android.manager.views.SettingFragment;
import cn.yesterday17.majsoul_android.extension.ExtensionFileOutputStream;

public class ManagerActivity extends AppCompatActivity {
    private final String TAG = "ManagerActivity";

    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;
    FloatingActionButton startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGlobal();

        if (!isOpeningFile() && false) {
            // TODO: 设置: 直接进入游戏
            startGame();
        }


        setContentView(R.layout.activity_manager);

        initView();
        prepareStartGame();
        ExtensionManager.GetInstance().init();
        prepareOpenInstall();

        // TODO: 在这个阶段就加载部分游戏内容 加快游戏启动
        // TODO: 弹出安装窗口
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        prepareOpenInstall();
    }

    void initGlobal() {
        Global.applicationContext = getApplicationContext();
        Global.dataDir = getDataDir().toString();
        Global.filesDir = getFilesDir().toString();

        // TODO: Load Settings here
        Global.gameUrl = getString(R.string.cnGameUrl);
    }

    void initView() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPager);

        bottomNavigation.setOnNavigationItemSelectedListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.tab_extension:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.tab_setting:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.tab_about:
                    viewPager.setCurrentItem(2);
                    return true;
                default:
                    return false;
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ExtensionFragment();
                    case 1:
                        return new SettingFragment();
                    case 2:
                        return new AboutFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        viewPager.setOnTouchListener((View v, MotionEvent event) -> true);
    }

    void prepareStartGame() {
        startGameButton = findViewById(R.id.start_game);
        startGameButton.setOnClickListener((View view) -> startGame()
        );
    }

    void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    void prepareOpenInstall() {
        Intent intent = getIntent();
        Log.d(TAG, intent.getAction());
        if (intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d(TAG, intent.getDataString());
            String id = "";

            Toast.makeText(this, "导入拓展中...", Toast.LENGTH_SHORT).show();
            try {
                InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    String[] parts = entry.getName().split("/");

                    id = parts[0];
                    if (id.equals("!!!")) {
                        // TODO: ID 唯一性检查
                        break;
                    }

                    // 跳过目录创建，目录创建在每次试图建立文件时进行
                    if (entry.isDirectory()) continue;

                    Log.d(TAG, "Unzipping " + entry.getName());
                    ExtensionFileOutputStream out = new ExtensionFileOutputStream(entry.getName());
                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                        out.write(c);
                    }
                    zipInputStream.closeEntry();
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            String folder = Global.filesDir + File.separator + id;

            // TODO: Metadata 读取
            try {
                Metadata metadata = ExtensionManager.GetInstance().loadMetadata(folder + File.separator + "extension.json");
                Log.d(TAG, "Name: " + metadata.getName());
                Log.d(TAG, "Desc: " + metadata.getDescription());
                ExtensionManager.GetInstance().load(metadata);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！未找到 extension.json！", Toast.LENGTH_SHORT).show();
                return;
            } catch (JsonParseException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "拓展" + id + "导入成功！", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isOpeningFile() {
        Intent intent = getIntent();
        return intent.ACTION_VIEW.equals(intent.getAction());
    }

}
