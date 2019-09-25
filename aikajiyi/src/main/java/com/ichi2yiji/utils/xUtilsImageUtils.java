package com.ichi2yiji.utils;

import android.widget.ImageView;

import com.chaojiyiji.yiji.R;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by ekar01 on 2017/6/1.
 */

public class xUtilsImageUtils {
    /**
     * 显示图片（默认情况）
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     */
    public static void display(ImageView imageView, String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(R.drawable.aika)
                .setLoadingDrawableId(R.drawable.aika)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    /**
     * 显示圆角图片
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     * @param radius    圆角半径，
     *                  <p>
     *                  ImageOptions options=new ImageOptions.Builder()
     *                  //设置加载过程中的图片
     *                  .setLoadingDrawableId(R.drawable.ic_launcher)
     *                  //设置加载失败后的图片
     *                  .setFailureDrawableId(R.drawable.ic_launcher)
     *                  //设置使用缓存
     *                  .setUseMemCache(true)
     *                  //设置显示圆形图片
     *                  .setCircular(true)
     *                  //设置支持gif
     *                  .setIgnoreGif(false)
     *                  .build();
     */
    public static void display(ImageView imageView, String iconUrl, int radius) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(radius))
                .setIgnoreGif(false)
                .setCrop(true)//是否对图片进行裁剪
                .setFailureDrawableId(R.drawable.aika)
                .setLoadingDrawableId(R.drawable.aika)
                .setUseMemCache(true)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    /**
     * 显示圆形头像，第三个参数为true
     *
     * @param imageView  图像控件
     * @param iconUrl    图片地址
     * @param isCircluar 是否显示圆形
     */
    public static void display(ImageView imageView, String iconUrl, boolean isCircluar) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(isCircluar)
                .setCrop(true)
                .setFailureDrawableId(R.drawable.loading01)
                .setLoadingDrawableId(R.drawable.loading02)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }
}
