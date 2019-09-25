/****************************************************************************************
 * Copyright (c) 2011 Kostas Spyropoulos <inigo.aldana@gmail.com>                       *
 * Copyright (c) 2014 Bruno Romero de Azevedo <brunodea@inf.ufsm.br>                    *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/
// TODO: implement own menu? http://www.codeproject.com/Articles/173121/Android-Menus-My-Way

package com.ichi2yiji.anki;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.dialogs.DialogHandler;
import com.ichi2yiji.anki.toprightandbottomrightmenu.TopRightMenu;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ViewShot;
import com.ichi2yiji.anki.util.ViewShotWithAnimation;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.compat.CompatHelper;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.themes.Themes;
import com.ichi2yiji.utils.SPUtil;
import com.ichi2yiji.widget.WidgetStatus;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class Reviewer extends AbstractFlashcardViewer implements View.OnClickListener {
    private boolean mHasDrawerSwipeConflicts = false;
    private boolean mShowWhiteboard = true;
    private boolean mBlackWhiteboard = true;
    private boolean mPrefFullscreenReview = false;
    //////////dx add  屏幕密度
    private static float md;
    private int resId=0;
    private TopRightMenu topRightMenu;
    private ImageView clearWhiteBoard;
    private ImageView concleWhiteBoard;
    final List<com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem> menuItems1=new ArrayList<>();
    final List<com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem> menuItems2=new ArrayList<>();
    final List<com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem> menuItems = new ArrayList<>();
    private boolean isBurder=true;
    /** 返回 **/
    private TextView text_back;
    /** 截屏 **/
    private ImageView icon_camera;
    /** 返回箭头 **/
    private ImageView icon_cancel;
    /** 旗子 **/
    private ImageView icon_star;
    /** 更多 **/
    private ImageView icon_more;

    private String deckname;

    @Override
    public DrawerLayout getDrawerLayout() {
        return super.getDrawerLayout();
    }

    @Override
    protected void setTitle() {
        try {
            String[] title = {""};
            if (colIsOpen()) {
                title = getCol().getDecks().current().getString("name").split("::");
            } else {
                Timber.e("Could not set title in reviewer because collection closed");
            }
//            getSupportActionBar().setTitle(title[title.length - 1]);
            super.setTitle(title[title.length - 1]);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        getSupportActionBar().setSubtitle("");
    }


    @Override
    protected void onCollectionLoaded(Collection col) {
        super.onCollectionLoaded(col);
        // Load the first card and start reviewing. Uses the answer card
        // task to load a card, but since we send null
        // as the card to answer, no card will be answered.

        mPrefWhiteboard = MetaDB.getWhiteboardState(this, getParentDid());
        mPrefWhiteboard = false;//修复白板每次都自动开启的BUG
        if (mPrefWhiteboard) {
            setWhiteboardEnabledState(true);
            setWhiteboardVisibility(true);
        }

        col.getSched().reset();     // Reset schedule incase card had previous been loaded
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ANSWER_CARD, mAnswerCardHandler, new DeckTask.TaskData(null, 0));

//        long did = getParentDid();
//        Bundle bundle = getIntent().getExtras();
//        SerializableMap serializableMap = (SerializableMap) bundle.get("map");
//        Map<Long, String> cardContentMap = serializableMap.getMap();
//        for (Map.Entry<Long, String> entry : cardContentMap.entrySet()) {
//            Long key = entry.getKey();
//            String cardContent = entry.getValue();
//            if (key == did) {
//                WebView webView = createWebView();
//                mCardFrame.addView(webView,0);
//                webView.loadDataWithBaseURL(Utils.getBaseUrl(col.getMedia().dir()) + "__viewer__.html", cardContent, "text/html", "utf-8", null);
//
//            }
//        }

        disableDrawerSwipeOnConflicts();
        // Add a weak reference to current activity so that scheduler can talk to to Activity
        mSched.setContext(new WeakReference<Activity>(this));

        // Set full screen/immersive mode if needed
        if (mPrefFullscreenReview) {
            CompatHelper.getCompat().setFullScreen(this);
        }

        timeEnd = System.currentTimeMillis();
        Log.e("AbstractFlashcardViewer", "整体耗时: " + (timeEnd - timeStart));

    }
    private long timeEnd;

    ////////////////////自定义ToolView的监听事件////////////////////////////////

    @Override
    protected int getDefaultEase() {
        return super.getDefaultEase();
    }

    private long timeStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        timeStart = System.currentTimeMillis();

        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        md = UIUtils.getDensity();
        initView();
        setListener();
        if (mCurrentCard != null && mCurrentCard.note().hasTag("marked")) {
//            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_unmark_note).setIcon(R.drawable.ic_star_white_24dp);
            //menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_unmark_note).setIcon(R.drawable.mark_white);
            icon_star.setImageResource(R.drawable.mark_fill);
        } else {
//            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_mark_note).setIcon(R.drawable.ic_star_outline_white_24dp);
            //menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_mark_note).setIcon(R.drawable.mark_black);
            icon_star.setImageResource(R.drawable.mark_not_fill);
        }

        //从SharedPreferences中获取已经学习了的所有卡片的数量
        // String allDeckReviewCount = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "AllDeckReviewCount", "0");
        // int allDeckReviewCountNumber = Integer.parseInt(allDeckReviewCount);
    }

    private void setListener() {
        text_back.setOnClickListener(this);
        icon_star.setOnClickListener(this);
        icon_camera.setOnClickListener(this);
        icon_cancel.setOnClickListener(this);

        //返回
        /*text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reviewer.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });*/
        //截图
        /*icon_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshots();
            }
        });*/
        //Undo
        /*icon_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("Reviewer:: Undo button pressed");
                undo();
            }
        });*/

        //mark card
        /*icon_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Timber.i("Reviewer:: Mark button pressed");
                onMark(mCurrentCard);
                refreshActionBar();
                setIconStarImg();
                icon_star.setImageResource(resId);
            }
        });*/

        /*icon_more.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showMore();
             }
         });*/
    }

    /**
     * 显示更多
     */
    private void showMore() {
        topRightMenu = createNewTopRightMenuWithWhiteBoardStartOrNot(mPrefWhiteboard);
        topRightMenu.setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                switch(position){
                    case 0:  //编辑笔记
//                      Timber.i("Reviewer:: Edit note button pressed");
                        editCard();
                        break;
                    case 1:  //重新播放
                        Timber.i("Reviewer:: Replay audio button pressed (from menu)");
                        playSounds(true);
                        break;
                    case 2:  //隐藏卡片
                        Timber.i("Reviewer:: Bury card button pressed");
                        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 4));
                        break;
                    case 3:  //暂停卡片
                        Timber.i("Reviewer:: Suspend card button pressed");
                        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 1));
                        break;
                    case 4:  //删除笔记
