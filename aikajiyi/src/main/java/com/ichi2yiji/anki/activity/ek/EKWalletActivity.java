package com.ichi2yiji.anki.activity.ek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by ekar01 on 2017/7/6.
 */

public class EKWalletActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_ek_activity_wallet_back;
    private TextView tv_ek_activity_wallet_balance;
    private TextView tv_ek_activity_wallet_detail;
    private TextView tv_ek_activity_wallet_withdraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_wallet);

        initView();

    }

    private void initView() {
        rl_ek_activity_wallet_back = (RelativeLayout) findViewById(R.id.rl_ek_activity_wallet_back);
        tv_ek_activity_wallet_balance = (TextView) findViewById(R.id.tv_ek_activity_wallet_balance);
        tv_ek_activity_wallet_detail = (TextView) findViewById(R.id.tv_ek_activity_wallet_detail);
        tv_ek_activity_wallet_withdraw = (TextView) findViewById(R.id.tv_ek_activity_wallet_withdraw);

        rl_ek_activity_wallet_back.setOnClickListener(this);
        tv_ek_activity_wallet_detail.setOnClickListener(this);
        tv_ek_activity_wallet_withdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_ek_activity_wallet_back:
                finish();
                break;
            case R.id.tv_ek_activity_wallet_detail:
                intent = new Intent(this, EKWalletDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_ek_activity_wallet_withdraw:
                intent = new Intent(this, EKWithdrawActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
