/****************************************************************************************
 * Copyright (c) 2009 Andrew Dubya <andrewdubya@gmail.com>                              *
 * Copyright (c) 2009 Nicolas Raoul <nicolas.raoul@gmail.com>                           *
 * Copyright (c) 2009 Edu Zamora <edu.zasu@gmail.com>                                   *
 * Copyright (c) 2009 Daniel Svard <daniel.svard@gmail.com>                             *
 * Copyright (c) 2010 Norbert Nagold <norbert.nagold@gmail.com>                         *
 * Copyright (c) 2014 Timothy Rae <perceptualchaos2@gmail.com>
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

package com.ichi2yiji.anki;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.StudyOptionsFragment.StudyOptionsListener;
import com.ichi2yiji.anki.dialogs.AsyncDialogFragment;
import com.ichi2yiji.anki.dialogs.ConfirmationDialog;
import com.ichi2yiji.anki.dialogs.CustomStudyDialog;
import com.ichi2yiji.anki.dialogs.DatabaseErrorDialog;
import com.ichi2yiji.anki.dialogs.DeckPickerBackupNoSpaceLeftDialog;
import com.ichi2yiji.anki.dialogs.DeckPickerConfirmDeleteDeckDialog;
import com.ichi2yiji.anki.dialogs.DeckPickerContextMenu;
import com.ichi2yiji.anki.dialogs.DeckPickerExportCompleteDialog;
import com.ichi2yiji.anki.dialogs.DeckPickerNoSpaceLeftDialog;
import com.ichi2yiji.anki.dialogs.DialogHandler;
import com.ichi2yiji.anki.dialogs.ExportDialog;
import com.ichi2yiji.anki.dialogs.ImportDialog;
import com.ichi2yiji.anki.dialogs.MediaCheckDialog;
import com.ichi2yiji.anki.dialogs.SyncErrorDialog;
import com.ichi2yiji.anki.exception.ConfirmModSchemaException;
import com.ichi2yiji.anki.exception.DeckRenameException;
import com.ichi2yiji.anki.receiver.SdCardReceiver;
import com.ichi2yiji.anki.util.OSSUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.anki.widgets.DeckAdapter;
import com.ichi2yiji.async.Connection;
import com.ichi2yiji.async.Connection.Payload;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.async.DeckTask.TaskData;
import com.ichi2yiji.compat.CompatHelper;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Sched;
import com.ichi2yiji.libanki.Utils;
import com.ichi2yiji.themes.StyledProgressDialog;
import com.ichi2yiji.themes.Themes;
import com.ichi2yiji.utils.SPUtil;
import com.ichi2yiji.utils.VersionUtils;
import com.ichi2yiji.widget.WidgetStatus;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.xutils.common.Callback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import timber.log.Timber;

public class DeckPicker extends NavigationDrawerActivity implements
        StudyOptionsListener, DatabaseErrorDialog.DatabaseErrorDialogListener,
        SyncErrorDialog.SyncErrorDialogListener, ImportDialog.ImportDialogListener,
        MediaCheckDialog.MediaCheckDialogListener, ExportDialog.ExportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback, CustomStudyDialog.CustomStudyListener {


    //*******************************************************************//
    //*******************************************************************//
    private WebView webView;

    public static final int DECK_YIJI_FILTER_OPTIONS = 301;
    //*******************************************************************//
    //*******************************************************************//
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    File downloadDecks = new File(ROOT_PATH + "/Chaojiyiji/downloadDecks");
//    private Receiver myReceiver;
    private InputDeckReceiver inputDeckReceiver;

    private String mImportPath;
    private String inputingDeckname;
    private String inputingDeckFilepath;

    public static final String EXTRA_DECK_ID = "deckId";

    public static final int RESULT_MEDIA_EJECTED = 202;
    public static final int RESULT_DB_ERROR = 203;



    /**
     * Available options performed by other activities
     */

    private static final int REQUEST_STORAGE_PERMISSION = 0;
    public static final int REPORT_FEEDBACK = 4;
    private static final int LOG_IN_FOR_SYNC = 6;
    private static final int SHOW_INFO_WELCOME = 8;
    private static final int SHOW_INFO_NEW_VERSION = 9;
    private static final int REPORT_ERROR = 10;
    public static final int SHOW_STUDYOPTIONS = 11;
    private static final int ADD_NOTE = 12;
    private static final int ADD_SHARED_DECKS = 15;

    // For automatic syncing
    // 10 minutes in milliseconds.
    public static final long AUTOMATIC_SYNC_MIN_INTERVAL = 600000;

    private MaterialDialog mProgressDialog;
    private View mStudyoptionsFrame;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mRecyclerViewLayoutManager;
    private DeckAdapter mDeckListAdapter;
    private FloatingActionsMenu mActionsMenu;   // Note this will be null below SDK 14

    private TextView mTodayTextView;

    private BroadcastReceiver mUnmountReceiver = null;

    private long mContextMenuDid;

    private EditText mDialogEditText;

    // flag asking user to do a full sync which is used in upgrade path
    boolean mRecommendFullSync = false;

    // flag keeping track of when the app has been paused
    private boolean mActivityPaused = false;

    /**
     * Flag to indicate whether the activity will perform a sync in its onResume.
     * Since syncing closes the database, this flag allows us to avoid doing any
     * work in onResume that might use the database and go straight to syncing.
     */
    private boolean mSyncOnResume = false;

    /**
     * Keep track of which deck was last given focus in the deck list. If we find that this value
     * has changed between deck list refreshes, we need to recenter the deck list to the new current
     * deck.
     */
    private long mFocusedDeck;



    // ----------------------------------------------------------------------------
    // LISTENERS
    // ----------------------------------------------------------------------------

    private final OnClickListener mDeckExpanderClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Long did = (Long) view.getTag();
            if (getCol().getDecks().children(did).size() > 0) {
                getCol().getDecks().collpase(did);
                updateDeckList();
                dismissAllDialogFragments();
            }
        }
    };

    private final OnClickListener mDeckClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            long deckId = (long) v.getTag();
            Timber.i("DeckPicker:: Selected deck with id %d", deckId);
            if (mActionsMenu != null && mActionsMenu.isExpanded()) {
                mActionsMenu.collapse();
            }
            handleDeckSelection(deckId, false);
            if (mFragmented || !CompatHelper.isLollipop()) {
                // Calling notifyDataSetChanged() will update the color of the selected deck.
                // This interferes with the ripple effect, so we don't do it if lollipop and not tablet view
                mDeckListAdapter.notifyDataSetChanged();
            }
        }
    };

    private final OnClickListener mCountsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            long deckId = (long) v.getTag();
            Timber.i("DeckPicker:: Selected deck with id %d", deckId);
            if (mActionsMenu != null && mActionsMenu.isExpanded()) {
                mActionsMenu.collapse();
            }
            handleDeckSelection(deckId, true);
            if (mFragmented || !CompatHelper.isLollipop()) {
                // Calling notifyDataSetChanged() will update the color of the selected deck.
                // This interferes with the ripple effect, so we don't do it if lollipop and not tablet view
                mDeckListAdapter.notifyDataSetChanged();
            }
        }
    };

    private final View.OnLongClickListener mDeckLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            long deckId = (long) v.getTag();
            Timber.i("DeckPicker:: Long tapped on deck with id %d", deckId);
            mContextMenuDid = deckId;
            showDialogFragment(DeckPickerContextMenu.newInstance(deckId));
            return true;
        }
    };

    DeckTask.TaskListener mImportAddListener = new DeckTask.TaskListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            String message = "";
            Resources res = getResources();
//            if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
//            if (result != null && result.getBoolean()) {
//                int count = result.getInt();
//                if (count < 0) {
//                    if (count == -2) {
//                        message = res.getString(R.string.import_log_no_apkg);
//                    } else {
//                        message = res.getString(R.string.import_log_error);
//                    }
//                    showSimpleMessageDialog(message, true);
//                } else {
//                    message = res.getString(R.string.import_log_success, count);
//                    showSimpleMessageDialog(message);
//                    updateDeckList();
//                }
//            } else {
//                showSimpleMessageDialog(res.getString(R.string.import_log_error));
//            }
//            // delete temp file if necessary and reset import path so that it's not incorrectly imported next time
//            // Activity starts
//            if (getIntent().getBooleanExtra("deleteTempFile", false)) {
//                new File(mImportPath).delete();
//            }
//            mImportPath = null;
            updateDeckList();
            disappearProgressCircle();
            showInputOkAndDisappear();

//            导入后删除文件
            if(inputingDeckFilepath != null){
                File file = new File(inputingDeckFilepath);
                file.delete();
            }

        }


        @Override
        public void onPreExecute() {
//            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
//                mProgressDialog = StyledProgressDialog.show(DeckPicker.this,
//                        getResources().getString(R.string.import_title),
//                        getResources().getString(R.string.import_importing), false);
//            }
            showInputProgressCircle(inputingDeckname, "导入中...");
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
//            mProgressDialog.setContent(values[0].getString());
        }


        @Override
        public void onCancelled() {
        }
    };

    DeckTask.TaskListener mImportReplaceListener = new DeckTask.TaskListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Resources res = getResources();
            if (result != null && result.getBoolean()) {
                int code = result.getInt();
                if (code == -2) {
                    // not a valid apkg file
                    showSimpleMessageDialog(res.getString(R.string.import_log_no_apkg));
                }
                updateDeckList();
            } else {
                showSimpleMessageDialog(res.getString(R.string.import_log_no_apkg), true);
            }
            // delete temp file if necessary and reset import path so that it's not incorrectly imported next time
            // Activity starts
            if (getIntent().getBooleanExtra("deleteTempFile", false)) {
                new File(mImportPath).delete();
            }
            mImportPath = null;
        }


        @Override
        public void onPreExecute() {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this,
                        getResources().getString(R.string.import_title),
                        getResources().getString(R.string.import_importing), false);
            }
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
            mProgressDialog.setContent(values[0].getString());
        }


        @Override
        public void onCancelled() {
        }
    };

    DeckTask.TaskListener mExportListener = new DeckTask.TaskListener() {

        @Override
        public void onPreExecute() {
            mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                    getResources().getString(R.string.export_in_progress), false);
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            String exportPath = result.getString();
            if (exportPath != null) {
                showAsyncDialogFragment(DeckPickerExportCompleteDialog.newInstance(exportPath));
            } else {
                Themes.showThemedToast(DeckPicker.this, getResources().getString(R.string.export_unsuccessful), true);
            }
        }


        @Override
        public void onProgressUpdate(TaskData... values) {
        }


        @Override
        public void onCancelled() {
        }
    };


    // ----------------------------------------------------------------------------
    // ANDROID ACTIVITY METHODS
    // ----------------------------------------------------------------------------

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        Timber.d("onCreate()");
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
        // Open Collection on UI thread while splash screen is showing
        boolean colOpen = firstCollectionOpen();

        // Then set theme and content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen01);

        applyKitKatTranslucency();
        //***************************************//
        //***************************************//
