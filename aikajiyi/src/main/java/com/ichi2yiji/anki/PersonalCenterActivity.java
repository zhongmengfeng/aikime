package com.ichi2yiji.anki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersonalCenterActivity extends BaseActivity {
    private WebView webView;
    private String mem_id;
    private String clientid;
    private SharedPreferences pref;
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);

        ApplyTranslucency.applyKitKatTranslucency(PersonalCenterActivity.this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mem_id = pref.getString("MEM_ID","");
        clientid = pref.getString("CLIENTID","");

        /**
         * WebView个人中心
         */
        webView=(WebView)findViewById(R.id.personal_center_webview);
        webView.loadUrl("file:///android_asset/allWebView/personalCenter.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                JSONObject jsonObjectOut = new JSONObject();
                JSONObject jsonObjectIn = new JSONObject();
                try {
                    jsonObjectIn.put("mem", getPersonInfoJson());
                    jsonObjectIn.put("decks",  getDeckJson());
                    jsonObjectIn.put("books", getBookJson());
                    jsonObjectIn.put("tests", getTestJson());

                    jsonObjectOut.put("code", 1000);
                    jsonObjectOut.put("data", jsonObjectIn);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String serverData = jsonObjectOut.toString();
                webView.loadUrl("javascript:initFirstWindow('"+null+"','"+serverData+"')");
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");

                //此处有Bug,更换头像后网页头像不改变，待解决
                String username = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", "");
                if(!username.equals("")){
//                    String fileName = ROOT_PATH + "/anki"+ username +"_"+"ankichina.net.png";// 头像路径和名字
                    String headImgName = getHeadImgName();//头像名字
                    String fileName = ROOT_PATH + "/"+ headImgName;// 头像路径
                    String picpath ="file://"+ fileName;
                    webView.loadUrl("javascript:changeHeadImage('"+picpath+"')");// 头像在网页上显示出来
                }


            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");

//        getDeckJson();
//        getBookJson();
//        getTestJson();
//        getPersonInfoJson();

    }

    private JSONObject getPersonInfoJson(){
        String result = pref.getString("PersonalInfoResult1","");
        JSONObject ja = null;
        try {
            JSONObject top = new JSONObject(result);
            int mcode = top.getInt("code");
            JSONObject mdata = top.getJSONObject("data");

            if (mcode == 1000){
                ja = mdata.getJSONObject("mem");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PersonalCenter", "getPersonInfoJson"+">>>>>>>>>" + e);
        }
        Log.e("PersonalCenter", "getPersonInfoJson>>ja"+">>>>>>>>>" + ja);
        return  ja;
    }

    Map<String, String > goodsidmap = new HashMap<>();
    private JSONArray getDeckJson(){
        String result = pref.getString("PersonalInfoResult1","");
        JSONArray ja = null;
        try {
            JSONObject top = new JSONObject(result);
            int mcode = top.getInt("code");
            JSONObject mdata = top.getJSONObject("data");

            if (mcode == 1000){
                JSONArray deckarray = mdata.getJSONArray("decks");
                for(int i=0; i < deckarray.length(); i++){
                    JSONObject bottom = deckarray.getJSONObject(i);
                    String goods_name = bottom.getString("goods_name");
                    String goods_id = bottom.getString("goods_id");
                    goodsidmap.put(goods_name, goods_id);

                    JSONArray jsonArray = new JSONArray();
                    File picture = new File(ROOT_PATH);
                    if(picture.exists()){
                        File[] files = picture.listFiles();
                        Log.e("files", "files in PersonalCenterActivity is "+ Arrays.toString(files));
                        if (files != null){
                            for(int j = 0; j <files.length; j++){
                                if (files[j].getName().startsWith(goods_name)){
                                    jsonArray.put("file://"+ files[j].getAbsolutePath());
                                }
                            }
                        }
                    }
                    top.getJSONObject("data").getJSONArray("decks").getJSONObject(i).put("images",jsonArray);
                    ja = top.getJSONObject("data").getJSONArray("decks");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PersonalCenter", "getDeckJson"+">>>>>>>>>" + e);
        }
        Log.e("PersonalCenter", "getDeckJson>>ja"+">>>>>>>>>" + ja);
        return  ja;
    }

    private JSONArray getBookJson(){
        String result = pref.getString("PersonalInfoResult1","");
        JSONArray ja = null;
        try {
            JSONObject top = new JSONObject(result);
            int mcode = top.getInt("code");
            JSONObject mdata = top.getJSONObject("data");

            if (mcode == 1000){
                JSONArray bookarray = mdata.getJSONArray("books");
                for(int i=0; i < bookarray.length(); i++){
                    JSONObject bottom = bookarray.getJSONObject(i);
                    String goods_name = bottom.getString("goods_name");

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("images/cbd.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");

                    top.getJSONObject("data").getJSONArray("books").getJSONObject(i).put("images",jsonArray);
//                    Log.e("PersonalCenter", "getBookJson>>top"+">>>>>>>>>" + top);
                    ja = top.getJSONObject("data").getJSONArray("books");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PersonalCenter", "getBookJson"+">>>>>>>>>" + e);
        }
        Log.e("PersonalCenter", "getBookJson>>ja"+">>>>>>>>>" + ja);
        return  ja;
    }

    private JSONArray getTestJson(){
        String result = pref.getString("PersonalInfoResult1","");
        JSONArray ja = null;
        try {
            JSONObject top = new JSONObject(result);
            int mcode = top.getInt("code");
            JSONObject mdata = top.getJSONObject("data");

            if (mcode == 1000){
                JSONArray bookarray = mdata.getJSONArray("tests");
                for(int i=0; i < bookarray.length(); i++){
                    JSONObject bottom = bookarray.getJSONObject(i);
                    String goods_name = bottom.getString("goods_name");

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("images/cbd.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");
                    jsonArray.put("images/1.jpg");

                    top.getJSONObject("data").getJSONArray("tests").getJSONObject(i).put("images",jsonArray);
                    ja = top.getJSONObject("data").getJSONArray("tests");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PersonalCenter", "getTestJson"+">>>>>>>>>" + e);
        }
        Log.e("PersonalCenter", "getTestJson>>ja"+">>>>>>>>>" + ja);
        return  ja;
    }

    /**
     * 获取头像的名字
     * @return
     */
    private String getHeadImgName(){
        String result = pref.getString("PersonalInfoResult1","");
        JSONObject ja = null;
        String headImgName = "";
        try {
            JSONObject top = new JSONObject(result);
            int mcode = top.getInt("code");
            JSONObject mdata = top.getJSONObject("data");

            if (mcode == 1000){
                ja = mdata.getJSONObject("mem");
                String face = ja.getString("face");
                headImgName = face.substring(face.lastIndexOf("/") + 1);
                Log.e("PersonalCenter", "headImgName>>>>>>>>>" + headImgName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PersonalCenter", "getPersonInfoJson"+">>>>>>>>>" + e);
        }

        return  headImgName;
    }

    /**
     * JS调用原生的接口类，个人中心
     */
    class MyObject{
        @JavascriptInterface
        public void backToParent(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PersonalCenterActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

        @JavascriptInterface
        public void comfirmShare(final String json){
            Log.e("comfirmShare", "json in comfirmShare is "+ json);
//            requestKeyandUpload(json);//正式的请求Token
            requestKeyandUploadtest(json);//测试用
        }

        @JavascriptInterface
        public void openImages(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openSysImages();
//                    showDialog();
                }
            });

        }

        @JavascriptInterface
        public void updatePersonalInfo(String personData){
            Log.e("personData",">>>>>>>>>>>>"+ personData);
            //调用以下方法前最好先判断个人信息是否更改，若没有，则不更新
            updatePersonInfoToBackstage(personData);
        }

        @JavascriptInterface
        public void signOut() {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(PersonalCenterActivity.this);
            SharedPreferences.Editor editor =  pref.edit();
            editor.putString("USERNAME","");
            editor.putString("PASSWORD","");
            editor.putString("MEM_ID","");
            editor.putString("VERSION","");
            editor.putString("IS_UPDATE","");
            editor.putString("PersonalInfoResult1", "");
//            editor.putString("PersonalInfoResult2", "");
//            editor.putString("PersonalInfoResult3", "");
            editor.commit();
            SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1", "");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //发送广播提醒更新侧边栏头像和用户名信息，，延迟1s发送广播，确保主页面已经显示出来了
                    Intent i = new Intent("Update Username And User Head Image");
                    sendBroadcast(i);
                    Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent!");
                }
            }, 1000);

            //向后台服务器发送退出登录请求
            postSignOut();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PersonalCenterActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

        /**
         * 邀请码关注班级
         */
        @JavascriptInterface
        public void giveYouInvite(String inviteValue){
            if (TextUtils.isEmpty(inviteValue)) {
                ToastAlone.showShortToast("请输入邀请码");
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("mem_id", mem_id);
            map.put("yao", inviteValue);
            ZXUtils.Post(Urls.URL_APP_YAO_GUAN_ZHU, map, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        int code = result.getInt("code");
                        String data = result.getString("data");
                        if (code == 1000) {
                            webView.loadUrl("javascript:isInvite('"+ true +"', '"+data+"')");
                        }
                        ToastAlone.showShortToast(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e("onError: ex = " + ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                }
            });
        }

    }

    private void postSignOut(){
        HashMap<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Application/logout/";
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("PersonalCenterActivity", "postSignOut>>>>>>>onSuccess>>>>>" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("PersonalCenterActivity", "postSignOut>>>>>>>onError>>>>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //调用系统图库相册更改头像
    private void openSysImages(){
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;

            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    if (head != null) {
                        Log.e("onActivityResult3",">>>>>>>>>>>done!");

                        savePicToSDcard(head);// 保存在SD卡中
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                        final String username = pref.getString("USERNAME", "");
                        String fileName = ROOT_PATH + "/anki"+ username +"_"+"ankichina.net.png";// 图片路径和名字
                        String picpath ="file://"+ fileName;
                        webView.loadUrl("javascript:changeHeadImage('"+picpath+"')");// 在网页上显示出来
                        /**
                         * 上传服务器代码
                         */
                        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
                        ClientConfiguration conf = new ClientConfiguration();
                        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                        OSSLog.enableLog();
                        final OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String time=sdf.format(date);//获取当前日期
                        String myUploadObject = "Faces/"+ time+ "/anki"+ username +"_"+"ankichina.net.png";  //云服务上的路径和文件名
//                        String myUploadObject = "Faces/"+ "head.png";  //云服务上的路径和文件名
                        String myUploadFilePath = ROOT_PATH + "/anki"+ username +"_"+"ankichina.net.png";  //本地的路径和文件名
                        asyncPutObjectFromLocalFile(oss, testBucket, myUploadObject, myUploadFilePath);

                        //POST头像的OSS地址给后台
                        String ossPath = "http://anki.oss-cn-hangzhou.aliyuncs.com/"+ myUploadObject;
                        postFaceInfo(ossPath);
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    };

    //POST头像的OSS地址给后台
    private void postFaceInfo(String faceOSSPath){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String mem_id = pref.getString("MEM_ID", " ");
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("face", faceOSSPath);

//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/updFace/";
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/updFace/";
        com.ankireader.util.ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("postFaceInfo", "result>>>>>>>>>onSuccess>>>"+ result);


            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("postFaceInfo","onError>>>>>>>>>onError>>>" + ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void savePicToSDcard(Bitmap bitmap ){
        FileOutputStream b = null;
        File file = new File(ROOT_PATH);
        file.mkdirs();// 创建文件夹
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(date);//获取当前日期
        Log.e("time",">>>>>>>>>>>>>"+ time);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = pref.getString("USERNAME", "");
        String fileName = ROOT_PATH + "/anki"+ username +"_"+"ankichina.net.png";// 图片路径和名字
        Log.e("fileName",">>>>>>>>>>>>>"+ fileName);
//        String fileName = ROOT_PATH + "/head.png";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    //暂时不用这个自定义的Dialog
//    private void showDialog(){
//        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
//        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
//        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        Window window = dialog.getWindow();
//        // 设置显示动画
//        window.setWindowAnimations(R.style.main_menu_animstyle);
//        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;
//        wl.y = getWindowManager().getDefaultDisplay().getHeight();
//        // 以下这两句是为了保证按钮可以水平满屏
//        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//
//        // 设置显示位置
//        dialog.onWindowAttributesChanged(wl);
//        // 设置点击外围解散
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//
//        Button btn_picture = (Button) window.findViewById(R.id.btn_picture);
//        Button btn_photo = (Button) window.findViewById(R.id.btn_photo);
//        Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
//
//        //图库相册
//        btn_picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent1, 1);
//                dialog.dismiss();
//            }
//        });
//        //相机
//        btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
//                startActivityForResult(intent2, 2);// 采用ForResult打开
//                dialog.dismiss();
//            }
//        });
//        //取消
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//    }

    //个人信息修改后向后台POST更新后的信息
    private void updatePersonInfoToBackstage(String data){
        String changedhoney = null;
        String changedgender = null;
        String changedqq = null;
        String changedwechat = null;
        String changedtele = null;
        String changedemail = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            changedhoney = jsonObject.getString("surname");
            changedgender = jsonObject.getString("gender");
            changedqq = jsonObject.getString("qq");
//            changedwechat = jsonObject.getString("wechat");
            changedtele = jsonObject.getString("telephone");
            changedemail = jsonObject.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String mem_id = pref.getString("MEM_ID", " ");
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("honey", changedhoney);
        map.put("gender", changedgender);
        map.put("qq", changedqq);
//        map.put("wechat", changedwechat);
        map.put("tele", changedtele);
        map.put("email", changedemail);

//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/updMyMsg/";
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/updMyMsg/";
        com.ankireader.util.ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("updatePersonInfo", "result>>>>>>>>>onSuccess>>>"+ result);


            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("updatePersonInfo","onError>>>>>>>>>onError>>>" + ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });

    }


    // 运行前需要配置以下字段为有效的值
    private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String accessKeyId = "8zpEuzFFgLw2ao9I";
    private static final String accessKeySecret = "3O6AgBvfyaS6Zlc1BpPY5nWs4cdcfH";
//    private static final String uploadFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/AnkiReader/dirBooks/ian.txt";//本地上传文件的路径

    private static final String testBucket = "anki";//云服务上的Bucket名
//    private static final String uploadObject = "test/myian.txt";//云服务上的路径和文件名
    //测试专用方法
    private void requestKeyandUploadtest(final String datafromhtml){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        final OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("requestKeyandUploadtest", "datafromhtml>>>>>"+ datafromhtml);

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String time = sdf.format(date);//获取当前日期
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final String username = pref.getString("USERNAME", "");
                    String defaultemail = "anki"+ username +"@"+"ankichina.net";// 默认格式的邮箱

                    //解析网页返回数据，传给后台
                    JSONObject jsonObjectOriginal = new JSONObject(datafromhtml); //网页数据不用解码的方法
                    String m_cat_id = jsonObjectOriginal.getString("cat_id");
                    JSONArray extend_cat_id = jsonObjectOriginal.getJSONArray("extend_cat_id");
                    String m_extend_cat_id = extend_cat_id.toString();
                    String m_goods_desc = jsonObjectOriginal.getString("goods_desc");
                    JSONArray jsonArrayOriginal = jsonObjectOriginal.getJSONArray("count");
                    JSONArray count = new JSONArray();
                    for(int i = 0; i < jsonArrayOriginal.length(); i++){
                        String jo = jsonArrayOriginal.getString(i);

                        String osspath = jo.replace("file://"+ROOT_PATH+"/", "http://anki.oss-cn-hangzhou.aliyuncs.com/"+"Pictures/"+ time +"/" +defaultemail +"_");

                        count.put(osspath);
//                        Log.e("requestKeyandUploadtest", "osspath>>>>>"+ osspath);
                    }
                    String mcount = count.toString();
                    Log.e("requestKeyandUploadtest", "count>>>>>"+ count);
                    Log.e("requestKeyandUploadtest", "mcount>>>>>"+ mcount);




                    //网页传回数据解码
                    String data = URLDecoder.decode(datafromhtml, "UTF-8");
                    JSONObject jsonObject = new JSONObject(data);
                    Log.e("requestKeyandUploadtest", "data>>>>>"+ data);
//                    JSONObject jsonObject = new JSONObject(datafromhtml); //网页数据不用解码的方法
                    JSONArray jsonArray = jsonObject.getJSONArray("count");
                    String m_goods_name = null;
                    for(int i = 0; i < jsonArray.length(); i++){
                        String jo = jsonArray.getString(i);

                        String filepath = jo.replace("file://", "");
                        String filename = filepath.replace(ROOT_PATH+"/", "");

                        String myUploadObject = "Pictures/"+ time +"/"+ defaultemail +"_"+filename;  //云服务上的路径和文件名
                        String myUploadFilePath = filepath;  //本地上传文件的路径
                        Log.e("requestKeyandUploadtest", "myUploadObject>>>>>"+ myUploadObject);
                        Log.e("requestKeyandUploadtest", "myUploadFilePath>>>>>"+ myUploadFilePath);

                        asyncPutObjectFromLocalFile(oss, testBucket, myUploadObject, myUploadFilePath);

                        //从返回的文件路径中提取goods_id
                        //从返回的文件路径中提取goods_name
                        String name = filename.replace(".png","");
                        int index = name.lastIndexOf("_");
                        m_goods_name = name.substring(0, index-1);

                    }

                    //更新UI，弹出Toast
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);

                    //向服务器POST信息
                    Log.e("m_goods_name",">>>>>>>>>>>>>"+ m_goods_name);
                    String m_goods_id = goodsidmap.get(m_goods_name);
                    Log.e("m_goods_id",">>>>>>>>>>>>>"+ m_goods_id);
                    postShareInfo(m_cat_id, m_extend_cat_id, m_goods_desc, mcount, m_goods_id);

                    //此处最好让线程休眠片刻等待上传成功后执行以下代码
//                    Thread.sleep(300);
//                    if(OSSPuttimes == 6){
//                        Log.e("OSSPuttimes",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                        //更新UI，弹出Toast
//                        Message message = Message.obtain();
//                        message.what = 1;
//                        handler.sendMessage(message);
//
//                        //向服务器POST信息
//                        postShareInfo(m_cat_id, m_extend_cat_id, m_goods_desc, mcount, m_goods_id);
//                        OSSPuttimes = 0;
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("requestKeyandUploadtest", "JSONException in comfirmShare : "+ e);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //正式的请求Token
    //向后台请求Token
    String mAccessKeyId = null;
    String mAccessKeySecret = null;
    String mEndpoint = null;
    String mUploadObject = null;
    private void requestKeyandUpload(final String datafromhtml){
        Map<String, String> map  = new HashMap<>();
        map.put("cat_id", "1");
        map.put("extend_cat_id", "2,11");
        map.put("goods_desc", "aa");
        map.put("pics", "bb");

//        String url = "http://192.168.1.13/ankioss/index.php/App/share";
        String url = AnkiDroidApp.BASE_DOMAIN + "App/share";
        com.ankireader.util.ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("requestKey", "onSuccess>>>>>>>>>" + result);
                //从返回值中获取accessKeyId,accessKeySecret,endpoint,uploadObject
                mAccessKeyId = "";
                mAccessKeySecret = "";
                mEndpoint = "";
                mUploadObject = "";

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("requestKey", "onError>>>>>>>>>" + ex);
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {
                OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(mAccessKeyId, mAccessKeySecret);
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                OSSLog.enableLog();
                final OSS oss = new OSSClient(getApplicationContext(), mEndpoint, credentialProvider, conf);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(datafromhtml);
                            for(int i = 0; i < jsonArray.length(); i++){
                                String uploadObject = "test/"+ jsonArray.getJSONArray(i).get(0);
                                String uploadFilePath = (String) jsonArray.getJSONArray(i).get(1);
                                asyncPutObjectFromLocalFile(oss, testBucket, uploadObject, uploadFilePath);
                            }

                            //此处最好让线程休眠片刻等待上传成功后执行以下代码
                            if(OSSPuttimes == 6){
                                //更新UI，弹出Toast
                                Message message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);

                                //向服务器POST信息
//                                postShareInfo();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("comfirmShare", "JSONException in comfirmShare : "+ e);
                        }
                    }
                }).start();

            }
        });
    }

    //给后台POST分享完的图片OSS地址
    private void postShareInfo(String m_cat_id, String m_extend_cat_id, String m_goods_desc, String mcount,String m_goods_id){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String mem_id = pref.getString("MEM_ID", " ");
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", m_cat_id);
        map.put("mem_id", mem_id);
        m_extend_cat_id = m_extend_cat_id.replace("[","").replace("]","");//把[2,11]改成2,11的类型
        Log.e("postShareInfo","m_extend_cat_id>>>>>>>>"+m_extend_cat_id);
        map.put("extend_cat_id", m_extend_cat_id);
//        map.put("extend_cat_id", "[2,11]");//临时用
        map.put("goods_id", m_goods_id);
        map.put("goods_desc", m_goods_desc);
        map.put("pics", mcount);
//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/share/";
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/share/";
        com.ankireader.util.ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("postShareInfo", "result>>>>>>>>>onSuccess>>>"+ result);

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("postShareInfo","onError>>>>>>>>>onError>>>" + ex.toString());

            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                Toast.makeText(PersonalCenterActivity.this, "分享成功！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    int OSSPuttimes = 0;
    // 从本地文件上传，使用非阻塞的异步接口
    public void asyncPutObjectFromLocalFile(OSS oss, String testBucket, String testObject, String uploadFilePath) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "UploadSuccess");

                Log.e("ETag", result.getETag());
                Log.e("RequestId", result.getRequestId());

                Log.e("asyncPutObject", ">>>>>>>"+ result);
                Log.e("asyncPutObject", ">>>>>>>"+ result.getServerCallbackReturnBody());
                OSSPuttimes++;
                Log.e("asyncPutObject", "OSSPuttimes>>>>>>>"+ OSSPuttimes);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }





}
