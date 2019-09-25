package com.ichi2yiji.anki.activity.ek;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.EKFineClassAdapter;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKFineClassActivity extends AppCompatActivity {

    private ListView fine_lst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTranlucency();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_fineclass);

        fine_lst = (ListView) findViewById(R.id.fine_lst);
        fine_lst.setAdapter(new EKFineClassAdapter(EKFineClassActivity.this));
    }

    public void setTranlucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
