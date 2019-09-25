package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.treeview.Node;
import com.ichi2yiji.anki.treeview.TreeListViewAdapter;

import java.util.List;

/**
 * Created by ekar01 on 2017/7/6.
 */

public class EKSimpleTreeAdapter<T> extends TreeListViewAdapter<T> {
    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public EKSimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, final int position, View convertView, ViewGroup parent) {
        EKSimpleTreeAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ek_selectchapter_list_item, parent, false);
            viewHolder = new EKSimpleTreeAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EKSimpleTreeAdapter.ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            viewHolder.iv_ek_selectchapter_list_item.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.iv_ek_selectchapter_list_item.setVisibility(View.VISIBLE);
            viewHolder.iv_ek_selectchapter_list_item.setImageResource(node.getIcon());
            viewHolder.iv_ek_selectchapter_list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrCollapse(position);
                }
            });
        }

        viewHolder.tv_ek_selectchapter_list_item.setText(node.getName());

        return convertView;
    }

    final class ViewHolder {
        ImageView iv_ek_selectchapter_list_item;
        TextView tv_ek_selectchapter_list_item;

        ////////////dx  end
        ViewHolder(View view) {
            iv_ek_selectchapter_list_item = (ImageView) view.findViewById(R.id.iv_ek_selectchapter_list_item);
            tv_ek_selectchapter_list_item = (TextView) view.findViewById(R.id.tv_ek_selectchapter_list_item);
        }

    }
}