//        String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/";
//        CopyRawToDataForInitDeck.readFromRaw(this, R.raw.chusanyingyu, outpath, "英语四级词汇.apkg");

//        downloadTodirDecks("http://deckstest.oss-cn-qingdao.aliyuncs.com/EnglishWord/英语四级词汇.apkg");
        //预下载英语四级词汇牌组
        downloadTodirDecks("http://deckstest.oss-cn-qingdao.aliyuncs.com/EnglishWord/%E5%88%9D%E4%B8%89%E8%8B%B1%E8%AF%AD%E5%8D%95%E8%AF%8D.apkg");

        //初始化
        webView=(WebView)findViewById(R.id.webView);
        //加载一张网页 http://www.baidu.com
        //webView.loadUrl("http://tts6.tarena.com.cn");
        //加载assets目录下的一张静态网页E:\AndroidStudioProjects\aikajiyi1.01\AnkiDroid\src\main\assets\testMUI_6.1_ios/z9_deckPicker_1_ios.html
        webView.loadUrl("file:///android_asset/testMUI_888.01/AA09_deckPicker_1_copy.html");

//        webView.loadUrl("file:///android_asset/HelloMUI_1/index.html");
        //开启webView的javascript功能
        //WebSettings中封装着webView的基本设置
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebViewClient
        webView.setWebViewClient(new WebViewClient(){
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                doOnFirstLoadedWebview();
            }
        });
        //WebChromeClient
        webView.setWebChromeClient(new WebChromeClient(){

        });
        //给webView添加javascript的调用接口
        webView.addJavascriptInterface(new MyObject(), "xuming");

        //***************************************//
        //***************************************//



        View mainView = findViewById(android.R.id.content);

        // check, if tablet layout
        mStudyoptionsFrame = findViewById(R.id.studyoptions_fragment);
        // set protected variable from NavigationDrawerActivity
        mFragmented = mStudyoptionsFrame != null && mStudyoptionsFrame.getVisibility() == View.VISIBLE;

        registerExternalStorageListener();

        // create inherited navigation drawer layout here so that it can be used by parent class
        initNavigationDrawer(mainView);
        setTitle(getResources().getString(R.string.app_name));

        //接收推送后下载文件的广播
//        myReceiver = new Receiver();
//        IntentFilter intentFilter = new IntentFilter("com.ankideck.ReceivedUrlFromPush");
//        registerReceiver(myReceiver, intentFilter);

        //确定导入牌组时将发送的广播
        inputDeckReceiver = new InputDeckReceiver();
        IntentFilter filter = new IntentFilter("comfirm input deck");
        registerReceiver(inputDeckReceiver, filter);

        //页面启动的时候去请求OSS数据，将AnkiChina的牌组数据预加载
        getDataFromAnkiChina();


//        mRecyclerView = (RecyclerView) findViewById(R.id.files);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
//
//        // specify a LinearLayoutManager for the RecyclerView
//        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
//
//        // create and set an adapter for the RecyclerView
//        mDeckListAdapter = new DeckAdapter(getLayoutInflater(), this);
//        mDeckListAdapter.setDeckClickListener(mDeckClickListener);
//        mDeckListAdapter.setCountsClickListener(mCountsClickListener);
//        mDeckListAdapter.setDeckExpanderClickListener(mDeckExpanderClickListener);
//        mDeckListAdapter.setDeckLongClickListener(mDeckLongClickListener);
//        mRecyclerView.setAdapter(mDeckListAdapter);

        // Setup the FloatingActionButtons
//        mActionsMenu = (FloatingActionsMenu) findViewById(R.id.add_content_menu);
//        if (mActionsMenu != null) {
//            configureFloatingActionsMenu();
//        } else {
//            // FloatingActionsMenu only works properly on Android 14+ so fallback on a context menu below API 14
//            Timber.w("Falling back on design support library FloatingActionButton");
//            android.support.design.widget.FloatingActionButton addButton;
//            addButton = (android.support.design.widget.FloatingActionButton)findViewById(R.id.add_note_action);
//            addButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CompatHelper.getCompat().supportAddContentMenu(DeckPicker.this);
//                }
//            });
//        }
//
//        mTodayTextView = (TextView) findViewById(R.id.today_stats_text_view);

        // Hide the fragment until the counts have been loaded so that the Toolbar fills the whole screen on tablets
//        if (mFragmented) {
//            mStudyoptionsFrame.setVisibility(View.GONE);
//        }

        // Show any necessary dialogs (e.g. changelog, special messages, etc)
//        if (colOpen) {
//            showStartupScreensAndDialogs(preferences, 0);
//        }
    }



    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

//            mTintManager.setStatusBarTintResource(R.color.material_top_blue);//通知栏所需颜色
//            mTintManager.setStatusBarTintResource(R.color.test5ru5ruu5ru55443543);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

