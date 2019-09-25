package com.ichi2yiji.anki.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AnkiDroidApp;
import com.ichi2yiji.anki.util.ZXUtils;

import org.xutils.common.Callback;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/7.
 */

public class CommentDialog extends Dialog {
    private Context context;
    private String commentContent_1 = "";
    private String commentContent_2 = "";
    private String commentContent_3 = "";
    private String commentContent_4 = "";
    private String commentContent_5 = "";
    private String commentContent_6 = "";


    public CommentDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected CommentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lyt_3, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager manager = ((Activity)context).getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (display.getWidth()*0.85);
        params.height = (int) (display.getHeight()*0.60);
        dialogWindow.setAttributes(params);

        setCancelable(true);
        final TextView comment_item_1 = (TextView)view.findViewById(R.id.comment_item_1);
        final TextView comment_item_2 = (TextView)view.findViewById(R.id.comment_item_2);
        final TextView comment_item_3 = (TextView)view.findViewById(R.id.comment_item_3);
        final TextView comment_item_4 = (TextView)view.findViewById(R.id.comment_item_4);
        final TextView comment_item_5 = (TextView)view.findViewById(R.id.comment_item_5);
        EditText comment_edit_text = (EditText)view.findViewById(R.id.comment_edit_text);
        final Button comment_confirm = (Button)view.findViewById(R.id.comment_confirm);

        comment_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_item_1.getBackground().getConstantState()
                        == context.getResources().getDrawable(R.drawable.text_view_not_selected_shape).getConstantState()){
                    comment_item_1.setBackgroundResource(R.drawable.text_view_selected_shape);
//                    commentContent_1 = "卡片不美观";
                    commentContent_1 = "0,";
                }else {
                    comment_item_1.setBackgroundResource(R.drawable.text_view_not_selected_shape);
                    commentContent_1 = "";
                }
            }
        });

        comment_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_item_2.getBackground().getConstantState()
                        == context.getResources().getDrawable(R.drawable.text_view_not_selected_shape).getConstantState()){
                    comment_item_2.setBackgroundResource(R.drawable.text_view_selected_shape);
//                    commentContent_2 = "表述不清晰";
                    commentContent_2 = "1,";
                }else {
                    comment_item_2.setBackgroundResource(R.drawable.text_view_not_selected_shape);
                    commentContent_2 = "";
                }
            }
        });

        comment_item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_item_3.getBackground().getConstantState()
                        == context.getResources().getDrawable(R.drawable.text_view_not_selected_shape).getConstantState()){
                    comment_item_3.setBackgroundResource(R.drawable.text_view_selected_shape);
//                    commentContent_3 = "软件卡顿";
                    commentContent_3 = "2,";
                }else {
                    comment_item_3.setBackgroundResource(R.drawable.text_view_not_selected_shape);
                    commentContent_3 = "";
                }
            }
        });

        comment_item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_item_4.getBackground().getConstantState()
                        == context.getResources().getDrawable(R.drawable.text_view_not_selected_shape).getConstantState()){
                    comment_item_4.setBackgroundResource(R.drawable.text_view_selected_shape);
//                    commentContent_4 = "使用太吃力";
                    commentContent_4 = "3,";
                }else {
                    comment_item_4.setBackgroundResource(R.drawable.text_view_not_selected_shape);
                    commentContent_4 = "";
                }
            }
        });

        comment_item_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_item_5.getBackground().getConstantState()
                        == context.getResources().getDrawable(R.drawable.text_view_not_selected_shape).getConstantState()){
                    comment_item_5.setBackgroundResource(R.drawable.text_view_selected_shape);
//                    commentContent_5 = "没有为什么，就是不喜欢";
                    commentContent_5 = "4,";
                }else {
                    comment_item_5.setBackgroundResource(R.drawable.text_view_not_selected_shape);
                    commentContent_5 = "";
                }
            }
        });

        comment_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                commentContent_6 = String.valueOf(s);
            }
        });

        comment_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog.this.dismiss();
                String comment_goodOrBad = commentContent_1 + commentContent_2 + commentContent_3 + commentContent_4 + commentContent_5 + commentContent_6;
                String self_define_comment = commentContent_6;
//                Log.e("CommentDialog", " >>>>>>" + comment);
                //此处添加向后台Post反馈数据的请求
                postCommentToBackstage(self_define_comment, comment_goodOrBad);
            }
        });


    }

    /**
     * 向后台Post评价内容
     * @param self_define_comment
     * @param comment_goodOrBad
     */
    private void postCommentToBackstage(String self_define_comment, String comment_goodOrBad){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mem_id = preferences.getString("MEM_ID","");
        if(mem_id.equals("")){
            Toast.makeText(context.getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("mem_id",mem_id);
        map.put("comment",self_define_comment);
        map.put("goodOrBad",comment_goodOrBad);

        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/pingi/";
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("CommentDialog", "postCommentToBackstage>>>>>>onSuccess>>>" + result);
                Toast.makeText(context.getApplicationContext(), "感谢您的评价！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("CommentDialog", "postCommentToBackstage>>>>>>onError>>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
