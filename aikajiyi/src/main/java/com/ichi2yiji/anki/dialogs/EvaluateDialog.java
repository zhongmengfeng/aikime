package com.ichi2yiji.anki.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;

/**
 * Created by Administrator on 2017/3/7.
 */

public class EvaluateDialog extends Dialog {
    private Context context;


    public EvaluateDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected EvaluateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public EvaluateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lyt_1, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity)context).getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (display.getWidth()*0.69);
        params.height = (int) (display.getHeight()*0.50);
        dialogWindow.setAttributes(params);

        setCancelable(false);
        RelativeLayout feel_good_lyt = (RelativeLayout)view.findViewById(R.id.feel_good_lyt);
        RelativeLayout feel_not_good_lyt = (RelativeLayout)view.findViewById(R.id.feel_not_good_lyt);
        RelativeLayout comment_later_lyt = (RelativeLayout)view.findViewById(R.id.comment_later_lyt);
        feel_good_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feelGoodClickListener.onViewClick();
            }
        });
        feel_not_good_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feelNotGoodClickListener.onViewClick();
            }
        });
        comment_later_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentLaterClickListener.onViewClick();
            }
        });
    }

    public interface FeelGoodClickListener{
        void onViewClick();
    }

    public void setFeelGoodClickListener(FeelGoodClickListener feelGoodClickListener){
        this.feelGoodClickListener = feelGoodClickListener;
    }

    private FeelGoodClickListener feelGoodClickListener;

    public interface FeelNotGoodClickListener{
        void onViewClick();
    }

    public void setFeelNotGoodClickListener(FeelNotGoodClickListener feelNotGoodClickListener){
        this.feelNotGoodClickListener = feelNotGoodClickListener;
    }

    private FeelNotGoodClickListener feelNotGoodClickListener;

    public interface CommentLaterClickListener{
        void onViewClick();
    }

    public void setCommentLaterClickListener(CommentLaterClickListener commentLaterClickListener){
        this.commentLaterClickListener = commentLaterClickListener;
    }

    private CommentLaterClickListener commentLaterClickListener;
}
