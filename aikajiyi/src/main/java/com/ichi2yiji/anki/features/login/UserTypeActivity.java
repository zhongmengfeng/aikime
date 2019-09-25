package com.ichi2yiji.anki.features.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AikaActivity;
import com.ichi2yiji.anki.AnkiDroidApp;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户类型选择
 * 教育程度
 * 邀请码
 */
public class UserTypeActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private ImageView ivBack;
    private TextView tvOk;
    private final String[] CHANNELS = new String[]{"教育程度", "邀请码"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ExamplePagerAdapter mExamplePagerAdapter;
    private int currentPosition;
    private int load_personal_info_times;


    private String mem_id;
    private String client_type;
    private String client_id;
    private String third_login_user_name;
    private String third_login_icon_url;
    private boolean isFromThirdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplyTranslucency.applyKitKatTranslucency(UserTypeActivity.this);
        setContentView(R.layout.activity_user_type);

        revMsg();
        initView();
        initMagicIndicator();
        setListener();
    }

    private void revMsg() {
        Bundle bundle = getIntent().getExtras();
        isFromThirdLogin = bundle.getBoolean("isFromThirdLogin", false);
        mem_id = bundle.getString("mem_id");
        client_type = bundle.getString("client_type");
        client_id = bundle.getString("client_id");
        third_login_user_name = bundle.getString("userName");
        third_login_icon_url = bundle.getString("third_login_icon_url");
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mExamplePagerAdapter = new ExamplePagerAdapter(mDataList, this);
        mViewPager.setAdapter(mExamplePagerAdapter);
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
            }
        });
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(Color.WHITE);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return DensityUtil.dip2px(15);
            }
        });
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    /**
     * 更新用户信息
     *
     * @param mem_id
     * @param client_type
     * @param client_id
     * @param code
     */
    private void postMsgToBackStage(final String mem_id, String client_type, String client_id, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("client_type", client_type);
        map.put("client_id", client_id);

        if (currentPosition == 0) {
            map.put("edu_level", code);
        } else if (currentPosition == 1) {
            map.put("yao", code);
        }

        String url;
        if (isFromThirdLogin) {
            // 第三方登录
            url = Urls.URL_APP_UPDATE_MSG;
        } else {
            // 普通登录
            url = Urls.URL_APP_UPDATE_MSG;
        }
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("typeonSuccess",result);
                int code = 0;
                String data = "登录失败";
                String user_name = null;
                String password = null;
                String version = null;
                String is_update = null;

                try {
                    JSONObject jo = new JSONObject(result);
                    code = jo.getInt("code");
                    if (code == 1) {
                        data = jo.getString("data");
                        LogUtil.e("onSuccess: data = " + data);
                        ToastAlone.showShortToast(data);
                        return;
                    }
                    user_name = jo.getJSONObject("data").getString("user_name");
                    password = jo.getJSONObject("data").getString("password");
                    version = jo.getJSONObject("data").getString("version");
                    is_update = jo.getJSONObject("data").getString("is_update");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (code == 1000) {
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", user_name);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PASSWORD", password);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", mem_id);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "VERSION", version);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "IS_UPDATE", is_update);

                    loadPersonalInfo( true);
//                    loadPersonalInfo("2", true);
//                    loadPersonalInfo("3", true);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
                            Intent i = new Intent("Update Username And User Head Image");
                            i.putExtra("isFromThirdLogin", isFromThirdLogin);
                            i.putExtra("third_login_icon_url", third_login_icon_url);
                            i.putExtra("third_login_user_name", third_login_user_name);
                            sendBroadcast(i);
                            Log.e("postMsgToBackStage", "Update Username And User Head Image ,Broadcast has sent! From Third Login");
                        }
                    }, 1000);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("postMsgToBackStage", "onError>>>>>>>>>>>" + ex);
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
     * 获取用户信息
     * @param isFromThirdLogin
     */
    private void loadPersonalInfo( final boolean isFromThirdLogin) {
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        ZXUtils.Post(Urls.URL_APP_MEMBER_MSG, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // TODO 设置 只显示我的课程
                SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1" , result);
                load_personal_info_times++;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        String data = jsonObject.getString("data");
                        ToastAlone.showShortToast(data);
                        LogUtil.e("onSuccess: data = " + data);
                        return;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject mem = data.getJSONObject("mem");
                    String schoolId = mem.getString("school_id");
                    if (!TextUtils.equals(schoolId, "0") && !TextUtils.equals(schoolId, "999")) {
                        // 既不是普通用户也不是开发人员,而是合作机构用户
                        SettingsBean settings = SettingUtil.getSettings();
                        settings.setHomePageStyle(3);
                        SettingUtil.upDateSettings(settings);
                    }

                    if (TextUtils.equals("1", "1")) {
                        /**
                         * 6.6--将LoginActivity和ankiLoginActivity杀死
                         */
                        Activity loginActivity = AnkiDroidApp.activityManager.get("LoginActivity");
                        Activity ankiLoginActivity = AnkiDroidApp.activityManager.get("AnkiLoginActivity");
//                        Toast.makeText(UserTypeActivity.this,"拿到loginActivity了" + loginActivity,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(UserTypeActivity.this,"拿到ankiLoginActivity了" + ankiLoginActivity,Toast.LENGTH_SHORT).show();
                        if(null != loginActivity){
                            loginActivity.finish();
                        }
                        if(null != ankiLoginActivity){
                            ankiLoginActivity.finish();
                        }

                        /**
                         * 6.6--跳转到应用主界面
                         */
                        Intent intent = new Intent(UserTypeActivity.this, AikaActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("onError>>>>>>>>>>>>", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (isFromThirdLogin) {
                    //如果是第三方登录的请求，则不启动广播
                } else {
                    //如果是正常的注册账号登录，则启动广播
                    if (load_personal_info_times == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
                                Intent i = new Intent("Update Username And User Head Image");
                                sendBroadcast(i);
                                Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent! From normal login");
                            }
                        }, 1000);
                    }
                }
            }
        });
    }
