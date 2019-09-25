package com.ichi2yiji.anki;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.dialogs.DialogHandler;
import com.ichi2yiji.anki.widgets.WelcomePageAdapter;
import com.ichi2yiji.utils.SPUtil;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import timber.log.Timber;

/**
 * Class which handles how the application responds to different intents, forcing it to always be single task,
 * but allowing custom behavior depending on the intent
 * 
 * @author Tim
 *
 */

public class IntentHandler extends Activity {

    private final String TAG = getClass().getSimpleName();

    private static final int REQUEST_PERMISSION = 0;

    // 首次使用程序的显示的欢迎图片
//    private int[] ids = { R.drawable.welcome_3, R.drawable.welcome_4, R.drawable.welcome_5, R.drawable.welcome_6,R.drawable.welcome_7,R.drawable.welcome_1};
    private int[] ids = { R.drawable.welcome_pager_1, R.drawable.welcome_pager_2, R.drawable.welcome_pager_3, R.drawable.welcome_pager_4 };

    private List<View> guides = new ArrayList<View>();
    private ViewPager pager;
    // 底部小图片
    private ImageView curDot;
    // 位移量
    private int offset;
    // 记录当前的位置
    private int curPos = 0;

    private ImageView launchPage;
    private FrameLayout frameLayout;
    private Button login;
    private Button register;
    private Button try_once;
    ////////////dx add
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟统计初始化
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);

//        setContentView(R.layout.progress_bar);
        /**
         * 新增权限的判断(zc)
         */
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission = pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        //实现悬浮窗
        boolean alertWindowPermission = pkgManager.checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, getPackageName()) == PackageManager.PERMISSION_GRANTED;
//        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
//            requestPermission();
//        } else {
//            startWelcome();
//            PushManager.getInstance().initialize(this.getApplicationContext());
//        }

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission ) {
            requestPermission();
        } else {
            startWelcome();
            PushManager.getInstance().initialize(this.getApplicationContext());
        }
    }

    private void startWelcome(){
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
        boolean hasShowWelcome = SPUtil.isContainKey(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome");
        if (hasShowWelcome) {
            //不是首次登录
            setContentView(R.layout.launch_page);
            ImageView iv = (ImageView) findViewById(R.id.launch_page);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            int num = SPUtil.getPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",0);
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",num++);
            skipActivity(200);
        } else {
            //首次登录
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "hasShowWelcome",1);
            SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "FirstUse",true);

            setContentView(R.layout.welcome_pages);
            frameLayout = (FrameLayout) findViewById(R.id.dot_layout);
            launchPage = (ImageView) findViewById(R.id.launchpage);
            launchPage.setScaleType(ImageView.ScaleType.FIT_XY);

            AlphaAnimation animation  = new AlphaAnimation(1.0f, 1.0f);
            animation.setDuration(1000);
            launchPage.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    launchPage.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    launchPage.setVisibility(View.INVISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    initView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void initView() {
        for (int i = 0; i < ids.length; i++) {
            // 创建一个imageView
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            guides.add(iv);
        }
        //最后一页的布局加入进去
        View view = LayoutInflater.from(IntentHandler.this).inflate(R.layout.viewpager_lastpage_lyt, null);
        guides.add(view);

        login = (Button) view.findViewById(R.id.login);
        register = (Button) view.findViewById(R.id.register);
        try_once = (Button) view.findViewById(R.id.try_once);

        login.setSelected(true);
        register.setSelected(false);

        curDot = (ImageView) findViewById(R.id.dot_green);
        // 绑定回调
        curDot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                offset = curDot.getWidth();
                return true;
            }
        });



        // 初始化Adapter
        WelcomePageAdapter adapter = new WelcomePageAdapter(guides);
        pager = (ViewPager) findViewById(R.id.welcome_page);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int arg0) {
                moveCursorTo(arg0);
                if (arg0 == ids.length ) {// 到最后一张了
//                    frameLayout.setVisibility(View.INVISIBLE);
//                    pager.setVisibility(View.INVISIBLE);

                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login.setSelected(true);
                            register.setSelected(false);
                            Intent intent = new Intent(IntentHandler.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                            IntentHandler.this.finish();
                        }
                    });

                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login.setSelected(false);
                            register.setSelected(true);
                            Intent intent = new Intent(IntentHandler.this, RegisterActivity.class);
                            intent.putExtra("IntentFromWelcomeActivity", true);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                            IntentHandler.this.finish();
                        }
                    });

                    try_once.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(IntentHandler.this, AikaActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                            IntentHandler.this.finish();
                        }
                    });

                }
                curPos = arg0;
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
        anim.setDuration(300);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
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
                    intent = new Intent(IntentHandler.this, LoginActivity.class);
                }

                if (hasShowWelcome){
                    //不是第一次启动应用
                    boolean containKey = SPUtil.isContainKey(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME");
                    if(containKey){
                        //如果以前登录过，本页面请求登陆，直接进入主界面
                        attemptLogin();
                        intent = new Intent(IntentHandler.this, AikaActivity.class);
                    }else{
                        //如果以前使用过但没有登录过,直接进入主界面
                        intent = new Intent(IntentHandler.this, AikaActivity.class);
                    }
                    intent.putExtra("LoadDeckListCookies", true);//让首页加载牌组列表的缓存数据
                }
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                IntentHandler.this.finish();
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

        String url = AnkiDroidApp.BASE_DOMAIN +"Home/App/login/";//url中不能有www

        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("IntentHandler", "resultLogin>>>>>>>" + result);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //发送广播提醒更新侧边栏头像和用户名信息，，延迟1s发送广播，确保主页面已经显示出来了
                        Intent i = new Intent("Update Username And User Head Image");
                        sendBroadcast(i);
                        Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent!");
                    }
                }, 1000);

