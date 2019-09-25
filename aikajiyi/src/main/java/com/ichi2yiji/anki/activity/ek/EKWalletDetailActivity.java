package com.ichi2yiji.anki.activity.ek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by ekar01 on 2017/7/7.
 */

public class EKWalletDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_ek_activity_wallet_detail_back;
    private TextView tv_ek_activity_wallet_detail_income;
    private TextView tv_ek_activity_wallet_detail_account;
    private TextView tv_ek_activity_wallet_detail_name;
    private RelativeLayout rl_ek_activity_wallet_detail_select;
    private TextView tv_ek_activity_wallet_detail_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_walletdetail);

        initView();

    }

    private void initView() {
        rl_ek_activity_wallet_detail_back = (RelativeLayout) findViewById(R.id.rl_ek_activity_wallet_detail_back);
        tv_ek_activity_wallet_detail_income = (TextView) findViewById(R.id.tv_ek_activity_wallet_detail_income);
        tv_ek_activity_wallet_detail_account = (TextView) findViewById(R.id.tv_ek_activity_wallet_detail_account);
        tv_ek_activity_wallet_detail_name = (TextView) findViewById(R.id.tv_ek_activity_wallet_detail_name);
        rl_ek_activity_wallet_detail_select = (RelativeLayout) findViewById(R.id.rl_ek_activity_wallet_detail_select);
        tv_ek_activity_wallet_detail_confirm = (TextView) findViewById(R.id.tv_ek_activity_wallet_detail_confirm);

        rl_ek_activity_wallet_detail_back.setOnClickListener(this);
        rl_ek_activity_wallet_detail_select.setOnClickListener(this);
        tv_ek_activity_wallet_detail_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ek_activity_wallet_detail_back:
                finish();
                break;
            case R.id.rl_ek_activity_wallet_detail_select:

                break;
            case R.id.tv_ek_activity_wallet_detail_confirm:

                break;

        }
    }
}
