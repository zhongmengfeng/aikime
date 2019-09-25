package com.ichi2yiji.anki.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.AnkiDroidApp;
import com.ichi2yiji.anki.activity.ek.EKLoginActivity;
import com.ichi2yiji.anki.activity.ek.EKWalletActivity;
import com.ichi2yiji.anki.bean.UserInfoBean;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.utils.SPUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.HashMap;

import static com.litesuits.common.utils.HandlerUtil.runOnUiThread;

/**
 * Created by ekar01 on 2017/7/4.
 */

public class EKFragment4 extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddDeckfragment";
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();;
    private ImageView iv_ekmain_personal_setting;
    private ImageView iv_ekmain_personal_icon;
    private TextView tv_ekmain_personal_name;
    private TextView tv_ekmain_personal_level;
    private TextView tv_ekmain_personal_wallet;
    private TextView tv_ekmain_personal_focus;
    private TextView tv_ekmain_personal_fans;
    private TextView tv_ekmain_personal_clockin;
    private TextView tv_ekmain_personal_points;
    private TextView tv_ekmain_personal_myclass;
    private TextView tv_ekmain_personal_myarticle;
    private TextView tv_ekmain_personal_mytest;
    private EditText et_ekmain_personal_invitecode;
    private RelativeLayout rl_ekmain_personal_classshare;
    private TextView tv_ekmain_personal_classshare;
    private TextView tv_ekmain_personal_exitlogin;
    private String mem_id;
    private String personal_data_1;
    private UserInfoBean userInfoBean;
    private String username;
    private Bitmap bitmap;
    private Integer member_level;
    private String jifen;

    public static EKFragment4 newInstance(String mFavoriteFragment) {
        EKFragment4 ekFragment4 = new EKFragment4();
        return ekFragment4;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        personal_data_1 = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1", "");

        if (!personal_data_1.isEmpty()) {
            userInfoBean = new Gson().fromJson(personal_data_1, UserInfoBean.class);
            Log.e("personal_data_1", userInfoBean.toString());
            try {

                if (Integer.valueOf(userInfoBean.getCode()) == 1000) {
                    UserInfoBean.DataBean.MemBean mem = userInfoBean.getData().getMem();
                    username = mem.getHoneyname();
                    member_level = Integer.valueOf(mem.getMember_level());
                    jifen = mem.getJifen();
//                    mem.get
                    String head_img_url = mem.getFace();
                    Log.e("personal_data_1", "正确" + userInfoBean.getData().getMem().toString());

                    DownloadHeadImage(head_img_url);
                } else {
                    if(iv_ekmain_personal_icon != null){
                        iv_ekmain_personal_icon.setImageResource(R.drawable.headportrait);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //从OSS服务器下载用户头像图片
    private void DownloadHeadImage(String url) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.e("DownloadHeadImage: 用户头像URL为:" + url);
            return;
        }
        String filename = url.substring(url.lastIndexOf("/") + 1);
        final String filepath = ROOT_PATH + "/Chaojiyiji/picture/" + filename;
        ZXUtils.DownLoadFile(url, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onSuccess");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onError>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onFinished");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(filepath, options);
                if(iv_ekmain_personal_icon == null){
                    return;
                }
                if (bitmap != null) {
                    iv_ekmain_personal_icon.setImageBitmap(bitmap);
                } else {
                    iv_ekmain_personal_icon.setImageResource(R.drawable.headportrait);
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ek_fragment_personal, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        iv_ekmain_personal_setting = (ImageView) view.findViewById(R.id.iv_ekmain_personal_setting);
        iv_ekmain_personal_icon = (ImageView) view.findViewById(R.id.iv_ekmain_personal_icon);
        tv_ekmain_personal_name = (TextView) view.findViewById(R.id.tv_ekmain_personal_name);
        tv_ekmain_personal_level = (TextView) view.findViewById(R.id.tv_ekmain_personal_level);
        tv_ekmain_personal_wallet = (TextView) view.findViewById(R.id.tv_ekmain_personal_wallet);
        tv_ekmain_personal_focus = (TextView) view.findViewById(R.id.tv_ekmain_personal_focus);
        tv_ekmain_personal_fans = (TextView) view.findViewById(R.id.tv_ekmain_personal_fans);
        tv_ekmain_personal_clockin = (TextView) view.findViewById(R.id.tv_ekmain_personal_clockin);
        tv_ekmain_personal_points = (TextView) view.findViewById(R.id.tv_ekmain_personal_points);
        tv_ekmain_personal_myclass = (TextView) view.findViewById(R.id.tv_ekmain_personal_myclass);
        tv_ekmain_personal_myarticle = (TextView) view.findViewById(R.id.tv_ekmain_personal_myarticle);
        tv_ekmain_personal_mytest = (TextView) view.findViewById(R.id.tv_ekmain_personal_mytest);
        et_ekmain_personal_invitecode = (EditText) view.findViewById(R.id.et_ekmain_personal_invitecode);
        rl_ekmain_personal_classshare = (RelativeLayout) view.findViewById(R.id.rl_ekmain_personal_classshare);
        tv_ekmain_personal_classshare = (TextView) view.findViewById(R.id.tv_ekmain_personal_classshare);
        tv_ekmain_personal_exitlogin = (TextView) view.findViewById(R.id.tv_ekmain_personal_exitlogin);

        tv_ekmain_personal_name.setText(username);
        if(member_level != null){
            tv_ekmain_personal_level.setText("Lv." + member_level);
        }
        if(jifen != null){
            tv_ekmain_personal_points.setText("积分" + jifen);
        }

        iv_ekmain_personal_setting.setOnClickListener(this);
        tv_ekmain_personal_wallet.setOnClickListener(this);
        tv_ekmain_personal_focus.setOnClickListener(this);
        tv_ekmain_personal_fans.setOnClickListener(this);
        tv_ekmain_personal_clockin.setOnClickListener(this);
        tv_ekmain_personal_points.setOnClickListener(this);

        tv_ekmain_personal_myclass.setOnClickListener(this);
        tv_ekmain_personal_myarticle.setOnClickListener(this);
        tv_ekmain_personal_mytest.setOnClickListener(this);
        rl_ekmain_personal_classshare.setOnClickListener(this);
        tv_ekmain_personal_exitlogin.setOnClickListener(this);


        mem_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("MEM_ID", "");
    }

    @Override
    public void onClick(View v) {
        final Intent[] intent = {null};
        switch (v.getId()){
            case R.id.iv_ekmain_personal_setting:
//                intent = new Intent(getActivity(), EKSettingActivity.class);
                intent[0] = new Intent(getActivity(), EKLoginActivity.class);
                startActivity(intent[0]);
                break;
            case R.id.tv_ekmain_personal_wallet:
                intent[0] = new Intent(getActivity(), EKWalletActivity.class);
                startActivity(intent[0]);
                break;
            case R.id.tv_ekmain_personal_focus:
//                intent = new Intent(getActivity(), EkFocusActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_fans:
//                intent = new Intent(getActivity(), EkFansActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_clockin:
//                intent = new Intent(getActivity(), EkClockinActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_points:
//                intent = new Intent(getActivity(), EkPointsActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_myclass:
//                intent = new Intent(getActivity(), EKClassActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_myarticle:
//                intent = new Intent(getActivity(), EKArticleActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_mytest:
//                intent = new Intent(getActivity(), EKTestActivity.class);
//                startActivity(intent);
                break;
            case R.id.rl_ekmain_personal_classshare:
//                intent = new Intent(getActivity(), EKPointsActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_ekmain_personal_exitlogin:
                Log.e(TAG, "tv_ekmain_personal_exitlogin: " + "正在退出");
                postSignOut();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(getContext(), EKLoginActivity.class);
                        startActivity(intent[0]);
                        getActivity().overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void postSignOut(){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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

        HashMap<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Application/logout/";
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("PersonalCenterActivity", "" + result);
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

}
