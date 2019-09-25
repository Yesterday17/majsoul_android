package cn.yesterday17.majsoul_android.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.R;
import cn.yesterday17.majsoul_android.game.GameActivity;
import cn.yesterday17.majsoul_android.manager.views.AboutFragment;
import cn.yesterday17.majsoul_android.manager.views.ExtensionFragment;
import cn.yesterday17.majsoul_android.manager.views.SettingFragment;

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
        prepareOpenInstall();

        // TODO: 在这个阶段就加载部分游戏内容 加快游戏启动
        // TODO: 弹出安装窗口
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Kill
        prepareOpenInstall();
    }

    void initGlobal() {
        Global.applicationContext = getApplicationContext();

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
        if (intent.ACTION_VIEW.equals(intent.getAction())) {
            // TODO: Install
            Log.d(TAG, intent.getDataString());
        }
    }

    boolean isOpeningFile() {
        Intent intent = getIntent();
        return intent.ACTION_VIEW.equals(intent.getAction());
    }

}