//                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
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







    private void startActivity(){
        Intent intent = getIntent();
        Timber.v(intent.toString());
        Intent reloadIntent = new Intent(this, DeckPicker.class);
        reloadIntent.setDataAndType(getIntent().getData(), getIntent().getType());
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
//            // This intent is used for opening apkg package files
//            // We want to go immediately to DeckPicker, clearing any history in the process
//            Timber.i("IntentHandler/ User requested to view a file");
//            boolean successful = false;
//            String errorMessage = getResources().getString(R.string.import_error_content_provider, AnkiDroidApp.getManualUrl() + "#importing");
//            // If the file is being sent from a content provider we need to read the content before we can open the file
//            if (intent.getData().getScheme().equals("content")) {
//                // Get the original filename from the content provider URI
//                String filename = null;
//                Cursor cursor = null;
//                try {
//                    cursor = this.getContentResolver().query(intent.getData(), new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        filename = cursor.getString(0);
//                    }
//                } finally {
//                    if (cursor != null)
//                        cursor.close();
//                }
//
//                // Hack to fix bug where ContentResolver not returning filename correctly
//                if (filename == null) {
//                    if (intent.getType().equals("application/apkg") || hasValidZipFile(intent)) {
//                        // Set a dummy filename if MIME type provided or is a valid zip file
//                        filename = "unknown_filename.apkg";
//                        Timber.w("Could not retrieve filename from ContentProvider, but was valid zip file so we try to continue");
//                    } else {
//                        Timber.e("Could not retrieve filename from ContentProvider or read content as ZipFile");
//                        AnkiDroidApp.sendExceptionReport(new RuntimeException("Could not import apkg from ContentProvider"), "IntentHandler.java", "apkg import failed");
//                    }
//                }
//
//                if (filename != null && !filename.toLowerCase().endsWith(".apkg")) {
//                    // Don't import if not apkg file
//                    errorMessage = getResources().getString(R.string.import_error_not_apkg_extension, filename);
//                } else if (filename != null) {
//                    // Copy to temporary file
//                    String tempOutDir = Uri.fromFile(new File(getCacheDir(), filename)).getEncodedPath();
//                    successful = copyFileToCache(intent, tempOutDir);
//                    // Show import dialog
//                    if (successful) {
//                        sendShowImportFileDialogMsg(tempOutDir);
//                    } else {
//                        AnkiDroidApp.sendExceptionReport(new RuntimeException("Error importing apkg file"), "IntentHandler.java", "apkg import failed");
//                    }
//                }
//            } else if (intent.getData().getScheme().equals("file")) {
//                // When the VIEW intent is sent as a file, we can open it directly without copying from content provider
//                String filename = intent.getData().getPath();
//                if (filename != null && filename.endsWith(".apkg")) {
//                    // If file has apkg extension then send message to show Import dialog
//                    sendShowImportFileDialogMsg(filename);
//                    successful = true;
//                } else {
//                    errorMessage = getResources().getString(R.string.import_error_not_apkg_extension, filename);
//                }
//            }
//            // Start DeckPicker if we correctly processed ACTION_VIEW
//            if (successful) {
//                reloadIntent.setAction(action);
//                reloadIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(reloadIntent);
//                finishWithFade();
//            } else {
//                // Don't import the file if it didn't load properly or doesn't have apkg extension
//                //Themes.showThemedToast(this, getResources().getString(R.string.import_log_no_apkg), true);
//                String title = getResources().getString(R.string.import_log_no_apkg);
//                new MaterialDialog.Builder(this)
//                        .title(title)
//                        .content(errorMessage)
//                        .positiveText(getResources().getString(R.string.dialog_ok))
//                        .callback(new MaterialDialog.ButtonCallback() {
//                            @Override
//                            public void onPositive(MaterialDialog dialog) {
//                                finishWithFade();
//                            }
//                        })
//                        .build().show();
//            }
        } else if ("com.ichi2.anki.DO_SYNC".equals(action)) {
//            sendDoSyncMsg();
//            reloadIntent.setAction(action);
//            reloadIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(reloadIntent);
//            finishWithFade();
        } else {
            // Launcher intents should start DeckPicker if no other task exists,
            // otherwise go to previous task
            reloadIntent.setAction(Intent.ACTION_MAIN);
            reloadIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            reloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityIfNeeded(reloadIntent, 0);
            IntentHandler.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            finishWithFade();
            IntentHandler.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }
    }