//添加方法用于显示和隐藏导入进度条；------begin

    public void showInputProgressCircle(String title, String action) {

        webView.loadUrl("javascript:showInDownload('" + title + "','" + action + "')");

    }


    public void disappearProgressCircle() {

        webView.loadUrl("javascript:hideBox()");

    }


    public void showInputOkAndDisappear() {

        webView.loadUrl("javascript:disappearOKBox()");

    }
    //添加方法用于显示和隐藏导入进度条；------end


    /**
     * Try to open the Collection for the first time, and do some error handling if it wasn't successful
     * @return whether or not we were successful
     */
    private boolean firstCollectionOpen() {
        if (CollectionHelper.hasStorageAccessPermission(this)) {
            // Try to open the collection
            // Todo_john firstcollectionope;
            Collection col = null;
            try {
                col = getCol();
            } catch (RuntimeException e) {
                Timber.e(e, "RuntimeException opening collection");
                AnkiDroidApp.sendExceptionReport(e, "DeckPicker.firstCollectionOpen");
            }
            // Show error dialog if collection could not be opened
            if (col == null) {
                showDatabaseErrorDialog(DatabaseErrorDialog.DIALOG_LOAD_FAILED);
                return false;
            }
        } else {
            // Request storage permission if we don't have it (e.g. on Android 6.0+)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    private void configureFloatingActionsMenu() {
        final FloatingActionButton addDeckButton = (FloatingActionButton) findViewById(R.id.add_deck_action);
        final FloatingActionButton addSharedButton = (FloatingActionButton) findViewById(R.id.add_shared_action);
        final FloatingActionButton addNoteButton = (FloatingActionButton) findViewById(R.id.add_note_action);
        addDeckButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionsMenu == null) {
                    return;
                }
                mActionsMenu.collapse();
                mDialogEditText = new EditText(DeckPicker.this);
                mDialogEditText.setSingleLine(true);
                // mDialogEditText.setFilters(new InputFilter[] { mDeckNameFilter });
                new MaterialDialog.Builder(DeckPicker.this)
                        .title(R.string.new_deck)
                        .positiveText(R.string.dialog_ok)
                        .customView(mDialogEditText, true)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                String deckName = mDialogEditText.getText().toString();
                                Timber.i("DeckPicker:: Creating new deck...");
                                getCol().getDecks().id(deckName, true);
                                CardBrowser.clearSelectedDeck();
                                updateDeckList();
                            }
                        })
                        .negativeText(R.string.dialog_cancel)
                        .show();
            }
        });
        addSharedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsMenu.collapse();
                addSharedDeck();
            }
        });
        addNoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsMenu.collapse();
                addNote();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Null check to prevent crash on API23 when we don't have required permission to access db
        if (getCol() == null) {
            return false;
        }
        // Show / hide undo
        if (mFragmented || !getCol().undoAvailable()) {
            menu.findItem(R.id.action_undo).setVisible(false);
        } else {
            Resources res = getResources();
            menu.findItem(R.id.action_undo).setVisible(true);
            String undo = res.getString(R.string.studyoptions_congrats_undo, getCol().undoName(res));
            menu.findItem(R.id.action_undo).setTitle(undo);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_picker, menu);
        boolean sdCardAvailable = AnkiDroidApp.isSdCardMounted();
        menu.findItem(R.id.action_sync).setEnabled(sdCardAvailable);
        menu.findItem(R.id.action_new_filtered_deck).setEnabled(sdCardAvailable);
        menu.findItem(R.id.action_check_database).setEnabled(sdCardAvailable);
        menu.findItem(R.id.action_check_media).setEnabled(sdCardAvailable);
        menu.findItem(R.id.action_empty_cards).setEnabled(sdCardAvailable);

        // Hide import, export, and restore backup on ChromeOS as users
        // don't have access to the file system.
        if (CompatHelper.isChromebook()) {
            menu.findItem(R.id.action_restore_backup).setVisible(false);
            menu.findItem(R.id.action_import).setVisible(false);
            menu.findItem(R.id.action_export).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources res = getResources();
        switch (item.getItemId()) {

            case R.id.action_undo:
                Timber.i("DeckPicker:: Undo button pressed");
                undo();
                return true;

            case R.id.action_sync:
                Timber.i("DeckPicker:: Sync button pressed");
                sync();
                return true;

            case R.id.action_import:
                Timber.i("DeckPicker:: Import button pressed");
                showImportDialog(ImportDialog.DIALOG_IMPORT_HINT);
                return true;

            case R.id.action_new_filtered_deck:
                Timber.i("DeckPicker:: New filtered deck button pressed");
                mDialogEditText = new EditText(DeckPicker.this);
                ArrayList<String> names = getCol().getDecks().allNames();
                int n = 1;
                String name = String.format("%s %d", res.getString(R.string.filtered_deck_name), n);
                while (names.contains(name)) {
                    n++;
                    name = String.format("%s %d", res.getString(R.string.filtered_deck_name), n);
                }
                mDialogEditText.setText(name);
                // mDialogEditText.setFilters(new InputFilter[] { mDeckNameFilter });
                new MaterialDialog.Builder(DeckPicker.this)
                        .title(res.getString(R.string.new_deck))
                        .customView(mDialogEditText, true)
                        .positiveText(res.getString(R.string.create))
                        .negativeText(res.getString(R.string.dialog_cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                String filteredDeckName = mDialogEditText.getText().toString();
                                Timber.i("DeckPicker:: Creating filtered deck...");
                                getCol().getDecks().newDyn(filteredDeckName);
                                openStudyOptions(true);
                            }
                        })
                        .show();
                return true;

            case R.id.action_check_database:
                Timber.i("DeckPicker:: Check database button pressed");
                showDatabaseErrorDialog(DatabaseErrorDialog.DIALOG_CONFIRM_DATABASE_CHECK);
                return true;

            case R.id.action_check_media:
                Timber.i("DeckPicker:: Check media button pressed");
                showMediaCheckDialog(MediaCheckDialog.DIALOG_CONFIRM_MEDIA_CHECK);
                return true;

            case R.id.action_empty_cards:
                Timber.i("DeckPicker:: Empty cards button pressed");
                handleEmptyCards();
                return true;

            case R.id.action_model_browser_open:
                Timber.i("DeckPicker:: Model browser button pressed");
                Intent noteTypeBrowser = new Intent(this, ModelBrowser.class);
                startActivityForResultWithAnimation(noteTypeBrowser, 0, ActivityTransitionAnimation.LEFT);
                //dx add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                return true;

            case R.id.action_restore_backup:
                Timber.i("DeckPicker:: Restore from backup button pressed");
                showDatabaseErrorDialog(DatabaseErrorDialog.DIALOG_CONFIRM_RESTORE_BACKUP);
                return true;

            case R.id.action_export:
                Timber.i("DeckPicker:: Export collection button pressed");
                String msg = getResources().getString(R.string.confirm_apkg_export);
                showDialogFragment(ExportDialog.newInstance(msg));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_MEDIA_EJECTED) {
            onSdCardNotMounted();
            return;
        } else if (resultCode == RESULT_DB_ERROR) {
            handleDbError();
            return;
        }

        if (requestCode == REPORT_ERROR) {
            showStartupScreensAndDialogs(AnkiDroidApp.getSharedPrefs(getBaseContext()), 4);
        } else if (requestCode == SHOW_INFO_WELCOME || requestCode == SHOW_INFO_NEW_VERSION) {
            if (resultCode == RESULT_OK) {
                showStartupScreensAndDialogs(AnkiDroidApp.getSharedPrefs(getBaseContext()),
                        requestCode == SHOW_INFO_WELCOME ? 2 : 3);
            } else {
                finishWithAnimation();
            }
        } else if (requestCode == REPORT_FEEDBACK && resultCode == RESULT_OK) {
        } else if (requestCode == LOG_IN_FOR_SYNC && resultCode == RESULT_OK) {
            mSyncOnResume = true;
        } else if (requestCode == ADD_SHARED_DECKS) {
            if (intent != null) {
                mImportPath = intent.getStringExtra("importPath");
            }
            if (colIsOpen() && mImportPath != null) {
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT, mImportAddListener, new TaskData(mImportPath, true));
                mImportPath = null;
            }
        } else if ((requestCode == REQUEST_REVIEW || requestCode == SHOW_STUDYOPTIONS)
                && resultCode == Reviewer.RESULT_NO_MORE_CARDS) {
            // Show a message when reviewing has finished
            int[] studyOptionsCounts = getCol().getSched().counts();
            if (studyOptionsCounts[0] + studyOptionsCounts[1] + studyOptionsCounts[2] == 0) {
                showSimpleSnackbar(R.string.studyoptions_congrats_finished, false);
            } else {
                showSimpleSnackbar(R.string.studyoptions_no_cards_due, false);
            }
        }

        //艾卡共享的返回值处理
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                String filepath = intent.getStringExtra("PathFromDownLoadDecks");
                inputingDeckname = filepath.substring(filepath.lastIndexOf("/")+1);
                Log.e("filepath", ">>>>>>>>>>>>>>>>>>>>"+ filepath);
                inputingDeckFilepath = filepath;
                importAdd(filepath);

            }
        }
        //Anki共享的返回值处理
        if (requestCode == 0000){
            if (resultCode == RESULT_OK){
                String filepath = intent.getStringExtra("PathFromDownLoadDecksFromAnki");
                inputingDeckname = filepath.substring(filepath.lastIndexOf("/")+1);
                Log.e("filepath", ">>>>>>>>>>>>>>>>>>>>"+ filepath);
                inputingDeckFilepath = filepath;
                importAdd(filepath);

            }
        }
    }


    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION && permissions.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showStartupScreensAndDialogs(AnkiDroidApp.getSharedPrefs(this), 0);
            } else {
                showSimpleSnackbar(R.string.directory_inaccessible, false);
            }
        }
    }


    @Override
    protected void onResume() {
        Timber.d("onResume()");
        super.onResume();
        mActivityPaused = false;
        if (mSyncOnResume) {
            sync();
            mSyncOnResume = false;
        } else if (colIsOpen()) {
            selectNavigationItem(DRAWER_DECK_PICKER);
            updateDeckList();
            setTitle(getResources().getString(R.string.app_name));
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("mContextMenuDid", mContextMenuDid);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mContextMenuDid = savedInstanceState.getLong("mContextMenuDid");
    }


    @Override
    protected void onPause() {
        Timber.d("onPause()");
        mActivityPaused = true;
        super.onPause();
    }


    @Override
    protected void onStop() {
        Timber.d("onStop()");
        super.onStop();
        if (colIsOpen()) {
            WidgetStatus.update(this);
            UIUtils.saveCollectionInBackground(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnmountReceiver != null) {
            unregisterReceiver(mUnmountReceiver);
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Timber.d("onDestroy()");
//        unregisterReceiver(myReceiver);
        unregisterReceiver(inputDeckReceiver);
    }

    private void automaticSync() {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());

        // Check whether the option is selected, the user is signed in and last sync was AUTOMATIC_SYNC_TIME ago
        // (currently 10 minutes)
        String hkey = preferences.getString("hkey", "");
        long lastSyncTime = preferences.getLong("lastSyncTime", 0);
        if (hkey.length() != 0 && preferences.getBoolean("automaticSyncMode", false) &&
                Connection.isOnline() && Utils.intNow(1000) - lastSyncTime > AUTOMATIC_SYNC_MIN_INTERVAL) {
            sync();
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            super.onBackPressed();
        } else {
            Timber.i("Back key pressed");
            if (mActionsMenu != null && mActionsMenu.isExpanded()) {
                mActionsMenu.collapse();
            } else {
                automaticSync();
                finishWithAnimation();
            }
        }
    }


    private void finishWithAnimation() {
        super.finishWithAnimation(ActivityTransitionAnimation.DOWN);
    }


    // ----------------------------------------------------------------------------
    // CUSTOM METHODS
    // ----------------------------------------------------------------------------


    /**
     * Perform the following tasks:
     * Automatic backup
     * loadStudyOptionsFragment() if tablet
     * Automatic sync
     */
    private void onFinishedStartup() {
        // create backup in background if needed
        BackupManager.performBackupInBackground(getCol().getPath());

        // Force a full sync if flag was set in upgrade path, asking the user to confirm if necessary
        if (mRecommendFullSync) {
            mRecommendFullSync = false;
            try {
                getCol().modSchema();
            } catch (ConfirmModSchemaException e) {
                // If libanki determines it's necessary to confirm the full sync then show a confirmation dialog
                // We have to show the dialog via the DialogHandler since this method is called via a Loader
                Resources res = getResources();
                Message handlerMessage = Message.obtain();
                handlerMessage.what = DialogHandler.MSG_SHOW_FORCE_FULL_SYNC_DIALOG;
                Bundle handlerMessageData = new Bundle();
                handlerMessageData.putString("message", res.getString(R.string.full_sync_confirmation_upgrade) +
                        "\n\n" + res.getString(R.string.full_sync_confirmation));
                handlerMessage.setData(handlerMessageData);
                getDialogHandler().sendMessage(handlerMessage);
            }
        }
        // Open StudyOptionsFragment if in fragmented mode
        if (mFragmented) {
            loadStudyOptionsFragment(false);
        }
        automaticSync();
    }

    @Override
    protected void onCollectionLoadError() {
//        getDialogHandler().sendEmptyMessage(DialogHandler.MSG_SHOW_COLLECTION_LOADING_ERROR_DIALOG);
    }


    public void addNote() {
        Preferences.COMING_FROM_ADD = true;
        Intent intent = new Intent(DeckPicker.this, NoteEditor.class);
        intent.putExtra(NoteEditor.EXTRA_CALLER, NoteEditor.CALLER_DECKPICKER);
        startActivityForResultWithAnimation(intent, ADD_NOTE, ActivityTransitionAnimation.LEFT);
    }


    private void showStartupScreensAndDialogs(SharedPreferences preferences, int skip) {
        if (!AnkiDroidApp.isSdCardMounted()) {
            // SD card not mounted
            onSdCardNotMounted();
        } else if (!CollectionHelper.isCurrentAnkiDroidDirAccessible(this)) {
            // AnkiDroid directory inaccessible
            Intent i = new Intent(this, Preferences.class);
            startActivityWithoutAnimation(i);
            showSimpleSnackbar(R.string.directory_inaccessible, false);
        } else if (!BackupManager.enoughDiscSpace(CollectionHelper.getCurrentAnkiDroidDirectory(this))) {
            // Not enough space to do backup
            showDialogFragment(DeckPickerNoSpaceLeftDialog.newInstance());
        } else if (preferences.getBoolean("noSpaceLeft", false)) {
            // No space left
            showDialogFragment(DeckPickerBackupNoSpaceLeftDialog.newInstance());
            preferences.edit().putBoolean("noSpaceLeft", false).commit();
        } else if (preferences.getString("lastVersion", "").equals("")) {
            // Fresh install
            preferences.edit().putString("lastVersion", VersionUtils.getPkgVersionName()).commit();
            onFinishedStartup();
        } else if (skip < 2 && !preferences.getString("lastVersion", "").equals(VersionUtils.getPkgVersionName())) {
            // AnkiDroid is being updated and a collection already exists. We check if we are upgrading
            // to a version that contains additions to the database integrity check routine that we would
            // like to run on all collections. A missing version number is assumed to be a fresh
            // installation of AnkiDroid and we don't run the check.
            int current = VersionUtils.getPkgVersionCode();
            int previous;
            if (!preferences.contains("lastUpgradeVersion")) {
                // Fresh install
                previous = current;
            } else {
                try {
                    previous = preferences.getInt("lastUpgradeVersion", current);
                } catch (ClassCastException e) {
                    // Previous versions stored this as a string.
                    String s = preferences.getString("lastUpgradeVersion", "");
                    // The last version of AnkiDroid that stored this as a string was 2.0.2.
                    // We manually set the version here, but anything older will force a DB
                    // check.
                    if (s.equals("2.0.2")) {
                        previous = 40;
                    } else {
                        previous = 0;
                    }
                }
            }
            preferences.edit().putInt("lastUpgradeVersion", current).commit();
            // Delete the media database made by any version before 2.3 beta due to upgrade errors.
            // It is rebuilt on the next sync or media check
            if (previous < 20300200) {
                File mediaDb = new File(CollectionHelper.getCurrentAnkiDroidDirectory(this), "collection.media.ad.db2");
                if (mediaDb.exists()) {
                    mediaDb.delete();
                }
            }
            // Recommend the user to do a full-sync if they're upgrading from before 2.3.1beta8
            if (previous < 20301208) {
                mRecommendFullSync = true;
            }
            // Check if preference upgrade or database check required, otherwise go to new feature screen
            int upgradePrefsVersion = AnkiDroidApp.CHECK_PREFERENCES_AT_VERSION;
            int upgradeDbVersion = AnkiDroidApp.CHECK_DB_AT_VERSION;

            if (previous < upgradeDbVersion || previous < upgradePrefsVersion) {
                if (previous < upgradePrefsVersion && current >= upgradePrefsVersion) {
                    Timber.d("Upgrading preferences");
                    CompatHelper.removeHiddenPreferences(this.getApplicationContext());
                    upgradePreferences(previous);
                }
                // Integrity check loads asynchronously and then restart deckpicker when finished
                if (previous < upgradeDbVersion && current >= upgradeDbVersion) {
                    integrityCheck();
                } else if (previous < upgradePrefsVersion && current >= upgradePrefsVersion) {
                    // If integrityCheck() doesn't occur, but we did update preferences we should restart DeckPicker to
                    // proceed
                    restartActivity();
                }
            } else {
                // If no changes are required we go to the new features activity
                // There the "lastVersion" is set, so that this code is not reached again
                if (VersionUtils.isReleaseVersion()) {
                    Intent infoIntent = new Intent(this, Info.class);
                    infoIntent.putExtra(Info.TYPE_EXTRA, Info.TYPE_NEW_VERSION);

                    if (skip != 0) {
                        startActivityForResultWithAnimation(infoIntent, SHOW_INFO_NEW_VERSION,
                                ActivityTransitionAnimation.LEFT);
                    } else {
                        startActivityForResultWithoutAnimation(infoIntent, SHOW_INFO_NEW_VERSION);
                    }
                } else {
                    // Don't show new features dialog for development builds
                    preferences.edit().putString("lastVersion", VersionUtils.getPkgVersionName()).apply();
                    String ver = getResources().getString(R.string.updated_version, VersionUtils.getPkgVersionName());
                    showSnackbar(ver, true, -1, null, findViewById(R.id.root_layout), null);
                    showStartupScreensAndDialogs(preferences, 2);
                }
            }
        } else {
            // This is the main call when there is nothing special required
            onFinishedStartup();
        }
    }


    private void upgradePreferences(int previousVersionCode) {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
        // clear all prefs if super old version to prevent any errors
        if (previousVersionCode < 20300130) {
            preferences.edit().clear().commit();
        }
        // when upgrading from before 2.5alpha35
        if (previousVersionCode < 20500135) {
            // Card zooming behaviour was changed the preferences renamed
            int oldCardZoom = preferences.getInt("relativeDisplayFontSize", 100);
            int oldImageZoom = preferences.getInt("relativeImageSize", 100);
            preferences.edit().putInt("cardZoom", oldCardZoom).commit();
            preferences.edit().putInt("imageZoom", oldImageZoom).commit();
            if (!preferences.getBoolean("useBackup", true)) {
                preferences.edit().putInt("backupMax", 0).commit();
            }
            preferences.edit().remove("useBackup").commit();
            preferences.edit().remove("intentAdditionInstantAdd").commit();
        }

        if (preferences.contains("fullscreenReview")) {
            // clear fullscreen flag as we use a integer
            try {
                boolean old = preferences.getBoolean("fullscreenReview", false);
                preferences.edit().putString("fullscreenMode", old ? "1": "0").commit();
            } catch (ClassCastException e) {
                // TODO:  can remove this catch as it was only here to fix an error in the betas
                preferences.edit().remove("fullscreenMode").commit();
            }
            preferences.edit().remove("fullscreenReview").commit();
        }
    }

    private void undo() {
        String undoReviewString = getResources().getString(R.string.undo_action_review);
        final boolean isReview = undoReviewString.equals(getCol().undoName(getResources()));
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_UNDO, new DeckTask.TaskListener() {
            @Override
            public void onCancelled() {
                hideProgressBar();
            }

            @Override
            public void onPreExecute() {
                showProgressBar();
            }

            @Override
            public void onPostExecute(TaskData result) {
                hideProgressBar();
                if (isReview) {
                    openReviewer();
                }
            }

            @Override
            public void onProgressUpdate(TaskData... values) {
            }
        });
    }


    // Show dialogs to deal with database loading issues etc
    @Override
    public void showDatabaseErrorDialog(int id) {
        AsyncDialogFragment newFragment = DatabaseErrorDialog.newInstance(id);
        showAsyncDialogFragment(newFragment);
    }


    @Override
    public void showMediaCheckDialog(int id) {
        showAsyncDialogFragment(MediaCheckDialog.newInstance(id));
    }


    @Override
    public void showMediaCheckDialog(int id, List<List<String>> checkList) {
        showAsyncDialogFragment(MediaCheckDialog.newInstance(id, checkList));
    }


    /**
     * Show a specific sync error dialog
     * @param id id of dialog to show
     */
    @Override
    public void showSyncErrorDialog(int id) {
        showSyncErrorDialog(id, "");
    }

    /**
     * Show a specific sync error dialog
     * @param id id of dialog to show
     * @param message text to show
     */
    @Override
    public void showSyncErrorDialog(int id, String message) {
        AsyncDialogFragment newFragment = SyncErrorDialog.newInstance(id, message);
        showAsyncDialogFragment(newFragment);
    }

    /**
     *  Show simple error dialog with just the message and OK button. Reload the activity when dialog closed.
     * @param message
     */
    private void showSyncErrorMessage(String message) {
        String title = getResources().getString(R.string.sync_error);
        showSimpleMessageDialog(title, message, true);
    }

    /**
     *  Show a simple snackbar message or notification if the activity is not in foreground
     * @param messageResource String resource for message
     */
    private void showSyncLogMessage(int messageResource) {
        if (mActivityPaused) {
            Resources res = AnkiDroidApp.getAppResources();
            showSimpleNotification(res.getString(R.string.app_name), res.getString(messageResource));
        } else {
            showSimpleSnackbar(messageResource, false);
        }
    }


    @Override
    public void showImportDialog(int id) {
        showImportDialog(id, "");
    }


    @Override
    public void showImportDialog(int id, String message) {
        DialogFragment newFragment = ImportDialog.newInstance(id, message);
        showDialogFragment(newFragment);
    }

    public void onSdCardNotMounted() {
        Themes.showThemedToast(this, getResources().getString(R.string.sd_card_not_mounted), false);
        finishWithoutAnimation();
    }

    // Callback method to submit error report
    @Override
    public void sendErrorReport() {
        AnkiDroidApp.sendExceptionReport(new RuntimeException(), "DeckPicker.sendErrorReport");
    }


    // Callback method to handle repairing deck
    @Override
    public void repairDeck() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_REPAIR_DECK, new DeckTask.TaskListener() {

            @Override
            public void onPreExecute() {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                        getResources().getString(R.string.backup_repair_deck_progress), false);
            }


            @Override
            public void onPostExecute(DeckTask.TaskData result) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (result == null || !result.getBoolean()) {
                    Themes.showThemedToast(DeckPicker.this, getResources().getString(R.string.deck_repair_error), true);
                    onCollectionLoadError();
                }
            }


            @Override
            public void onProgressUpdate(TaskData... values) {
            }


            @Override
            public void onCancelled() {
            }
        });
    }


    // Callback method to handle database integrity check
    @Override
    public void integrityCheck() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_CHECK_DATABASE, new DeckTask.TaskListener() {
            @Override
            public void onPreExecute() {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                        getResources().getString(R.string.check_db_message), false);
            }


            @Override
            public void onPostExecute(TaskData result) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (result != null && result.getBoolean()) {
                    String msg = "";
                    long shrunk = Math.round(result.getLong() / 1024.0);
                    if (shrunk > 0.0) {
                        msg = String.format(Locale.getDefault(),
                                getResources().getString(R.string.check_db_acknowledge_shrunk), (int) shrunk);
                    } else {
                        msg = getResources().getString(R.string.check_db_acknowledge);
                    }
                    // Show result of database check and restart the app
                    showSimpleMessageDialog(msg, true);
                } else {
                    handleDbError();
                }
            }


            @Override
            public void onProgressUpdate(TaskData... values) {
            }


            @Override
            public void onCancelled() {
            }
        });
    }


    @Override
    public void mediaCheck() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_CHECK_MEDIA, new DeckTask.TaskListener() {
            @Override
            public void onPreExecute() {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                        getResources().getString(R.string.check_media_message), false);
            }


            @Override
            public void onPostExecute(TaskData result) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (result != null && result.getBoolean()) {
                    @SuppressWarnings("unchecked")
                    List<List<String>> checkList = (List<List<String>>) result.getObjArray()[0];
                    showMediaCheckDialog(MediaCheckDialog.DIALOG_MEDIA_CHECK_RESULTS, checkList);
                } else {
                    showSimpleMessageDialog(getResources().getString(R.string.check_media_failed));
                }
            }


            @Override
            public void onProgressUpdate(TaskData... values) {
            }


            @Override
            public void onCancelled() {
            }
        });
    }


    @Override
    public void deleteUnused(List<String> unused) {
        com.ichi2yiji.libanki.Media m = getCol().getMedia();
        for (String fname : unused) {
            m.removeFile(fname);
        }
        showSimpleMessageDialog(String.format(getResources().getString(R.string.check_media_deleted), unused.size()));
    }


    @Override
    public void exit() {
        CollectionHelper.getInstance().closeCollection(false);
        finishWithoutAnimation();
        System.exit(0);
    }


    public void handleDbError() {
        showDatabaseErrorDialog(DatabaseErrorDialog.DIALOG_LOAD_FAILED);
    }


    @Override
    public void restoreFromBackup(String path) {
        importReplace(path);
    }


    // Helper function to check if there are any saved stacktraces
    @Override
    public boolean hasErrorFiles() {
        for (String file : this.fileList()) {
            if (file.endsWith(".stacktrace")) {
                return true;
            }
        }
        return false;
    }


    // Sync with Anki Web
    @Override
    public void sync() {
        sync(null);
    }


    /**
     * The mother of all syncing attempts. This might be called from sync() as first attempt to sync a collection OR
     * from the mSyncConflictResolutionListener if the first attempt determines that a full-sync is required.
     *
     * @param syncConflictResolution Either "upload" or "download", depending on the user's choice.
     */
    @Override
    public void sync(String syncConflictResolution) {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
        String hkey = preferences.getString("hkey", "");
        if (hkey.length() == 0) {
            showSyncErrorDialog(SyncErrorDialog.DIALOG_USER_NOT_LOGGED_IN_SYNC);
        } else {
            Connection.sync(mSyncListener,
                    new Connection.Payload(new Object[] { hkey, preferences.getBoolean("syncFetchesMedia", true),
                            syncConflictResolution }));
        }
    }


    private Connection.TaskListener mSyncListener = new Connection.CancellableTaskListener() {

        String currentMessage;
        long countUp;
        long countDown;

        @Override
        public void onDisconnected() {
            showSyncLogMessage(R.string.youre_offline);
        }

        @Override
        public void onCancelled() {
            mProgressDialog.dismiss();
            showSyncLogMessage(R.string.sync_cancelled);
        }

        @Override
        public void onPreExecute() {
            countUp = 0;
            countDown = 0;
            // Store the current time so that we don't bother the user with a sync prompt for another 10 minutes
            // Note: getLs() in Libanki doesn't take into account the case when no changes were found, or sync cancelled
            SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
            final long syncStartTime = System.currentTimeMillis();
            preferences.edit().putLong("lastSyncTime", syncStartTime).apply();

            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = StyledProgressDialog
                        .show(DeckPicker.this, getResources().getString(R.string.sync_title),
                                getResources().getString(R.string.sync_title) + "\n"
                                        + getResources().getString(R.string.sync_up_down_size, countUp, countDown),
                                false);

                // Override the back key so that the user can cancel a sync which is in progress
                mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // Make sure our method doesn't get called twice
                        if (event.getAction()!=KeyEvent.ACTION_DOWN) {
                            return true;
                        }

                        if (keyCode == KeyEvent.KEYCODE_BACK && Connection.isCancellable() &&
                                !Connection.getIsCancelled()) {
                            // If less than 2s has elapsed since sync started then don't ask for confirmation
                            if (System.currentTimeMillis() - syncStartTime < 2000) {
                                Connection.cancel();
                                mProgressDialog.setContent(R.string.sync_cancel_message);
                                return true;
                            }
                            // Show confirmation dialog to check if the user wants to cancel the sync
                            MaterialDialog.Builder builder = new MaterialDialog.Builder(mProgressDialog.getContext());
                            builder.content(R.string.cancel_sync_confirm)
                                    .cancelable(false)
                                    .positiveText(R.string.dialog_ok)
                                    .negativeText(R.string.continue_sync)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            mProgressDialog.setContent(R.string.sync_cancel_message);
                                            Connection.cancel();
                                        }
                                    });
                            builder.show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
        }


        @Override
        public void onProgressUpdate(Object... values) {
            Resources res = getResources();
            if (values[0] instanceof Boolean) {
                // This is the part Download missing media of syncing
                int total = (Integer) values[1];
                int done = (Integer) values[2];
                values[0] = (values[3]);
                values[1] = res.getString(R.string.sync_downloading_media, done, total);
            } else if (values[0] instanceof Integer) {
                int id = (Integer) values[0];
                if (id != 0) {
                    currentMessage = res.getString(id);
                }
                if (values.length >= 3) {
                    countUp = (Long) values[1];
                    countDown = (Long) values[2];
                }
            } else if (values[0] instanceof String) {
                currentMessage = (String) values[0];
                if (values.length >= 3) {
                    countUp = (Long) values[1];
                    countDown = (Long) values[2];
                }
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                // mProgressDialog.setTitle((String) values[0]);
                mProgressDialog.setContent(currentMessage + "\n"
                        + res
                        .getString(R.string.sync_up_down_size, countUp / 1024, countDown / 1024));
            }
        }


        @SuppressLint("StringFormatMatches")
        @SuppressWarnings("unchecked")
        @Override
        public void onPostExecute(Payload data) {
            String dialogMessage = "";
            String syncMessage = "";
            Timber.d("Sync Listener onPostExecute()");
            Resources res = getResources();
            try {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (IllegalArgumentException e) {
                Timber.e(e, "Could not dismiss mProgressDialog. The Activity must have been destroyed while the AsyncTask was running");
                AnkiDroidApp.sendExceptionReport(e, "DeckPicker.onPostExecute", "Could not dismiss mProgressDialog");
            }
            syncMessage = data.message;
            if (!data.success) {
                Object[] result = (Object[]) data.result;
                if (result[0] instanceof String) {
                    String resultType = (String) result[0];
                    if (resultType.equals("badAuth")) {
                        // delete old auth information
                        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
                        Editor editor = preferences.edit();
                        editor.putString("username", "");
                        editor.putString("hkey", "");
                        editor.commit();
                        // then show not logged in dialog
                        showSyncErrorDialog(SyncErrorDialog.DIALOG_USER_NOT_LOGGED_IN_SYNC);
                    } else if (resultType.equals("noChanges")) {
                        // show no changes message, use false flag so we don't show "sync error" as the Dialog title
                        showSyncLogMessage(R.string.sync_no_changes_message);
                    } else if (resultType.equals("clockOff")) {
                        long diff = (Long) result[1];
                        if (diff >= 86100) {
                            // The difference if more than a day minus 5 minutes acceptable by ankiweb error
                            dialogMessage = res.getString(R.string.sync_log_clocks_unsynchronized, diff,
                                    res.getString(R.string.sync_log_clocks_unsynchronized_date));
                        } else if (Math.abs((diff % 3600.0) - 1800.0) >= 1500.0) {
                            // The difference would be within limit if we adjusted the time by few hours
                            // It doesn't work for all timezones, but it covers most and it's a guess anyway
                            dialogMessage = res.getString(R.string.sync_log_clocks_unsynchronized, diff,
                                    res.getString(R.string.sync_log_clocks_unsynchronized_tz));
                        } else {
                            dialogMessage = res.getString(R.string.sync_log_clocks_unsynchronized, diff, "");
                        }
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("fullSync")) {
                        if (getCol().isEmpty()) {
                            // don't prompt user to resolve sync conflict if local collection empty
                            sync("download");
                            // TODO: Also do reverse check to see if AnkiWeb collection is empty if Anki Desktop
                            // implements it
                        } else {
                            // If can't be resolved then automatically then show conflict resolution dialog
                            showSyncErrorDialog(SyncErrorDialog.DIALOG_SYNC_CONFLICT_RESOLUTION);
                        }
                    } else if (resultType.equals("dbError")  || resultType.equals("basicCheckFailed")) {
                        String repairUrl = res.getString(R.string.repair_deck);
                        dialogMessage = res.getString(R.string.sync_corrupt_database, repairUrl);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("overwriteError")) {
                        dialogMessage = res.getString(R.string.sync_overwrite_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("remoteDbError")) {
                        dialogMessage = res.getString(R.string.sync_remote_db_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("sdAccessError")) {
                        dialogMessage = res.getString(R.string.sync_write_access_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("finishError")) {
                        dialogMessage = res.getString(R.string.sync_log_finish_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("connectionError")) {
                        dialogMessage = res.getString(R.string.sync_connection_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("IOException")) {
                        handleDbError();
                    } else if (resultType.equals("genericError")) {
                        dialogMessage = res.getString(R.string.sync_generic_error);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("OutOfMemoryError")) {
                        dialogMessage = res.getString(R.string.error_insufficient_memory);
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("sanityCheckError")) {
                        dialogMessage = res.getString(R.string.sync_sanity_failed);
                        showSyncErrorDialog(SyncErrorDialog.DIALOG_SYNC_SANITY_ERROR,
                                joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("serverAbort")) {
                        // syncMsg has already been set above, no need to fetch it here.
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    } else if (resultType.equals("mediaSyncServerError")) {
                        dialogMessage = res.getString(R.string.sync_media_error_check);
                        showSyncErrorDialog(SyncErrorDialog.DIALOG_MEDIA_SYNC_ERROR,
                                joinSyncMessages(dialogMessage, syncMessage));
                    } else {
                        if (result.length > 1 && result[1] instanceof Integer) {
                            int type = (Integer) result[1];
                            switch (type) {
                                case 501:
                                    dialogMessage = res.getString(R.string.sync_error_501_upgrade_required);
                                    break;
                                case 503:
                                    dialogMessage = res.getString(R.string.sync_too_busy);
                                    break;
                                case 409:
                                    dialogMessage = res.getString(R.string.sync_error_409);
                                    break;
                                default:
                                    dialogMessage = res.getString(R.string.sync_log_error_specific,
                                            Integer.toString(type), result[2]);
                                    break;
                            }
                        } else if (result[0] instanceof String) {
                            dialogMessage = res.getString(R.string.sync_log_error_specific, -1, result[0]);
                        } else {
                            dialogMessage = res.getString(R.string.sync_generic_error);
                        }
                        showSyncErrorMessage(joinSyncMessages(dialogMessage, syncMessage));
                    }
                }
            } else {
                if (data.data[2] != null && !data.data[2].equals("")) {
                    String message = res.getString(R.string.sync_database_acknowledge) + "\n\n" + data.data[2];
                    showSimpleMessageDialog(message);
                } else if (data.data.length > 0 && data.data[0] instanceof String
                        && ((String) data.data[0]).length() > 0) {
                    String dataString = (String) data.data[0];
                    if (dataString.equals("upload")) {
                        showSyncLogMessage(R.string.sync_log_uploading_message);
                    } else if (dataString.equals("download")) {
                        showSyncLogMessage(R.string.sync_log_downloading_message);
                        // set downloaded collection as current one
                    } else {
                        showSyncLogMessage(R.string.sync_database_acknowledge);
                    }
                } else {
                    showSyncLogMessage(R.string.sync_database_acknowledge);
                }
                updateDeckList();
                WidgetStatus.update(DeckPicker.this);
                if (mFragmented) {
                    try {
                        loadStudyOptionsFragment(false);
                    } catch (IllegalStateException e) {
                        // Activity was stopped or destroyed when the sync finished. Losing the
                        // fragment here is fine since we build a fresh fragment on resume anyway.
                        Timber.w(e, "Failed to load StudyOptionsFragment after sync.");
                    }
                }
            }
        }
    };


    private String joinSyncMessages(String dialogMessage, String syncMessage) {
        // If both strings have text, separate them by a new line, otherwise return whichever has text
        if (!TextUtils.isEmpty(dialogMessage) && !TextUtils.isEmpty(syncMessage)) {
            return dialogMessage + "\n\n" + syncMessage;
        } else if (!TextUtils.isEmpty(dialogMessage)) {
            return dialogMessage;
        } else {
            return syncMessage;
        }
    }


    @Override
    public void loginToSyncServer() {
        Intent myAccount = new Intent(this, MyAccount.class);
        myAccount.putExtra("notLoggedIn", true);
        startActivityForResultWithAnimation(myAccount, LOG_IN_FOR_SYNC, ActivityTransitionAnimation.FADE);
    }


    // Callback to import a file -- adding it to existing collection
    @Override
    public void importAdd(String importPath) {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT, mImportAddListener,
                new TaskData(importPath, false));
    }


    // Callback to import a file -- replacing the existing collection
    @Override
    public void importReplace(String importPath) {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT_REPLACE, mImportReplaceListener, new TaskData(importPath));
    }


    @Override
    public void exportApkg(String filename, Long did, boolean includeSched, boolean includeMedia) {
        // get export path
        File colPath = new File(getCol().getPath());
        File exportDir = new File(colPath.getParentFile(), "export");
        exportDir.mkdirs();
        File exportPath;
        if (filename != null) {
            // filename has been explicitly specified
            exportPath = new File(exportDir, filename);
        } else if (did != null) {
            // filename not explicitly specified, but a deck has been specified so use deck name
            try {
                exportPath = new File(exportDir, getCol().getDecks().get(did).getString("name").replaceAll("\\W+", "_") + ".apkg");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else if (!includeSched) {
            // full export without scheduling is assumed to be shared with someone else -- use "All Decks.apkg"
            exportPath = new File(exportDir, "All Decks.apkg");
        } else {
            // full collection export -- use "collection.apkg"
            exportPath = new File(exportDir, colPath.getName().replace(".anki2", ".apkg"));
        }
        // add input arguments to new generic structure
        Object[] inputArgs = new Object[5];
        inputArgs[0] = getCol();
        inputArgs[1] = exportPath.getPath();
        inputArgs[2] = did;
        inputArgs[3] = includeSched;
        inputArgs[4] = includeMedia;
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_EXPORT_APKG, mExportListener, new TaskData(inputArgs));
    }


    public void emailFile(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "AnkiDroid Apkg");
        File attachment = new File(path);
        if (attachment.exists()) {
            Uri uri = Uri.fromFile(attachment);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        try {
            startActivityWithoutAnimation(intent);
        } catch (ActivityNotFoundException e) {
            Themes.showThemedToast(this, getResources().getString(R.string.no_email_client), false);
        }
    }


    /**
     * Load a new studyOptionsFragment. If withDeckOptions is true, the deck options activity will
     * be loaded on top of it. Use this flag when creating a new filtered deck to allow the user to
     * modify the filter settings before being shown the fragment. The fragment itself will handle
     * rebuilding the deck if the settings change.
     */
    private void loadStudyOptionsFragment(boolean withDeckOptions) {
        StudyOptionsFragment details = StudyOptionsFragment.newInstance(withDeckOptions);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.studyoptions_fragment, details);
        ft.commit();
    }


    public StudyOptionsFragment getFragment() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.studyoptions_fragment);
        if (frag != null && (frag instanceof StudyOptionsFragment)) {
            return (StudyOptionsFragment) frag;
        }
        return null;
    }


    /**
     * Show a message when the SD card is ejected
     */
    private void registerExternalStorageListener() {
        if (mUnmountReceiver == null) {
            mUnmountReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(SdCardReceiver.MEDIA_EJECT)) {
                        onSdCardNotMounted();
                    } else if (intent.getAction().equals(SdCardReceiver.MEDIA_MOUNT)) {
                        restartActivity();
                    }
                }
            };
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(SdCardReceiver.MEDIA_EJECT);
            iFilter.addAction(SdCardReceiver.MEDIA_MOUNT);
            registerReceiver(mUnmountReceiver, iFilter);
        }
    }


    public void addSharedDeck() {
        openUrl(Uri.parse(getResources().getString(R.string.shared_decks_url)));
    }


    private void openStudyOptions(boolean withDeckOptions) {
        if (mFragmented) {
            // The fragment will show the study options screen instead of launching a new activity.
            loadStudyOptionsFragment(withDeckOptions);
        } else {
            Intent intent = new Intent();
            intent.putExtra("withDeckOptions", withDeckOptions);
            intent.setClass(this, StudyOptionsActivity.class);
            startActivityForResultWithAnimation(intent, SHOW_STUDYOPTIONS, ActivityTransitionAnimation.LEFT);
        }
    }


    private void handleDeckSelection(long did, boolean dontSkipStudyOptions) {
        // Forget what the last used deck was in the browser
        CardBrowser.clearSelectedDeck();
        // Clear the undo history when selecting a new deck
        if (getCol().getDecks().selected() != did) {
            getCol().clearUndo();
        }
        // Select the deck
        getCol().getDecks().select(did);
        // Reset the schedule so that we get the counts for the currently selected deck
        getCol().getSched().reset();
        mFocusedDeck = did;
        // Get some info about the deck to handle special cases
        //int pos = mDeckListAdapter.findDeckPosition(did);  //先去掉，暂时不用了
        //Sched.DeckDueTreeNode deckDueTreeNode = mDeckListAdapter.getDeckList().get(pos);//先去掉，暂时不用了
        int[] studyOptionsCounts = getCol().getSched().counts();
        // Figure out what action to take
        //if (deckDueTreeNode.newCount + deckDueTreeNode.lrnCount + deckDueTreeNode.revCount > 0) {

        if (5 > 0) {
            // If there are cards to study then either go to Reviewer or StudyOptions
            if (mFragmented || dontSkipStudyOptions) {
                // Go to StudyOptions screen when tablet or deck counts area was clicked
                openStudyOptions(false);
            } else {
                // Otherwise jump straight to the reviewer
                openReviewer();
            }
        } else if (studyOptionsCounts[0] + studyOptionsCounts[1] + studyOptionsCounts[2] > 0) {
            // If there are cards due that can't be studied yet (due to the learn ahead limit) then go to study options
            openStudyOptions(false);
        } else if (getCol().getSched().newDue() || getCol().getSched().revDue()) {
            // If there are no cards to review because of the daily study limit then give "Study more" option
            showSnackbar(R.string.studyoptions_limit_reached, false, R.string.study_more, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomStudyDialog d = CustomStudyDialog.newInstance(
                            CustomStudyDialog.CONTEXT_MENU_LIMITS,
                            getCol().getDecks().selected(), true);
                    showDialogFragment(d);
                }
            }, findViewById(R.id.root_layout), mSnackbarShowHideCallback);
        } else if (getCol().getDecks().isDyn(did)) {
            // Go to the study options screen if filtered deck with no cards to study
            openStudyOptions(false);
        //} else if (deckDueTreeNode.children.size() == 0 && getCol().cardCount(new Long[]{did}) == 0) {
        } else if (2 == 0 && getCol().cardCount(new Long[]{did}) == 0) {
            // If the deck is empty and has no children then show a message saying it's empty
            final Uri helpUrl = Uri.parse(getResources().getString(R.string.link_manual_getting_started));
            mayOpenUrl(helpUrl);
            showSnackbar(R.string.empty_deck, false, R.string.help, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(helpUrl);
                }
            }, findViewById(R.id.root_layout), mSnackbarShowHideCallback);
        } else {
            // Otherwise say there are no cards scheduled to study, and give option to do custom study
            showSnackbar(R.string.studyoptions_empty_schedule, false, R.string.custom_study, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomStudyDialog d = CustomStudyDialog.newInstance(
                            CustomStudyDialog.CONTEXT_MENU_EMPTY_SCHEDULE,
                            getCol().getDecks().selected(), true);
                    showDialogFragment(d);
                }
            }, findViewById(R.id.root_layout), mSnackbarShowHideCallback);
        }
    }


    /**
     * Scroll the deck list so that it is centered on the current deck.
     *
     * @param did The deck ID of the deck to select.
     */
    private void scrollDecklistToDeck(long did) {
        int position = mDeckListAdapter.findDeckPosition(did);
        mRecyclerViewLayoutManager.scrollToPositionWithOffset(position, (mRecyclerView.getHeight() / 2));
    }


    /**
     * Launch an asynchronous task to rebuild the deck list and recalculate the deck counts. Use this
     * after any change to a deck (e.g., rename, collapse, add/delete) that needs to be reflected
     * in the deck list.
     *
     * This method also triggers an update for the widget to reflect the newly calculated counts.
     */
    private void updateDeckList() {
        //****************************************************//
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_LOAD_DECK_COUNTS, new DeckTask.TaskListener() {

            @Override
            public void onPreExecute() {
//                if (!colIsOpen()) {
//                    showProgressBar();
//                }
//                Timber.d("Refreshing deck list");
            }

            @Override
            public void onPostExecute(TaskData result) {
//                hideProgressBar();
//                // Make sure the fragment is visible
//                if (mFragmented) {
//                    mStudyoptionsFrame.setVisibility(View.VISIBLE);
//                }
//                if (result == null) {
//                    Timber.e("null result loading deck counts");
//                    onCollectionLoadError();
//                    return;
//                }
                List<Sched.DeckDueTreeNode> nodes = (List<Sched.DeckDueTreeNode>) result.getObjArray()[0];
                Gson gson = new Gson();
                String nodesStr = gson.toJson(nodes);

                Log.e("updateDeckList","nodes>>>>>>>" + nodes);
                Log.e("updateDeckList","nodesStr>>>>>>>" + nodesStr);

                //webView.loadUrl("javascript:initFirWindow('"+nodesStr+"')");


                webView.loadUrl("javascript:initFirWindow('"+null+"','"+nodesStr+"')");

                //webView.loadUrl("javascript:removeXXListeners()");
//                String str = "aa";
//                webView.loadUrl("javascript:showmark('"+str+"')");

                long tid = Thread.currentThread().getId();
                Log.i("tid", Long.toString(tid));


                //初始化在线导入的红点的显示
                boolean showmark = hasFilesInDownloadDecks();
                File[] files = downloadDecks.listFiles();
                String downloadFileName;
                StringBuffer stringBuffer = new StringBuffer();
                for(int i = 0; i < files.length; i++){
                    stringBuffer.append(files[i].getName()).append(",");
                }
                downloadFileName = String.valueOf(stringBuffer);
                if (showmark){
                    webView.loadUrl("javascript:showmark('"+downloadFileName+"')");
                }else {
                    webView.loadUrl("javascript:hidemark()");
                }

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                String testurl = pref.getString("URLTODECK", "");
                if(pref.getString("URLTODECK", "") != null && pref.getString("URLTODECK", "") != ""){
                    DownloadFromBackstage(pref.getString("URLTODECK", ""));
                    editor.remove("URLTODECK");
                    editor.commit();
                }

                Log.e("预加载牌组", ">>>>>>>>>>>>>>>>>>>>>>done!");
                String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/英语四级词汇.apkg";
                File file  = new File(outpath);

                boolean firstUse = SPUtil.getPreferences(SPUtil.TYPE_WELCOME_CONFIG, "FirstUse", false);
                if (file.exists() && firstUse){
                    inputingDeckname = "英语四级词汇.apkg";
                    importAdd(outpath);
                    SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG,"FirstUse",false);
                }



                //toOpenDrawer();

//                mDeckListAdapter.buildDeckList(nodes, getCol());
//
//                // Set the "x due in y minutes" subtitle
//                try {
//                    int eta = mDeckListAdapter.getEta(); //获取估计的学习次数；
//                    int due = mDeckListAdapter.getDue(); //获得今天所需要学习的总任务量；
//                    Resources res = getResources();
//                    if (getCol().cardCount() != -1) {  // 判断卡片总数量是否等于-1；
//                        String time = "-";
//                        if (eta != -1) {
//                            time = res.getString(R.string.time_quantity_minutes, eta);
//                        }
//                        if (getSupportActionBar() != null) {
//                            getSupportActionBar().setSubtitle(res.getQuantityString(R.plurals.deckpicker_title, due, due, time));
//                            // getQuantityString--- String The string data associated with the resource,
//                        }
//                    }
//                } catch (RuntimeException e) {
//                    Timber.e(e, "RuntimeException setting time remaining");
//                }
//
//                long current = getCol().getDecks().current().optLong("id");
//                if (mFocusedDeck != current) {
//                    scrollDecklistToDeck(current);
//                    mFocusedDeck = current;
//                }
//
//                // Update the mini statistics bar as well
//                AnkiStatsTaskHandler.createSmallTodayOverview(getCol(), mTodayTextView);
            }

            @Override
            public void onProgressUpdate(TaskData... values) {
            }

            @Override
            public void onCancelled() {
            }

        });
        //****************************************************//
