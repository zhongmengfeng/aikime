package com.ichi2yiji.anki.treeview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.FilteDeckFile;
import com.ichi2yiji.anki.view.SlideView;
import com.ichi2yiji.libanki.Collection;

import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    private Context context;
    private SlideView mLastSlideViewWithStatusOn;
    private Collection mCol;

    private Runnable updataDeckListRun;

    ////////////////////////////////////dx add
    List<String> names = new ArrayList<>();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    ////////////////////////////////////dx add
    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
    }

//    @Override
//    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder = null;
//        SlideView slideView = (SlideView) convertView;
//        if (slideView == null) {
//            View itemView = mInflater.inflate(R.layout.deckpicker_list_item, parent, false);
//
//            slideView = new SlideView(context);
//            slideView.setContentView(itemView);
//
//            slideView.setOnSlideListener(new SlideView.OnSlideListener() {
//                @Override
//                public void onSlide(View view, int status) {
//                    if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
//                        mLastSlideViewWithStatusOn.shrink();
//                    }
//
//                    if (status == SLIDE_STATUS_ON) {
//                        mLastSlideViewWithStatusOn = (SlideView) view;
//                    }
//                }
//            });
//
//            viewHolder = new ViewHolder(slideView);
//            slideView.setTag(viewHolder);
//
//        } else {
//            viewHolder = (ViewHolder) slideView.getTag();
//        }
//        ///////////////////// dx  add
//        //判断item的灰色横线是否显示
//        if (TreeHelper.isSetNodeBlanck(node)) {
//            viewHolder.viewBlanck.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.viewBlanck.setVisibility(View.INVISIBLE);
//        }
//        ///////////////////// dx  add
//        if (node.getIcon() == -1) {
//            viewHolder.icon.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.icon.setVisibility(View.VISIBLE);
//            viewHolder.icon.setImageResource(node.getIcon());
//        }
//        ///////////////////// dx  add
//        //判断是否是新筛选的牌组
//        FilteDeckFile filteDeckFile=new FilteDeckFile(context);
//        String str = null;
//        if(null != filteDeckFile.read()){
//            str = filteDeckFile.read();
//        }
//        String a[] = str.split(",");
//        for(int i = 0;i<a.length;i++){
//            names.add(a[i]);
//        }
//        if (names != null && names.contains(node.getName())) {
//            viewHolder.label.setText(node.getName());//牌组名称
//            viewHolder.label.setTextColor(context.getResources().getColor(R.color.material_top_blue));
//        } else {
//            viewHolder.label.setText(node.getName());//牌组名称
//            viewHolder.label.setTextColor(Color.BLACK);
//        }
//        ///////////////////// dx  add
//        int lrnCount = Integer.parseInt(node.getLrnCount());
//        int revCount = Integer.parseInt(node.getRevCount());
//        int total = lrnCount + revCount;
//        String total_String = String.valueOf(total);
//        viewHolder.revAndlrnCount.setText(total_String);//待复习数(lrnCount + revCount)
//        viewHolder.newCount.setText(node.getNewCount());//新卡片数(newCount)
//
//        //删除按钮的接口回调
//        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnDeleteListener.delete(node, position);
//            }
//        });
//
//        //设置子列表的缩进
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(24, 24);
//        params.setMargins((node.getLevel() + 1) * 30, 20, 0, 0);
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
//        viewHolder.icon.setLayoutParams(params);
//
//        //解决item点击事件冲突
//        node.setSlideView(slideView);
//
//        return slideView;
//    }
//
//
//    private final class ViewHolder {
//        ImageView icon;
//        TextView label;
//        TextView mDelete;
//        TextView revAndlrnCount;
//        TextView newCount;
//        ////////////dx  addstart
//        View viewBlanck;
//        RelativeLayout layout;
//
//        ////////////dx  end
//        ViewHolder(View view) {
//            icon = (ImageView) view.findViewById(R.id.id_treenode_icon);
//            label = (TextView) view.findViewById(R.id.id_treenode_label);
//            mDelete = (TextView) view.findViewById(R.id.delete);
//            revAndlrnCount = (TextView) view.findViewById(R.id.num_1);
//            newCount = (TextView) view.findViewById(R.id.num_2);
//            ////////dx  start
//            viewBlanck = view.findViewById(R.id.divider);
//            layout = (RelativeLayout) view.findViewById(R.id.id_treenode_layout);
//            /////////dx   end
//        }
//
//    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.deckpicker_new_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch(TreeHelper.nodeIsRootOrChildOrLeaf(node)){
            case 0:
                viewHolder.layout.setBackgroundResource(R.drawable.deckpick_list_bg_shape_0);
                viewHolder.viewBlanck.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewHolder.layout.setBackgroundResource(R.drawable.deckpick_list_bg_shape_1);
                viewHolder.viewBlanck.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.layout.setBackgroundResource(R.drawable.deckpick_list_bg_shape_2);
                viewHolder.viewBlanck.setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.layout.setBackgroundResource(R.drawable.deckpick_list_bg_shape_3);
                viewHolder.viewBlanck.setVisibility(View.GONE);
                break;
        }

