package com.ichi2yiji.anki.activity.ek;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.fragment.EKFragment1;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

/**
 * Created by ekar01 on 2017/7/5.
 */

/**
 * Created by Kevin on 2016/11/29.
 * Blog:http://blog.csdn.net/student9128
 * Description: Bottom Navigation Bar by TextView + LinearLayout.
 */

public class EKMainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private TextView mTHome, mTLocation, mTLike, mTMe, mTextView;
    private EKFragment1 mHomeFragment;
    private EKFragment1 mLocationFragment;
    private EKFragment1 mLikeFragment;
    private EKFragment1 mPersonFragment;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_main2);

        ApplyTranslucency.applyKitKatTranslucency(this);
        mTextView = (TextView) findViewById(R.id.activity_text_view);
        mTHome = (TextView) findViewById(R.id.tv_home);
        mTLocation = (TextView) findViewById(R.id.tv_location);
        mTLike = (TextView) findViewById(R.id.tv_like);
        mTMe = (TextView) findViewById(R.id.tv_person);

        mTHome.setOnClickListener(this);
        mTLocation.setOnClickListener(this);
        mTLike.setOnClickListener(this);
        mTMe.setOnClickListener(this);
        setDefaultFragment();
    }

    /**
     * set the default Fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setDefaultFragment() {
        switchFrgment(0);
        //set the defalut tab state
        setTabState(mTHome, R.drawable.home_filter, getColor2(R.color.colorPrimary));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        resetTabState();//reset the tab state
        switch (view.getId()) {
            case R.id.tv_home:
                setTabState(mTHome, R.drawable.home_filter, getColor2(R.color.colorPrimary));
                switchFrgment(0);
                break;
            case R.id.tv_location:
                setTabState(mTLocation, R.drawable.home_filter, getColor2(R.color.colorPrimary));
                switchFrgment(1);
                break;
            case R.id.tv_like:
                setTabState(mTLike, R.drawable.home_filter, getColor2(R.color.colorPrimary));
                switchFrgment(2);
                break;
            case R.id.tv_person:
                setTabState(mTMe, R.drawable.home_filter, getColor2(R.color.colorPrimary));
                switchFrgment(3);
                break;
        }
    }

    /**
     * switch the fragment accordting to id
     * @param i id
     */
    private void switchFrgment(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = EKFragment1.newInstance("home");
                }
                transaction.replace(R.id.sub_content, mHomeFragment);
                break;
            case 1:
                if (mLocationFragment == null) {
                    mLocationFragment = EKFragment1.newInstance("location");
                }
                transaction.replace(R.id.sub_content, mLocationFragment);
                break;
            case 2:
                if (mLikeFragment == null) {
                    mLikeFragment = EKFragment1.newInstance("like");
                }
                transaction.replace(R.id.sub_content, mLikeFragment);
                break;
            case 3:
                if (mPersonFragment == null) {
                    mPersonFragment = EKFragment1.newInstance("me");
                }
                transaction.replace(R.id.sub_content, mPersonFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * set the tab state of bottom navigation bar
     *
     * @param textView the text to be shown
     * @param image    the image
     * @param color    the text color
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTabState(TextView textView, int image, int color) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, image, 0, 0);//Call requires API level 17
        textView.setTextColor(color);
    }


    /**
     * revert the image color and text color to black
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void resetTabState() {
        setTabState(mTHome, R.drawable.home_filter, getColor2(R.color.black));
        setTabState(mTLocation, R.drawable.home_filter, getColor2(R.color.black));
        setTabState(mTLike, R.drawable.home_filter, getColor2(R.color.black));
        setTabState(mTMe, R.drawable.home_filter, getColor2(R.color.black));

    }

    /**
     * @param i the color id
     * @return color
     */
//    private int getColor(int i) {
//        int color = ContextCompat.getColor(this, i);
//
//
//        return color;
//    }

    private int getColor2(int i){
        return ContextCompat.getColor(this, i);
    }
}
