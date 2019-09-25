package com.ichi2yiji.anki.base;

import android.app.Activity;
import android.os.Bundle;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.SettingActivity;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
    }

}
