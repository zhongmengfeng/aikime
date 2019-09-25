package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.utils.SPUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/2/8.
 */

public class SlideMenuManager implements View.OnClickListener{
    private SlidingMenu slidingMenu;
    private ImageView headImg;
    private ImageView loginImg;

    /** 牌组列表 **/
    private RelativeLayout lyt1;
    /** 阅读制卡 **/
    private RelativeLayout lyt2;
    /** 模考制卡 **/
    private RelativeLayout lyt3;
    /** 卡片预览 **/
    private RelativeLayout lyt4;
    /** 学习统计 **/
    private RelativeLayout lyt5;
    /** 偏好设置 **/
    private RelativeLayout lyt6;
    /** 个人中心 **/
    private RelativeLayout lyt7;
    /** 在线帮助 **/
    private RelativeLayout lyt8;
    private TextView loginName;
    private Activity activity;
    public  final  int RESQUEST_FROM_SLIDINGMENU = 0010;

    private Runnable progressRun;



    /**
     * SlideMenuManager的构造方法，初始化SlidingMenu
     */
    public SlideMenuManager(Activity activity){
        this.activity = activity;
        slidingMenu = new SlidingMenu(activity);
        setSlidingMenu(activity);
        initSlidMenuLayout(activity);
    }

    public void initSlidMenuLayout(Activity activity){
        headImg=(ImageView) activity.findViewById(R.id.slm_iv_head);
        loginImg=(ImageView) activity.findViewById(R.id.slm_iv_login);

        lyt1=(RelativeLayout) activity.findViewById(R.id.slm_lyt_1);
        lyt2=(RelativeLayout) activity.findViewById(R.id.slm_lyt_2);
        lyt3=(RelativeLayout) activity.findViewById(R.id.slm_lyt_3);
        lyt4=(RelativeLayout) activity.findViewById(R.id.slm_lyt_4);
        lyt5=(RelativeLayout) activity.findViewById(R.id.slm_lyt_5);
        lyt6=(RelativeLayout) activity.findViewById(R.id.slm_lyt_6);
        lyt7=(RelativeLayout) activity.findViewById(R.id.slm_lyt_7);
        lyt8=(RelativeLayout) activity.findViewById(R.id.slm_lyt_8);
        loginName=(TextView) activity.findViewById(R.id.slm_tv_name);
        headImg.setOnClickListener(this);
        loginImg.setOnClickListener(this);
        lyt1.setOnClickListener(this);
        lyt2.setOnClickListener(this);
        lyt3.setOnClickListener(this);
        lyt4.setOnClickListener(this);
        lyt5.setOnClickListener(this);
        lyt6.setOnClickListener(this);
        lyt7.setOnClickListener(this);
        lyt8.setOnClickListener(this);
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                showFeatures();
            }
        });
