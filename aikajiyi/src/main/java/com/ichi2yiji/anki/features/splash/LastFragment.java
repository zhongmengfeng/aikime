package com.ichi2yiji.anki.features.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.LoginActivity;
import com.ichi2yiji.anki.AikaActivity;
import com.ichi2yiji.anki.RegisterActivity;

import org.xutils.common.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LastFragment extends Fragment {

    @Bind(R.id.login)
    Button login;
    @Bind(R.id.register)
    Button register;
    @Bind(R.id.try_once)
    Button tryOnce;

    private Context mContext;

    public static LastFragment newInstance() {
        LastFragment fragment = new LastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View root = inflater.inflate(R.layout.fragment_last_layout, container, false);
        ButterKnife.bind(this, root);
        login.setSelected(true);
        return root;
    }



    @OnClick({R.id.login, R.id.register, R.id.try_once})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.login:
                // 登录
                login.setSelected(true);
                register.setSelected(false);
                intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                getActivity().finish();
                break;
            case R.id.register:
                // 注册
                login.setSelected(false);
                register.setSelected(true);
                intent = new Intent(mContext, RegisterActivity.class);
                intent.putExtra("IntentFromWelcomeActivity", true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                getActivity().finish();
                break;
            case R.id.try_once:
                // 随便看看
                intent = new Intent(mContext, AikaActivity.class);
                LogUtil.e("goto AikaActivity: AnkiLoginActivity IntentHandler4" );
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