//        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_LOAD_DECK_COUNTS, new DeckTask.TaskListener() {
//
//            @Override
//            public void onPreExecute() {
//                if (!colIsOpen()) {
//                    showProgressBar();
//                }
//                Timber.d("Refreshing deck list");
//            }
//
//            @Override
//            public void onPostExecute(TaskData result) {
//                hideProgressBar();
//                // Make sure the fragment is visible
//                if (mFragmented) {
//                    mStudyoptionsFrame.setVisibility(View.VISIBLE);
//                }
//                if (result == null) {
//                    Timber.e("null result loading deck counts");
//                    onCollectionLoadError();
//                    return;
//                }
//                List<Sched.DeckDueTreeNode> nodes = (List<Sched.DeckDueTreeNode>) result.getObjArray()[0];
//                mDeckListAdapter.buildDeckList(nodes, getCol());
//
//                // Set the "x due in y minutes" subtitle
//                try {
//                    int eta = mDeckListAdapter.getEta(); //获取估计的学习次数；
//                    int due = mDeckListAdapter.getDue(); //获得今天所需要学习的总任务量；
//                    Resources res = getResources();
//                    if (getCol().cardCount() != -1) {  // 判断卡片总数量是否等于-1；
//                        String time = "-";
//                        if (eta != -1) {
//                            time = res.getString(R.string.time_quantity_minutes, eta);
//                        }
//                        if (getSupportActionBar() != null) {
//                            getSupportActionBar().setSubtitle(res.getQuantityString(R.plurals.deckpicker_title, due, due, time));
//                            // getQuantityString--- String The string data associated with the resource,
//                        }
//                    }
//                } catch (RuntimeException e) {
//                    Timber.e(e, "RuntimeException setting time remaining");
//                }
//
//                long current = getCol().getDecks().current().optLong("id");
//                if (mFocusedDeck != current) {
//                    scrollDecklistToDeck(current);
//                    mFocusedDeck = current;
//                }
//
//                // Update the mini statistics bar as well
//                AnkiStatsTaskHandler.createSmallTodayOverview(getCol(), mTodayTextView);
//            }
//
//            @Override
//            public void onProgressUpdate(TaskData... values) {
//            }
//
//            @Override
//            public void onCancelled() {
//            }
//
//        });
    }

    /**
//     * 在此广播接收器中进行deck的下载，并更新UI
//     */
//    class Receiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e("DeckP-onReceive", ">>>>>>>>>>onReceived!");
//            Toast.makeText(getApplicationContext(), "有新的牌组可以在线导入了！", Toast.LENGTH_SHORT).show();
//            String url = intent.getStringExtra("URL");
//            DownloadFromBackstage(url);
//
//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences.Editor editor = pref.edit();
//            if(pref.getString("URLTODECK", "") != null && pref.getString("URLTODECK", "") != ""){
//                editor.remove("URLTODECK");
//                editor.commit();
//            }
//        }
//    }
    /**
     * 从后台下载文件到downloadDecks目录下，下载成功后更新UI
     * @param url 推送透传过来的下载地址
     */
    private void  DownloadFromBackstage(String url){
        if(url.lastIndexOf("/") != -1) {
            final String filename = url.substring(url.lastIndexOf("/")+1);//获取文件名

            //将编码格式转换成能显示汉字的UTF-8格式
            String filename2 = filename;
            try {
                filename2 = URLDecoder.decode(filename, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String filepath = downloadDecks.getAbsolutePath() + "/" + filename2;//下载文件的存储路径,最后一个“/”后的为文件名

            com.ankireader.util.ZXUtils.DownLoadFile(url, filepath, new Callback.CommonCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    Log.e("文件下载的回调onSuccess", "下载成功 ");
                    String filename = result.getName();
                    Log.e("文件下载的回调onSuccess", filename);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("文件下载的回调onError", ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.e("文件下载的回调onCancelled", cex.toString());

                }

                @Override
                public void onFinished() {
                    Log.e("文件下载的回调onFinished", "finish");
                    boolean showmark = hasFilesInDownloadDecks();
                    if (showmark) {
                        webView.loadUrl("javascript:showmark('"+filename+"')");
                    } else {
                        webView.loadUrl("javascript:hidemark()");
                    }
                }
            });
        }else {
            Log.e("DownloadFromBackstage", "从后台获取的url地址有错误");
        }
    }

    /**
     * 判断downloadDecks文件夹下是否存在文件
     */
    private boolean hasFilesInDownloadDecks(){
        if (!downloadDecks.exists()) {
            downloadDecks.mkdirs();
        }
        File[] files = downloadDecks.listFiles();
        if(files.length == 0 || files == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 遍历deck的数据库，得到相应的JSON，传给HTML页面，再次刷新牌组页面
     */
    private void loadDeckList(){

        String deckJson = "";//需要写方法获取已有牌组的JSON数据
        webView.loadUrl("javascript:initFirWindow('"+null+"', '"+deckJson+"')");
    }

    // Callback to show study options for currently selected deck
    public void showContextMenuDeckOptions() {
        // open deck options
        if (getCol().getDecks().isDyn(mContextMenuDid)) {
            // open cram options if filtered deck
            Intent i = new Intent(DeckPicker.this, FilteredDeckOptions.class);
            i.putExtra("did", mContextMenuDid);
            startActivityWithAnimation(i, ActivityTransitionAnimation.FADE);
        } else {
            // otherwise open regular options
            Intent i = new Intent(DeckPicker.this, DeckOptions.class);
            i.putExtra("did", mContextMenuDid);
            startActivityWithAnimation(i, ActivityTransitionAnimation.FADE);
        }
    }


    // Callback to show export dialog for currently selected deck
    public void showContextMenuExportDialog() {
        exportDeck(mContextMenuDid);
    }
    public void exportDeck(long did) {
        String msg;
        try {
            msg = getResources().getString(R.string.confirm_apkg_export_deck, getCol().getDecks().get(did).get("name"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        showDialogFragment(ExportDialog.newInstance(msg, did));
    }


    // Callback to show dialog to rename the current deck
    public void renameDeckDialog() {
        renameDeckDialog(mContextMenuDid);
    }

    public void renameDeckDialog(final long did) {
        final Resources res = getResources();
        mDialogEditText = new EditText(DeckPicker.this);
        mDialogEditText.setSingleLine();
        final String currentName = getCol().getDecks().name(did);
        mDialogEditText.setText(currentName);
        new MaterialDialog.Builder(DeckPicker.this)
                .title(res.getString(R.string.rename_deck))
                .customView(mDialogEditText, true)
                .positiveText(res.getString(R.string.rename))
                .negativeText(res.getString(R.string.dialog_cancel))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        String newName = mDialogEditText.getText().toString().replaceAll("\"", "");
                        Collection col = getCol();
                        if (!TextUtils.isEmpty(newName) && !newName.equals(currentName)) {
                            try {
                                col.getDecks().rename(col.getDecks().get(did), newName);
                            } catch (DeckRenameException e) {
                                // We get a localized string from libanki to explain the error
                                Themes.showThemedToast(DeckPicker.this, e.getLocalizedMessage(res), false);
                            }
                        }
                        dismissAllDialogFragments();
                        mDeckListAdapter.notifyDataSetChanged();
                        updateDeckList();
                        if (mFragmented) {
                            loadStudyOptionsFragment(false);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dismissAllDialogFragments();
                    }
                })
                .build().show();
    }


    // Callback to show confirm deck deletion dialog before deleting currently selected deck
    public void confirmDeckDeletion() {
        confirmDeckDeletion(mContextMenuDid);
    }

    public void confirmDeckDeletion(long did) {
        Resources res = getResources();
        if (!colIsOpen()) {
            return;
        }
        if (did == 1) {
            showSimpleSnackbar(R.string.delete_deck_default_deck, true);
            dismissAllDialogFragments();
            return;
        }
        // Get the number of cards contained in this deck and its subdecks
        TreeMap<String, Long> children = getCol().getDecks().children(did);
        long[] dids = new long[children.size() + 1];
        dids[0] = did;
        int i = 1;
        for (Long l : children.values()) {
            dids[i++] = l;
        }
        String ids = Utils.ids2str(dids);
        int cnt = getCol().getDb().queryScalar(
                "select count() from cards where did in " + ids + " or odid in " + ids);
        // Delete empty decks without warning
        if (cnt == 0) {
            deleteDeck(did);
            dismissAllDialogFragments();
            return;
        }
        // Otherwise we show a warning and require confirmation
        String msg;
        String deckName = "\'" + getCol().getDecks().name(did) + "\'";
        boolean isDyn = getCol().getDecks().isDyn(did);
        if (isDyn) {
            msg = String.format(res.getString(R.string.delete_cram_deck_message), deckName);
        } else {
            msg = res.getQuantityString(R.plurals.delete_deck_message, cnt, deckName, cnt);
        }
        showDialogFragment(DeckPickerConfirmDeleteDeckDialog.newInstance(msg));
    }


    // Callback to delete currently selected deck
    public void deleteContextMenuDeck() {
        deleteDeck(mContextMenuDid);
    }
    public void deleteDeck(final long did) {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DELETE_DECK, new DeckTask.TaskListener() {
            // Flag to indicate if the deck being deleted is the current deck.
            private boolean removingCurrent;

            @Override
            public void onPreExecute() {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                        getResources().getString(R.string.delete_deck), false);
                if (did == getCol().getDecks().current().optLong("id")) {
                    removingCurrent = true;
                }
            }


            @SuppressWarnings("unchecked")
            @Override
            public void onPostExecute(TaskData result) {
                if (result == null) {
                    return;
                }
                // In fragmented mode, if the deleted deck was the current deck, we need to reload
                // the study options fragment with a valid deck and re-center the deck list to the
                // new current deck. Otherwise we just update the list normally.
                if (mFragmented && removingCurrent) {
                    updateDeckList();
                    openStudyOptions(false);
                } else {
                    updateDeckList();
                }

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    try {
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        Timber.e(e, "onPostExecute - Exception dismissing dialog");
                    }
                }
                CardBrowser.clearSelectedDeck();
                // TODO: if we had "undo delete note" like desktop client then we won't need this.
                getCol().clearUndo();
            }


            @Override
            public void onProgressUpdate(TaskData... values) {
            }


            @Override
            public void onCancelled() {
            }
        }, new TaskData(did));
    }

    /**
     * Show progress bars and rebuild deck list on completion
     */
    DeckTask.TaskListener mSimpleProgressListener = new DeckTask.TaskListener() {

        @Override
        public void onPreExecute() {
            showProgressBar();
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            updateDeckList();
            if (mFragmented) {
                loadStudyOptionsFragment(false);
            }
        }


        @Override
        public void onProgressUpdate(TaskData... values) {
        }


        @Override
        public void onCancelled() {
        }
    };

    public void rebuildFiltered() {
        getCol().getDecks().select(mContextMenuDid);
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_REBUILD_CRAM, mSimpleProgressListener,
                new DeckTask.TaskData(mFragmented));
    }

    public void emptyFiltered() {
        getCol().getDecks().select(mContextMenuDid);
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_EMPTY_CRAM, mSimpleProgressListener,
                new DeckTask.TaskData(mFragmented));
    }

    @Override
    public void onAttachedToWindow() {

        if (!mFragmented) {
            Window window = getWindow();
            window.setFormat(PixelFormat.RGBA_8888);
        }
    }


    @Override
    public void onRequireDeckListUpdate() {
        updateDeckList();
    }


    private void openReviewer() {
        Intent reviewer = new Intent(this, Reviewer.class);
        startActivityForResultWithAnimation(reviewer, REQUEST_REVIEW, ActivityTransitionAnimation.LEFT);
        getCol().startTimebox();
    }

    @Override
    public void onCreateCustomStudySession() {
        updateDeckList();
        openStudyOptions(false);
    }

    @Override
    public void onExtendStudyLimits() {
        if (mFragmented) {
            getFragment().refreshInterface(true);
        }
        updateDeckList();
    }

    /**
     * FAB can't be animated to move out of the way of the snackbar button on API < 11
     */
    Snackbar.Callback mSnackbarShowHideCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            if (!CompatHelper.isHoneycomb()) {
                final android.support.design.widget.FloatingActionButton b;
                b = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add_note_action);
                b.setEnabled(true);
            }
        }

        @Override
        public void onShown(Snackbar snackbar) {
            if (!CompatHelper.isHoneycomb()) {
                final android.support.design.widget.FloatingActionButton b;
                b = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add_note_action);
                b.setEnabled(false);
            }
        }
    };

    public void handleEmptyCards() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_FIND_EMPTY_CARDS, new DeckTask.Listener() {
            @Override
            public void onPreExecute(DeckTask task) {
                mProgressDialog = StyledProgressDialog.show(DeckPicker.this, "",
                        getResources().getString(R.string.emtpy_cards_finding), false);
            }

            @Override
            public void onPostExecute(DeckTask task, TaskData result) {
                final List<Long> cids = (List<Long>) result.getObjArray()[0];
                if (cids.size() == 0) {
                    showSimpleMessageDialog(getResources().getString(R.string.empty_cards_none));
                } else {
                    String msg = String.format(getResources().getString(R.string.empty_cards_count), cids.size());
                    Bundle bundle = new Bundle();
                    bundle.putString("message", msg);
                    bundle.putString("title", "");
                    ConfirmationDialog dialog = ConfirmationDialog.newInstance(bundle);
                    dialog.setRunConfirmation(new Runnable() {
                        @Override
                        public void run() {
                            getCol().remCards(Utils.arrayList2array(cids));
                            showSimpleSnackbar(String.format(
                                    getResources().getString(R.string.empty_cards_deleted), cids.size()), false);
                        }
                    });
                    showDialogFragment(dialog);



                    /*String msg = String.format(getResources().getString(R.string.empty_cards_count), cids.size());
                    ConfirmationDialog dialog = new ConfirmationDialog() {
                        @Override
                        public void confirm() {
                            getCol().remCards(Utils.arrayList2array(cids));
                            showSimpleSnackbar(String.format(
                                    getResources().getString(R.string.empty_cards_deleted), cids.size()), false);
                        }
                    };
                    dialog.setArgs(msg);
                    showDialogFragment(dialog);*/
                }

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onProgressUpdate(DeckTask task, TaskData... values) {

            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private void doOnFirstLoadedWebview(){
//        updateDeckList();
        //////////预加载牌组////////开始
//        SharedPreferences preferences = getSharedPreferences("showWelcome", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
////        preferences.getBoolean("FirstUse",false)
//        Log.e("预加载牌组", ">>>>>>>>>>>>>>>>>>>>>>done!");
//        String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/英语四级词汇.apkg";
//        File file  = new File(outpath);
//        if (file.exists() && preferences.getBoolean("FirstUse",false)){
//            Log.e("file.exists()", ">>>>>>>>>>>>>>>>>>>>>>done!");
//            importAdd(outpath);
//            editor.putBoolean("FirstUse",false);
//            editor.commit();
//        }


        updateDeckList();
        //////////预加载牌组////////结束

    }
    private void downloadTodirDecks(String path){
//        String filename = path.substring(path.lastIndexOf("/")).replace("yiji", "apkg");
        String filename = "英语四级词汇.apkg";
        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" +filename;
        ZXUtils.DownLoadFile(path, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e("downloadTodirDecks", "onSuccess");

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("downloadTodirDecks", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                webView.loadUrl("javascript:disappearOKBox()");
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent();
//                        i.putExtra("PathFromDownLoadDecks", filepath);
//                        setResult(RESULT_OK, i);
//                        DownLoadDecks_webview.this.finish();
//                    }
//                },1000);

            }
        });
    }



    private void jumpToCreateFilterActivity(){


        Intent reviewer = new Intent(this, FilteredDeckActivity.class);

        startActivityForResultWithAnimation(reviewer, DECK_YIJI_FILTER_OPTIONS, ActivityTransitionAnimation.LEFT);

    }


//    private void jumpToGuojiSharedDecksActivity(){
//
//
//        Intent reviewer = new Intent(this, DownLoadDecks_webview.class);
//
//        startActivityForResultWithAnimation(reviewer, DECK_YIJI_FILTER_OPTIONS, ActivityTransitionAnimation.LEFT);
//
//    }

    private void jumpToGuojiSharedDecksFromAnkiActivity(){


        Intent reviewer = new Intent(this, DownLoadDecksFromAnki.class);

        startActivityForResultWithAnimation(reviewer, 0000, ActivityTransitionAnimation.LEFT);

    }

    private void jumpToGuoneiSharedDecksActivity(){

        Log.e("jumpToGuoneiShared", ">>>>>>>>>>>>>>>times");
        Intent reviewer = new Intent(this, DownLoadDecks_webview.class);

        startActivityForResultWithAnimation(reviewer, 1111, ActivityTransitionAnimation.LEFT);

    }

    //***********************************************************//



    //***********************************************************//
    class  MyObject{
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public String getTime(){
            Log.i("fdsafsa", "fdasfasf");
            return new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date());
        }

        @JavascriptInterface
        public void xmhandleDeckSelection(String did, boolean skipStudyOptioon){
            Log.i("xxxxx", "xxxxrrr");
            long longDid = Long.parseLong(did);
            handleDeckSelection(longDid, skipStudyOptioon);
        }

        @JavascriptInterface
        public void renameCurrentDeck(String did){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void oneDeckOption(String did){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void selfDefineStudy(String did, boolean b){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void switch_reader(String did, boolean b){
            Log.i("xxx666xx", "xx666666xxrrr888888");
//            Intent intent = new Intent(DeckPicker.this, DeckReader.class);
            Intent intent = new Intent(DeckPicker.this, DeckReader.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckPicker.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void switch_test(String did, boolean b){
            Log.i("xxx666xx", "xx666666xxrrr888888");
            Intent intent = new Intent(DeckPicker.this, DeckTest.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckPicker.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void deleteOneDeck(String did){
            Log.i("xxx666xx", "xx666666xxrrr");
            final Long longDid = Long.parseLong(did);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    deleteDeck(longDid);
                }
            });
        }

        @JavascriptInterface
        public void showSlideMenu(){
            //Log.i("xxxxx", "xxxxrrr");
            //long tid = Thread.currentThread().getId();
            //Log.i("tid", Long.toString(tid));


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toOpenDrawer();
                }
            });

//            //测试个人中心页面
//            Intent intent = new Intent(DeckPicker.this, PersonalCenterActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
        }

        @JavascriptInterface
        public void createFilterDeck(final String filterDeckName){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Timber.i("DeckPicker:: Creating filtered deck...");
                    getCol().getDecks().newDyn(filterDeckName);

                    jumpToCreateFilterActivity();
                    //Log.i("xxx666xx", "xx666666xxrrr");
                }
            });
        }

        @JavascriptInterface
        public void datacheck(){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void mediaCheck(){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void createNewtDeck(final String deckName){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("xxx666xx", "xx666666xxrrr");
                    //String deckName = mDialogEditText.getText().toString();
                    Timber.i("DeckPicker:: Creating new deck...");
                    getCol().getDecks().id(deckName, true);
                    CardBrowser.clearSelectedDeck();
                    updateDeckList();
                }
            });
        }

        @JavascriptInterface
        public void addNewNote(){
            Log.i("xxx666xx", "xx666666xxrrr888888");
        }

        @JavascriptInterface
        public void openGuojiSharedDecks(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Timber.i("DeckPicker:: Creating filtered deck...");

//                    jumpToGuojiSharedDecksFromAnkiActivity();

                    jumpToGuoneiSharedDecksActivity();//暂时用艾卡共享代替
                    //Log.i("xxx666xx", "xx666666xxrrr");
                }
            });
        }

        @JavascriptInterface
        public void openGuoneiSharedDecks(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Timber.i("DeckPicker:: Creating filtered deck...");
                    Log.e("jumpToGuoneiShared", ">>>>>>>>>>>>>>>openGuoneiSharedDecks");
                    jumpToGuoneiSharedDecksActivity();

                }
            });
        }

        @JavascriptInterface
        public void importDownloadDeck(){
            Log.i("importDownloadDeck", ">>>>>>>>>>>>>done!");
            //将downloadDecks目录下的文件添加到数据库，并删除所有文件
            File[] files = downloadDecks.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                String filename = files[i].getName();
//                String filepath = files[i].getAbsolutePath();
//                //此处添加加到数据库的代码
//
////                files[i].delete();//最后删除该文件
//            }
            inputingDeckname = files[0].getName();
            String filepath = files[0].getAbsolutePath();
            inputingDeckFilepath = filepath;
            Intent i = new Intent("comfirm input deck");
            sendBroadcast(i);

            //更新UI，使HTML页面重新刷新一遍
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:hidemark()");//使红点消失
                    loadDeckList();
                }
            });

        }



        @JavascriptInterface
        public void setTitleColor(){
            Log.e("setTitleColor", ">>>>>>>>>>>>setTitleColor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ApplyTranslucency.applyKitKatTranslucency2(DeckPicker.this);
                }
            });

        }

        @JavascriptInterface
        public void cancelTitleColor(){
            Log.e("cancelTitleColor", ">>>>>>>>>>>>cancelTitleColor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ApplyTranslucency.applyKitKatTranslucency(DeckPicker.this);
                }
            });

        }

    }

    class InputDeckReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("inputDeckReceiver", ">>>>>>>>>>onReceived!");
            importAdd(inputingDeckFilepath);
        }
    }

    //从OSS服务器下载AnkiChina的牌组资源数据
    private void getDataFromAnkiChina(){
        Log.e("getDataFromAnkiChina", "getDataFromAnkiChina>>>>>>>>>>done!");
        OSSUtil.getDeckInfo(this);
    }
    //***********************************************************//
    //***********************************************************//
}