//        setStatusBar();
    }

    /**
     * 根据设置显示相应功能
     */
    private void showFeatures() {
        SettingsBean settings = SettingUtil.getSettings();
        if (settings != null) {
            int homePageStyle = settings.getHomePageStyle();
            if (homePageStyle == 0) {
                // 阅读 + 牌组 + 模考
                lyt1.setVisibility(View.VISIBLE);
                lyt2.setVisibility(View.VISIBLE);
                lyt3.setVisibility(View.VISIBLE);

            } else if (homePageStyle == 1) {
                // 牌组 + 阅读
                lyt1.setVisibility(View.VISIBLE);
                lyt2.setVisibility(View.VISIBLE);
                lyt3.setVisibility(View.GONE);

            }else if(homePageStyle == 2){
                // 牌组 + 模考
                lyt1.setVisibility(View.VISIBLE);
                lyt2.setVisibility(View.GONE);
                lyt3.setVisibility(View.VISIBLE);
            } else if (homePageStyle == 3) {
                // TODO 我的课程

            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.slm_iv_head:

                break;
            case R.id.slm_iv_login://立即登录
                intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("isFromSildingMenu",true);

                try {
                    ((AikaActivity)activity).isSkipToLoginActivity = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;

            case R.id.slm_lyt_1://牌组列表
                ((AikaActivity)activity).selectDeckPicker();
                slidingMenu.toggle();

                break;
            case R.id.slm_lyt_2://阅读制卡
                ((AikaActivity)activity).selectDeckReader();
                slidingMenu.toggle();
                break;
            case R.id.slm_lyt_3://模考制卡
                ((AikaActivity)activity).selectDeckTest();
                slidingMenu.toggle();
                break;
            case R.id.slm_lyt_4://卡片浏览
                //Toast.makeText(activity, "Sorry，这个功能还在完善中哦",Toast.LENGTH_SHORT).show();
                intent = new Intent(activity,CardBrowserActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
            case R.id.slm_lyt_5://学习统计
                if (progressRun != null) {
                    progressRun.run();
                }
                intent = new Intent(activity, StatisticsAika.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
            case R.id.slm_lyt_6://偏好设置
                intent = new Intent(activity, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
            case R.id.slm_lyt_7://个人中心
                String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
                if (TextUtils.isEmpty(memId)) {
                    intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("isFromSildingMenu",true);
                }else{
                    intent = new Intent(activity, PersonalCenterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                }
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
            case R.id.slm_lyt_8://在线帮助
                intent = new Intent(activity,OnLineHelpActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
        }
        slidingMenu.toggle();
    }


    /**
     * 设置SlidingMenu的样式，并将SlidingMenu挂载在Activity上
     *
     * @param activity
     */
    public void setSlidingMenu(Activity activity) {
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // menu.setShadowWidthRes(R.dimen.shadow_width);
        // menu.setShadowDrawable(R.color.colorAccent);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //为侧滑菜单设置布局
//        slidingMenu.setMenu(R.layout.slide_layout);
        slidingMenu.setMenu(R.layout.navigationview_layout);
    }

    /**
     * 设置SlidingMenu的布局，并将SlidingMenu挂载在Activity上
     */
    public void setSlidingMenu(int resId, Activity activity) {
        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(resId);
    }

    /**
     * 获得SlidingMenu对象
     *
     * @return
     */
    public SlidingMenu getSlidingMenu() {
        return slidingMenu;
    }

    /**
     * 设置头像
     * @param resId
     */
    public void setHeadImg(int resId){
        headImg.setImageResource(resId);
    }

    public void setHeadImg(Drawable drawable){
        headImg.setImageDrawable(drawable);
    }
    public void setHeadImg(Bitmap bitmap){
        headImg.setImageBitmap(bitmap);
    }

    /**
     * 隐藏“立即登录”
     */
    public void setLoginImgHide(){
        loginImg.setVisibility(View.INVISIBLE);
        loginName.setVisibility(View.VISIBLE);
    }

    /**
     * 显示“立即登录”
     */
    public void setLoginImgShow(){
        loginName.setVisibility(View.INVISIBLE);
        loginImg.setVisibility(View.VISIBLE);
    }

    /**
     * 登录成功时显示用户名
     */
    public void setLoginName(String name){
        setLoginImgHide();
        loginName.setVisibility(View.VISIBLE);
        loginName.setText(name);
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final LinearLayout linear_bar = (LinearLayout) activity.findViewById(R.id.linear_bar);
            final int statusHeight = getStatusBarHeight();
            linear_bar.post(new Runnable() {
                @Override
                public void run() {
                    int titleHeight = linear_bar.getHeight();
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) linear_bar.getLayoutParams();
                    params.height = statusHeight + titleHeight;
                    linear_bar.setLayoutParams(params);
                }
            });
        }
    }
    /**
     54      * 获取状态栏的高度
     55      * @return
     56      */
    protected int getStatusBarHeight(){
        try{
            Class<?> c=Class.forName("com.android.internal.R$dimen");
            Object obj=c.newInstance();
            Field  field=c.getField("status_bar_height");
            int x=Integer.parseInt(field.get(obj).toString());
            return  activity.getResources().getDimensionPixelSize(x);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setProgressRun(Runnable progressRun) {
        this.progressRun = progressRun;
    }
}
