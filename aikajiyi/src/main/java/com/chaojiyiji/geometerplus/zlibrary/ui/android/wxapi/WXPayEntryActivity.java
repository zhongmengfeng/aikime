package com.chaojiyiji.geometerplus.zlibrary.ui.android.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ichi2yiji.common.Constants;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.utils.SPUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private String memId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_MEM_ID, "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(getApplicationContext(), "onReq", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResp(BaseResp resp) {

        int code = resp.errCode;

        if (code == 0) {

            //显示充值成功的页面和需要的操作

        }

        if (code == -1) {
            //错误

        }

        if (code == -2) {

            //用户取消
        }

    }
}