package com.ichi2yiji.anki.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.ankireader.CopyRawtodata;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DeckTestReal;
import com.ichi2yiji.anki.adapter.holder.FooterHolder;
import com.ichi2yiji.anki.adapter.holder.HeaderHolder;
import com.ichi2yiji.anki.adapter.holder.TestHolder;
import com.ichi2yiji.anki.bean.TestGroupBean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 模考组Adapter
 * 该功能实现参考：http://blog.csdn.net/wzlyd1/article/details/52292548
 * Created by Administrator on 2017/4/1.
 */
public class TestsGroupAdapter extends SectionedRecyclerViewAdapter<HeaderHolder,TestHolder,FooterHolder> {


    private SparseBooleanArray mBooleanMap;
    private List<TestGroupBean> dirTestList;
    private Context mContext;

    public TestsGroupAdapter(Context context, @NonNull List<TestGroupBean> dirTestsList) {
        this.mContext = context;
        this.dirTestList = dirTestsList;
        mBooleanMap = new SparseBooleanArray();
    }

    @Override
    protected int getSectionCount() {
        return dirTestList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = dirTestList.get(section).getTestList().size();
        if(count > 3 && !mBooleanMap.get(section)){
            return 3;
        }
        return count;
    }

    /**
     * 是否显示footer   true：显示 false：不显示
     * @param section
     * @return
     */
    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_tests_groups_header, null);
        return new HeaderHolder(inflate);
    }

    @Override
    protected FooterHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_tests_groups_footer, null);
        return new FooterHolder(inflate);
    }

    @Override
    protected TestHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(parent.getContext(), R.layout.item_tests_groups_test, null);
        return new TestHolder(inflate);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
        TestGroupBean testGroupBean = dirTestList.get(section);
        holder.testCourseName.setText(testGroupBean.getGroupName());
        if (testGroupBean.getTestList().size() > 3) {
            holder.more.setSelected(true);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isShowMore = !mBooleanMap.get(section);
                    String text = isShowMore ? "收起" : "更多";
                    holder.more.setText(text);
                    mBooleanMap.put(section,isShowMore);
                    notifyDataSetChanged();
                }
            });
            holder.more.setText(mBooleanMap.get(section)?"收起":"更多");

        }else{
            holder.more.setSelected(false);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(FooterHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(TestHolder holder, int section, int position) {
        final TestGroupBean.TestBean testBean = dirTestList.get(section).getTestList().get(position);
        holder.text1.setText(testBean.getTypeName());
        holder.text2.setText(testBean.getTestName());
        holder.rlTestsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeckTestReal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                String json = getJsonByFilePath(testBean.getFilePath());
                intent.putExtra("testData",json);
                intent.putExtra("testFullName",testBean.getFullName());
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }
        });
    }

    /**
     * 从文件获取json
     * @param filePath
     * @return
     */
    @NonNull
    private String getJsonByFilePath(String filePath) {
        String json = "";
        try {
            // FileInputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirTests/" + "中医助理医师考试模拟试题.mtest");
            FileInputStream inputStream = new FileInputStream(filePath);
            json = CopyRawtodata.readTextFromRaw(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public void setData(List<TestGroupBean> dirTestsList) {
        if (dirTestsList == null) {
            dirTestsList = new ArrayList<>();
        }
        this.dirTestList.clear();
        this.dirTestList.addAll(dirTestsList);
        notifyDataSetChanged();
    }

}
