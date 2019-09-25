package com.ichi2yiji.anki.features.login;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.List;

/**
 * Created by hackware on 2016/9/10.
 */

public class ExamplePagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private List<String> mDataList;
    private String eduLevel;
    private TextView[] tvLevels = new TextView[5];
    private EditText etInvitationCode;

    public ExamplePagerAdapter(List<String> dataList,Context context ) {
        mDataList = dataList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = null;
        if(position == 0){
            inflate = View.inflate(container.getContext(), R.layout.tab_edu_level, null);
            assignEduLevelViews(inflate);
        }else if(position == 1){
            inflate = View.inflate(container.getContext(), R.layout.tab_invitation_code, null);
            assignInvitationCodeViews(inflate);
        }
        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        TextView textView = (TextView) object;
        String text = textView.getText().toString();
        int index = mDataList.indexOf(text);
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }

    private void assignEduLevelViews(View view) {
        tvLevels[0] = (TextView) view.findViewById(R.id.tv_level1);
        tvLevels[1] = (TextView) view.findViewById(R.id.tv_level2);
        tvLevels[2] = (TextView) view.findViewById(R.id.tv_level3);
        tvLevels[3] = (TextView) view.findViewById(R.id.tv_level4);
        tvLevels[4] = (TextView) view.findViewById(R.id.tv_level5);

        for (int i = 0; i < tvLevels.length; i++) {
            tvLevels[i].setOnClickListener(this);
        }
    }

    private void setTvLevelsBg(@IntRange(from = 0,to = 4) int currentLevel){
        tvLevels[0].setSelected(currentLevel == 0);
        tvLevels[1].setSelected(currentLevel == 1);
        tvLevels[2].setSelected(currentLevel == 2);
        tvLevels[3].setSelected(currentLevel == 3);
        tvLevels[4].setSelected(currentLevel == 4);
    }

    private void assignInvitationCodeViews(View view) {
        etInvitationCode = (EditText) view.findViewById(R.id.et_invitation_code);
        etInvitationCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                UserTypeActivity activity = (UserTypeActivity) mContext;
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_ENTER:
                        // 隐藏软键盘
                        if(imm.isActive()){
                            imm.hideSoftInputFromWindow(activity .getWindow().getDecorView().getWindowToken(), 0);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取邀请码
     * @return  邀请码
     */
    public String getCode(int position) {
        if(position == 0){
            return eduLevel;
        }else if(position == 1){
            return etInvitationCode.getText().toString();
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_level1:
                setTvLevelsBg(0);
                break;
            case R.id.tv_level2:
                setTvLevelsBg(1);
                break;
            case R.id.tv_level3:
                setTvLevelsBg(2);
                break;
            case R.id.tv_level4:
                setTvLevelsBg(3);
                break;
            case R.id.tv_level5:
                setTvLevelsBg(4);
                break;
        }
        TextView textView = (TextView) v;
        eduLevel = textView.getText().toString();
        // ToastAlone.showShortToast(eduLevel);
    }

}
