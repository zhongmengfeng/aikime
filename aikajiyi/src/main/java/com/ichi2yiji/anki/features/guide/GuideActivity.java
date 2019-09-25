package com.ichi2yiji.anki.features.guide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AnkiActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.utils.SPUtil;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导界面
 */
public class GuideActivity extends AnkiActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_guide)
    ImageView ivGuide;

    private ImageOptions imageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        ApplyTranslucency.applyKitKatTranslucency(this);
        initImageOptions();
        revMsg();
    }

    private void revMsg() {
        Intent intent = getIntent();
        // String filePath = "/sdcard/Chaojiyiji/images/guide_book1.gif";
        String filePath = intent.getStringExtra("filePath");
        x.image().bind(ivGuide, new File(filePath).toURI().toString(), imageOptions);
    }

    private void initImageOptions() {
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(240), DensityUtil.dip2px(300))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                // .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                // .setLoadingDrawableId(R.drawable.ic_launcher)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.ic_launcher)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
    }
}
