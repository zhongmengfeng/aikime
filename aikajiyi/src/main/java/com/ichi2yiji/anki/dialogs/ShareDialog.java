package com.ichi2yiji.anki.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by Administrator on 2017/3/7.
 */

public class ShareDialog extends Dialog {
    private Context context;


    public ShareDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lyt_2, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity)context).getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (display.getWidth()*0.69);
        params.height = (int) (display.getHeight()*0.50);
        dialogWindow.setAttributes(params);

        setCancelable(false);
        TextView tv_no_thinks = (TextView)view.findViewById(R.id.tv_no_thinks);
        TextView tv_goto_share = (TextView)view.findViewById(R.id.tv_goto_share);
        tv_no_thinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noThinksClickListener.onViewClick();
            }
        });
        tv_goto_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShareClickListener.onViewClick();
            }
        });

    }

    public interface NoThinksClickListener{
        void onViewClick();
    }

    public void setNoThinksClickListener(NoThinksClickListener noThinksClickListener){
        this.noThinksClickListener = noThinksClickListener;
    }

    private NoThinksClickListener noThinksClickListener;

    public interface GoToShareClickListener{
        void onViewClick();
    }

    public void setGoToShareClickListener(GoToShareClickListener goToShareClickListener){
        this.goToShareClickListener = goToShareClickListener;
    }

    private GoToShareClickListener goToShareClickListener;


}
