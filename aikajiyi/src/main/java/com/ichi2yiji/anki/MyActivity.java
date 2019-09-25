package com.ichi2yiji.anki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.dialogs.LoadingDialog;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.utils.AssetsUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ekar01 on 2017/6/1.
 */

public class MyActivity extends AppCompatActivity {
    private Button bt_download;
    private Button bt_upload;
    private ProgressDialog progressDialog;
    private boolean hasError;
    private File result;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        Timber.d("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
//        applyKitKatAndLollipopTranslucency();
        ApplyTranslucency.applyKitKatTranslucency(this);

//        //透明状态栏(只是变成透明,并不能变成颜色一样)
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏(只是变成透明,并不能变成颜色一样)
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        final String url = "http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%9B%85%E6%80%9D%E5%8D%95%E8%AF%8D__01.apkg";
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + "filename";

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        progressDialog = new ProgressDialog(this).builder();
        bt_download = (Button)findViewById(R.id.bt_download);
        bt_upload = (Button)findViewById(R.id.bt_upload);

        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                progressDialog.setDownloadTip("准备下载");
//                progressDialog.showWithProgress();

                com.ankireader.util.ZXUtils.DownLoadFileWithProgress(url, path, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onWaiting() {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onWaiting");
                    }

                    @Override
                    public void onStarted() {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onStarted");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {

                        Float i = Float.parseFloat(String.valueOf(current))/Float.parseFloat(String.valueOf(total));
                        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        String progress = decimalFormat.format(i * 100);

                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>i" +i);
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>progress" + progress);
                        progressDialog.setDownloadTip("下载中：" + progress + "%");
//                        progressDialog.setProgress(Integer.valueOf(progress));

                    }

                    @Override
                    public void onSuccess(File result) {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onSuccess");
                        MyActivity.this.result = result;
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onError" + ex);
                        hasError = true;
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onCancelled");
                    }

                    @Override
                    public void onFinished() {
                        Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onFinished");

                        if (!hasError && result != null) {
                            //成功获取数据
                            progressDialog.showSuccessWithStatus();
                            progressDialog.setDownloadTip("恭喜，下载成功");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent();
                                    i.putExtra("PathFromDownLoadDecksFromAnki", path);
                                    setResult(RESULT_OK, i);
                                    MyActivity.this.finish();
                                    //dx  add
                                    MyActivity.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                                }
                            },1000);
                        }else{
                            progressDialog.showErrorWithStatus();
                            progressDialog.setDownloadTip("下载出现问题了");
                        }

                    }

                });
            }
        });


        final String url_upload = "http://zang.ankichina.net/" + "Home/Index/upload2";
        String fileName = "inline_play_button.png";
        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" +fileName;
        AssetsUtil.copyAssetsFile2Storage(fileName, destFile);

        final RequestParams params = new RequestParams(url_upload);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", new File(destFile)));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("MyActivity", "请求结果：" + result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(MyActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
//                        Toast.makeText(MyActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /**
     * 验证普通登录接口:BASE_DOMAIN + "Home/App/login/"
     * Map<String, String> map = new HashMap<>();
     * map.put("tele", username);
     * map.put("client_id", this.clientid);
     * map.put("password", password);
     * map.put("client_type", String.valueOf(1));
     * ZXUtils.Post(Urls.URL_APP_LOGIN, map, new Callback.CommonCallback<String>() {}
     *
     *{ "code": 1000, "data": [ { "msg": "登录成功！", "mem_id": "7280", "version": "1209", "is_update": "0" } ] }
     *
     *               在onSuccess()方法里面请求接口:BASE_DOMAIN + "Home/App/memberMsg/"
     * Map<String, String> map = new HashMap<>();
     * map.put("mem_id", mem_id);
     * map.put("type_id", type_id);
     * ZXUtils.Post(Urls.URL_APP_MEMBER_MSG, map, new Callback.CommonCallback<String>() {
     *
     {
         "code": 1000,
         "data": {
             "mem": {
                 "user_name": "seis",
                 "email": "seis@ankichina.net",
                 "face": "",
                 "jifen": "20",
                 "jyz": "0",
                 "honeyname": "seis",
                 "gender": "男",
                 "telephone": "",
                 "qq": "",
                 "school_id": "1",
                 "is_teacher": "1",
                 "member_level": "2",
                 "school_logo": "http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/591ace265ed7e.png",
                 "deck": "http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201705221700/%E5%9C%B0%E9%9C%87%E5%9C%B0%E5%B1%82%E5%AD%A6-%E5%90%8D%E8%AF%8D%E8%A7%A3%E9%87%8A.apkg",
                 "class_ids": "2001005,2001004"
             },
             "decks": [
             {
                 "goods_id": "1937",
                 "goods_name": "单词背诵02",
                 "logo": "",
                 "decksize": "2800.00",
                 "deckname": "单词背诵02",
                 "images": "",
                 "is_share": "0"
             },....]
         }
     }
     */



    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
