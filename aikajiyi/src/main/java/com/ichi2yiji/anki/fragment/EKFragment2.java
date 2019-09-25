package com.ichi2yiji.anki.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.activity.ek.EKExpertClassActivity;
import com.ichi2yiji.anki.activity.ek.EKFineClassActivity;
import com.ichi2yiji.anki.activity.ek.EKSearchActivity;
import com.ichi2yiji.anki.adapter.ek.EKExpertGridAdpter;
import com.ichi2yiji.anki.view.ek.MyGridView;

/**
 * Created by ekar01 on 2017/7/4.
 */

public class EKFragment2 extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddDeckfragment";
    private HorizontalScrollView hsv_ek_fragment_find_scrollview;
    private EditText et_ek_fragment_find_edittext;
    private MyGridView mgv_ek_fragment_find_gridview;
    private EKExpertGridAdpter mEKExpertGridAdpter;
    private LinearLayout ll_ek_fragment_find_fineclass;
    private LinearLayout ll_ek_fragment_find_expert;

    public static EKFragment2 newInstance(String mBookFragment) {
        EKFragment2 ekFragment2 = new EKFragment2();
        return ekFragment2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ek_fragment_find, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        et_ek_fragment_find_edittext = (EditText) view.findViewById(R.id.et_ek_fragment_find_edittext);
        hsv_ek_fragment_find_scrollview = (HorizontalScrollView) view.findViewById(R.id.hsv_ek_fragment_find_scrollview);
        mgv_ek_fragment_find_gridview = (MyGridView) view.findViewById(R.id.mgv_ek_fragment_find_gridview);
        ll_ek_fragment_find_expert = (LinearLayout) view.findViewById(R.id.ll_ek_fragment_find_expert);
        ll_ek_fragment_find_fineclass = (LinearLayout) view.findViewById(R.id.ll_ek_fragment_find_fineclass);

        ll_ek_fragment_find_expert.setOnClickListener(this);
        ll_ek_fragment_find_fineclass.setOnClickListener(this);

        //达人
        mEKExpertGridAdpter = new EKExpertGridAdpter(getActivity());
        mgv_ek_fragment_find_gridview.setAdapter(mEKExpertGridAdpter);

        mgv_ek_fragment_find_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击了达人条目" + position, Toast.LENGTH_SHORT).show();
//                ArticleBean article = (ArticleBean) parent.getAdapter().getItem(position);
//                if(null != article){
//                    intent = new Intent(getActivity(), CareHairDetailActivity.class);
//                    intent.putExtra("id", article.getId());
//                    intent.putExtra("name", article.getName());
//                    mActivity.startActivity(intent);
//                }

                Intent intent = new Intent(getActivity(), EKExpertClassActivity.class);
                getActivity().startActivity(intent);
            }
        });

//        img_add_return = (ImageView) view.findViewById(R.id.img_add_return);
//        rl_add_deck_back = (RelativeLayout) view.findViewById(R.id.rl_add_deck_back);
//        mListview = (BaseListView) view.findViewById(R.id.addRecyclerview);
//        rl_wrong = (RelativeLayout) view.findViewById(R.id.add_rl_wrong);
//        add_rl = (RelativeLayout) view.findViewById(R.id.add_rl);
//        tv_more.setOnClickListener(this);
//        rl_add_deck_back.setOnClickListener(this);
        et_ek_fragment_find_edittext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.tv_more:
                break;
            case R.id.et_ek_fragment_find_edittext:
                intent = new Intent(getActivity(), EKSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_add_deck_back:
                break;
            case R.id.ll_ek_fragment_find_expert:
                intent = new Intent(getActivity(), EKExpertClassActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_ek_fragment_find_fineclass:
                intent = new Intent(getActivity(), EKFineClassActivity.class);
                startActivity(intent);
                break;

        }
    }

    public static Bitmap Bytes2Bimap(byte[] b) {

        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }

    }
}
