package com.ichi2yiji.anki.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekar01 on 2017/5/31.
 */

public class YaoqingmaFragment extends Fragment {

    public static final int RESULT_OK = 200;
    private Button bt_yaoqingma_confirm;
    private EditText et_yaoqingma;
    private String mem_id;

    public static YaoqingmaFragment newInstance(String mem_id) {
        YaoqingmaFragment fragment = new YaoqingmaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mem_id", mem_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaoqingma, null);

        Bundle arguments = getArguments();
        mem_id = arguments.getString("mem_id");

        bt_yaoqingma_confirm = (Button) view.findViewById(R.id.bt_yaoqingma_confirm);
        et_yaoqingma = (EditText) view.findViewById(R.id.et_yaoqingma);
        et_yaoqingma.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_ENTER:
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        if(imm.isActive()){
//                            imm.toggleSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),1,0);
                            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                        }
                        return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        bt_yaoqingma_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = et_yaoqingma.getText().toString();
                giveYouInvite(s);
            }
        });
    }

    public void giveYouInvite(String inviteValue) {
        if (TextUtils.isEmpty(inviteValue)) {
            ToastAlone.showShortToast("请输入邀请码");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("yao", inviteValue);
        ZXUtils.Post(Urls.URL_APP_YAO_GUAN_ZHU, map, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    int code = result.getInt("code");
                    String data = result.getString("data");
                    if (code == 1000) {
                        Intent intent = new Intent();

                        // 设置返回结果为RESULT_OK, intent可以传入一些其他的参数, 在onActivityResult中的data中可以获取到
                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();

                        Log.e("YaoqingmaFragment", result.toString());
                    } else {
                        ToastAlone.showShortToast(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError: ex = " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                getActivity().getAttentionJsonData(mem_id);
            }
        });
    }
}