//                                Timber.i("Reviewer:: Delete note button pressed");
//                                showDeleteNoteDialog();
                        break;
                    case 5:  //启用白板
                        clearWhiteBoard=(ImageView) findViewById(R.id.icon_clearWhiteBoard);
                        concleWhiteBoard=(ImageView) findViewById(R.id.icon_cancleWhiteBoard);
                        if(!mPrefWhiteboard){
                            mPrefWhiteboard = ! mPrefWhiteboard;
                            setWhiteboardEnabledState(true);
                            setWhiteboardVisibility(true);
                            getDelegate().invalidateOptionsMenu();

                            clearWhiteBoard.setVisibility(View.VISIBLE);
                            icon_cancel.setVisibility(View.INVISIBLE);
                            icon_camera.setVisibility(View.INVISIBLE);

                            concleWhiteBoard.setVisibility(View.VISIBLE);

                            clearWhiteBoard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mWhiteboard != null) {
                                        mWhiteboard.clear();
                                    }
                                }
                            });
                            concleWhiteBoard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setWhiteboardVisibility(false);
                                    clearWhiteBoard.setVisibility(View.INVISIBLE);
                                    concleWhiteBoard.setVisibility(View.INVISIBLE);
                                    icon_cancel.setVisibility(View.VISIBLE);
                                    icon_camera.setVisibility(View.VISIBLE);
                                    mPrefWhiteboard = ! mPrefWhiteboard;
                                }
                            });
                        }else{
                            clearWhiteBoard.setVisibility(View.INVISIBLE);
                            concleWhiteBoard.setVisibility(View.INVISIBLE);
                            icon_cancel.setVisibility(View.VISIBLE);
                            icon_camera.setVisibility(View.VISIBLE);
                            setWhiteboardVisibility(false);
                            mPrefWhiteboard = ! mPrefWhiteboard;
                        }

                        break;
                    case 6:  //牌组选项
                        Intent i = new Intent(Reviewer.this, DeckOptionsAika.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                        break;
                }
            }
        }).showAsDropDown(icon_more,(int)(-83*md), (int)(13*md));
    }

    /**
     * 获取整个屏幕的截图
     */
    private void screenshots() {
        deckname = "as";
        String[] title = {""};
        try {
            title = getCol().getDecks().current().getString("name").split("::");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Reviewer", "title>>>>>>>>>"+ Arrays.toString(title));
        if(title.length == 1){
            deckname = title[0];
        }
        if(title.length == 2){
            deckname = title[0] + "__" + title[1];
        }

        Bitmap bitmap = ViewShot.captureScreen(Reviewer.this);
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture", deckname+"__"+ num, bitmap);
        //////////////dx  add
        final AlertDialog cutDialog=popShotSrceenDialog(bitmap);
        cutDialog.show();
        new DialogHandler(Reviewer.this).postDelayed(new Runnable() {
            @Override
            public void run() {
                cutDialog.dismiss();
            }
        },2000);
        //cutDialog.dismiss();
        //////////////dx  add
//                Toast.makeText(getApplicationContext(), "截图已保存！",Toast.LENGTH_SHORT).show();

        //请求权限
//                boolean alertWindowPermission =
//                        getPackageManager().checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, getPackageName()) == PackageManager.PERMISSION_GRANTED;
//                if (Build.VERSION.SDK_INT >= 23 && !alertWindowPermission ) {
//                    requestAlertWindowPermission();
//                }else{
//                    ViewShotWithAnimation viewShotWithAnimation = new ViewShotWithAnimation(Reviewer.this);
//                    viewShotWithAnimation.takeScreenshot(getWindow().getDecorView(),
//                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture/", deckname + "__" + num,
//                            new Runnable() {
//                                @Override
//                                public void run() {
//
//                                }
//                            }, true, true);
//                    num++;
//                }

//                ViewShotWithAnimation viewShotWithAnimation = new ViewShotWithAnimation(Reviewer.this);
//                viewShotWithAnimation.takeScreenshot(getWindow().getDecorView(),
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture/", deckname + "__" + num,
//                        new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }, true, true);
//                num++;
    }

    private void initView() {
        text_back = (TextView) findViewById(R.id.text_back);
        // RelativeLayout rlayout=(RelativeLayout)findViewById(R.id.flash_rl_layout);
        icon_camera = (ImageView) findViewById(R.id.icon_camera);
        icon_cancel = (ImageView) findViewById(R.id.icon_cancel);
        icon_star = (ImageView) findViewById(R.id.icon_star);
        setIconStarImg();
        icon_star.setImageResource(resId);
        icon_more = (ImageView) findViewById(R.id.icon_more);
        icon_more.setOnClickListener(this);
    }

    public TopRightMenu createNewTopRightMenuWithWhiteBoardStartOrNot(boolean whiteBoardIsStart){
        TopRightMenu mTopRightMenu = new TopRightMenu(Reviewer.this);
        //添加菜单项
        menuItems.clear();
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_editnote, "编辑笔记"));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_reload, "重新播放"));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_hide, "隐藏卡片"));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_stop, "暂停卡片"));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_delete, "删除笔记"));
        if(whiteBoardIsStart){
            menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_off_whiteboard, "禁用白板"));
        }else{
            menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_on_whiteboard, "启用白板"));
        }
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem(R.drawable.icon_option, "牌组选项"));

        mTopRightMenu
                .setHeight((int)(44 * menuItems.size() * md))     //默认高度480
                .setWidth((int)(120*md))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .showRadioButton(false);
        return mTopRightMenu;
    }

    ////////////////////自定义ToolView的监听事件/////////////////////////////////////
    /////////////////////悬浮窗权限//////////////////////////////////////////////////
    private static final int REQUEST_CODE = 110;
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }
/////////////////////悬浮窗权限//////////////////////////////////////////////////


    int num = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Timber.i("Reviewer:: Home button pressed");
                closeReviewer(RESULT_OK, true);
                break;

            case R.id.action_undo:
                Timber.i("Reviewer:: Undo button pressed");
                undo();
                break;

            case R.id.action_mark_card:
                Timber.i("Reviewer:: Mark button pressed");
                onMark(mCurrentCard);
                refreshActionBar();
                break;

            case R.id.action_replay://重新播放
                Timber.i("Reviewer:: Replay audio button pressed (from menu)");
                playSounds(true);
                break;

            case R.id.action_edit://编辑
                Timber.i("Reviewer:: Edit note button pressed");
                return editCard();

            case R.id.action_bury_card://隐藏卡片
                Timber.i("Reviewer:: Bury card button pressed");
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 4));
                break;

            case R.id.action_bury_note:
                Timber.i("Reviewer:: Bury note button pressed");
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 0));
                break;

            case R.id.action_suspend_card://暂停卡片
                Timber.i("Reviewer:: Suspend card button pressed");
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 1));
                break;

            case R.id.action_suspend_note:
                Timber.i("Reviewer:: Suspend note button pressed");
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 2));
                break;

            case R.id.action_delete://删除笔记
                Timber.i("Reviewer:: Delete note button pressed");
                showDeleteNoteDialog();
                break;

            case R.id.action_clear_whiteboard://清空白板
                Timber.i("Reviewer:: Clear whiteboard button pressed");
                if (mWhiteboard != null) {
                    mWhiteboard.clear();
                }
                break;

            case R.id.action_hide_whiteboard://禁用白板
                // toggle whiteboard visibility
                Timber.i("Reviewer:: Whiteboard visibility set to %b", !mShowWhiteboard);
                setWhiteboardVisibility(!mShowWhiteboard);
                refreshActionBar();
                break;

            case R.id.action_enable_whiteboard://启用白板
                // toggle whiteboard enabled state (and show/hide whiteboard item in action bar)
                mPrefWhiteboard = ! mPrefWhiteboard;
                Timber.i("Reviewer:: Whiteboard enabled state set to %b", mPrefWhiteboard);
                setWhiteboardEnabledState(mPrefWhiteboard);
                setWhiteboardVisibility(mPrefWhiteboard);
                refreshActionBar();
                break;

            case R.id.action_search_dictionary:
                Timber.i("Reviewer:: Search dictionary button pressed");
                lookUpOrSelectText();
                break;

            case R.id.action_open_deck_options://牌组选项
                Intent i = new Intent(this, DeckOptions.class);
                startActivityForResultWithAnimation(i, DECK_OPTIONS, ActivityTransitionAnimation.FADE);
                break;

            //新增的截图事件
            case R.id.action_viewshot:
                //获取整个屏幕的截图
                String deckname = "as";
                String[] title = {""};
                try {
                    title = getCol().getDecks().current().getString("name").split("::");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Reviewer", "title>>>>>>>>>"+ Arrays.toString(title));
                if(title.length == 1){
                    deckname = title[0];
                }
                if(title.length == 2){
                    deckname = title[0] + "__" + title[1];
                }

                Bitmap bitmap = ViewShot.captureScreen(Reviewer.this);
                ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture", deckname+"__"+ num, bitmap);
                Toast.makeText(getApplicationContext(), "截图已保存！",Toast.LENGTH_SHORT).show();
                num++;
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reviewer, menu);
        Resources res = getResources();
        if (mCurrentCard != null && mCurrentCard.note().hasTag("marked")) {
//            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_unmark_note).setIcon(R.drawable.ic_star_white_24dp);
            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_unmark_note).setIcon(R.drawable.mark_white);
        } else {
//            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_mark_note).setIcon(R.drawable.ic_star_outline_white_24dp);
            menu.findItem(R.id.action_mark_card).setTitle(R.string.menu_mark_note).setIcon(R.drawable.mark_black);
        }
        if (colIsOpen() && getCol().undoAvailable()) {
            menu.findItem(R.id.action_undo).setEnabled(true).getIcon().setAlpha(Themes.ALPHA_ICON_ENABLED_LIGHT);
        } else {
            menu.findItem(R.id.action_undo).setEnabled(false).getIcon().setAlpha(
                    Themes.ALPHA_ICON_DISABLED_LIGHT);
        }
        if (mPrefWhiteboard) {
            // Don't force showing mark icon when whiteboard enabled
            // TODO: allow user to customize which icons are force-shown
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_mark_card), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            // Check if we can forceably squeeze in 3 items into the action bar, if not hide "show whiteboard"
            if (CompatHelper.getSdkVersion() >= 14 &&  !ViewConfiguration.get(this).hasPermanentMenuKey()) {
                // Android 4.x device with overflow menu in the action bar and small screen can't
                // support forcing 2 extra items into the action bar
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics ();
                display.getMetrics(outMetrics);
                float density  = getResources().getDisplayMetrics().density;
                float dpWidth  = outMetrics.widthPixels / density;
                if (dpWidth < 360) {
                    menu.findItem(R.id.action_hide_whiteboard).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                }
            }
            // Configure the whiteboard related items in the action bar
            menu.findItem(R.id.action_enable_whiteboard).setTitle(R.string.disable_whiteboard);
            menu.findItem(R.id.action_hide_whiteboard).setVisible(true);
            menu.findItem(R.id.action_clear_whiteboard).setVisible(true);

            Drawable whiteboardIcon = getResources().getDrawable(R.drawable.ic_gesture_white_24dp);
            if (mShowWhiteboard) {
                whiteboardIcon.setAlpha(255);
                menu.findItem(R.id.action_hide_whiteboard).setIcon(whiteboardIcon);
                menu.findItem(R.id.action_hide_whiteboard).setTitle(R.string.hide_whiteboard);
            } else {
                whiteboardIcon.setAlpha(77);
                menu.findItem(R.id.action_hide_whiteboard).setIcon(whiteboardIcon);
                menu.findItem(R.id.action_hide_whiteboard).setTitle(R.string.show_whiteboard);
            }
        } else {
            menu.findItem(R.id.action_enable_whiteboard).setTitle(R.string.enable_whiteboard);
        }
        if (!CompatHelper.isHoneycomb() && !mDisableClipboard) {
            menu.findItem(R.id.action_search_dictionary).setVisible(true).setEnabled(!(mPrefWhiteboard && mShowWhiteboard))
                    .setTitle(clipboardHasText() ? Lookup.getSearchStringTitle() : res.getString(R.string.menu_select));
        }
        if (getCol().getDecks().isDyn(getParentDid())) {
            menu.findItem(R.id.action_open_deck_options).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char keyPressed = (char) event.getUnicodeChar();
        if (mAnswerField != null && !mAnswerField.isFocused()) {
	        if (sDisplayAnswer) {
	            if (keyPressed == '1') {
	                answerCard(EASE_1);
	                return true;
	            }
	            if (keyPressed == '2') {
	                answerCard(EASE_2);
	                return true;
	            }
	            if (keyPressed == '3') {
	                answerCard(EASE_3);
	                return true;
	            }
	            if (keyPressed == '4') {
	                answerCard(EASE_4);
	                return true;
	            }
	            if (keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
	                answerCard(getDefaultEase());
	                return true;
	            }
	        }
	        if (keyPressed == 'e') {
	            editCard();
	            return true;
	        }
	        if (keyPressed == '*') {
                onMark(mCurrentCard);
                refreshActionBar();
	            return true;
	        }
	        if (keyPressed == '-') {
	            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 4));
	            return true;
	        }
	        if (keyPressed == '=') {
	            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 0));
	            return true;
	        }
	        if (keyPressed == '@') {
	            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 1));
	            return true;
	        }
	        if (keyPressed == '!') {
	            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDismissCardHandler, new DeckTask.TaskData(mCurrentCard, 2));
	            return true;
	        }
	        if (keyPressed == 'r' || keyCode == KeyEvent.KEYCODE_F5) {
	            playSounds(true);
	            return true;
	        }

            // different from Anki Desktop
            if (keyPressed == 'z') {
                undo();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected SharedPreferences restorePreferences() {
        super.restorePreferences();
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());

        /*mBlackWhiteboard = preferences.getBoolean("blackWhiteboard", true);
        mPrefFullscreenReview = Integer.parseInt(preferences.getString("fullscreenMode", "0")) >0;*/

        mBlackWhiteboard = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "blackWhiteboard", true);
        mBlackWhiteboard = Integer.parseInt(SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "fullscreenMode", "0")) > 0;
        return preferences;
    }

    @Override
    public void fillFlashcard() {
        super.fillFlashcard();
        if (!sDisplayAnswer) {
            if (mShowWhiteboard && mWhiteboard != null) {
                mWhiteboard.clear();
            }
        }
    }


    @Override
    public void displayCardQuestion() {
        // show timer, if activated in the deck's preferences
        initTimer();
        super.displayCardQuestion();
        //此处更新标记按钮的显示状态
        if (mCurrentCard != null && mCurrentCard.note().hasTag("marked")) {
            icon_star.setImageResource(R.drawable.mark_fill);
        } else {
            icon_star.setImageResource(R.drawable.mark_not_fill);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isFinishing()) {
            if (colIsOpen() && mSched != null) {
                WidgetStatus.update(this);
            }
        }
        UIUtils.saveCollectionInBackground(this);
    }


    @Override
    protected void initControls() {
        super.initControls();
        if (mPrefWhiteboard) {
            setWhiteboardVisibility(mShowWhiteboard);
        }
    }


    private void setWhiteboardEnabledState(boolean state) {
        mPrefWhiteboard = state;
        MetaDB.storeWhiteboardState(this, getParentDid(), state);
        if (state && mWhiteboard == null) {
            createWhiteboard();
        }
    }

    // Create the whiteboard
    private void createWhiteboard() {
        mWhiteboard = new Whiteboard(this, mNightMode, mBlackWhiteboard);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        mWhiteboard.setLayoutParams(lp2);
        FrameLayout fl = (FrameLayout) findViewById(R.id.whiteboard);
        fl.addView(mWhiteboard);

        mWhiteboard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mShowWhiteboard) {
                    return false;
                }
                return getGestureDetector().onTouchEvent(event);
            }
        });
        mWhiteboard.setEnabled(true);
    }

    // Show or hide the whiteboard
    private void setWhiteboardVisibility(boolean state) {
        mShowWhiteboard = state;
        if (state) {
            mWhiteboard.setVisibility(View.VISIBLE);
            disableDrawerSwipe();
        } else {
            mWhiteboard.setVisibility(View.GONE);
            if (!mHasDrawerSwipeConflicts) {
                enableDrawerSwipe();
            }
        }
    }


    private void disableDrawerSwipeOnConflicts() {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
        boolean gesturesEnabled = AnkiDroidApp.initiateGestures(preferences);
        if (gesturesEnabled) {
            int gestureSwipeUp = Integer.parseInt(preferences.getString("gestureSwipeUp", "9"));
            int gestureSwipeDown = Integer.parseInt(preferences.getString("gestureSwipeDown", "0"));
            int gestureSwipeRight = Integer.parseInt(preferences.getString("gestureSwipeRight", "17"));
            if (gestureSwipeUp != GESTURE_NOTHING ||
                    gestureSwipeDown != GESTURE_NOTHING ||
                    gestureSwipeRight != GESTURE_NOTHING) {
                mHasDrawerSwipeConflicts = true;
                super.disableDrawerSwipe();
            }
        }
    }

    @Override
    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
        // Tell the browser the current card ID so that it can tell us when we need to reload
        if (mCurrentCard != null) {
            setCurrentCardId(mCurrentCard.getId());
        }
        return super.onItemClick(view, i, iDrawerItem);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Restore full screen once we regain focus
        if (hasFocus) {
            delayedHide(INITIAL_HIDE_DELAY);
        } else {
            mFullScreenHandler.removeMessages(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_STATISTICS || requestCode == REQUEST_BROWSE_CARDS) {
            // select original deck if the statistics or card browser were opened,
            // which can change the selected deck
            if (data != null && data.hasExtra("originalDeck")) {
                getCol().getDecks().select(data.getLongExtra("originalDeck", 0L));
            }
        }

        //悬浮窗权限的返回
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    ViewShotWithAnimation viewShotWithAnimation = new ViewShotWithAnimation(Reviewer.this);
                    viewShotWithAnimation.takeScreenshot(getWindow().getDecorView(),
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture/", deckname + "__" + num,
                            new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, true, true);
                    num++;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
/////////////////////////////////////dx Start
    public void setIconStarImg(){
        if (mCurrentCard != null && mCurrentCard.note().hasTag("marked")) {
            resId= R.drawable.mark_fill;
            Log.e("icon_star",resId+"");
        } else {
            resId= R.drawable.mark_not_fill;
            Log.e("icon_star",resId+"");

        }
    }

    /**
     * 此方法用在截屏时，弹出对话框显示截屏的图片
     */
    public AlertDialog popShotSrceenDialog(Bitmap bitmap){
        final AlertDialog cutDialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.show_cut_screen_layout, null);
        ImageView showImg = (ImageView) dialogView.findViewById(R.id.show_cut_screen_img);
        showImg.setImageBitmap(bitmap);
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();

        cutDialog.setView(dialogView);
        Window window = cutDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width=(int)(d.getWidth()*0.4);
        p.gravity = Gravity.CENTER;//设置弹出框位置
        window.setAttributes(p);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        //cutDialog.show();
        return cutDialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_back:
                // 返回
                Reviewer.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                break;
            case R.id.icon_camera:
                // 截屏
                screenshots();
                break;
            case R.id.icon_cancel:
                // 返回箭头
                Timber.i("Reviewer:: Undo button pressed");
                undo();
                break;
            case R.id.icon_star:
                // 旗子
                Timber.i("Reviewer:: Mark button pressed");
                onMark(mCurrentCard);
                refreshActionBar();
                setIconStarImg();
                icon_star.setImageResource(resId);
                break;
            case R.id.icon_more:
                // 更多
                Timber.i("Reviewer:: icon_more button ");
                showMore();
                break;
        }
    }

/////////////////////////////////////dx End
}
