package com.ichi2yiji.anki;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.fragment.AddDeckfragment;
import com.ichi2yiji.anki.fragment.ShakeDeckfragment;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.LogUtils;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.xutils.common.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownLoadDecks11 extends AppCompatActivity {

    private static final String TAG = "DownLoadDecks11";
    private boolean isHaveMyselfClass;
    private Bundle bundle;

    private Fragment mFrag;
    private List<Fragment> frags = new ArrayList<>();
    private FragmentManager fm;
    public SharedDecksBean.DataBean dataBean;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_deck);
        ApplyTranslucency.applyKitKatTranslucency(this);
        bundle = new Bundle();
        progressDialog = new ProgressDialog(DownLoadDecks11.this).builder();
        addOrShake();
    }

    private void addOrShake() {
        HashMap<String, String> map = new HashMap<String, String>();
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        fm = getSupportFragmentManager();

        map.put("mem_id", memId);
        ZXUtils.Post(Urls.URL_APP_PICKER, map, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                SharedDecksBean sharedDecksBean = gson.fromJson(result, SharedDecksBean.class);
                List<SharedDecksBean.DataBean.ClassCreatedByMeBean> classCreatedByMe = sharedDecksBean.getData().getClassCreatedByMe();
                List<SharedDecksBean.DataBean.ProfessionalsBean> professionals = sharedDecksBean.getData().getProfessionals();
                SharedDecksBean.DataBean dataBean = sharedDecksBean.getData();

                DownLoadDecks11.this.dataBean = dataBean;

                if (classCreatedByMe.size() == 0 && professionals.size() == 0) {
                    isHaveMyselfClass = false;
                    showFragment("shakeDeckfragment");
                } else {
                    isHaveMyselfClass = true;
                    showFragment("addDeckfragment");
                }
                LogUtils.showLogCompletion("onSuccess>>>>SharedDecksBean.DataBean: " + dataBean.toString(),3999);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                SharedDecksBean.DataBean dataBean = loadData();
                if(null != dataBean){
                    DownLoadDecks11.this.dataBean = dataBean;

                    List<SharedDecksBean.DataBean.ClassCreatedByMeBean> classCreatedByMe = dataBean.getClassCreatedByMe();
                    List<SharedDecksBean.DataBean.ProfessionalsBean> professionals = dataBean.getProfessionals();
                    if (classCreatedByMe.size() == 0 && professionals.size() == 0) {
                        isHaveMyselfClass = false;
                        showFragment("shakeDeckfragment");
                    } else {
                        isHaveMyselfClass = true;
                        showFragment("addDeckfragment");
                    }
                    Log.e(TAG, "onSuccess>>>>SharedDecksBean.DataBean: " + dataBean.toString());
                }else{
                    progressDialog.showErrorWithStatus();
                    progressDialog.setDownloadTip("网络错误");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void showFragment(String fragmentTag){
        AddDeckfragment addDeckfragment = (AddDeckfragment) fm.findFragmentByTag("addDeckfragment");
        ShakeDeckfragment shakeDeckfragment = (ShakeDeckfragment) fm.findFragmentByTag("shakeDeckfragment");
        FragmentTransaction ft = fm.beginTransaction();

        if (addDeckfragment != null) {
            ft.hide(addDeckfragment);
        }
        if (shakeDeckfragment != null) {
            ft.hide(shakeDeckfragment);
        }

        if("addDeckfragment".equals(fragmentTag)){
            if (addDeckfragment == null) {
                addDeckfragment = new AddDeckfragment();
                ft.add(R.id.fragment, addDeckfragment, fragmentTag);
                addDeckfragment.set(dataBean);
            } else {
                ft.show(addDeckfragment);
            }
        }else if("shakeDeckfragment".equals(fragmentTag)){
            if (shakeDeckfragment == null) {
                shakeDeckfragment = new ShakeDeckfragment();
                ft.add(R.id.fragment, shakeDeckfragment, fragmentTag);
                shakeDeckfragment.set(dataBean,isHaveMyselfClass);
            } else {
                ft.show(shakeDeckfragment);
            }
        }
        ft.commit();
    }

    private void loadFragment(int position) {

        //从集合中获取相对序号的Fragment
        Fragment fragment = frags.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //首先判断mFrag 是否为空，如果不为，先隐藏起来，接着判断从List 获取的Fragment是否已经添加到Transaction中，如果未添加，添加后显示，如果已经添加，直接显示
        if (mFrag != null) {
            fragmentTransaction.hide(mFrag);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment, fragment);

        } else {
            fragmentTransaction.show(fragment);
        }
        //将获取的Fragment 赋值给声明的Fragment 中，提交
        mFrag = fragment;
        fragmentTransaction.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != progressDialog){
            progressDialog.dismiss();
        }
    }

//    public MyKeyDownListener myKeyDownListener;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            //下载过程中出现用户点击返回键,有两种处理
//            //1.是禁止 2.是取消网络下载
//            //现在用第二种
//            myKeyDownListener.onKeyDown(keyCode, event);
//        }
//        return false;
//    }
//
//    public interface MyKeyDownListener{
//        boolean onKeyDown(int keyCode, KeyEvent event);
//    }
//
//    public void setMyKeyDownListener(MyKeyDownListener myKeyDownListener){
//        this.myKeyDownListener = myKeyDownListener;
//    }

    public SharedDecksBean.DataBean loadData() {
        String mem_id = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        //取出数据
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +"Chaojiyiji"+ File.separator + "tempcache" + File.separator + ("EKShareDeckModel" + "_" + mem_id));
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            Log.e("getAttentionJsonData", "loadData>>>>readObject前>>>>>>>>try>>>>>>>>>>>>>" + objectInputStream.toString());
            SharedDecksBean.DataBean data = (SharedDecksBean.DataBean) objectInputStream.readObject();
            Log.e("getAttentionJsonData", "loadData>>>>>>>>>>>>try>>>>>>>>>>>>>" + data.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getAttentionJsonData", "loadData>>>>>>>catch>>>>>>>>>>>>>" + e.getMessage());
        } finally {
            if (objectInputStream!=null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addOrShake();
    }
}