//        SlideView slideView = (SlideView) convertView;
//        if (slideView == null) {
//            View itemView = mInflater.inflate(R.layout.deckpicker_new_list_item, parent, false);
//
//            slideView = new SlideView(context);
//            slideView.setContentView(itemView);

//            slideView.setOnSlideListener(new SlideView.OnSlideListener() {
//                @Override
//                public void onSlide(View view, int status) {
//                    if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
//                        mLastSlideViewWithStatusOn.shrink();
//                    }
//
//                    if (status == SLIDE_STATUS_ON) {
//                        mLastSlideViewWithStatusOn = (SlideView) view;
//                    }
//                }
//            });
//
//            viewHolder = new ViewHolder(slideView);
//            slideView.setTag(viewHolder);
//
//        } else {
//            viewHolder = (ViewHolder) slideView.getTag();
//        }
//        ///////////////////// dx  add
//        //判断item的灰色横线是否显示
//        if (TreeHelper.isSetNodeBlanck(node)) {
//            viewHolder.viewBlanck.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.viewBlanck.setVisibility(View.INVISIBLE);
//        }
//        ///////////////////// dx  add
        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
            viewHolder.rlTreenodeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrCollapse(position);
                }
            });
        }
        ///////////////////// dx  add
        //判断是否是新筛选的牌组
        FilteDeckFile filteDeckFile=new FilteDeckFile(context);
        String str = null;
        if(null != filteDeckFile.read()){
            str = filteDeckFile.read();
        }
        String a[] = str != null ? str.split(",") : new String[0];
        for(int i = 0;i<a.length;i++){
            names.add(a[i]);
        }
        if (names != null && names.contains(node.getName())) {
            // 牌组名称
            viewHolder.label.setText(node.getName());
            viewHolder.label.setTextColor(context.getResources().getColor(R.color.material_top_blue));
        } else {
            // 牌组名称
            viewHolder.label.setText(node.getName());
            viewHolder.label.setTextColor(Color.BLACK);
        }
        ///////////////////// dx  add
        int lrnCount = NumberUtils.toInt(node.getLrnCount());
        int revCount = NumberUtils.toInt(node.getRevCount());

        final int total = lrnCount + revCount;
        String total_String = String.valueOf(total);
        // 待复习数(lrnCount + revCount)
        viewHolder.revAndlrnCount.setText(total_String);
        // 新卡片数(newCount)
        final String newCount = node.getNewCount();
        viewHolder.newCount.setText(newCount);

        viewHolder.rlTreenodeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "预留事件", Toast.LENGTH_SHORT).show();
            }
        });
//        //删除按钮的接口回调
//        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnDeleteListener.delete(node, position);
//            }
//        });

        //设置子列表的缩进
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(24, 24);
        params.setMargins((node.getLevel() + 1) * 30, 20, 0, 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        viewHolder.icon.setLayoutParams(params);

//        //解决item点击事件冲突
//        node.setSlideView(slideView);

        if(getCount()-1 == position && mOnLoadComplete != null){
            mOnLoadComplete.loadComplete();
        }
        return convertView;
    }




    public static final class ViewHolder {
        RelativeLayout rlTreenodeNumber;
        RelativeLayout rlTreenodeIcon;
        LinearLayout idTreenodeNumber;
        ImageView icon;
        TextView label;
        TextView mDelete;
        TextView revAndlrnCount;
        TextView newCount;
        ////////////dx  addstart
        View viewBlanck;
        RelativeLayout layout;

        ////////////dx  end
        ViewHolder(View view) {
            rlTreenodeIcon = (RelativeLayout) view.findViewById(R.id.rl_treenode_icon);
            rlTreenodeNumber = (RelativeLayout) view.findViewById(R.id.rl_treenode_number);
            idTreenodeNumber = (LinearLayout) view.findViewById(R.id.id_treenode_number);
            icon = (ImageView) view.findViewById(R.id.id_treenode_icon);
            label = (TextView) view.findViewById(R.id.id_treenode_label);
            mDelete = (TextView) view.findViewById(R.id.delete);
            revAndlrnCount = (TextView) view.findViewById(R.id.num_1);
            newCount = (TextView) view.findViewById(R.id.num_2);
            ////////dx  start
            viewBlanck = view.findViewById(R.id.divider_new);
            layout = (RelativeLayout) view.findViewById(R.id.id_treenode_layout);
            /////////dx   end
        }

    }

    /**
     * 删除的回调接口
     */
    //创建接口
    public interface OnDeleteListener {
        void delete(Node node, int position);
    }

    //声明接口对象
    private OnDeleteListener mOnDeleteListener;

    //设置监听器,实例化接口
    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    public interface OnLoadComplete{
        void loadComplete();
    }

    private OnLoadComplete mOnLoadComplete;

    public void setOnLoadComplete(OnLoadComplete onLoadComplete){
        this.mOnLoadComplete = onLoadComplete;
    }

    @Override
    public List<Node> getNodes() {
        return super.getNodes();
    }



    public void setUpdataDeckListRun(Runnable updataDeckListRun) {
        this.updataDeckListRun = updataDeckListRun;
    }

}
