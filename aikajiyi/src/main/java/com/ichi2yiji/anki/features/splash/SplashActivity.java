package com.ichi2yiji.anki.features.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AikaActivity;
import com.ichi2yiji.anki.LoginActivity;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;
    private static final int REQUEST_PERMISSION = 0;
    private ViewPager vpContent;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ImageView curDot;
    private int offset;
    private int curPos;
    private FrameLayout flDot;
    private ImageView ivLaunch;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /**
         * 6.9个推初始化
         */
        PushManager.getInstance().initialize(this.getApplicationContext());
        init();
        initView();
        setListener();
        getPermissions();
        getVersion();
        // copyFile2SD();
    }

    private void getVersion() {
        //获取sp的setting里面是否有版本号
        SettingsBean settingsJson = SettingUtil.getSettings();

        if(settingsJson.getVersion().equals("")){
            try {
                String pkName = this.getPackageName();
                String versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
                Log.e("versionCode",versionCode+"");
                settingsJson.setVersion(versionCode);
                SettingUtil.upDateSettings(settingsJson);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else {
//        如果为空 进行添加
                SPUtil.setPreferences(SPUtil.TYPE_SETTINGS_DATA,"settings","123");
        }


    }

    private void init() {
        mContext = this;
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        //友盟统计初始化
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    /**
     * 申请权限
     */
    private void getPermissions() {
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission ) {
            requestPermission();
        } else {
            startWelcome();
            PushManager.getInstance().initialize(this.getApplicationContext());
        }
    }

    /**
     * 请求权限的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED )) {
                startWelcome();
                PushManager.getInstance().initialize(this.getApplicationContext());
            } else {
                Toast.makeText(this, "对不起，艾卡记忆的使用需要得到您对以上权限的许可，请您重启应用并选择允许，谢谢！", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startWelcome() {
        boolean hasShowWelcome = SPUtil.isContainKey(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome");
        if (!hasShowWelcome) {
            // 首次登录
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",1);
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "FirstUse",true);
            AlphaAnimation animation  = new AlphaAnimation(1.0f, 1.0f);
            animation.setDuration(1000);
            ivLaunch.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ivLaunch.setVisibility(View.VISIBLE);
                    flDot.setVisibility(View.GONE);
                    vpContent.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ivLaunch.setVisibility(View.GONE);
                    flDot.setVisibility(View.VISIBLE);
                    vpContent.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            // 非首次登录
            int num = SPUtil.getPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",0);
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",num++);
            skipActivity(1000);
        }
    }

    /**
     * 延迟多少毫秒进入主界面
     *
     * @param  second 毫秒
     */
    private void skipActivity(int second) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = null;
                boolean hasShowWelcome = SPUtil.isContainKey(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome");
                if (!hasShowWelcome){
                    //第一次启动应用
                    intent = new Intent(mContext, LoginActivity.class);
                }
                if (hasShowWelcome){
                    //不是第一次启动应用
                    boolean containKey = SPUtil.isContainKey(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME");
                    if(containKey){
                        //如果以前登录过，本页面请求登陆，直接进入主界面
                        attemptLogin();
                        intent = new Intent(mContext, AikaActivity.class);
                    }else{
                        //如果以前使用过但没有登录过,直接进入主界面
                        intent = new Intent(mContext, AikaActivity.class);
                    }
                    intent.putExtra("LoadDeckListCookies", true);//让首页加载牌组列表的缓存数据
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                finish();
            }
        }, second);
    }

    public void attemptLogin() {
        String clientid = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"CLIENTID","");
        String username = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"USERNAME", null);
        String password = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"PASSWORD", null);
        Map<String, String> map = new HashMap<>();
        map.put("tele", username);
        map.put("client_id", clientid);
        map.put("password", password);
        map.put("client_type", String.valueOf(1));
        ZXUtils.Post(Urls.URL_APP_LOGIN, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //发送广播提醒更新侧边栏头像和用户名信息，，延迟1s发送广播，确保主页面已经显示出来了
                        Intent i = new Intent("Update Username And User Head Image");
                        sendBroadcast(i);
                    }
                }, 1000);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("attemptLogin","onError>>>>>>>>>>>>"+ ex.toString());
                Toast.makeText(getApplicationContext(), "网络异常，请稍后重新登录", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void initView() {
        flDot = (FrameLayout)findViewById(R.id.fl_dot);
        vpContent = (ViewPager)findViewById(R.id.vp_content);
        curDot = (ImageView) findViewById(R.id.dot_green);
        ivLaunch = (ImageView) findViewById(R.id.iv_launch);


        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(mPagerAdapter);
        curDot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // 获取圆点宽度
                offset = curDot.getWidth();
                return true;
            }
        });
    }

    private void setListener() {
        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                moveCursorTo(position);
            }
        });
    }


    /**
     * 移动指针到相邻的位置
     *
     * @param position 指针的索引值
     */
    private void moveCursorTo(int position) {
        TranslateAnimation anim = new TranslateAnimation(offset * curPos, offset * position, 0, 0);
        anim.setDuration(150);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
        curPos = position;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == getCount()-1){
                return LastFragment.newInstance();
            }else{
                return SplashFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }


}
