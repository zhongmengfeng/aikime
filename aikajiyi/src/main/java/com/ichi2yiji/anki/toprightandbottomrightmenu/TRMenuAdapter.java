package com.ichi2yiji.anki.toprightandbottomrightmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Bro0cL on 2016/12/26.
 */
public class TRMenuAdapter extends RecyclerView.Adapter<TRMenuAdapter.TRMViewHolder> {
    private Context mContext;
    private List<MenuItem> menuItemList;
    private boolean showIcon;
    private boolean showRadioButton;
    private TopRightMenu mTopRightMenu;
    private TopRightMenu.OnMenuItemClickListener onMenuItemClickListener;
    private boolean[] checkedInfo;//实现RadioButton的单选效果

    public TRMenuAdapter(Context context, TopRightMenu topRightMenu, List<MenuItem> menuItemList, boolean show) {
        this.mContext = context;
        this.mTopRightMenu = topRightMenu;
        this.menuItemList = menuItemList;
        this.showIcon = show;
        checkedInfo = new boolean[]{true, false, false};//实现RadioButton的单选效果

    }

    public void setData(List<MenuItem> data){
        menuItemList = data;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    public void setShowRadioButton(boolean showRadioButton) {
        this.showRadioButton = showRadioButton;
        notifyDataSetChanged();
    }

    @Override
    public TRMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trm_item_popup_menu_list, parent, false);
        return new TRMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TRMViewHolder holder, final int position) {
        final MenuItem menuItem = menuItemList.get(position);
        if (showIcon){
            holder.icon.setVisibility(View.VISIBLE);
            int resId = menuItem.getIcon();
            holder.icon.setImageResource(resId < 0 ? 0 : resId);
        }else{
            holder.icon.setVisibility(View.GONE);
        }
        holder.text.setText(menuItem.getText());

        if (showRadioButton){
            holder.radioButton.setVisibility(View.VISIBLE);
        }else {
            holder.radioButton.setVisibility(View.GONE);
        }

        if (position == 0){
//            holder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_top_pressed));
        }else if (position == menuItemList.size() - 1){
//            holder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_bottom_pressed));
//            holder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.popup_top_bottom));
        }else {
//            holder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.trm_popup_middle_pressed));
//            holder.container.setBackgroundDrawable(addStateDrawable(mContext, -1, R.drawable.popup_middle));
        }

        final int pos = holder.getAdapterPosition();

        //实现RadioButton的单选效果
        if (position == 0 ){
            holder.radioButton.setChecked(checkedInfo[0]);
        }
        if (position == 1 ){
            holder.radioButton.setChecked(checkedInfo[1]);
        }
        if (position == 2 ){
            holder.radioButton.setChecked(checkedInfo[2]);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现RadioButton的单选效果
                if(position == 0){
                    if(!holder.radioButton.isChecked() && (checkedInfo[1]||checkedInfo[2])){
                        checkedInfo[0] = true;
                        checkedInfo[1] = false;
                        checkedInfo[2] = false;
                        holder.radioButton.setChecked(checkedInfo[0]);
                        TRMenuAdapter.this.notifyDataSetChanged();
                    }
                }

                if(position == 1){
                    if(!holder.radioButton.isChecked() && (checkedInfo[0]||checkedInfo[2])){
                        checkedInfo[0] = false;
                        checkedInfo[1] = true;
                        checkedInfo[2] = false;
                        holder.radioButton.setChecked(checkedInfo[1]);
                        TRMenuAdapter.this.notifyDataSetChanged();
                    }
                }

                if(position == 2){
                    if(!holder.radioButton.isChecked() && (checkedInfo[0]||checkedInfo[1])){
                        checkedInfo[0] = false;
                        checkedInfo[1] = false;
                        checkedInfo[2] = true;
                        holder.radioButton.setChecked(checkedInfo[2]);
                        TRMenuAdapter.this.notifyDataSetChanged();
                    }
                }



                if (onMenuItemClickListener != null) {

                    mTopRightMenu.dismiss();
                    onMenuItemClickListener.onMenuItemClick(pos);
                }
            }
        });
    }

    private StateListDrawable addStateDrawable(Context context, int normalId, int pressedId){
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : context.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : context.getResources().getDrawable(pressedId);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    class TRMViewHolder extends RecyclerView.ViewHolder{
        ViewGroup container;
        ImageView icon;
        TextView text;
        RadioButton radioButton;

        TRMViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.trm_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.trm_menu_item_text);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_button);
        }
    }

    public void setOnMenuItemClickListener(TopRightMenu.OnMenuItemClickListener listener){
        this.onMenuItemClickListener = listener;
    }
}