//    /**
//     * 获取用户信息
//     *
//     * @param type_id
//     * @param isFromThirdLogin
//     */
//    private void loadPersonalInfo(final String type_id, final boolean isFromThirdLogin) {
//        Map<String, String> map = new HashMap<>();
//        map.put("mem_id", mem_id);
//        map.put("type_id", type_id);
//        ZXUtils.Post(Urls.URL_APP_MEMBER_MSG, map, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                // TODO 设置 只显示我的课程
//                SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult" + type_id, result);
//                load_personal_info_times++;
//
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    int code = jsonObject.getInt("code");
//                    if (code == 1) {
//                        String data = jsonObject.getString("data");
//                        ToastAlone.showShortToast(data);
//                        LogUtil.e("onSuccess: data = " + data);
//                        return;
//                    }
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    JSONObject mem = data.getJSONObject("mem");
//                    String schoolId = mem.getString("school_id");
//                    if (!TextUtils.equals(schoolId, "0") && !TextUtils.equals(schoolId, "999")) {
//                        // 既不是普通用户也不是开发人员,而是合作机构用户
//                        SettingsBean settings = SettingUtil.getSettings();
//                        settings.setHomePageStyle(3);
//                        SettingUtil.upDateSettings(settings);
//                    }
//
//                    if (TextUtils.equals(type_id, "1")) {
//                        /**
//                         * 6.6--将LoginActivity和ankiLoginActivity杀死
//                         */
//                        Activity loginActivity = AnkiDroidApp.activityManager.get("LoginActivity");
//                        Activity ankiLoginActivity = AnkiDroidApp.activityManager.get("AnkiLoginActivity");
////                        Toast.makeText(UserTypeActivity.this,"拿到loginActivity了" + loginActivity,Toast.LENGTH_SHORT).show();
////                        Toast.makeText(UserTypeActivity.this,"拿到ankiLoginActivity了" + ankiLoginActivity,Toast.LENGTH_SHORT).show();
//                        if(null != loginActivity){
//                            loginActivity.finish();
//                        }
//                        if(null != ankiLoginActivity){
//                            ankiLoginActivity.finish();
//                        }
//
//                        /**
//                         * 6.6--跳转到应用主界面
//                         */
//                        Intent intent = new Intent(UserTypeActivity.this, AikaActivity.class);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("onError>>>>>>>>>>>>", ex.toString());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                if (isFromThirdLogin) {
//                    //如果是第三方登录的请求，则不启动广播
//                } else {
//                    //如果是正常的注册账号登录，则启动广播
//                    if (load_personal_info_times == 1) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
//                                Intent i = new Intent("Update Username And User Head Image");
//                                sendBroadcast(i);
//                                Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent! From normal login");
//                            }
//                        }, 1000);
//                    }
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case R.id.tv_ok:
                if (mExamplePagerAdapter != null) {
                    String code = mExamplePagerAdapter.getCode(currentPosition);
                    if (TextUtils.isEmpty(code) && currentPosition == 0) {
                        ToastAlone.showShortToast("请选择教育程度");
                        return;
                    } else if (TextUtils.isEmpty(code) && currentPosition == 1) {
                        ToastAlone.showShortToast("请输入邀请码");
                        return;
                    }
                    postMsgToBackStage(mem_id, client_type, client_id, code);
                }
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {



        return false;
    }
}
