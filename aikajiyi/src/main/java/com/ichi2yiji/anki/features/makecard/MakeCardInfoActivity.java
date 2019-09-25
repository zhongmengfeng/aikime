package com.ichi2yiji.anki.features.makecard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AddNoteInAnyWindow;
import com.ichi2yiji.anki.AnkiActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MakeCardInfoActivity extends AnkiActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_front)
    TextView tvFront;
    @Bind(R.id.tv_back)
    TextView tvBack;
    @Bind(R.id.et_note)
    EditText etNote;
    @Bind(R.id.et_mark)
    EditText etMark;


    private List<String> fieldList;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);
        ButterKnife.bind(this);
        ApplyTranslucency.applyKitKatTranslucency(this);
        revMsg();
    }

    private void revMsg() {
        Intent intent = getIntent();
        String text = intent.getStringExtra("TEXT");
        String sentence = intent.getStringExtra("sentence");
        String sentenceHtml = intent.getStringExtra("sentenceHtml");
        bookName = intent.getStringExtra("BOOK_NAME");
        bookName = bookName.replaceAll("@@@[0-9]", "");
        int startElementIndex = intent.getIntExtra("startElementIndex", 0);
        int endElementIndex = intent.getIntExtra("endElementIndex", 0);

        String reg = "<span><font color=\"#3696e9\"><u>.*</u></font></span>";
        tvFront.setText(sentenceHtml.replaceAll(reg," ____ "));
        /**             *
         * 只有调用了该方法，TextView才能不依赖于ScrollView而实现滚动的效果。
         * 要在XML中设置TextView的textcolor，否则，当TextView被触摸时，会灰掉。
         */
        tvFront.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvBack.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvBack.setText(Html.fromHtml(sentenceHtml));

        fieldList = new ArrayList<>();
        fieldList.add(sentence);
        fieldList.add(text);
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                makeCard();
                break;
        }
    }

    private void makeCard() {
        String note = etNote.getText().toString();
        String mark = etMark.getText().toString();
        fieldList.add(note);
        fieldList.add(mark);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AddNoteInAnyWindow.addNote(Constants.MODEL_BLANK,bookName,fieldList);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
