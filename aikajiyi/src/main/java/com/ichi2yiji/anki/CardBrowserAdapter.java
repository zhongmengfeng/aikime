package com.ichi2yiji.anki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.themes.Themes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/1.
 */

public class CardBrowserAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> mCards;
    private static final int BACKGROUND_NORMAL = 0;
    private static final int BACKGROUND_MARKED = 1;
    private static final int BACKGROUND_SUSPENDED = 2;
    private static final int BACKGROUND_MARKED_SUSPENDED = 3;
    public CardBrowserAdapter(Context context,ArrayList<HashMap<String, String>> mCards){
        this.context=context;
        this.mCards=mCards;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int position) {
        return mCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.card_browser_listitem_layout,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.card_browser_item_text);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.card_browser_item_detail);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.card_browser_item_layout);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        HashMap<String, String> c=mCards.get(position);
        int flag=Integer.valueOf(c.get("flags"));
        viewHolder.bindView(c,flag);
        return convertView;
    }
    class ViewHolder{
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;
        int[] colors = Themes.getColorFromAttr(context, new int[]{android.R.attr.colorBackground,
                R.attr.markedColor, R.attr.suspendedColor, R.attr.markedColor});
        public void bindView(HashMap<String, String> card,int flags){
            textView.setText("id="+card.get("id")+"  note="+card.get("note")+"  flags="+flags+"  deck="+card.get("deck")+"  answer="+card.get("answer"));
            linearLayout.setBackgroundColor(colors[flags]);
        }
    }

}
