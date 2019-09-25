package com.ichi2yiji.anki.activity.ek;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.view.FlowLayout;
import com.ichi2yiji.anki.view.FlowTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKSearchActivity extends AppCompatActivity {

    private FlowLayout fl_ek_activity_search_flowLayout;
    private List<TextView> textList = new ArrayList<>();//科目集合

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_search);


        fl_ek_activity_search_flowLayout = (FlowLayout) findViewById(R.id.fl_ek_activity_search_flowLayout);

        Resources res = getResources();
        final Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.study_background);


        init();
    }

    private void init() {

        ArrayList<String> datas = new ArrayList<>();

        //2.准备好流失布局;设置listview的边距等参数;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 15;
        lp.leftMargin = 20;

        datas.add("语文");
        datas.add("政治");
        datas.add("司法考试");
        datas.add("化学");
        datas.add("音乐");
        datas.add("生物");
        datas.add("数学");
        datas.add("数学");

        for (int i = 0; i < datas.size(); i++) {
            FlowTextView textView = new FlowTextView(this);
            textView.setTagIndex(textList.size());


            //////////////////////////
//            textlist.get(i).setBackgroundResource(R.drawable.add_pro);
//            textlist.get(i).setTextColor(Color.parseColor("#ffce54"));
//            for (int j = 0; j < classCreatedByMe.size(); j++) {
//                textlist.get(j).setBackgroundResource(R.drawable.addgrid);
            ///////////////////////////

            //为textView设置样式;自己的课程与关注的课程颜色不一样;
            textView.setText(datas.get(i));
            textView.setTextColor(Color.parseColor("#a0d468"));
            textView.setBackgroundResource(R.drawable.add_pro);
            textView.setItsFocusBackgroundColor(R.drawable.add_pro);
            textView.setItsBackgroundColor(R.drawable.add_pro);
            textView.setItsTextColor(Color.parseColor("#a0d468"));//绿色
            //textView.setItsFocusTextColor(Color.parseColor("#ffce54"));

            //为textView设置点击事件;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.便利所有的标签,保持原有
                    for (int i = 0; i < textList.size(); i++) {
                        FlowTextView TextView = (FlowTextView) textList.get(i);
                        TextView.setBackgroundResource(TextView.getItsBackgroundColor());
                        TextView.setItsFocusBackgroundColor(TextView.getItsFocusBackgroundColor());
                        TextView.setTextColor(TextView.getItsTextColor());
                    }
                    //2.被点中的变样子;
                    FlowTextView flowTextView = (FlowTextView) v;
                    int touchIndex = flowTextView.getTagIndex();
                }
            });


            //将textView添加到集合和流失布局中;
            textList.add(textView);

            fl_ek_activity_search_flowLayout.addView(textView, lp);
        }
    }
}