//    /**
//     * 请求权限
//     */
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
//                REQUEST_PERMISSION);
//
//    }
//    /**
//     * 请求权限的回调
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == REQUEST_PERMISSION) {
//            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//
//                startWelcome();
//                PushManager.getInstance().initialize(this.getApplicationContext());
//
//            } else {
//                Toast.makeText(this, "对不起，艾卡记忆的使用需要得到您对以上权限的许可，请您重启应用并选择允许，谢谢！", Toast.LENGTH_LONG).show();
//                finish();
//
//            }
//        } else {
//            onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }


    /**
     * 请求权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);

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

    /**
     * Send a Message to AnkiDroidApp so that the DialogMessageHandler shows the Import apkg dialog.
     * @param path path to apkg file which will be imported
     */
    private void sendShowImportFileDialogMsg(String path) {
        // Get the filename from the path
        File f = new File(path);
        String filename = f.getName();

        // Create a new message for DialogHandler so that we see the appropriate import dialog in DeckPicker
        Message handlerMessage = Message.obtain();
        Bundle msgData = new Bundle();
        msgData.putString("importPath", path);
        handlerMessage.setData(msgData);
        if (filename.equals("collection.apkg")) {
            // Show confirmation dialog asking to confirm import with replace when file called "collection.apkg"
            handlerMessage.what = DialogHandler.MSG_SHOW_COLLECTION_IMPORT_REPLACE_DIALOG;
        } else {
            // Otherwise show confirmation dialog asking to confirm import with add
            handlerMessage.what = DialogHandler.MSG_SHOW_COLLECTION_IMPORT_ADD_DIALOG;
        }
        // Store the message in AnkiDroidApp message holder, which is loaded later in AnkiActivity.onResume
        DialogHandler.storeMessage(handlerMessage);
    }

    /**
     * Send a Message to AnkiDroidApp so that the DialogMessageHandler forces a sync
     */
    private void sendDoSyncMsg() {
        // Create a new message for DialogHandler
        Message handlerMessage = Message.obtain();
        handlerMessage.what = DialogHandler.MSG_DO_SYNC;
        // Store the message in AnkiDroidApp message holder, which is loaded later in AnkiActivity.onResume
        DialogHandler.storeMessage(handlerMessage);
    }

    /** Finish Activity using FADE animation **/
    private void finishWithFade() {
    	finish();
    	ActivityTransitionAnimation.slide(this, ActivityTransitionAnimation.UP);
    }

    /**
     * Check if the InputStream is to a valid non-empty zip file
     * @param intent intent from which to get input stream
     * @return whether or not valid zip file
     */
    private boolean hasValidZipFile(Intent intent) {
        // Get an input stream to the data in ContentProvider
        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(intent.getData());
        } catch (FileNotFoundException e) {
            Timber.e(e, "Could not open input stream to intent data");
        }
        // Make sure it's not null
        if (in == null) {
            Timber.e("Could not open input stream to intent data");
            return false;
        }
        // Open zip input stream
        ZipInputStream zis = new ZipInputStream(in);
        boolean ok = false;
        try {
            try {
                ZipEntry ze = zis.getNextEntry();
                if (ze != null) {
                    // set ok flag to true if there are any valid entries in the zip file
                    ok = true;
                }
            } catch (IOException e) {
                // don't set ok flag
                Timber.d(e, "Error checking if provided file has a zip entry");
            }
        } finally {
            // close the input streams
            try {
                zis.close();
                in.close();
            } catch (Exception e) {
                Timber.d(e, "Error closing the InputStream");
            }
        }
        return ok;
    }


    /**
     * Copy the data from the intent to a temporary file
     * @param intent intent from which to get input stream
     * @param tempPath temporary path to store the cached file
     * @return whether or not copy was successful
     */
    private boolean copyFileToCache(Intent intent, String tempPath) {
        // Get an input stream to the data in ContentProvider
        InputStream in;
        try {
            in = getContentResolver().openInputStream(intent.getData());
        } catch (FileNotFoundException e) {
            Timber.e(e, "Could not open input stream to intent data");
            return false;
        }
        // Check non-null
        if (in == null) {
            return false;
        }
        // Create new output stream in temporary path
        OutputStream out;
        try {
            out = new FileOutputStream(tempPath);
        } catch (FileNotFoundException e) {
            Timber.e(e, "Could not access destination file %s", tempPath);
            return false;
        }

        try {
            // Copy the input stream to temporary file
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
        } catch (IOException e) {
            Timber.e(e, "Could not copy file to %s", tempPath);
            return false;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Timber.e(e, "Error closing tempOutDir %s", tempPath);
            }
        }
        return true;
    }
}