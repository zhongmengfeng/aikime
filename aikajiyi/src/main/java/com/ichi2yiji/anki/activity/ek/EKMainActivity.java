package com.ichi2yiji.anki.activity.ek;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.fragment.EKFragment1;
import com.ichi2yiji.anki.fragment.EKFragment2;
import com.ichi2yiji.anki.fragment.EKFragment3;
import com.ichi2yiji.anki.fragment.EKFragment4;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.utils.SPUtil;

/**
 * Created by ekar01 on 2017/7/4.
 */


public class EKMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private int lastSelectedPosition;
    private int mHomeMessage;
    private BottomNavigationBar bottomNavigationBar;
    private BadgeItem mHomeNumberBadgeItem;
    private BadgeItem mMusicNumberBadgeItem;

    private EKFragment1 mHomeFragment;
    private EKFragment2 mBookFragment;
    private EKFragment3 mMusicFragment;
    private EKFragment4 mFavoriteFragment;
    private FragmentManager mFragmentManager;
    private String mem_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranlucency();
//        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
         setContentView(R.layout.ek_activity_main);
//        ApplyTranslucency.initState2(this);
        ApplyTranslucency.applyKitKatTranslucency(this);
        mem_id = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_MEM_ID, "");
        initView();
        setDefaultFragment();
    }


    public void setTranlucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void initView() {

//        /**
//         *添加标签的消息数量
//         */
//        mHomeNumberBadgeItem = new BadgeItem()
//                .setBorderWidth(2)
//                .setBackgroundColor(Color.RED)
//                .setText("")
//                .setHideOnSelect(false); // TODO 控制便签被点击时 消失 | 不消失
//
//
//        /**
//         *添加标签的消息数量
//         */
//        mMusicNumberBadgeItem = new BadgeItem()
//                .setBorderWidth(2)
//                .setBackgroundColor(Color.RED)
//                .setText("99+")
//                .setHideOnSelect(true);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);


        // TODO 设置模式
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        // TODO 设置背景色样式
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar.setTabSelectedListener(this);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.tab_study, "学习").setActiveColorResource(R.color.md_deep_orange_500).setBadgeItem(mHomeNumberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.tab_find, "发现").setActiveColorResource(R.color.md_teal_500))
                .addItem(new BottomNavigationItem(R.drawable.tab_personal, "分享").setActiveColorResource(R.color.md_light_blue_500).setBadgeItem(mMusicNumberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.tab_personal, "个人").setActiveColorResource(R.color.md_brown_500))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();


        // TODO 设置 BadgeItem 默认隐藏 注意 这句代码在添加 BottomNavigationItem 之后
        //mHomeNumberBadgeItem.hide();

//        bottomNavigationBar.
    }

    private void setDefaultFragment() {

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mHomeFragment = EKFragment1.newInstance("EKFragment1");
        transaction.add(R.id.tb, mHomeFragment);
        transaction.commit();

    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;

        //开启事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        /**
         * fragment 用 add + show + hide 方式
         * 只有第一次切换会创建fragment，再次切换不创建
         *
         * fragment 用 replace 方式
         * 每次切换都会重新创建
         *
         */
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = EKFragment1.newInstance("EKFragment1");
                    transaction.add(R.id.tb, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
//                transaction.replace(R.id.tb, mHomeFragment);
                break;
            case 1:
                if (mBookFragment == null) {
                    mBookFragment = EKFragment2.newInstance("EKFragment2");
                    transaction.add(R.id.tb, mBookFragment);
                } else {
                    transaction.show(mBookFragment);
                }
//                transaction.replace(R.id.tb, mBookFragment);
                break;
            case 2:
                if (mMusicFragment == null) {
                    mMusicFragment = EKFragment3.newInstance("EKFragment3");
                    transaction.add(R.id.tb, mMusicFragment);
                } else {
                    transaction.show(mMusicFragment);
                }
//                transaction.replace(R.id.tb, mMusicFragment);
                break;
            case 3:

                if (!TextUtils.isEmpty(mem_id)) {
                    if (mFavoriteFragment == null) {
                        mFavoriteFragment = EKFragment4.newInstance("EKFragment4");
                        transaction.add(R.id.tb, mFavoriteFragment);
                    } else {
                        transaction.show(mFavoriteFragment);
                    }
                }else{
                    Intent intent = new Intent(this, EKLoginActivity.class);
                    startActivity(intent);
                }

//                transaction.replace(R.id.tb, mFavoriteFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    /**
     * 隐藏当前fragment
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mBookFragment != null) {
            transaction.hide(mBookFragment);
        }
        if (mMusicFragment != null) {
            transaction.hide(mMusicFragment);
        }
        if (mFavoriteFragment != null) {
            transaction.hide(mFavoriteFragment);
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    public void addMessage() {
        mHomeMessage++;
        mHomeNumberBadgeItem.setText(mHomeMessage + "");
        mHomeNumberBadgeItem.show();
    }
}
