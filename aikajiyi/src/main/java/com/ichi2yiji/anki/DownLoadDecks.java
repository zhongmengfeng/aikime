package com.ichi2yiji.anki;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.fragment.AddDeckfragment;
import com.ichi2yiji.anki.fragment.ShakeDeckfragment;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownLoadDecks extends AppCompatActivity {


    private AddDeckfragment addDeckfragment;
    private ShakeDeckfragment shakeDeckfragment;
    private SVProgressHUD svProgressHUD;
    private String decode;
    private int themeName;
    private boolean isHaveMyselfClass;
    private Bundle bundle;

    private Fragment mFrag;
    private List<Fragment> frags = new ArrayList<>();
    private FragmentManager sfm;
    public SharedDecksBean.DataBean dataBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_deck);
        ApplyTranslucency.applyKitKatTranslucency(this);
        svProgressHUD = new SVProgressHUD(this);
        bundle = new Bundle();
        sfm = getSupportFragmentManager();
        addOrShake();
    }


    public void showInputProgressCircle(String title, String action) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (svProgressHUD != null) {
            svProgressHUD.showWithStatus(title + " " + action);
        }
    }

    /**
     * 关闭进度条
     */
    public void disappearProgress() {
        if (svProgressHUD != null && svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }

    private void addOrShake() {
        HashMap<String, String> map = new HashMap<String, String>();
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        addDeckfragment = new AddDeckfragment();
        shakeDeckfragment = new ShakeDeckfragment();
        frags.add(addDeckfragment);
        frags.add(shakeDeckfragment);
        map.put("mem_id", memId);
        ZXUtils.Post(Urls.URL_APP_PICKER, map, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SharedDecksBean sharedDecksBean = gson.fromJson(result, SharedDecksBean.class);
                List<SharedDecksBean.DataBean.ClassCreatedByMeBean> classCreatedByMe = sharedDecksBean.getData().getClassCreatedByMe();
                List<SharedDecksBean.DataBean.ProfessionalsBean> professionals = sharedDecksBean.getData().getProfessionals();
                SharedDecksBean.DataBean dataBean = sharedDecksBean.getData();
                DownLoadDecks.this.dataBean = dataBean;

//                bundle.putString("result", result);
                if (classCreatedByMe.size() == 0 && professionals.size() == 0) {
                    isHaveMyselfClass = false;
                    switchToShareDeckFragment();
                } else {
                    isHaveMyselfClass = true;
                    switchToAddClassFragment();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void switchToAddClassFragment() {
//        bundle.putBoolean("isHaveMyselfClass", isHaveMyselfClass);
//        addDeckfragment.setArguments(bundle);
        addDeckfragment.set(dataBean);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, addDeckfragment).addToBackStack(null).commit();
    }

    public void switchToShareDeckFragment() {
//        bundle.putBoolean("isHaveMyselfClass", isHaveMyselfClass);
//        shakeDeckfragment.setArguments(bundle);
        shakeDeckfragment.set(dataBean,isHaveMyselfClass);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, shakeDeckfragment).addToBackStack(null).commit();
    }

    private void loadFragment(int position) {

        //从集合中获取相对序号的Fragment
        Fragment fragment = frags.get(position);
        FragmentTransaction fragmentTransaction = sfm.beginTransaction();
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
}
