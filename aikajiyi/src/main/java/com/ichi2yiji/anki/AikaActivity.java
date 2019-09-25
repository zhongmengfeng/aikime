package com.ichi2yiji.anki;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.activity.ek.EKMainActivity;
import com.ichi2yiji.anki.bean.PersonInfo1;
import com.ichi2yiji.anki.bean.SerializableMap;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.anki.bean.UserInfoBean;
import com.ichi2yiji.anki.dialogs.ActionSheetDialog;
import com.ichi2yiji.anki.dialogs.AlertDialog;
import com.ichi2yiji.anki.dialogs.AsyncDialogFragment;
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
import com.ichi2yiji.anki.dialogs.LoadingDialog;
import com.ichi2yiji.anki.dialogs.MediaCheckDialog;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.dialogs.SyncErrorDialog;
import com.ichi2yiji.anki.exception.ConfirmModSchemaException;
import com.ichi2yiji.anki.exception.DeckRenameException;
import com.ichi2yiji.anki.features.guide.GuideActivity;
import com.ichi2yiji.anki.features.ready.ExamPreparationActivity;
import com.ichi2yiji.anki.fragment.DeckReaderFragment;
import com.ichi2yiji.anki.fragment.DeckTestFragment2;
import com.ichi2yiji.anki.receiver.SdCardReceiver;
import com.ichi2yiji.anki.reviewer.ReviewerExtRegistry;
import com.ichi2yiji.anki.toprightandbottomrightmenu.BottomRightMenu;
import com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem;
import com.ichi2yiji.anki.toprightandbottomrightmenu.TopRightMenu;
import com.ichi2yiji.anki.treeview.FileBean;
import com.ichi2yiji.anki.treeview.Node;
import com.ichi2yiji.anki.treeview.SimpleTreeAdapter;
import com.ichi2yiji.anki.treeview.TreeListViewAdapter;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.CopyRawToDataForInitDeck;
import com.ichi2yiji.anki.util.LogUtils;
import com.ichi2yiji.anki.util.OSSUtil;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.VersionInfoUtils;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.anki.widgets.DeckAdapter;
import com.ichi2yiji.async.Connection;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.common.Constants;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.compat.CompatHelper;
import com.ichi2yiji.libanki.Card;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Sched;
import com.ichi2yiji.libanki.Sound;
import com.ichi2yiji.libanki.Utils;
import com.ichi2yiji.themes.StyledProgressDialog;
import com.ichi2yiji.themes.Themes;
import com.ichi2yiji.utils.AssetsUtil;
import com.ichi2yiji.utils.GsonUtil;
import com.ichi2yiji.utils.SPUtil;
import com.ichi2yiji.utils.StorageUtil;
import com.ichi2yiji.utils.VersionUtils;
import com.ichi2yiji.widget.WidgetStatus;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;
import timber.log.Timber;


public class AikaActivity extends NavigationDrawerActivity implements RadioGroup.OnCheckedChangeListener,
        StudyOptionsFragment.StudyOptionsListener, DatabaseErrorDialog.DatabaseErrorDialogListener,
        SyncErrorDialog.SyncErrorDialogListener, ImportDialog.ImportDialogListener,
        MediaCheckDialog.MediaCheckDialogListener, ExportDialog.ExportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback, CustomStudyDialog.CustomStudyListener, View.OnClickListener {


    private static final String TAG = "AikaActivity";
    private MessageBroadcastReceiver deckDownloadBroadcastReceiver;
    private boolean activityCreated = false;
    private boolean isFromDownloadDecks = false;

    /**
     * 做一次细小改动,用于测试svn提交
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

    public static final String EXTRA_DECK_ID = "deckId";
    public static final int RESULT_MEDIA_EJECTED = 202;
    public static final int RESULT_DB_ERROR = 203;
    public static final int DECK_YIJI_FILTER_OPTIONS = 301;
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static File downloadDecks = new File(ROOT_PATH + "/Chaojiyiji/downloadDecks");
    private static List<FileBean> mDatas2 = new ArrayList<FileBean>();
    //在线导入的牌组名称
    private static String downloadFileName;
    private static String inputingDeckname;
    // 给该变量赋值用于导入该文件数据后删除该文件
    private static String inputingDeckFilepath;
    //手机屏幕密度md
    private static float md;

    //是否跳转到登录界面:
    public boolean isSkipToLoginActivity;


    @Bind(R.id.deck_reader_lyt)
    RelativeLayout deck_reader_lyt;
    @Bind(R.id.deck_picker_lyt)
    RelativeLayout deck_picker_lyt;
    @Bind(R.id.deck_picker_lyt2)
    RelativeLayout deck_picker_lyt2;
    @Bind(R.id.deck_test_lyt)
    RelativeLayout deck_test_lyt;
    @Bind(R.id.rl_guide)
    RelativeLayout rlGuide;

    @Bind(R.id.deck_reader)
    TextView deck_reader;
    @Bind(R.id.deck_picker)
    TextView deck_picker;
    @Bind(R.id.deck_picker2)
    TextView deck_picker2;
    @Bind(R.id.deck_test)
    TextView deck_test;

    @Bind(R.id.deck_reader_mark_view)
    View deck_reader_mark_view;
    @Bind(R.id.deck_picker_mark_view)
    View deck_picker_mark_view;
    @Bind(R.id.deck_picker_mark_view2)
    View deck_picker_mark_view2;
    @Bind(R.id.deck_test_mark_view)
    View deck_test_mark_view;


    @Bind(R.id.iv_guide)
    ImageView ivGuide;
    @Bind(R.id.iv_guide_close)
    ImageView ivGuideClose;
    @Bind(R.id.icon_main_home)
    ImageView icon_home;
    /**
     * 校徽
     **/
    @Bind(R.id.icon_main_home2)
    ImageView icon_home2;

    /*@Bind(R.id.divider) View viewBlank;
    @Bind(R.id.top_right) ImageView topRightMenu;
    @Bind(R.id.top_right_lyt) RelativeLayout top_right_lyt;
    @Bind(R.id.studyoptions_fragment) View mStudyoptionsFrame;*/

    private static View viewBlank;
    private static ListView listView;
    private static SimpleTreeAdapter mAdapter;
    private static TextView text_share_deck;
    private static TextView text_load_online;
    private static ImageView red_dot;


    private FragmentManager fm;
    private FragmentTransaction ft;

    // 牌组
    private DeckPickerFragment pickerFragment;
    // 阅读
    private DeckReaderFragment readerFragment;
    // 模考
    private DeckTestFragment2 testFragment;
    // private Fragment baseFragment;

    private SVProgressHUD svProgressHUD;
    private LoadingDialog loadingDialog;
    private SegmentedGroup segmented;
    private SlideMenuManager slideMenuManager;
    private SlidingMenu slidingMenu;


    private GuideView guideView1;
    private GuideView guideView2;
    private GuideView guideView3;
    private GuideView guideView4;
    private GuideView guideView5;
    private TextView hideText;
    private MaterialDialog mProgressDialog;
    private View mStudyoptionsFrame;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mRecyclerViewLayoutManager;
    private DeckAdapter mDeckListAdapter;
    // Note this will be null below SDK 14
    private FloatingActionsMenu mActionsMenu;
    private TextView mTodayTextView;
    private EditText mDialogEditText;
    private ImageView topRightMenu;
    private RelativeLayout top_right_lyt;

    private Receiver myReceiver;
    private BroadcastReceiver mUnmountReceiver = null;
    private InputDeckReceiver inputDeckReceiver;
    private UpdateSildingMenuUserInfo updateSildingMenuUserInfo;
    private ImageOptions imageOptions;

    private List<Node> nodes;
    private int page_tag;
    private boolean iconIsList;
    private boolean isSecondTimeUseAPP;
    private boolean canGoToAttentionPage;
    private boolean showmark;
    private int themeName;
    private String mImportPath;
    private String createdNewName;
    private long mContextMenuDid;

    // flag asking user to do a full sync which is used in upgrade path
    private boolean mRecommendFullSync = false;
    // flag keeping track of when the app has been paused
    private boolean mActivityPaused = false;
    private boolean mActivityResumed = false;
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
    private ActionSheetDialog sheetDialog;


    private Fragment mContent = null;
    @BindColor(R.color.c_ffffff)
    int white;
    @BindColor(R.color.text_dark)
    int text_dark;
    private boolean beingPreLoadingDeck = false;


    private ProgressDialog progressDialog;

    private boolean isImportingDeck;
    private long mDeckId;


    private SerializableMap serializableMap ;
    private int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //0. 设置主题样式
        ThemeChangeUtil.changeTheme(this);
        themeName = ThemeChangeUtil.getCurrentThemeName();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //1. 加载数据库
        boolean colOpen = firstCollectionOpen();

        //2. 初始化辅助工具
        initAdditionalTools();

        //3. 设置view
        initAllViews();

        //4. 安装后第一次进入这个页面的工作
        doFirstLoad();

        //5. 预加载fragment
        preloadFragment();

        //6. 预加载nodelist
        preloadNodelist();

        //7. 注册各种广播
        registAllBroadcast();

        //8. 预请求网络数据,为一些webview页面准备数据
        preLoadNetdata();



        final Map<Long, String> cardContentMap = new HashMap<>();

        if (colOpen) {
            Object[] o = new Object[]{getCol().getSched().deckDueTree()};
            List<Sched.DeckDueTreeNode> nodes = (List<Sched.DeckDueTreeNode>) o[0];

            for (Sched.DeckDueTreeNode deckDueTreeNode : nodes) {
                final long did = deckDueTreeNode.did;
                String name = deckDueTreeNode.names[0];
                if (name.equals("Default") && deckDueTreeNode.lrnCount == 0 && deckDueTreeNode.newCount == 0) {
                    //跳过掉名字为Default，并且复习数量和新卡片数量都为0的牌组数据
                } else {
                    if (getCol().getDecks().selected() != did) {
                        getCol().clearUndo();
                    }

                    // Select the deck
                    getCol().getDecks().select(did);
                    // Reset the schedule so that we get the counts for the currently selected deck
                    getCol().getSched().reset();
                    DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ANSWER_CARD, new DeckTask.TaskListener() {
                        @Override
                        public void onPreExecute() {

                        }

                        @Override
                        public void onProgressUpdate(DeckTask.TaskData... values) {

                            Card mCurrentCard = values[0].getCard();

//            mCurrentCard.getId()

                            if (mCurrentCard == null) {

                                return;
                            } else {
                                long id = mCurrentCard.getId();
                                String question;
                                String displayString = "";
                                if (mCurrentCard.isEmpty()) {
                                    displayString = getResources().getString(R.string.empty_card_warning);
                                } else {
                                    question = mCurrentCard.q();
                                    question = getCol().getMedia().escapeImages(question);
//                    question = typeAnsQuestionFilter(question);

                                    displayString = enrichWithQADiv(question, false);


                                }
                                String mBaseUrl = Utils.getBaseUrl(getCol().getMedia().dir());
                                String content = Sound.expandSounds(mBaseUrl, displayString);
                                content = content.replace("font-weight:600;", "font-weight:700;");
                                String cardClass = "card card" + (mCurrentCard.getOrd() + 1);


                                StringBuilder style = new StringBuilder();

                                ReviewerExtRegistry mExtensions = new ReviewerExtRegistry(getBaseContext());
                                mExtensions.updateCssStyle(style);

                                StringBuffer sb = new StringBuffer();
                                Matcher m = Pattern.compile("([^\u0000-\uFFFF])").matcher(content);
                                while (m.find()) {
                                    String a = "&#x" + Integer.toHexString(m.group(1).codePointAt(0)) + ";";
                                    m.appendReplacement(sb, a);
                                }
                                m.appendTail(sb);
                                content = sb.toString();

                                String mCardTemplate = null;
                                try {
                                    mCardTemplate = Utils.convertStreamToString(getAssets().open("card_template.html"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                SpannedString mCardContent = new SpannedString(mCardTemplate.replace("::content::", content)
                                        .replace("::style::", style.toString()).replace("::class::", cardClass));
                                LogUtils.showLogCompletion(TAG, mCardContent.toString(), 3900);


//                                serializableMap  = new SerializableMap();
//                                cardContentMap.put(did, mCardContent.toString());
//                                serializableMap .setMap(cardContentMap);

                            }
                        }

                        @Override
                        public void onPostExecute(DeckTask.TaskData result) {

                        }

                        @Override
                        public void onCancelled() {

                        }
                    }, new DeckTask.TaskData(null, 0));
                    Log.e("AikaActivity", "onCreate>>>>" + "did: " + did);
                }

            }

        }

    }

    private static String enrichWithQADiv(String content, boolean isAnswer) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"");
        if (isAnswer) {
            sb.append("answer");
        } else {
            sb.append("question");
        }
        sb.append("\">");
        sb.append(content);
        sb.append("</div>");
        return sb.toString();
    }


    private void preLoadNetdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //页面启动的时候去请求OSS数据，将AnkiChina的牌组数据预加载
                getDataFromAnkiChina();

                //页面启动的时候去请求后台分发作业页面数据，将数据预加载
                getDistributeJsonData();

                //页面启动的时候去请求后台关注班级页面数据，将数据预加载
                getAttentionJsonData();
            }
        }).start();
    }

    private void registAllBroadcast() {
        //接收推送后下载文件的广播
        myReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.ankideck.ReceivedUrlFromPush");
        registerReceiver(myReceiver, intentFilter);

        //确定导入牌组时将发送的广播
        inputDeckReceiver = new InputDeckReceiver();
        IntentFilter filter = new IntentFilter("comfirm input deck");
        registerReceiver(inputDeckReceiver, filter);

        //更新侧边栏头像和用户名信息的广播
        updateSildingMenuUserInfo = new UpdateSildingMenuUserInfo();
        IntentFilter ft = new IntentFilter("Update Username And User Head Image");
        registerReceiver(updateSildingMenuUserInfo, ft);

        //注册广播
        IntentFilter intentFilter_deckDownload = new IntentFilter("message_broadcast");
        deckDownloadBroadcastReceiver = new MessageBroadcastReceiver();
        registerReceiver(deckDownloadBroadcastReceiver, intentFilter_deckDownload);

        //注册EventBus的事件订阅
//        EventBus.getDefault().register(this);
    }

    private void preloadNodelist() {
        //判断是否是第二次（或者次）进入更多主界面，如果是则加载缓存的牌组数据
        Intent intent = getIntent();
        isSecondTimeUseAPP = intent.getBooleanExtra("LoadDeckListCookies", false);
        if (isSecondTimeUseAPP) {
            String data = SPUtil.getPreferences(SPUtil.TYPE_DECK_LIST_DATA, "mData2", "");
            Log.e("AikaActivity", "preloadNodelist>>>>>>>>>>" + data);
            if (!TextUtils.isEmpty(data)) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<FileBean>>() {
                }.getType();
                mDatas2.clear();
                mDatas2 = gson.fromJson(data, type);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initListView();
                }
            }, 50);
        } else {
            //如果是第一次使用, 开启线程查询数据库，加载牌组数据
            doOnFirstLoadedWebview();
        }
    }

    private void preloadFragment() {
        pickerFragment = new DeckPickerFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fl_content, pickerFragment);//此处需用add，不能用replace
        ft.commit();
        mContent = pickerFragment;
    }

    private void doFirstLoad() {
        loadingDialog = new LoadingDialog(AikaActivity.this);
        //拷贝数据到SD卡
        copyFile2SD1();
    }

    private void initAllViews() {
        //透明状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //初始化控件
        initView();
        //必须要放在初始化控件后面
        ApplyTranslucency.applyKitKatTranslucency(AikaActivity.this);
        //设置按钮监听
        setListener();
        //根据sp里面储存的设置数据显示tab相应功能,是我的课程还是牌组
        showFeatures();
        //设置侧边栏
        slideMenuManager = new SlideMenuManager(this);
        slideMenuManager.setProgressRun(new Runnable() {
            @Override
            public void run() {
                Log.e("AikaActivity", "正在加载中...第496行");
                progressDialog.show();
                progressDialog.setDownloadTip("正在加载中...");
            }
        });
        slidingMenu = slideMenuManager.getSlidingMenu();
        //设置右上角下拉菜单的触摸事件
        setTopRightMenuTouchEvent();
        //
        progressDialog = new ProgressDialog(this).builder();
    }

    private void initView() {
        viewBlank = findViewById(R.id.divider);
        topRightMenu = (ImageView) findViewById(R.id.top_right);
        top_right_lyt = (RelativeLayout) findViewById(R.id.top_right_lyt);
        mStudyoptionsFrame = (FrameLayout) findViewById(R.id.studyoptions_fragment);
    }

    private void initAdditionalTools() {
        //拿取屏幕密度
        md = UIUtils.getDensity();
        //初始化xutils的ImageOptions对象
        initImageOptions();
        //设置标题
        setTitle(getResources().getString(R.string.app_name));
        //版本检查
        checkVersion();
        //绑定控件
        ButterKnife.bind(this);
        //注册广播监听
        registerExternalStorageListener();
    }

    protected void onCreate_old(Bundle savedInstanceState) {
        // TODO: Also d
        ThemeChangeUtil.changeTheme(this);
        themeName = ThemeChangeUtil.getCurrentThemeName();
        Timber.d("onCreate()");
        // Open Collection on UI thread while splash screen is showing
        boolean colOpen = firstCollectionOpen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        initView();
        showFeatures();
        setListener();
        initImageOptions();
        checkVersion();
        //dx  add
        md = UIUtils.getDensity();

        progressDialog = new ProgressDialog(this).builder();

//        svProgressHUD = new SVProgressHUD(AikaActivity.this);
//        SVProgressHUD.SVProgressHUDMaskType.valueOf("BlackCancel");

        loadingDialog = new LoadingDialog(AikaActivity.this);


        ApplyTranslucency.applyKitKatTranslucency(AikaActivity.this);
        //copyFile2SD();
        copyFile2SD1();
//        segmented = (SegmentedGroup)findViewById(R.id.segmented);
//        segmented.setTintColor(Color.WHITE, Color.parseColor("#007aff"));
//        segmented.setOnCheckedChangeListener(this);
        // setTitleCheckedEvent();
//        View mainView = findViewById(android.R.id.content);
//        initNavigationDrawer(mainView);
        //设置侧边栏
        slideMenuManager = new SlideMenuManager(this);
        slideMenuManager.setProgressRun(new Runnable() {
            @Override
            public void run() {
                //svProgressHUD.showWithStatus("正在加载中...");

                progressDialog.show();
                progressDialog.setDownloadTip("正在加载中...");


            }
        });
        slidingMenu = slideMenuManager.getSlidingMenu();

        setTopRightMenuTouchEvent();

        pickerFragment = new DeckPickerFragment();
        fm = getSupportFragmentManager();

        // baseFragment = fm.findFragmentById(R.id.fragment_main2);
        //fragment每次重建刷新的代码
//        ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_main2, pickerFragment);
////        ft.add(R.id.fragment_main2, pickerFragment);
//        ft.commit();

        ft = fm.beginTransaction();
        ft.add(R.id.fl_content, pickerFragment);//此处需用add，不能用replace
        ft.commit();
        mContent = pickerFragment;

        //判断是否是第二次（或者次）进入更多主界面，如果是则加载缓存的牌组数据
        Intent intent = getIntent();
        isSecondTimeUseAPP = intent.getBooleanExtra("LoadDeckListCookies", false);
        if (isSecondTimeUseAPP) {
            String data = SPUtil.getPreferences(SPUtil.TYPE_DECK_LIST_DATA, "mData2", "");
            if (!TextUtils.isEmpty(data)) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<FileBean>>() {
                }.getType();
                mDatas2.clear();
                mDatas2 = gson.fromJson(data, type);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initListView();
                }
            }, 50);
        } else {
            //如果是第一次使用
            doOnFirstLoadedWebview();//开启线程查询数据库，加载牌组数据
        }

//        doOnFirstLoadedWebview();//开启线程查询数据库，加载牌组数据
//        segmented.check(R.id.deck_picker);//设置segment默认选中“牌组”
//        topRightMenu.setImageResource(R.drawable.top_right);
//        doOnFirstLoadedWebview();

        // set protected variable from NavigationDrawerActivity
//        mFragmented = mStudyoptionsFrame != null && mStudyoptionsFrame.getVisibility() == View.VISIBLE;

        registerExternalStorageListener();

        // create inherited navigation drawer layout here so that it can be used by parent class
//        initNavigationDrawer(mainView);
        setTitle(getResources().getString(R.string.app_name));
        //接收推送后下载文件的广播
        myReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.ankideck.ReceivedUrlFromPush");
        registerReceiver(myReceiver, intentFilter);

        //确定导入牌组时将发送的广播
        inputDeckReceiver = new InputDeckReceiver();
        IntentFilter filter = new IntentFilter("comfirm input deck");
        registerReceiver(inputDeckReceiver, filter);

        //更新侧边栏头像和用户名信息的广播
        updateSildingMenuUserInfo = new UpdateSildingMenuUserInfo();
        IntentFilter ft = new IntentFilter("Update Username And User Head Image");
        registerReceiver(updateSildingMenuUserInfo, ft);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //页面启动的时候去请求OSS数据，将AnkiChina的牌组数据预加载
                getDataFromAnkiChina();

                //页面启动的时候去请求后台分发作业页面数据，将数据预加载
                getDistributeJsonData();

                //页面启动的时候去请求后台关注班级页面数据，将数据预加载
                getAttentionJsonData();
            }
        }).start();


        /*String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491392692577&di=50aa5ccd0735b848814962e3c7bd24d8&imgtype=0&src=http%3A%2F%2Fimg16.3lian.com%2Fp2015%2F30%2F38s.jpg";
        x.image().bind(icon_home2, url, imageOptions);*/

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

        // setGuideView();
    }

    /**
     * @param intent MainActivtity2为单例模式
     *               第二次进入时要走onNewIntent()回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        closeLoginActivities();
    }


    private void closeLoginActivities() {
        /**
         * 6.6--将LoginActivity和ankiLoginActivity杀死
         */
        Activity loginActivity = AnkiDroidApp.activityManager.get("LoginActivity");
        Activity ankiLoginActivity = AnkiDroidApp.activityManager.get("AnkiLoginActivity");
        Log.e("AikaActivity", ">>>>>>>>>>onNewIntent>>>>>>>>>>> loginActivity: " + loginActivity + "   ankiLoginActivity: " + ankiLoginActivity);
        if (null != loginActivity) {
            loginActivity.finish();
        }
        if (null != ankiLoginActivity) {
            ankiLoginActivity.finish();
        }
    }

    private void copyFile2SD1() {

        ///////////////**************************/////////////////////////////////
        //从copyFile2SD方法拷贝过来的
        copyDeckImg();
        copyTests();
        copyGuideImg();


        //////////////**************************///////////////////////////////////


        //这个方法应该是准备数据到sd卡;的Deckdir目录;
        //0. 从偏好设置中得到"个人信息"和"是否是安装后第一次使用"
        String personInfoResult = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1", "");
        //loadingDialog.show();
        boolean isFirstUse = SPUtil.getPreferences(SPUtil.TYPE_WELCOME_CONFIG, "FirstUse", false);
        //1. 如果不是第一次启动APP,并且是到达这个页面前,走了login的窗口.
        if (!isFirstUse) {
            //1.1 不是第一次使用,但是是第一次通过登录界面登录的,这时候也要考虑导入牌组;
            boolean isLoginFromWindow = SPUtil.getPreferences(SPUtil.TYPE_WELCOME_CONFIG, "isLoginFromWindow", false);
            String isFirstLoginFromWindow = isLoginFromWindow ? "true" : "false";
            if (isLoginFromWindow) {
                try {
                    String thisMemberDefaultImportDeckurl = (new JSONObject(personInfoResult)).getJSONObject("data").getJSONObject("mem").getString("deck");
                    if (TextUtils.isEmpty(thisMemberDefaultImportDeckurl)) {
                        Log.e("AkiaActivity", "默认牌组的url为空>>>>>>>>>>" + "isLoginFromWindow: " + isFirstLoginFromWindow + "   thisMemberDefaultImportDeckurl: " + thisMemberDefaultImportDeckurl);
                        return;
                    } else {
                        Log.e("AkiaActivity", "默认牌组的url不为空>>>>>>>>>>" + "isLoginFromWindow: " + isFirstLoginFromWindow + "   thisMemberDefaultImportDeckurl: " + thisMemberDefaultImportDeckurl);
                        loadingDialog.show();
                        downloadDefaultDeckToSD(thisMemberDefaultImportDeckurl);

                        //做个标记,标明是从登录窗口登录过来的
                        SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "isLoginFromWindow", false);
                    }
                } catch (JSONException e) {
                    Log.e("AkiaActivity", "isLoginFromWindow" + isLoginFromWindow + "  JSONException: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return;
        }
        //2 走到这里就是第一次启动,就会调用,动态加载图片;
        loadingDialog.show();
        //3. 判断个人信息中是否有"deck"字段;

        if (TextUtils.isEmpty(personInfoResult)) {
            // 如果没有个人信息传过来,则就是用户随便看看,或是网络错误的问题,则直接拷贝assets下预备好的牌组;
            copyDefaultDeckToSD();
        } else {
            JSONObject jsonObject = null;
            try {
                String thisMemberDefaultImportDeckurl = (new JSONObject(personInfoResult)).getJSONObject("data").getJSONObject("mem").getString("deck");
                if (TextUtils.isEmpty(thisMemberDefaultImportDeckurl)) {
                    copyDefaultDeckToSD();
                } else {
                    downloadDefaultDeckToSD(thisMemberDefaultImportDeckurl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //3. 已经跨过第一次启动APP的过程了,修改偏好设置中的"FirstUse"
        SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "FirstUse", false);


        /**
         "data": {
         "mem": {
         "user_name": "f123456",
         "email": "1419692004@qq.com",
         "face": "http://q.qlogo.cn/qqapp/1105783665/FFC0D1F7C618710DE4FE43B42BF9AFD6/100",
         "jifen": "355",
         "jyz": "0",
         "honeyname": "KD_小小曼",
         "gender": "男",
         "telephone": "18135673854",
         "qq": "1419692005",
         "school_id": "999",
         "is_teacher": "0",
         "member_level": "1",
         "school_logo": "http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58df4dcadf943.png",
         "deck": "",
         "class_ids": "6,3,8,11"
         },

         **/
    }

    private void downloadDefaultDeckToSD(String deckurl) {

        // deckurl   URLDecoder.decode("http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201705021511/%E8%BE%BE%E5%86%85%E8%AF%BE%E4%BB%B6%E7%AE%80%E6%98%93%E7%89%88.apkg","UTF-8")

        String decodePath = null;
        try {
            decodePath = URLDecoder.decode(deckurl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String filename = decodePath.substring(decodePath.lastIndexOf("/") + 1);

        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + filename;
        ZXUtils.DownLoadFile(deckurl, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {

                LogUtil.e("onSuccess: result.getAbsolutePath() = " + result.getAbsolutePath());
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

            }
        });

    }

    private void copyDefaultDeckToSD() {
        String fileName = "初三英语单词.apkg";
        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + fileName;
        AssetsUtil.copyAssetsFile2Storage(fileName, destFile);
    }

    /**
     * 根据设置显示相应功能
     */
    private void showFeatures() {
        SettingsBean settings = SettingUtil.getSettings();
        Log.e("setting", "setting==" + settings.getHomePageStyle());
        if (settings != null) {
            int homePageStyle = settings.getHomePageStyle();
            if (homePageStyle == 0) {
                // 阅读 + 牌组 + 模考
                deck_reader_lyt.setVisibility(View.VISIBLE);
                deck_picker_lyt.setVisibility(View.VISIBLE);
                deck_picker_lyt2.setVisibility(View.GONE);
                deck_test_lyt.setVisibility(View.VISIBLE);
                deck_picker.setText("牌组");

            } else if (homePageStyle == 1) {
                // 牌组 + 阅读
                deck_reader_lyt.setVisibility(View.VISIBLE);
                deck_picker_lyt.setVisibility(View.GONE);
                deck_picker_lyt2.setVisibility(View.VISIBLE);
                deck_test_lyt.setVisibility(View.GONE);
                deck_picker.setText("牌组");

            } else if (homePageStyle == 2) {
                // 牌组 + 模考
                deck_reader_lyt.setVisibility(View.GONE);
                deck_picker_lyt.setVisibility(View.VISIBLE);
                deck_picker_lyt2.setVisibility(View.GONE);
                deck_test_lyt.setVisibility(View.VISIBLE);
                deck_picker.setText("牌组");

            } else if (homePageStyle == 3) {
                // TODO 我的课程
                deck_picker.setTextColor(white);
                deck_reader_lyt.setVisibility(View.GONE);
                deck_picker_lyt.setVisibility(View.GONE);
                deck_picker_lyt2.setVisibility(View.VISIBLE);
                deck_test_lyt.setVisibility(View.GONE);
                deck_picker.setText("我的课程");
                selectDeckPicker();
            }
        }
    }

    private void setListener() {
        ivGuideClose.setOnClickListener(this);
        icon_home.setOnClickListener(this);
        icon_home2.setOnClickListener(this);
        deck_reader_lyt.setOnClickListener(this);
        deck_picker_lyt.setOnClickListener(this);
        deck_picker_lyt2.setOnClickListener(this);
        deck_test_lyt.setOnClickListener(this);
    }

    private void copyFile2SD() {
        // copyPicker();
        copyDeckImg();
        copyTests();
        copyGuideImg();
        // downloadTodirDecks();
        // preloadingDeck();
        getDeckFile();
    }

    /**
     * 根据登录情况和用户类型,获取牌组文件
     */
    private void getDeckFile() {
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_MEM_ID, "");
        if (TextUtils.isEmpty(memId)) {
            // 未登录,导入默认牌组
            copyPicker();
        } else {
            // 已登录,根据用户类型导入牌组
            String infoJson = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_INFO_RESULT_1, "");
            if (TextUtils.isEmpty(infoJson)) {
                return;
            }
            PersonInfo1 info = GsonUtil.json2Bean(infoJson, PersonInfo1.class);
            LogUtil.e("getDeckFile: personInfo1 = " + info.toString());

            if (!TextUtils.equals(info.getCode(), "1") &&
                    info.getData() != null &&
                    !TextUtils.equals(info.getData().getMem().getSchool_id(), "999") &&
                    !TextUtils.equals(info.getData().getMem().getSchool_id(), "0")) {
                // 既不是普通用户也不是开发人员,而是合作机构用户,导入用户专有牌组
                // ToastAlone.showShortToast("学校用户");
                LogUtil.e("getDeckFile: 学校用户");
                downloadTodirDecks();
            } else {
                // 导入默认牌组
                // ToastAlone.showShortToast("普通用户");
                LogUtil.e("getDeckFile: 普通用户");
                copyPicker();
            }
        }
    }

    /**
     * 复制引导帮助gif图片到内存卡
     */
    private void copyGuideImg() {
        boolean isImportGuideImg = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, "isImportGuideImg", false);
        if (!isImportGuideImg) {
            // 未导入引导帮助图片
            String destDir = StorageUtil.getAppCustomCacheDirectory(Constants.APP_CACHE_DIR_IMAGES).getAbsolutePath();
            AssetsUtil.copyAssetsDir2StorageDir("images_guide", destDir);
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, "isImportGuideImg", true);
        }
    }

    /**
     * 显示引导动画
     */
    private void showGuide() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String isShowGuideDeck = "isShowGuideDeck";
                boolean isShowGuide = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, isShowGuideDeck, false);
                String guidePicker = "/sdcard/Chaojiyiji/images/guide_deck1.gif";
                showGuide(isShowGuideDeck, isShowGuide, guidePicker);
            }
        }, 1700);
    }


    private void initImageOptions() {
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                // .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                // .setLoadingDrawableId(R.drawable.ic_launcher)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.ic_launcher)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();
    }

    /**
     * 复制牌组数据到内存卡
     */
    private void copyPicker() {
        boolean preLoadNormalDeck = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOAD_NORMAL_DECK, false);
        if (!preLoadNormalDeck) {
            String fileName = "初三英语单词.apkg";
            String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + fileName;
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOADING_DECK_PATH, destFile);
            // 给该变量赋值用于导入该文件数据后删除该文件
            inputingDeckFilepath = destFile;
            AssetsUtil.copyAssetsFile2Storage(fileName, destFile);
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOAD_NORMAL_DECK, true);
        }
    }

    /**
     * 复制模考数据到内存卡
     */
    private void copyTests() {
        boolean isImportTest = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, "isImportTest", false);
        if (!isImportTest) {
            String fileName = "驾照考试__试题四.mtest";
            String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirTests/" + fileName;
            AssetsUtil.copyAssetsFile2Storage(fileName, destFile);
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, "isImportTest", true);
        }
    }


    /**
     * 复制牌组图标到内存卡
     */
    private void copyDeckImg() {
        boolean isImportGuideImg = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, "isImportDeckImg", false);
        if (!isImportGuideImg) {
            String destDir = StorageUtil.getAppCustomCacheDirectory(Constants.APP_CACHE_DIR_COLLECTION).getAbsolutePath();
            AssetsUtil.copyAssetsDir2StorageDir("choose_images", destDir);
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, "isImportDeckImg", true);
        }
    }

    private void checkVersion() {
        updateBackstageVersionInfo();
        int version_backstage = 0;
        if (version != null) {
            version_backstage = Integer.parseInt(version);
        }
//        int version_backstage = 1;//测试
        int localVersion = Integer.parseInt(VersionInfoUtils.getVerName(getApplicationContext()).replace(".", ""));

        if (is_update.equals("0")) {
            //不用更新
        } else if (is_update.equals("1")) {
            if (localVersion < version_backstage) {
                //提醒更新版本
                new AlertDialog(AikaActivity.this).builder()
                        .setTitle("有新的应用版本")
                        .setMsg("请去应用商店下载最新版本再使用吧")
                        .setCancelable(false)
                        .setNegativeButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AikaActivity.this.finish();
                            }
                        }).show();
            }
        } else if (is_update.equals("9")) {
            //预留功能
        }
    }

    private String version = null;
    private String is_update = "0";

    private void updateBackstageVersionInfo() {
        String clientid = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "CLIENTID", "");
        String username = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", "");
        String password = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PASSWORD", "");

        Map<String, String> map = new HashMap<>();
        map.put("tele", username);
        map.put("client_id", clientid);
        map.put("password", password);
        map.put("client_type", String.valueOf(1));

        ZXUtils.Post(Urls.URL_APP_LOGIN, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!TextUtils.equals(code, "1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        version = jsonArray.getJSONObject(0).getString("version");
                        is_update = jsonArray.getJSONObject(0).getString("is_update");
                    } else {
                        String data = jsonObject.getString("data");
                        LogUtil.e("onSuccess: data = " + data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("attemptLogin", "onError>>>>>>>>>>>>" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 阅读
     */
    public void selectDeckReader() {
        if (deck_reader_lyt.getVisibility() == View.GONE) {
            showDialog();
            return;
        }
        deck_reader.setTextColor(white);
        deck_reader_mark_view.setVisibility(View.VISIBLE);
        deck_picker.setTextColor(text_dark);
        deck_picker_mark_view.setVisibility(View.INVISIBLE);
        if (deck_picker_lyt2.getVisibility() == View.VISIBLE) {
            deck_picker2.setTextColor(text_dark);
            deck_picker_mark_view2.setVisibility(View.INVISIBLE);
        }
        deck_test.setTextColor(text_dark);
        deck_test_mark_view.setVisibility(View.INVISIBLE);
        if (readerFragment == null) {
            readerFragment = new DeckReaderFragment();
        }
        //fragment不会每次重建刷新的代码
        switchContent(readerFragment);

        if (readerFragment.isAdded()) {
            if (iconIsList) {
                topRightMenu.setImageResource(R.drawable.deckreader_listview_icon);
            } else {
                topRightMenu.setImageResource(R.drawable.deckreader_gridview_icon);
            }
        } else {
            topRightMenu.setImageResource(R.drawable.deckreader_gridview_icon);
            iconIsList = false;
        }
        page_tag = 1;//阅读页面的标识

        String isShowGuideReader = "isShowGuideReader";
        boolean isShowGuide = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, isShowGuideReader, false);
        String guideBook = "/sdcard/Chaojiyiji/images/guide_book1.gif";
        showGuide(isShowGuideReader, isShowGuide, guideBook);
    }

    /**
     * 显示引导帮助
     *
     * @param isShowGuideReader
     * @param isShowGuide
     * @param guideBook         gif图片路径
     */
    private void showGuide(String isShowGuideReader, boolean isShowGuide, String guideBook) {
        if (rlGuide != null && rlGuide.getVisibility() == View.VISIBLE) {
            rlGuide.setVisibility(View.GONE);
        } else {
            LogUtil.e("showGuide: rlGuide = null");
        }
        if (!isShowGuide) {
            rlGuide.setVisibility(View.VISIBLE);
            x.image().bind(ivGuide, new File(guideBook).toURI().toString(), imageOptions);
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, isShowGuideReader, true);
        }
    }


    /**
     * 牌组
     */
    public void selectDeckPicker() {
        deck_picker.setTextColor(white);
        deck_picker_mark_view.setVisibility(View.VISIBLE);
        if (deck_picker_lyt2.getVisibility() == View.VISIBLE) {
            deck_picker2.setTextColor(white);
            deck_picker_mark_view2.setVisibility(View.VISIBLE);
        }
        deck_reader.setTextColor(text_dark);
        deck_reader_mark_view.setVisibility(View.INVISIBLE);
        deck_test.setTextColor(text_dark);
        deck_test_mark_view.setVisibility(View.INVISIBLE);
        //fragment不会每次重建刷新的代码
        switchContent(pickerFragment);
        topRightMenu.setImageResource(R.drawable.top_right_new);
        page_tag = 0;//牌组页面的标识
        if (rlGuide.getVisibility() == View.VISIBLE) {
            rlGuide.setVisibility(View.GONE);
        }
    }

    /**
     * 模考
     */
    public void selectDeckTest() {
        if (deck_test_lyt.getVisibility() == View.GONE) {
            showDialog();
            return;
        }
        deck_test.setTextColor(white);
        deck_test_mark_view.setVisibility(View.VISIBLE);
        deck_reader.setTextColor(text_dark);
        deck_reader_mark_view.setVisibility(View.INVISIBLE);
        deck_picker.setTextColor(text_dark);
        deck_picker_mark_view.setVisibility(View.INVISIBLE);

        if (testFragment == null) {
            testFragment = new DeckTestFragment2();
        }
        //fragment不会每次重建刷新的代码
        switchContent(testFragment);
        topRightMenu.setImageResource(R.drawable.img_guide_test);
        page_tag = 2;//模考页面的标识

        String isShowGuideTest = "isShowGuideTest";
        boolean isShowGuide = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, isShowGuideTest, false);
        String guideTest = "/sdcard/Chaojiyiji/images/guide_test1.gif";
        showGuide(isShowGuideTest, isShowGuide, guideTest);
    }

    private void showDialog() {
        final AlertDialog alertDialog = new AlertDialog(AikaActivity.this).builder();
        alertDialog.setTitle("主页面暂无此功能");
        alertDialog.setMsg("可通过【设置->常用设置->主页面功能】进行切换");
        alertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alertDialog.show();
    }

    private void setTopRightMenuTouchEvent() {
        top_right_lyt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch (page_tag) {
                            case 0:
                                // 牌组右上角图标
                                topRightMenu.setImageResource(R.drawable.top_right_press_new);
                                break;
                            case 1:
//                                topRightMenu.setImageResource(R.drawable.question_press);
                                break;
                            case 2:
                                // 模考右上角图标
                                topRightMenu.setImageResource(R.drawable.img_guide_test);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        switch (page_tag) {
                            case 0:
                                // 牌组右上角图标
                                topRightMenu.setImageResource(R.drawable.top_right_new);
                                break;
                            case 1:
//                                topRightMenu.setImageResource(R.drawable.question);
                                break;
                            case 2:
                                // 模考右上角图标
                                topRightMenu.setImageResource(R.drawable.img_guide_test);
                                break;
                        }
                        break;
                }
                return false;
            }
        });

        top_right_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (page_tag) {
                    case 0://牌组页面
//                        is_teacher = 0;
                        if (is_teacher == 0 && member_level == 0) {
                            //用户信息为学生且用户等级为0
                            TopRightMenu mTopRightMenu = new TopRightMenu(AikaActivity.this);
                            //添加菜单项
                            List<MenuItem> menuItems = new ArrayList<>();
                            menuItems.add(new MenuItem(R.drawable.list_icon_choose_deck, "筛选牌组"));
                            menuItems.add(new MenuItem(R.drawable.img_exam_preparation, "临考备战"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_new_deck, "新建牌组"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_edit_note, "添加笔记"));
                            menuItems.add(new MenuItem(R.drawable.img_guide_picker, "使用指导"));

                            mTopRightMenu
                                    .setHeight((int) (44 * menuItems.size() * md))       //默认高度480  540    // 增加一个子菜单高度加45
                                    .setWidth((int) (120 * md))                            //默认宽度wrap_content 220
                                    .showIcon(true)                                     //显示菜单图标，默认为true
                                    .dimBackground(true)                                //背景变暗，默认为true
                                    .needAnimationStyle(true)                           //显示动画，默认为true
                                    .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                                    .addMenuList(menuItems)
                                    .showRadioButton(false)
                                    .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                                        @Override
                                        public void onMenuItemClick(int position) {
                                            switch (position) {
                                                case 0:
                                                    //筛选牌组
                                                    filterDeck();
                                                    break;
                                                case 1:
                                                    // 临考备战
                                                    goToExamPreparation();
                                                    break;
                                                case 2:
                                                    //新建牌组
                                                    createNewDeck();
                                                    break;
                                                case 3:
                                                    //添加笔记
                                                    addAikaNote();
                                                    break;
                                                case 4:
                                                    // 使用指导
                                                    goToGuide();
                                                    break;
                                            }
                                        }
                                    })
                                    .showAsDropDown(topRightMenu, (int) (-80 * md), (int) (11 * md));
                        } else if (is_teacher == 0 && member_level > 0) {
                            //用户信息为学生且等级大于0

                            TopRightMenu mTopRightMenu = new TopRightMenu(AikaActivity.this);
                            //添加菜单项
                            List<MenuItem> menuItems = new ArrayList<>();
                            menuItems.add(new MenuItem(R.drawable.list_icon_choose_deck, "筛选牌组"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_new_deck, "新建牌组"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_edit_note, "添加笔记"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_attention_class, "我的关注"));
                            menuItems.add(new MenuItem(R.drawable.img_guide_picker, "使用指导"));
                            mTopRightMenu
                                    .setHeight((int) (44 * menuItems.size() * md))     //默认高度480  540
                                    .setWidth((int) (120 * md))      //默认宽度wrap_content 220
                                    .showIcon(true)     //显示菜单图标，默认为true
                                    .dimBackground(true)        //背景变暗，默认为true
                                    .needAnimationStyle(true)   //显示动画，默认为true
                                    .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                                    .addMenuList(menuItems)
                                    .showRadioButton(false)
                                    .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                                        @Override
                                        public void onMenuItemClick(int position) {
                                            switch (position) {
                                                case 0:  //筛选牌组
                                                    filterDeck();
                                                    break;
                                                case 1:  //新建牌组
                                                    createNewDeck();
                                                    break;
                                                case 2:  //添加笔记
                                                    addAikaNote();
                                                    break;
                                                case 3:  //我的关注
                                                    attentionClass();
                                                    break;
                                                case 4:
                                                    // 使用指导
                                                    goToGuide();
                                                    break;
                                            }
                                        }
                                    })
                                    .showAsDropDown(topRightMenu, (int) (-80 * md), (int) (11 * md));
                        } else {
                            //用户信息为老师
                            TopRightMenu mTopRightMenu = new TopRightMenu(AikaActivity.this);
                            //添加菜单项
                            List<MenuItem> menuItems = new ArrayList<>();
                            menuItems.add(new MenuItem(R.drawable.list_icon_choose_deck, "筛选牌组"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_new_deck, "新建牌组"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_edit_note, "添加笔记"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_attention_class, "我的关注"));
                            menuItems.add(new MenuItem(R.drawable.list_icon_distribute_new, "分发作业"));
                            menuItems.add(new MenuItem(R.drawable.img_guide_picker, "使用指导"));

                            mTopRightMenu
                                    .setHeight((int) (44 * menuItems.size() * md))     //默认高度480
                                    .setWidth((int) (120 * md))      //默认宽度wrap_content
                                    .showIcon(true)     //显示菜单图标，默认为true
                                    .dimBackground(true)        //背景变暗，默认为true
                                    .needAnimationStyle(true)   //显示动画，默认为true
                                    .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                                    .addMenuList(menuItems)
                                    .showRadioButton(false)
                                    .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                                        @Override
                                        public void onMenuItemClick(int position) {
                                            switch (position) {
                                                case 0:  //筛选牌组
                                                    filterDeck();
                                                    break;
                                                case 1:  //新建牌组
                                                    createNewDeck();
                                                    break;
                                                case 2:  //添加笔记
                                                    addAikaNote();
                                                    break;
                                                case 3:  //我的关注
                                                    attentionClass();
                                                    break;
                                                case 4:  //分发作业
                                                    String username = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", "");
                                                    if (TextUtils.isEmpty(username)) {
                                                        //如果未登录
                                                        ToastAlone.showShortToast("请先登录！");
                                                    } else {
                                                        Intent i_2 = new Intent(AikaActivity.this, AikaDistributeActivity2.class);
                                                        startActivity(i_2);
                                                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                                                    }
                                                    break;
                                                case 5:
                                                    // 使用指导
                                                    goToGuide();
                                                    break;
                                            }
                                        }
                                    })
                                    .showAsDropDown(topRightMenu, (int) (-80 * md), (int) (11 * md));
                        }

                        break;
                    case 1://阅读页面
                        if (iconIsList) {
                            readerFragment.showListViewOrGridView("ListView");
                            topRightMenu.setImageResource(R.drawable.deckreader_gridview_icon);
                            iconIsList = false;
                        } else {
                            readerFragment.showListViewOrGridView("GridView");
                            topRightMenu.setImageResource(R.drawable.deckreader_listview_icon);
                            iconIsList = true;
                        }
                        break;
                    case 2://模考页面
                        Intent intent = new Intent(AikaActivity.this, GuideActivity.class);
                        String guideTest = "/sdcard/Chaojiyiji/images/guide_test1.gif";
                        intent.putExtra("filePath", guideTest);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 关注班级
     */
    private void attentionClass() {
        if (canGoToAttentionPage) {
            Intent i = new Intent(AikaActivity.this, AikaAttentClassActivity2.class);
            startActivity(i);
            overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
        } else {
            String username = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", "");
            if (TextUtils.isEmpty(username)) {
                //如果未登录
                ToastAlone.showShortToast("请先登录！");
//                Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_SHORT).show();
            } else {
                //如果登录了但是后台返回的数据为空
                ToastAlone.showShortToast("您的等级不够，尚不能关注班级！");
//                Toast.makeText(getApplicationContext(),"您的等级不够，尚不能关注班级！",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 筛选牌组
     */
    private void filterDeck() {
        int num = getDeckNumber();
        final String name = "过滤牌组0" + String.valueOf(num);
        final AlertDialog alertDialog_1 = new AlertDialog(AikaActivity.this);
        alertDialog_1.builder()
                .setTitle("创建新的过滤牌组")
                .setMsg("请输入筛选牌组的名称：")
                .setEditTextVisible(true)
                .setEditTextHint(name)
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long did;
                        String deckName = alertDialog_1.getEditTextContent();
                        if (TextUtils.isEmpty(deckName)) {
                            did = getCol().getDecks().newDyn(name);
                            jumpToCreateFilterActivity(name, did);
                        } else {
                            did = getCol().getDecks().newDyn(deckName);
                            jumpToCreateFilterActivity(deckName, did);
                        }
                    }
                }).show();
    }

    /**
     * 跳转使用指导
     */
    private void goToGuide() {
        Intent intentGuidePicker = new Intent(AikaActivity.this, GuideActivity.class);
        String guideDeck = "/sdcard/Chaojiyiji/images/guide_deck1.gif";
        intentGuidePicker.putExtra("filePath", guideDeck);
        startActivity(intentGuidePicker);
    }

    /**
     * 跳转临考备战页
     */
    private void goToExamPreparation() {
        Intent intent1 = new Intent(AikaActivity.this, ExamPreparationActivity.class);
        // 获取牌组数据
        if (mAdapter != null) {
            List<Node> nodes = mAdapter.getNodes();
            String[] decks = new String[nodes.size()];
            for (int i = 0; i < nodes.size(); i++) {
                decks[i] = nodes.get(i).getName();
            }
            intent1.putExtra("decks", decks);
        }
        startActivityWithoutAnimation(intent1);
    }

    /**
     * 牌组 -> 右上角菜单 -> 添加笔记
     */
    private void addAikaNote() {
        Intent intent = new Intent(AikaActivity.this, EKMainActivity.class);
        intent.putExtra(AikaNoteEditor.EXTRA_CALLER, AikaNoteEditor.CALLER_DECKPICKER);
        startActivity(intent);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
    }

    /**
     * 牌组 -> 右上角菜单 -> 创建新牌组
     */
    private void createNewDeck() {
        final AlertDialog alertDialog_2 = new AlertDialog(AikaActivity.this);
        alertDialog_2.builder()
                .setTitle("创建新牌组")
                .setMsg("请输入新牌组的名称：")
                .setEditTextVisible(true)
                .setEditTextHint("新牌组名称")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCol().getDecks().id(alertDialog_2.getEditTextContent(), true);
                        CardBrowser.clearSelectedDeck();
                        Log.e("AikaActivity", "调用了第" + 1639 + "行");
                        updateDeckList();
                    }
                }).show();
    }

    public SegmentedGroup getSegmented() {
        return segmented;
    }

/////////////添加方法用于显示和隐藏导入进度条------begin

    public void showInputProgressCircle(String title, String action) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (progressDialog != null) {

            progressDialog.show();
            progressDialog.setDownloadTip(title + " " + action);


        }
    }


    /**
     * 关闭进度条
     */
    public void disappearProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    /**
     * 关闭进度条
     */
    public void disappearProgressCircle() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            showGuide();
        }
    }
//////////////////添加方法用于显示和隐藏导入进度条------end


    /**
     * Try to open the Collection for the first time, and do some error handling if it wasn't successful
     *
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    private void configureFloatingActionsMenu() {
        final FloatingActionButton addDeckButton = (FloatingActionButton) findViewById(R.id.add_deck_action);
        final FloatingActionButton addSharedButton = (FloatingActionButton) findViewById(R.id.add_shared_action);
        final FloatingActionButton addNoteButton = (FloatingActionButton) findViewById(R.id.add_note_action);
        if (addDeckButton != null) {
            addDeckButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionsMenu == null) {
                        return;
                    }
                    mActionsMenu.collapse();
                    mDialogEditText = new EditText(AikaActivity.this);
                    mDialogEditText.setSingleLine(true);
                    // mDialogEditText.setFilters(new InputFilter[] { mDeckNameFilter });
                    new MaterialDialog.Builder(AikaActivity.this)
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
                                    Log.e("AikaActivity", "调用了第" + 1742 + "行");
                                    updateDeckList();
                                }
                            })
                            .negativeText(R.string.dialog_cancel)
                            .show();
                }
            });
        }
        if (addSharedButton != null) {
            addSharedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActionsMenu.collapse();
                    addSharedDeck();
                }
            });
        }
        if (addNoteButton != null) {
            addNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActionsMenu.collapse();
                    addNote();
                }
            });
        }
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
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
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
                mDialogEditText = new EditText(AikaActivity.this);
                ArrayList<String> names = getCol().getDecks().allNames();
                int n = 1;
                String name = String.format("%s %d", res.getString(R.string.filtered_deck_name), n);
                while (names.contains(name)) {
                    n++;
                    name = String.format("%s %d", res.getString(R.string.filtered_deck_name), n);
                }
                mDialogEditText.setText(name);
                // mDialogEditText.setFilters(new InputFilter[] { mDeckNameFilter });
                new MaterialDialog.Builder(AikaActivity.this)
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


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        //通过主页面条目进入详情页返回会调用,会显示对话框"正在加载中...",先这么解决
////        disappearProgress();
//        Log.e("MainActivity222222", "onActivityResult>>>>>>>>>>>>>>>>>>>>done!" + "requestCode: " + requestCode + "       resultCode: " + resultCode);
//        Log.e("AikaActivity", "requestCode>>>>>>>>>>>>>>>>>>>>"+ requestCode);
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        if (resultCode == RESULT_MEDIA_EJECTED) {
//            onSdCardNotMounted();
//            return;
//        } else if (resultCode == RESULT_DB_ERROR) {
//            handleDbError();
//            return;
//        }
//
//        if (requestCode == REPORT_ERROR) {
//            showStartupScreensAndDialogs(AnkiDroidApp.getSharedPrefs(getBaseContext()), 4);
//        } else if (requestCode == SHOW_INFO_WELCOME || requestCode == SHOW_INFO_NEW_VERSION) {
//            if (resultCode == RESULT_OK) {
//                showStartupScreensAndDialogs(AnkiDroidApp.getSharedPrefs(getBaseContext()),
//                        requestCode == SHOW_INFO_WELCOME ? 2 : 3);
//            } else {
//                finishWithAnimation();
//            }
//        } else if (requestCode == REPORT_FEEDBACK && resultCode == RESULT_OK) {
//        } else if (requestCode == LOG_IN_FOR_SYNC && resultCode == RESULT_OK) {
//            mSyncOnResume = true;
//        } else if (requestCode == ADD_SHARED_DECKS) {
//            if (intent != null) {
//                mImportPath = intent.getStringExtra("importPath");
//            }
//            /**
//             * @TODO
//             * 在添加课程页面下载完课程返回后会调用???
//             */
//            if (colIsOpen() && mImportPath != null) {
//                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT, mImportAddListener, new DeckTask.TaskData(mImportPath, true));
//                mImportPath = null;
//            }
//        } else if ((requestCode == REQUEST_REVIEW || requestCode == SHOW_STUDYOPTIONS)
//                && resultCode == Reviewer.RESULT_NO_MORE_CARDS) {
//            // Show a message when reviewing has finished
//            int[] studyOptionsCounts = getCol().getSched().counts();
//            if (studyOptionsCounts[0] + studyOptionsCounts[1] + studyOptionsCounts[2] == 0) {
////                showSimpleSnackbar(R.string.studyoptions_congrats_finished, false);
//                //跳转至恭喜完成页面
//                Intent intent_congratulation = new Intent(AikaActivity.this, CongratulationActivity.class);
//                startActivity(intent_congratulation);
//                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
//
//            } else {
//                showSimpleSnackbar(R.string.studyoptions_no_cards_due, false);
//            }
//        }else if ((requestCode == REQUEST_REVIEW || requestCode == SHOW_STUDYOPTIONS)
//                && resultCode == Reviewer.RESULT_DECK_CLOSED) {
//            //找到过滤牌组,卡片数是零的,把他删除掉;
//            //1. 遍历node, 找到其学习卡片数目为总和为0的;然后删掉它.
//
//
//            deleteDeck(Long.parseLong(intent.getStringExtra("DeckId")));
//
//        }
//
//        /**
//         * @TODO
//         * 在添加课程页面下载完课程返回后会调用
//         */
//        //艾卡共享的返回值处理
//        if (requestCode == 1111){
//            if (resultCode == RESULT_OK){
//                Log.e("DOWNLOADDECKS_RESULT_OK","DOWNLOADDECKS_RESULT_OK");
//                progressDialog.show();
//                progressDialog.setDownloadTip("正在加载中...");
////                Log.e("AikaActivity", "艾卡共享的返回值处理>>>>>>>>>>>>>>>>>>>>RESULT_OK");
////                String filepath = intent.getStringExtra("PathFromDownLoadDecks");
////                inputingDeckname = filepath.substring(filepath.lastIndexOf("/")+1);
////                Log.e("filepath", ">>>>>>>>>>>>>>>>>>>>"+ filepath);
////                inputingDeckFilepath = filepath;
////                importAdd(filepath);
//
//            }
//        }
//        /**
//         * @TODO
//         * 在添加课程页面下载完课程返回后会调用???
//         */
//        //Anki共享的返回值处理
//
//        if (requestCode == 0000){
//            if (resultCode == RESULT_OK){
//                String filepath = intent.getStringExtra("PathFromDownLoadDecksFromAnki");
//                inputingDeckname = filepath.substring(filepath.lastIndexOf("/")+1);
//                Log.e("filepath", ">>>>>>>>>>>>>>>>>>>>"+ filepath);
//                inputingDeckFilepath = filepath;
//                //导入牌组
//                importAdd(filepath);
//            }
//        }
//
//        //侧边栏SlidingMenu立即登录的返回值处理
////        if (requestCode == 0010){
////            if (resultCode == RESULT_OK){
////                Log.e("SlidingMenu", "login>>>result>>>>>>>>>>>>>>>>>>>>done!");
////                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
////                pref.getString("PersonalInfoResult1","");
////            }
////        }
//
//        if(requestCode==DECK_YIJI_FILTER_OPTIONS){
//            // 筛选牌组
//            createdNewName = intent.getStringExtra("newName");
//            if(!createdNewName.equals("onBackPressed")){
//                FilteDeckFile filteDeckFile = new FilteDeckFile(AikaActivity.this);
//                filteDeckFile.save(createdNewName+",");
//            }
//        }
//    }
    public static final int REQUEST_CODE_DOWNLOADDECKS = 3233;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.e("AikaActivity", "onActivityResult>>>>>>>>>>>>" + "requestCode: " + requestCode + "       resultCode: " + resultCode);
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
            /**
             * @TODO
             * 在添加课程页面下载完课程返回后会调用???
             */
            if (colIsOpen() && mImportPath != null) {
                Log.e("AikaActivity", "requestCode == ADD_SHARED_DECKS满足了,colIsOpen() && mImportPath != null都满足了");
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT, mImportAddListener, new DeckTask.TaskData(mImportPath, true));

                mImportPath = null;
            }
        } else if ((requestCode == REQUEST_REVIEW || requestCode == SHOW_STUDYOPTIONS)
                && resultCode == Reviewer.RESULT_NO_MORE_CARDS) {
            // Show a message when reviewing has finished
            int[] studyOptionsCounts = getCol().getSched().counts();
            if (studyOptionsCounts[0] + studyOptionsCounts[1] + studyOptionsCounts[2] == 0) {
//                showSimpleSnackbar(R.string.studyoptions_congrats_finished, false);
                //跳转至恭喜完成页面
                Intent intent_congratulation = new Intent(AikaActivity.this, CongratulationActivity.class);
                startActivity(intent_congratulation);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);

            } else {
                showSimpleSnackbar(R.string.studyoptions_no_cards_due, false);
            }
        } else if ((requestCode == REQUEST_REVIEW || requestCode == SHOW_STUDYOPTIONS)
                && resultCode == Reviewer.RESULT_DECK_CLOSED) {
            //找到过滤牌组,卡片数是零的,把他删除掉;
            //1. 遍历node, 找到其学习卡片数目为总和为0的;然后删掉它.


            deleteDeck(Long.parseLong(intent.getStringExtra("DeckId")));

        }

//        /**
//         * @TODO
//         * 在添加课程页面下载完课程返回后会调用
//         */
//        //艾卡共享的返回值处理
//        if (requestCode == REQUEST_CODE_DOWNLOADDECKS){
//            if (resultCode == RESULT_OK){
//                Log.e("AikaActivity", "艾卡共享的返回值处理>>>>>>>>>>>>>>>>>>>>RESULT_OK");
//                String filepath = intent.getStringExtra("PathFromDownLoadDecks");
//                inputingDeckname = filepath.substring(filepath.lastIndexOf("/")+1);
//                Log.e("filepath", "onActivityResult>>>>>>>>>>>>>>>>>>>>"+ filepath);
//                inputingDeckFilepath = filepath;
//                importAdd(filepath);
//
//            }
//        }
        /**
         * @TODO
         * 在添加课程页面下载完课程返回后会调用???
         */
        //Anki共享的返回值处理

        if (requestCode == 0000) {
            if (resultCode == RESULT_OK) {
                String filepath = intent.getStringExtra("PathFromDownLoadDecksFromAnki");
                inputingDeckname = filepath.substring(filepath.lastIndexOf("/") + 1);
                Log.e("filepath", ">>>>>>>>>>>>>>>>>>>>" + filepath);
                inputingDeckFilepath = filepath;
                //导入牌组
                importAdd(filepath);
            }
        }

        //侧边栏SlidingMenu立即登录的返回值处理
//        if (requestCode == 0010){
//            if (resultCode == RESULT_OK){
//                Log.e("SlidingMenu", "login>>>result>>>>>>>>>>>>>>>>>>>>done!");
//                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//                pref.getString("PersonalInfoResult1","");
//            }
//        }

        if (requestCode == DECK_YIJI_FILTER_OPTIONS) {
            // 筛选牌组
            createdNewName = intent.getStringExtra("newName");
            if (!createdNewName.equals("onBackPressed")) {
                FilteDeckFile filteDeckFile = new FilteDeckFile(AikaActivity.this);
                filteDeckFile.save(createdNewName + ",");
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        showFeatures();
        getDeckFile();
        mActivityResumed = true;
        mActivityPaused = false;
//        isAlreadyEnterUpdateMethod = false;
//        isAlreadyEnterImportMethod = false;
//        hasImported = false;
//        enterUpdateMethodCount = 0;
        if (mSyncOnResume) {
            sync();
            mSyncOnResume = false;
        } else if (colIsOpen() && !isFromDownloadDecks) {
            Log.e("AikaActivity", "onRsume>>>>>>>>>>>>>>" + "colIsOpen,  updateDeckList被调用了");
//            selectNavigationItem(DRAWER_DECK_PICKER);
            Log.e("AikaActivity", "调用了第" + 2123 + "行");
            updateDeckList();
            //setTitle(getResources().getString(R.string.app_name));
        }
        getDistributeJsonData();//重新获取分发作业页面数据
        getAttentionJsonData();//重新获取关注班级页面数据


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setGuideView();//dxAdd
            }
        }, 2000);

        //对从设置页面主题更换回来的页面进行主题变换
        if (ThemeChangeUtil.getCurrentThemeName() != themeName) {
            ThemeChangeUtil.changeTheme(this);
            ApplyTranslucency.applyKitKatTranslucency(AikaActivity.this);
            AikaActivity.this.restartActivity();
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
        disappearProgress();
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
        ButterKnife.unbind(this);
        if (mUnmountReceiver != null) {
            unregisterReceiver(mUnmountReceiver);
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Timber.d("onDestroy()");
        unregisterReceiver(myReceiver);
        unregisterReceiver(inputDeckReceiver);
        unregisterReceiver(updateSildingMenuUserInfo);
        unregisterReceiver(deckDownloadBroadcastReceiver);

        //取消EventBus的事件订阅
//        EventBus.getDefault().unregister(this);
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
//        if (isDrawerOpen()) {
//        if (slidingMenu.isMenuShowing()) {
////            super.onBackPressed();
//        } else {
        Timber.i("Back key pressed");
        if (mActionsMenu != null && mActionsMenu.isExpanded()) {
            mActionsMenu.collapse();
            //点击两下退出程序；
            //System.exit(0);
        } else {
            automaticSync();
            finishWithAnimation();
        }
//        }
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
        Intent intent = new Intent(AikaActivity.this, NoteEditor.class);
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
                preferences.edit().putString("fullscreenMode", old ? "1" : "0").commit();
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
            public void onPostExecute(DeckTask.TaskData result) {
                hideProgressBar();
                if (isReview) {
                    openReviewer();
                }
            }

            @Override
            public void onProgressUpdate(DeckTask.TaskData... values) {
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
     *
     * @param id id of dialog to show
     */
    @Override
    public void showSyncErrorDialog(int id) {
        showSyncErrorDialog(id, "");
    }

    /**
     * Show a specific sync error dialog
     *
     * @param id      id of dialog to show
     * @param message text to show
     */
    @Override
    public void showSyncErrorDialog(int id, String message) {
        AsyncDialogFragment newFragment = SyncErrorDialog.newInstance(id, message);
        showAsyncDialogFragment(newFragment);
    }

    /**
     * Show simple error dialog with just the message and OK button. Reload the activity when dialog closed.
     *
     * @param message
     */
    private void showSyncErrorMessage(String message) {
        String title = getResources().getString(R.string.sync_error);
        showSimpleMessageDialog(title, message, true);
    }

    /**
     * Show a simple snackbar message or notification if the activity is not in foreground
     *
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
                mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
                        getResources().getString(R.string.backup_repair_deck_progress), false);
            }


            @Override
            public void onPostExecute(DeckTask.TaskData result) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (result == null || !result.getBoolean()) {
                    Themes.showThemedToast(AikaActivity.this, getResources().getString(R.string.deck_repair_error), true);
                    onCollectionLoadError();
                }
            }


            @Override
            public void onProgressUpdate(DeckTask.TaskData... values) {
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
                mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
                        getResources().getString(R.string.check_db_message), false);
            }


            @Override
            public void onPostExecute(DeckTask.TaskData result) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (result != null && result.getBoolean()) {
                    String msg;
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
            public void onProgressUpdate(DeckTask.TaskData... values) {
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
                mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
                        getResources().getString(R.string.check_media_message), false);
            }


            @Override
            public void onPostExecute(DeckTask.TaskData result) {
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
            public void onProgressUpdate(DeckTask.TaskData... values) {
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
                    new Connection.Payload(new Object[]{hkey, preferences.getBoolean("syncFetchesMedia", true),
                            syncConflictResolution}));
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
                        .show(AikaActivity.this, getResources().getString(R.string.sync_title),
                                getResources().getString(R.string.sync_title) + "\n"
                                        + getResources().getString(R.string.sync_up_down_size, countUp, countDown),
                                false);

                // Override the back key so that the user can cancel a sync which is in progress
                mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // Make sure our method doesn't get called twice
                        if (event.getAction() != KeyEvent.ACTION_DOWN) {
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
        public void onPostExecute(Connection.Payload data) {
            String dialogMessage = "";
            String syncMessage;
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
                        SharedPreferences.Editor editor = preferences.edit();
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
                    } else if (resultType.equals("dbError") || resultType.equals("basicCheckFailed")) {
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
                Log.e("AikaActivity", "调用了第" + 3021 + "行");
                updateDeckList();
                WidgetStatus.update(AikaActivity.this);
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

    /**
     * 导入牌组
     *
     * @param importPath 牌组文件路径
     *                   实现接口：ImportDialog.ImportDialogListener
     */
    @Override
    public void importAdd(String importPath) {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT, mImportAddListener,
                new DeckTask.TaskData(importPath, false));
    }


    // Callback to import a file -- replacing the existing collection
    @Override
    public void importReplace(String importPath) {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_IMPORT_REPLACE, mImportReplaceListener, new DeckTask.TaskData(importPath));
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
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_EXPORT_APKG, mExportListener, new DeckTask.TaskData(inputArgs));
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
     * sd卡移除的广播接收
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
            showSnackbar(R.string.studyoptions_limit_reached, false, R.string.study_more, new View.OnClickListener() {
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
            showSnackbar(R.string.empty_deck, false, R.string.help, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(helpUrl);
                }
            }, findViewById(R.id.root_layout), mSnackbarShowHideCallback);
        } else {
            // Otherwise say there are no cards scheduled to study, and give option to do custom study
            showSnackbar(R.string.studyoptions_empty_schedule, false, R.string.custom_study, new View.OnClickListener() {
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
     * <p>
     * This method also triggers an update for the widget to reflect the newly calculated counts.
     */
    private void updateDeckList() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_LOAD_DECK_COUNTS, new DeckTask.TaskListener() {

            @Override
            public void onPreExecute() {
                //  showInputProgressCircle("", "加载中...");
//                if (!colIsOpen()) {
//                    showProgressBar();
//                }
//                Timber.d("Refreshing deck list");
            }

            @Override
            public void onPostExecute(DeckTask.TaskData result) {
                hideProgressBar();
                // Make sure the fragment is visible
                if (mFragmented) {
                    mStudyoptionsFrame.setVisibility(View.VISIBLE);
                }
                if (result == null) {
                    Timber.e("null result loading deck counts");
                    onCollectionLoadError();
                    return;
                }
                List<Sched.DeckDueTreeNode> nodes = (List<Sched.DeckDueTreeNode>) result.getObjArray()[0];
                Gson gson = new Gson();
                String nodesStr = gson.toJson(nodes);

                // 先清空list，防止数据重复加载
                mDatas2.clear();
                id = 1;
                // 递归处理牌组数据库数据
                ProcessData(nodes, 0);
                // 保存牌组列表数据
                String mData2_json = gson.toJson(mDatas2);
                SPUtil.setPreferences(SPUtil.TYPE_DECK_LIST_DATA, "mData2", mData2_json);
                CopyRawToDataForInitDeck.save(mData2_json, Environment.getExternalStorageDirectory().getAbsolutePath(), "datasssss");
                initListView();

                long tid = Thread.currentThread().getId();

                //初始化在线导入的红点的显示
                showmark = hasFilesInDownloadDecks();
                File[] files = downloadDecks.listFiles();
//                String downloadFileName;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < files.length; i++) {
                    stringBuffer.append(files[i].getName()).append(" ");
                }
                Log.e("stringBuffer", stringBuffer.toString());
                downloadFileName = Uri.decode(String.valueOf(stringBuffer));
//                downloadFileName = String.valueOf(stringBuffer);

                if (showmark) {
                    red_dot.setVisibility(View.VISIBLE);
                } else {
                    red_dot.setVisibility(View.GONE);
                }

                String testUrl = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "URLTODECK", "");
                if (!TextUtils.isEmpty(testUrl)) {
                    DownloadFromBackstage(testUrl);
                    SPUtil.remove(SPUtil.TYPE_DEFAULT_CONFIG, "URLTODECK");
                }
                LogUtil.e("onPostExecute: testUrl = " + testUrl);

                Log.e("onError>>>>>>>>>>>>", "======================001===========");

                preLoadingDeck1();

                Log.e("onError>>>>>>>>>>>>", "======================010===========");
                //preloadingDeck();
                //disappearProgressCircle();
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

                Log.e("onError>>>>>>>>>>>>", "======================011===========");
                if (loadingDialog != null && loadingDialog.isShowing() && beingPreLoadingDeck == false) {
                    Log.e("onError>>>>>>>>>>>>", "======================012===========");
                    loadingDialog.dismiss();
                    showGuide();
                }

                String loadingDialogIsShowing = loadingDialog.isShowing() ? "true" : "false";
                String activityIsFinishing = isFinishing() ? "true" : "false";
                String ActivityResume = mActivityResumed ? "true" : "false";
                //不是第一次启动,并且不是第一次通过登录界面登录//
//            if(!mActivityPaused && !isFirstUse && !isLoginFromWindow && !loadingDialog.isShowing()){
                Log.e("AikaActivity", "onPostExecute" + "  isShowing: " + loadingDialogIsShowing + "   activityIsFinishing：" + activityIsFinishing + "   isFromDownloadDecks：" + isFromDownloadDecks);

//                if(!loadingDialog.isShowing() && !isFinishing() && isImportingDeck){
                if (!loadingDialog.isShowing() && !isFinishing() && isFromDownloadDecks) {
                    progressDialog.showSuccessWithStatus();
                    progressDialog.setDownloadTip("恭喜，导入成功");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 1000);
                    Log.e("improt", "进入了:恭喜，导入成功");
                }

                Log.e("onError>>>>>>>>>>>>", "======================013===========");
            }

            @Override
            public void onProgressUpdate(DeckTask.TaskData... values) {
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

    private void initListView() {
        if (mDatas2 == null) {
            mDatas2 = new ArrayList<>();
        }
        try {
            mAdapter = new SimpleTreeAdapter<FileBean>(listView, AikaActivity.this, mDatas2, 0);//第四个参数控制默认展开的层数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //item点击事件
        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {

//                showInputProgressCircle("", "正在加载中...");

                /*if (node.isLeaf()){
                    // 子节点
                    String did = node.getDid();
                    long longDid = Long.parseLong(did);
                    handleDeckSelection(longDid, false);
                }else{
                    // 父节点,打开第一个子节点
                    Node node1 = node.getChildren().get(0);
                    String did = node1.getDid();
                    long longDid = Long.parseLong(did);
                    handleDeckSelection(longDid, false);
                }*/

                if ((Integer.parseInt(node.getLrnCount()) + Integer.parseInt(node.getRevCount()) + Integer.parseInt(node.getNewCount())) == 0) {
                    doItAgain(node);
                } else {
                    Log.e("AikaActivity", "正在加载中...第3354行");

                    showInputProgressCircle("", "正在加载中...");
                    if (node.isLeaf()) {
                        // 子节点
                        String did = node.getDid();
                        long longDid = Long.parseLong(did);
                        handleDeckSelection(longDid, false);
                    } else {
                        /**
                         * @TODO---6.26
                         */
                        // 父节点,打开第一个子节点
                        Node node1 = node.getChildren().get(0);
                        String did = node1.getDid();
                        long longDid = Long.parseLong(did);
                        handleDeckSelection(longDid, false);
                    }
                }
            }
        });


        //item长按事件
        mAdapter.setOnTreeNodeLongClickListener(new TreeListViewAdapter.OnTreeNodeLongClickListener() {
            @Override
            public void onClick(final Node node, int position) {

                if (isFinishing()) {
                    // 避免该错误的发生:Unable to add window -- token android.os.BinderProxy@6f7ba8 is not valid; is your activity running?
                    return;
                }

                sheetDialog = new ActionSheetDialog(AikaActivity.this)
                        .builder()
                        .setViewPostionAndWidth(Gravity.CENTER, 0.75)
                        .setTitle(node.getName())
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("重命名", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        final AlertDialog alertDialog = new AlertDialog(AikaActivity.this);
                                        alertDialog.builder()
                                                .setTitle(node.getName())
                                                .setMsg("请输入此牌组的新名称：")
                                                .setEditTextVisible(true)
                                                .setCancelable(false)
                                                .setNegativeButton("取消", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setPositiveButton("确定", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String newName = alertDialog.getEditTextContent();
                                                        String did = node.getDid();
                                                        Long longDid = Long.parseLong(did);
                                                        String currentName = getCol().getDecks().name(longDid);

                                                        Collection col = getCol();
                                                        if (newName != null && newName.length() != 0 && !newName.equals(currentName)) {
                                                            try {
                                                                col.getDecks().rename(col.getDecks().get(longDid), newName);
                                                            } catch (DeckRenameException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        Log.e("AikaActivity", "调用了第" + 3598 + "行");
                                                        updateDeckList();
                                                    }
                                                }).show();


                                    }
                                })
                        .addSheetItem("牌组选项", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        String did = node.getDid();
                                        Long longDid = Long.parseLong(did);
                                        Intent i = new Intent(AikaActivity.this, DeckOptionsAika.class);
                                        i.putExtra("did", longDid);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);

                                    }
                                })
                        .addSheetItem("自定义学习", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        String did = node.getDid();
                                        Long longDid = Long.parseLong(did);
                                        Collection col = CollectionHelper.getInstance().getCol(AikaActivity.this);
                                        col.getDecks().select(longDid);

                                        int totalNewForCurrentDeck = col.getSched().totalNewForCurrentDeck();//牌组中新卡片的数量
                                        int totalRevForCurrentDeck = col.getSched().totalRevForCurrentDeck();//牌组中到期的卡牌数量

                                        Intent intent = new Intent(AikaActivity.this, CustomStudyActivity.class);
                                        intent.putExtra("totalNewForCurrentDeck", totalNewForCurrentDeck);
                                        intent.putExtra("totalRevForCurrentDeck", totalRevForCurrentDeck);
                                        intent.putExtra("did", longDid);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                                    }
                                })
                        .addSheetItem("删除牌组", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        new AlertDialog(AikaActivity.this).builder()
                                                .setTitle("删除牌组")
                                                .setMsg("你确定要删除" + node.getName() + "吗？")
                                                .setCancelable(false)
                                                .setNegativeButton("否", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setPositiveButton("是", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String did = node.getDid();
                                                        Long longDid = Long.parseLong(did);
                                                        deleteDeck(longDid);
                                                    }
                                                }).show();
                                    }
                                })
                        .addSheetItem("取消", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        sheetDialog.dismiss();
                                    }
                                });
                sheetDialog.show();
            }
        });

        //侧滑删除按钮点击事件
        mAdapter.setOnDeleteListener(new SimpleTreeAdapter.OnDeleteListener() {
            @Override
            public void delete(final Node node, int position) {
                new AlertDialog(AikaActivity.this).builder()
                        .setTitle("删除牌组")
                        .setMsg("你确定要删除" + node.getName() + "吗？")
                        .setCancelable(false)
                        .setNegativeButton("否", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String did = node.getDid();
                                Long longDid = Long.parseLong(did);
                                deleteDeck(longDid);
                            }
                        }).show();
            }
        });

        mAdapter.setOnLoadComplete(new SimpleTreeAdapter.OnLoadComplete() {
            @Override
            public void loadComplete() {
                //@TODO....6.12会影响dialog显示
                Log.e("AikaActivity", "disappearProgressCircle调用了");
                disappearProgressCircle();
//                showGuide();
            }
        });

        mAdapter.setUpdataDeckListRun(new Runnable() {
            @Override
            public void run() {
                // 更新牌组列表
                Log.e("AikaActivity", "调用了第" + 3709 + "行");
                updateDeckList();
            }
        });
        listView.setAdapter(mAdapter);
    }

    /**
     * 当该牌组未00时点击弹出对话框
     *
     * @param node
     */
    private void doItAgain(Node node) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(AikaActivity.this).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawableResource(R.drawable.c_00000000);
        window.setContentView(R.layout.dialog_practice_again);
        RelativeLayout rlAlignLearn = (RelativeLayout) window.findViewById(R.id.rl_align_learn);
        RelativeLayout rlLearnToday = (RelativeLayout) window.findViewById(R.id.rl_learn_today);
        ImageView ivClose = (ImageView) window.findViewById(R.id.iv_close);
        TextView tvName = (TextView) window.findViewById(R.id.tv_name);
        final String name = node.getName();
        tvName.setText(name);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        rlAlignLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // （重温）:deck:"初三英语单词" rated:1
                // （今日错题）:deck:"初三英语单词" rated:1:1
                if (!name.contains("（重温）")) {
                    filterDeck(name + "（重温）", "rated:1");
                } else {
                    filterDeck(name, "rated:1");
                }
                alertDialog.dismiss();
            }
        });
        rlLearnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.contains("（今日错题）")) {
                    filterDeck(name + "（今日错题）", "rated:1:1");
                } else {
                    filterDeck(name, "rated:1:1");
                }
                alertDialog.dismiss();
            }
        });

        /*if((Integer.parseInt(node.getLrnCount()) + Integer.parseInt(node.getRevCount()) + Integer.parseInt(node.getNewCount())) == 0){
        }*/
    }

    /**
     * 筛选牌组
     *
     * @param deckName
     * @param search
     */
    private void filterDeck(String deckName, String search) {
        Collection mCol = CollectionHelper.getInstance().getCol(AikaActivity.this);
        if (mCol == null) {
            return;
        }
        JSONObject mDeck;
        long did = mCol.getDecks().newDyn(deckName);
        mDeck = mCol.getDecks().current();
//        mDeck = mCol.getDecks().get(did);
        try {
            JSONArray term = new JSONArray();
            term.put(search);   // search
            term.put(100);      // limit
            term.put(1);        // order

            JSONArray terms = new JSONArray();
            terms.put(0, term);

            JSONArray delays = new JSONArray();
            delays.put(1);
            delays.put(10);

            mDeck.put("terms", terms);
            mDeck.put("delays", delays);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCol.getDecks().save(mDeck);
        mCol.getSched().rebuildDyn(mCol.getDecks().selected());
        mCol.save();

        FilteDeckFile filteDeckFile = new FilteDeckFile(AikaActivity.this);
        filteDeckFile.save(deckName + ",");
        Log.e("AikaActivity", "调用了第" + 3810 + "行");
        updateDeckList();
    }

    int id = 0;

    /**
     * 递归处理牌组数据库数据
     *
     * @param nodes    牌组数据
     * @param parentId 父级ID
     */
    private void ProcessData(List<Sched.DeckDueTreeNode> nodes, int parentId) {

        for (int i = 0; i < nodes.size(); i++) {
            Sched.DeckDueTreeNode deckDueTreeNode = nodes.get(i);
            String name = deckDueTreeNode.names[0];
            String depth = String.valueOf(deckDueTreeNode.depth);
            String did = String.valueOf(deckDueTreeNode.did);
            String lrnCount = String.valueOf(deckDueTreeNode.lrnCount);
            String newCount = String.valueOf(deckDueTreeNode.newCount);
            String revCount = String.valueOf(deckDueTreeNode.revCount);

            if (name.equals("Default") && deckDueTreeNode.lrnCount == 0 && deckDueTreeNode.newCount == 0) {
                //跳过掉名字为Default，并且复习数量和新卡片数量都为0的牌组数据
            } else {
                // 添加节点
                id++;
                mDatas2.add(new FileBean(id, parentId, name, depth, did, lrnCount, newCount, revCount));
                if (!deckDueTreeNode.children.isEmpty()) {
                    // 添加该节点下的子节点
                    int childrensParentId = id;
                    ProcessData(deckDueTreeNode.children, childrensParentId);
                }
            }
        }
    }

    //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_guide_close:
                // 关闭帮助指导
                rlGuide.setVisibility(View.GONE);

                break;
            //左上角的弹出侧边栏按钮,写两个?
            case R.id.icon_main_home:
            case R.id.icon_main_home2:
                // toOpenDrawer();
                slidingMenu.toggle();
                break;

            case R.id.deck_reader_lyt:
                // 阅读
                selectDeckReader();
                break;
            case R.id.deck_picker_lyt:
            case R.id.deck_picker_lyt2:
                // 牌组
                selectDeckPicker();
                break;
            case R.id.deck_test_lyt:
                // 模考
                selectDeckTest();
                break;
        }
    }


    /**
     * 收到推送的消息后,进行下载,并且改变ui ,加上小红点;
     * 在此广播接收器中进行deck的下载，并更新UI
     */
    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("DeckP-onReceive", ">>>>>>>>>>onReceived!");
            Toast.makeText(getApplicationContext(), "有新的牌组可以在线导入了！", Toast.LENGTH_SHORT).show();
            String url = intent.getStringExtra("URL");
            DownloadFromBackstage(url);

            String urltodeck = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "URLTODECK", "");
            if (!TextUtils.isEmpty(urltodeck)) {
                SPUtil.remove(SPUtil.TYPE_DEFAULT_CONFIG, "URLTODECK");
            }
        }
    }

    /**
     * 从后台下载文件到downloadDecks目录下，下载成功后更新UI
     *
     * @param url 推送透传过来的下载地址
     */
    private void DownloadFromBackstage(String url) {
        if (url.lastIndexOf("/") != -1) {
            final String filename = url.substring(url.lastIndexOf("/") + 1);//获取文件名

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
                    showmark = hasFilesInDownloadDecks();
                    downloadFileName = filename;
                    if (showmark) {
                        red_dot.setVisibility(View.VISIBLE);
                    } else {
                        red_dot.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Log.e("DownloadFromBackstage", "从后台获取的url地址有错误");
        }
    }

    /**
     * 判断downloadDecks文件夹下是否存在文件
     */
    private boolean hasFilesInDownloadDecks() {
        if (!downloadDecks.exists()) {
            downloadDecks.mkdirs();
        }
        File[] files = downloadDecks.listFiles();
        if (files.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 遍历deck的数据库，得到相应的JSON，传给HTML页面，再次刷新牌组页面
     */
    private void loadDeckList() {

        String deckJson = "";//需要写方法获取已有牌组的JSON数据
    }

    // Callback to show study options for currently selected deck
    public void showContextMenuDeckOptions() {
        // open deck options
        if (getCol().getDecks().isDyn(mContextMenuDid)) {
            // open cram options if filtered deck
            Intent i = new Intent(AikaActivity.this, FilteredDeckOptions.class);
            i.putExtra("did", mContextMenuDid);
            startActivityWithAnimation(i, ActivityTransitionAnimation.FADE);
        } else {
            // otherwise open regular options
            Intent i = new Intent(AikaActivity.this, DeckOptions.class);
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
        mDialogEditText = new EditText(AikaActivity.this);
        mDialogEditText.setSingleLine();
        final String currentName = getCol().getDecks().name(did);
        mDialogEditText.setText(currentName);
        new MaterialDialog.Builder(AikaActivity.this)
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
                                Themes.showThemedToast(AikaActivity.this, e.getLocalizedMessage(res), false);
                            }
                        }
                        dismissAllDialogFragments();
                        mDeckListAdapter.notifyDataSetChanged();
                        Log.e("AikaActivity", "调用了第" + 4039 + "行");
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

    @SuppressLint("StringFormatInvalid")
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
//                mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
//                        getResources().getString(R.string.delete_deck), false);

                //svProgressHUD.showWithStatus("牌组删除中...");

                progressDialog.show();
                progressDialog.setDownloadTip("牌组删除中...");

                if (did == getCol().getDecks().current().optLong("id")) {
                    removingCurrent = true;
                }
            }


            @SuppressWarnings("unchecked")
            @Override
            public void onPostExecute(DeckTask.TaskData result) {
                if (result == null) {
                    return;
                }
                // In fragmented mode, if the deleted deck was the current deck, we need to reload
                // the study options fragment with a valid deck and re-center the deck list to the
                // new current deck. Otherwise we just update the list normally.
                if (mFragmented && removingCurrent) {
                    Log.e("AikaActivity", "调用了第" + 4136 + "行");
                    updateDeckList();
                    openStudyOptions(false);
                } else {
                    Log.e("AikaActivity", "调用了第" + 4140 + "行");
                    updateDeckList();
                }

//                if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                    try {
//                        mProgressDialog.dismiss();
//                    } catch (Exception e) {
//                        Timber.e(e, "onPostExecute - Exception dismissing dialog");
//                    }
//                }

//                svProgressHUD.dismiss();
                progressDialog.dismiss();
                CardBrowser.clearSelectedDeck();
                // TODO: if we had "undo delete note" like desktop client then we won't need this.
                getCol().clearUndo();
            }


            @Override
            public void onProgressUpdate(DeckTask.TaskData... values) {
            }


            @Override
            public void onCancelled() {
            }
        }, new DeckTask.TaskData(did));
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
            Log.e("AikaActivity", "调用了第" + 4184 + "行");
            updateDeckList();
            if (mFragmented) {
                loadStudyOptionsFragment(false);
            }
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
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
        Log.e("AikaActivity", "调用了第" + 4226 + "行");
        updateDeckList();
    }


    private void openReviewer() {
        Intent reviewer = new Intent(this, Reviewer.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("map", serializableMap);
        reviewer.putExtras(bundle);
//        startActivityForResultWithAnimation(reviewer, REQUEST_REVIEW, ActivityTransitionAnimation.LEFT);
        startActivityForResult(reviewer, REQUEST_REVIEW);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
        getCol().startTimebox();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isSkipToLoginActivity) {
            copyFile2SD1();
            isSkipToLoginActivity = false;
        }
    }

    @Override
    public void onCreateCustomStudySession() {
        Log.e("AikaActivity", "调用了第" + 4250 + "行");
        updateDeckList();
        openStudyOptions(false);
    }

    @Override
    public void onExtendStudyLimits() {
        if (mFragmented) {
            getFragment().refreshInterface(true);
        }
        Log.e("AikaActivity", "调用了第" + 4260 + "行");
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
                if (b != null) {
                    b.setEnabled(true);
                }
            }
        }

        @Override
        public void onShown(Snackbar snackbar) {
            if (!CompatHelper.isHoneycomb()) {
                final android.support.design.widget.FloatingActionButton b;
                b = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add_note_action);
                if (b != null) {
                    b.setEnabled(false);
                }
            }
        }
    };

    public void handleEmptyCards() {
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_FIND_EMPTY_CARDS, new DeckTask.Listener() {
            @Override
            public void onPreExecute(DeckTask task) {
                mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
                        getResources().getString(R.string.emtpy_cards_finding), false);
            }

            @Override
            public void onPostExecute(DeckTask task, DeckTask.TaskData result) {
                final List<Long> cids = (List<Long>) result.getObjArray()[0];
                if (cids.size() == 0) {
                    showSimpleMessageDialog(getResources().getString(R.string.empty_cards_none));
                } else {
//                    String msg = String.format(getResources().getString(R.string.empty_cards_count), cids.size());
//                    ConfirmationDialog dialog = new ConfirmationDialog() {
//                        @Override
//                        public void confirm() {
//                            getCol().remCards(Utils.arrayList2array(cids));
//                            showSimpleSnackbar(String.format(
//                                    getResources().getString(R.string.empty_cards_deleted), cids.size()), false);
//                        }
//                    };
//                    dialog.setArgs(msg);
//                    showDialogFragment(dialog);
                }

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onProgressUpdate(DeckTask task, DeckTask.TaskData... values) {

            }

            @Override
            public void onCancelled() {

            }
        });
    }

    private void doOnFirstLoadedWebview() {
//        updateDeckList();
        //////////预加载牌组////////开始
//        SharedPreferences preferences = getSharedPreferences("showWelcome", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
////        preferences.getBoolean("FirstUse",false)
//        Log.e("预加载牌组", ">>>>>>>>>>>>>>>>>>>>>>done!");
//        String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/初三英语单词.apkg";
//        File file  = new File(outpath);
//        if (file.exists() && preferences.getBoolean("FirstUse",false)){
//            Log.e("file.exists()", ">>>>>>>>>>>>>>>>>>>>>>done!");
//            importAdd(outpath);
//            editor.putBoolean("FirstUse",false);
//            editor.commit();
//        }

        Log.e("AikaActivity", "调用了第" + 4313 + "行");
        updateDeckList();
        //////////预加载牌组////////结束
    }

    /**
     * 预下载牌组,vip用户
     */
    private void downloadTodirDecks() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean preLoadVipDeck = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOAD_VIP_DECK, false);
                if (!preLoadVipDeck) {
                    String infoJson = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_INFO_RESULT_1, "");
                    if (TextUtils.isEmpty(infoJson)) {
                        return;
                    }
                    PersonInfo1 info = GsonUtil.json2Bean(infoJson, PersonInfo1.class);
                    String path = null;
                    if (!TextUtils.equals(info.getCode(), "1")) {
                        path = info.getData().getMem().getDeck();
                    } else {
                        LogUtil.e("run: 获取用户信息失败");
                    }
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
//                     URLDecoder.decode("http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201705021511/%E8%BE%BE%E5%86%85%E8%AF%BE%E4%BB%B6%E7%AE%80%E6%98%93%E7%89%88.apkg","UTF-8")
                    String filename = "def.apkg";
                    try {
                        String decodePath = URLDecoder.decode(path, "UTF-8");
                        filename = decodePath.substring(decodePath.lastIndexOf("/") + 1);
                        inputingDeckname = filename;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + filename;
                    ZXUtils.DownLoadFile(path, filepath, new Callback.CommonCallback<File>() {
                        @Override
                        public void onSuccess(File result) {
                            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOADING_DECK_PATH, result.getAbsolutePath());
                            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOAD_VIP_DECK, true);
                            LogUtil.e("onSuccess: result.getAbsolutePath() = " + result.getAbsolutePath());
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
                        }
                    });
                }
            }
        }, 1000);
    }


    /**
     * @TODO
     * 在添加课程页面下载完课程返回后会调用???
     */
    /**
     * 预加载牌组
     */
    private void preLoadingDeck1() {
        //1.抽取出DeckDir下面所有以.apkg结尾的文件,并形成一个File数组
        String deckDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks";
        File[] deckArray = (new File(deckDir)).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".apkg")) ;
                return true;
            }
        });
        //2.判断File数组的长度小于1返回,否则导入第一个元素,
        if (deckArray.length < 1) {
            return;
        } else {
            beingPreLoadingDeck = true;
            Log.e("AikaActivity", "importAdd调用了,第4445行");
            importAdd(deckArray[0].getAbsolutePath());
        }
    }

    /**
     * 预加载牌组
     */
    private void preloadingDeck() {
        //1.判断是否是刚刚安装上的应用,


        // String outpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/达内课件简易版.apkg";
        String filepath = SPUtil.getPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOADING_DECK_PATH, "");
        File file = new File(filepath);
        if (!TextUtils.isEmpty(filepath) && file.exists()) {
            importAdd(filepath);
            LogUtil.e("preloadingDeck: 导入牌组:" + filepath);
            inputingDeckname = file.getName();
            inputingDeckFilepath = file.getAbsolutePath();
            SPUtil.setPreferences(SPUtil.TYPE_APP_CONFIG, SPConstants.APP_CONFIG_PRE_LOADING_DECK_PATH, "");
        }
    }


    private void jumpToCreateFilterActivity() {
        Intent reviewer = new Intent(this, FilteredDeckActivity.class);
        startActivityForResultWithAnimation(reviewer, DECK_YIJI_FILTER_OPTIONS, ActivityTransitionAnimation.LEFT);
    }

    private void jumpToCreateFilterActivity(String name, long did) {
        Intent reviewer = new Intent(this, FilteredDeckActivity.class);
        reviewer.putExtra("name", name);
        reviewer.putExtra("did", did);
        startActivityForResultWithAnimation(reviewer, DECK_YIJI_FILTER_OPTIONS, ActivityTransitionAnimation.LEFT);
    }


//    private void jumpToGuojiSharedDecksActivity(){
//        Intent reviewer = new Intent(this, DownLoadDecks_webview.class);
//        startActivityForResultWithAnimation(reviewer, DECK_YIJI_FILTER_OPTIONS, ActivityTransitionAnimation.LEFT);
//    }

    private void jumpToGuojiSharedDecksFromAnkiActivity() {
        Intent reviewer = new Intent(this, DownLoadDecksFromAnki.class);
        startActivityForResultWithAnimation(reviewer, 0000, ActivityTransitionAnimation.LEFT);
    }

    private void jumpToGuoneiSharedDecksActivity() {
        Intent reviewer = new Intent(this, DownLoadDecks.class);
        startActivityForResultWithAnimation(reviewer, REQUEST_CODE_DOWNLOADDECKS, ActivityTransitionAnimation.LEFT);
    }

    class MyObject {
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public String getTime() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }

        @JavascriptInterface
        public void xmhandleDeckSelection(String did, boolean skipStudyOptioon) {
            long longDid = Long.parseLong(did);
            handleDeckSelection(longDid, skipStudyOptioon);
        }

        @JavascriptInterface
        public void renameCurrentDeck(String did) {
        }

        @JavascriptInterface
        public void oneDeckOption(String did) {
        }

        @JavascriptInterface
        public void selfDefineStudy(String did, boolean b) {
        }

        @JavascriptInterface
        public void switch_reader(String did, boolean b) {
//            Intent intent = new Intent(DeckPicker.this, DeckReader.class);
            Intent intent = new Intent(AikaActivity.this, DeckReader.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            AikaActivity.this.finish();
            AikaActivity.this.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void switch_test(String did, boolean b) {
            Intent intent = new Intent(AikaActivity.this, DeckTest.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            AikaActivity.this.finish();
            AikaActivity.this.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void deleteOneDeck(String did) {
            final Long longDid = Long.parseLong(did);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deleteDeck(longDid);
                }
            });
        }

        @JavascriptInterface
        public void showSlideMenu() {
            //Log.i("xxxxx", "xxxxrrr");
            //long tid = Thread.currentThread().getId();
            //Log.i("tid", Long.toString(tid));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    toOpenDrawer();
                }
            });

//            //测试个人中心页面
//            Intent intent = new Intent(DeckPicker.this, PersonalCenterActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
        }

        @JavascriptInterface
        public void createFilterDeck(final String filterDeckName) {
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
        public void datacheck() {
        }

        @JavascriptInterface
        public void mediaCheck() {
        }

        @JavascriptInterface
        public void createNewtDeck(final String deckName) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String deckName = mDialogEditText.getText().toString();
                    Timber.i("DeckPicker:: Creating new deck...");
                    getCol().getDecks().id(deckName, true);
                    CardBrowser.clearSelectedDeck();
                    Log.e("AikaActivity", "调用了第" + 4600 + "行");
                    updateDeckList();
                }
            });
        }

        @JavascriptInterface
        public void addNewNote() {
        }

        @JavascriptInterface
        public void openGuojiSharedDecks() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jumpToGuojiSharedDecksFromAnkiActivity();
                }
            });
        }

        @JavascriptInterface
        public void openGuoneiSharedDecks() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jumpToGuoneiSharedDecksActivity();

                }
            });
        }

        @JavascriptInterface
        public void importDownloadDeck() {
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
                    loadDeckList();
                }
            });
        }


        @JavascriptInterface
        public void setTitleColor() {
            Log.e("setTitleColor", ">>>>>>>>>>>>setTitleColor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ApplyTranslucency.applyKitKatTranslucency2(DeckPicker.this);
                }
            });

        }

        @JavascriptInterface
        public void cancelTitleColor() {
            Log.e("cancelTitleColor", ">>>>>>>>>>>>cancelTitleColor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ApplyTranslucency.applyKitKatTranslucency(DeckPicker.this);
                }
            });
        }

    }

    /**
     * @TODO 在添加课程页面下载完课程返回后会调用???
     */
    class InputDeckReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("AikaActivity", "importAdd调用了,第4699行");
            importAdd(inputingDeckFilepath);
        }
    }

    String personal_data_1;
    String personal_data_2;
    String personal_data_3;
    UserInfoBean userInfoBean;
    String username = null;
    String head_img_url;
    String schoolLogo;
    private int is_teacher = 0;
    private int member_level = 0;
    private boolean canExecuteDownloadHeadImage;//解决第三方登录后广播收到两次的下载头像失败的BUG

    class UpdateSildingMenuUserInfo extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("UpdateSildingUserInfo", "Update Username And User Head Image ,Broadcast has Received!");
            boolean isFromThirdLogin = intent.getBooleanExtra("isFromThirdLogin", false);
            Log.e("UpdateSildingUserInfo", "isFromThirdLogin>>>" + isFromThirdLogin);
            String url = null;
            String name = null;
            canExecuteDownloadHeadImage = !canExecuteDownloadHeadImage;
            if (isFromThirdLogin) {
                url = intent.getStringExtra("third_login_icon_url");
                name = intent.getStringExtra("third_login_user_name");
//                if (canExecuteDownloadHeadImage) {
//                    DownloadHeadImage(url);
//                }
//                DownloadHeadImage(url);
                if (name != null) {
                    slideMenuManager.setLoginName(name);
                    DownloadHeadImage(url);
                } else {
                    slideMenuManager.setLoginImgShow();
                    slideMenuManager.setHeadImg(R.drawable.headportrait);
                    canGoToAttentionPage = false;
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        personal_data_1 = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1", "");
                        Log.e("personal_data_1", personal_data_1);
                        String personal_data = "";
                        if (!personal_data_1.isEmpty()) {
                            userInfoBean = new Gson().fromJson(personal_data_1, UserInfoBean.class);
                            Log.e("personal_data_1", userInfoBean.toString());
                            try {
                                UserInfoBean.DataBean.MemBean mem = userInfoBean.getData().getMem();
                                username = mem.getHoneyname();
                                head_img_url = mem.getFace();
                                is_teacher = Integer.valueOf(mem.getIs_teacher());
                                member_level = Integer.valueOf(mem.getMember_level());
                                schoolLogo = mem.getSchool_logo();
                                Log.e("personal_data_1", "1");
                                if (Integer.valueOf(userInfoBean.getCode()) == 1000) {
                                    Log.e("personal_data_1", "2");
                                    Log.e("personal_data_1", "正确" + userInfoBean.getData().getMem().toString());

                                    //保存用户的身份信息
                                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "IS_TEACHER", is_teacher);
                                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEMBER_LEVEL", member_level);
                                    initSchoolLogo(schoolLogo);
                                    DownloadHeadImage(head_img_url);
                                } else {
                                    icon_home.setVisibility(View.VISIBLE);
                                    icon_home2.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (username != null) {
                                slideMenuManager.setLoginName(username);
                                canGoToAttentionPage = true;
                            } else {
                                slideMenuManager.setLoginImgShow();
                                slideMenuManager.setHeadImg(R.drawable.headportrait);
                                canGoToAttentionPage = false;
                            }
                        } else {

                            slideMenuManager.setLoginImgShow();
                            slideMenuManager.setHeadImg(R.drawable.headportrait);
                            canGoToAttentionPage = false;

                        }
                    }
                }, 200);

            }

        }

    }

    private void initSchoolLogo(String schoolLogo) {
        x.image().bind(icon_home2, schoolLogo, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                icon_home.setVisibility(View.GONE);
                icon_home2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                icon_home.setVisibility(View.VISIBLE);
                icon_home2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                icon_home.setVisibility(View.VISIBLE);
                icon_home2.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //从OSS服务器下载用户头像图片
    private void DownloadHeadImage(String url) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.e("DownloadHeadImage: 用户头像URL为:" + url);
            return;
        }
        String filename = url.substring(url.lastIndexOf("/") + 1);
        final String filepath = ROOT_PATH + "/Chaojiyiji/picture/" + filename;
        ZXUtils.DownLoadFile(url, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onSuccess");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onError>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("DownloadHeadImage", ">>>>>>>>>>>onFinished");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
                if (bitmap != null) {
                    slideMenuManager.setHeadImg(bitmap);
                } else {
                    slideMenuManager.setHeadImg(R.drawable.headportrait);
                }

            }
        });
    }


    //从OSS服务器下载AnkiChina的牌组资源数据
    private void getDataFromAnkiChina() {
        Log.e("getDataFromAnkiChina", "getDataFromAnkiChina>>>>>>>>>>done!");
        OSSUtil.getDeckInfo(this);
    }
    //***********************************************************//
    //***********************************************************//

    //从后台请求分发作业页面的数据
    public void getDistributeJsonData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
        String mem_id = pref.getString("MEM_ID", "");
        Map map = new HashMap<String, String>();
        map.put("mem_id", mem_id);
        ZXUtils.Post(Urls.MESSAGE_LIST, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getDistributeJsonData", ">>>onSuccess>>>" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 1000) {
                        editor.putString("DistributeJsonData", jsonObject.toString());
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 从后台请求关注班级页面的数据
     */
    public void getAttentionJsonData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
        String mem_id = pref.getString("MEM_ID", "");

        Map map = new HashMap<String, String>();
        map.put("mem_id", mem_id);
        ZXUtils.Post(Urls.CLASS_LIST, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAttentionJsonData", ">>>onSuccess>>>" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 1000) {
                        editor.putString("AttentionJsonData", jsonObject.toString());
                        editor.commit();
                        canGoToAttentionPage = true;
                    } else {
                        //标志符 让用户先登录
                        canGoToAttentionPage = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    ////////////////////////////////////AikaActivity////////////////////////////////////////////

    /**
     * segment的点击事件
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.deck_reader:
                Log.e("onCheckedChanged", "deck_reader done!");
                if (readerFragment == null) {
                    readerFragment = new DeckReaderFragment();
                }

                //fragment每次重建刷新的代码
//                FragmentTransaction ft1 = fm.beginTransaction();
//                ft1.replace(R.id.fragment_main2, readerFragment);
//                ft1.commit();


                //fragment不会每次重建刷新的代码
                switchContent(readerFragment);

                topRightMenu.setImageResource(R.drawable.question);
                page_tag = 1;//阅读页面的标识
                break;
            case R.id.deck_picker:
            case R.id.deck_picker2:
                Log.e("onCheckedChanged", "deck_picker done!");
                //fragment每次重建刷新的代码
//                FragmentTransaction ft2 = fm.beginTransaction();
//                ft2.replace(R.id.fragment_main2, pickerFragment);
//                ft2.commit();

                //fragment不会每次重建刷新的代码
                switchContent(pickerFragment);


//                doOnFirstLoadedWebview();/////////再次加载牌组数据

                topRightMenu.setImageResource(R.drawable.top_right);
                /*if(topRightMenu.isPressed()){
                    topRightMenu.setImageResource(R.drawable.top_right_press);
                }else{
                    topRightMenu.setImageResource(R.drawable.top_right);

                }*/
                page_tag = 0;//牌组页面的标识


                break;
            case R.id.deck_test:
                Log.e("onCheckedChanged", "deck_test done!");
                if (testFragment == null) {
                    testFragment = new DeckTestFragment2();
                }

                //fragment每次重建刷新的代码
//                FragmentTransaction ft3 = fm.beginTransaction();
//                ft3.replace(R.id.fragment_main2, testFragment);
//                ft3.commit();

                //fragment不会每次重建刷新的代码
                switchContent(testFragment);

                topRightMenu.setImageResource(R.drawable.question);
                /*if(topRightMenu.isPressed()){
                    topRightMenu.setImageResource(R.drawable.question_press);
                }else{
                    topRightMenu.setImageResource(R.drawable.question);
                }*/
                page_tag = 2;//模考页面的标识
                break;

            default:
        }
    }
    ////////////////////////////////////AikaActivity////////////////////////////////////////////


    /**
     * 修改显示的内容 不会重新加载
     * to 下一个fragment
     * mContent 当前的fragment
     */
    private void switchContent(Fragment to) {
        if (mContent != to) {
            ft = fm.beginTransaction();
            if (!to.isAdded()) { // 判断是否被add过
                // 隐藏当前的fragment，将 下一个fragment 添加进去
                ft.hide(mContent).add(R.id.fl_content, to).commit();
                Log.e("switchContent", "!to.isAdded() done!");
            } else {
                // 隐藏当前的fragment，显示下一个fragment
                ft.hide(mContent).show(to).commit();
                Log.e("switchContent", "!to.isAdded() else done!");
            }
            mContent = to;
            Log.e("switchContent", String.valueOf(mContent));
        }

    }


    public static class DeckPickerFragment extends Fragment {

        public DeckPickerFragment() {
            // Required empty public constructor
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_deck_picker, container, false);
            return view;

        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            listView = (ListView) view.findViewById(R.id.deckpicker_listview);
            text_share_deck = (TextView) view.findViewById(R.id.text_share_deck);
            //在线导入的点击事件
            red_dot = (ImageView) view.findViewById(R.id.red_dot);
            red_dot.setVisibility(View.GONE);
            text_load_online = (TextView) view.findViewById(R.id.text_load_online);
            text_load_online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (red_dot.isShown()) {
                        //在线导入旁边红点显示的时候
                        new AlertDialog(getActivity()).builder()
                                .setTitle("确认在线导入")
                                .setMsg(downloadFileName)  //在线导入牌组的名称
                                .setCancelable(true)
                                .setNegativeButton("否", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .setPositiveButton("是", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        File[] files = downloadDecks.listFiles();
                                        inputingDeckname = files[0].getName();
                                        String filepath = files[0].getAbsolutePath();
                                        inputingDeckFilepath = filepath;
                                        Intent i = new Intent("comfirm input deck");
                                        getActivity().sendBroadcast(i);
                                    }
                                }).show();
                    } else {
                        //在线导入旁边红点不显示的时候
                        new AlertDialog(getActivity()).builder()
                                .setTitle("没有待导入牌组")
                                .setMsg("请进入官方网站上传牌组")
                                .setCancelable(true)
                                .setNegativeButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                    }
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            SettingsBean settings = SettingUtil.getSettings();
            int homePageStyle = settings.getHomePageStyle();

            if (homePageStyle == 3) {
                text_share_deck.setText("添加课程");
                text_load_online.setText("云端推送");
                text_share_deck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastAlone.showShortToast("跳转添加课程界面");
                        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
                        if (TextUtils.isEmpty(memId)) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                            return;
                        }

                        //艾卡共享
                        Intent aika_share = new Intent(getActivity(), DownLoadDecks11.class);
                        getActivity().startActivity(aika_share);
                        //getActivity().startActivityForResult(aika_share, REQUEST_CODE_DOWNLOADDECKS);
                        getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);
                    }
                });
            } else {
                text_share_deck.setText("共享牌组");
                text_load_online.setText("在线导入");
                //共享牌组的点击事件
                text_share_deck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
                        if (TextUtils.isEmpty(memId)) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                            return;
                        }

                        BottomRightMenu mBottomRightMenu = new BottomRightMenu(getActivity());
                        //添加菜单项
                        List<MenuItem> menuItems = new ArrayList<>();
                        menuItems.add(new MenuItem("Anki共享"));
                        menuItems.add(new MenuItem("艾卡共享"));
                        mBottomRightMenu
                                .setHeight((int) (45 * menuItems.size() * md))     //默认高度480  200
                                .setWidth((int) (90 * md))      //默认宽度wrap_content   190
                                .showIcon(false)     //显示菜单图标，默认为true
                                .dimBackground(true)        //背景变暗，默认为true
                                .needAnimationStyle(true)   //显示动画f，默认为true
                                .setAnimationStyle(R.style.BOTTOM_ANIM_STYLE)
                                .addMenuList(menuItems)
                                .setOnMenuItemClickListener(new BottomRightMenu.OnMenuItemClickListener() {
                                    @Override
                                    public void onMenuItemClick(int position) {
                                        switch (position) {
                                            case 0:
                                                //Anki共享
                                                Intent anki_share = new Intent(getActivity(), DownLoadDecksFromAnki.class);
                                                getActivity().startActivityForResult(anki_share, 0000);
                                                getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);
                                                break;
                                            case 1:
                                                //艾卡共享
                                                Intent aika_share = new Intent(getActivity(), DownLoadDecks11.class);
                                                getActivity().startActivity(aika_share);
//                                                getActivity().startActivityForResult(aika_share, REQUEST_CODE_DOWNLOADDECKS);
                                                getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);

                                                break;
                                        }

                                    }
                                })
                                .showAsDropDown(text_share_deck, (int) (25 * md), (int) (1 * md));

                    }
                });
            }
        }
    }

    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setStatusBarTintResource(R.color.material_top_blue);//通知栏所需颜色
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

/////////////////////////////////dx Start

    /**
     * 添加引导页
     */
    public void setGuideView() {
        hideText = (TextView) findViewById(R.id.main2_tv_text);
        final View listChile = listView.getChildAt(0);
        TextView textView = (TextView) listChile.findViewById(R.id.id_treenode_label);
        Log.e("textView", textView + "");

        AssetManager mgr = getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/STXINGKA.TTF");
        //设置字体
        TextView tv1 = new TextView(this);
        tv1.setText("点击可以展开侧边栏哦");
        tv1.setTextColor(getResources().getColor(R.color.white));
        tv1.setTextSize(26);
        tv1.setTypeface(tf);
        tv1.setPadding(0, 40, 0, 0);
        tv1.setGravity(Gravity.CENTER);

        TextView tv2 = new TextView(this);
        tv2.setText("点击这里可以切换视图哦");
        tv2.setTextColor(getResources().getColor(R.color.white));
        tv2.setTextSize(26);
        tv2.setPadding(0, 100, 0, 0);
        tv2.setTypeface(tf);
        tv2.setGravity(Gravity.CENTER);

        TextView tv3 = new TextView(this);
        tv3.setText("这是牌组，点击可以浏览卡片哦");
        tv3.setTextColor(getResources().getColor(R.color.white));
        tv3.setTextSize(24);
        tv3.setTypeface(tf);
        tv3.setGravity(Gravity.CENTER);

        TextView tv4 = new TextView(this);
        tv4.setText("这里可以导入牌组哦");
        tv4.setTextColor(getResources().getColor(R.color.white));
        tv4.setTextSize(26);
        tv4.setTypeface(tf);
        tv4.setGravity(Gravity.CENTER);

        TextView tv5 = new TextView(this);
        tv5.setText("这里可以下载网络牌组哦");
        tv5.setTextColor(getResources().getColor(R.color.white));
        tv5.setTextSize(26);
        tv5.setTypeface(tf);
        tv5.setGravity(Gravity.CENTER);

        guideView1 = GuideView.Builder
                .newInstance(this)
                .setTargetView(icon_home)
                .setCustomGuideView(tv1)
                .setDirction(GuideView.Direction.RIGHT_BOTTOM)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置矩形显示区域，
                .setRadius(40)          // 设置圆形或矩形透明区域半径，默认是targetView的显示矩形的半径，如果是矩形，这里是设置矩形圆角大小
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView1.hide();
                        guideView2.show();
                    }
                }).build();
        guideView2 = GuideView.Builder
                .newInstance(this)
                .setTargetView(segmented)
                .setCustomGuideView(tv2)
                .setDirction(GuideView.Direction.BOTTOM)
                .setShape(GuideView.MyShape.RECTANGULAR)   // 设置圆形显示区域，
                .setRadius(10)
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView2.hide();
                        guideView3.show();
                        Log.e("guideView3", guideView3 + "");
                    }
                }).build();
        guideView3 = GuideView.Builder
                .newInstance(this)
                .setTargetView(hideText)
                .setCenter(300, 190)
                .setCustomGuideView(tv3)
                .setDirction(GuideView.Direction.BOTTOM)
                .setShape(GuideView.MyShape.RECTANGULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView3.hide();
                        guideView4.show();
                    }
                }).build();
        guideView4 = GuideView.Builder
                .newInstance(this)
                .setTargetView(text_load_online)
                .setCustomGuideView(tv4)
                .setDirction(GuideView.Direction.RIGHT_TOP)
                .setShape(GuideView.MyShape.ELLIPSE)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView4.hide();
                        guideView5.show();
                    }
                }).build();
        guideView5 = GuideView.Builder
                .newInstance(this)
                .setTargetView(text_share_deck)
                .setCustomGuideView(tv5)
                .setDirction(GuideView.Direction.LEFT_TOP)
                .setShape(GuideView.MyShape.ELLIPSE)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView5.hide();
                    }
                }).build();
        guideView1.show();
    }

    /**
     * 此方法用来获取屏幕密度
     * @return
     */
    /*public float getMD(){
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density  = dm.density;
        return density;
    }*/

    /**
     * 此方法用来在“新建牌组时”，获得已存在牌组中
     * 名字以“过滤牌组”开头的牌组的个数
     *
     * @return
     */
    public int getDeckNumber() {
        int returnNum = 1;
        nodes = mAdapter.getNodes();
        for (Node n : nodes) {
            if (n.getName().contains("过滤牌组")) {
                int currentNum = Integer.valueOf(n.getName().substring(4));
                if (returnNum == currentNum) {
                    returnNum++;
                }
            }
        }
        return returnNum;
    }

/////////////////////////////////dx End


    /////////////////////////////// listener  start ///////////////////////////////
    private final View.OnClickListener mDeckExpanderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Long did = (Long) view.getTag();
            if (getCol().getDecks().children(did).size() > 0) {
                getCol().getDecks().collpase(did);
                Log.e("AikaActivity", "调用了第" + 5422 + "行");
                updateDeckList();
                dismissAllDialogFragments();
            }
        }
    };

    private final View.OnClickListener mDeckClickListener = new View.OnClickListener() {
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

    private final View.OnClickListener mCountsClickListener = new View.OnClickListener() {
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


    //mImportAddListener会被importAdd(),onActivityResult会调用
    //importAdd会被eventBus回调调用,preLoadingDeck1调用
    //而preLoadingDeck1会被
    // updeckList调用
    DeckTask.TaskListener mImportAddListener = new DeckTask.TaskListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            //showInputProgressCircle(inputingDeckname, "导入中...");
            String message = "";
          /*  Resources res = getResources();
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (result != null && result.getBoolean())boolean {
                int count = result.getInt();
                if (count < 0) {
                    if (count == -2) {
                       message = res.getString(R.string.import_log_no_apkg);
                    } else {
                        message = res.getString(R.string.import_log_error);
                    }
                   showSimpleMessageDialog(message, true);
                } else {
                    message = res.getString(R.string.import_log_success, count);
                   showSimpleMessageDialog(message);
                    updateDeckList();
                }
            } else {
                showSimpleMessageDialog(res.getString(R.string.import_log_error));
            }
           // delete temp file if necessary and reset import path so that it's not incorrectly imported next time
           // Activity starts
            if (getIntent().getBooleanExtra("deleteTempFile", false)) {
                new File(mImportPath).delete();
            }
            mImportPath = null;*/


            Log.e("onError>>>>>>>>>>>>", "======================002===========");
            //导入后删除文件
            String downloadDeckDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks";
            File[] downloadDeckFiles = (new File(downloadDeckDir)).listFiles();
            for (File file : downloadDeckFiles) {
                file.delete();
            }

            Log.e("onError>>>>>>>>>>>>", "======================003===========");
            String deckDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/downloadDecks";
            File[] deckDirFiles = (new File(deckDir)).listFiles();
            for (File file : deckDirFiles) {
                file.delete();
            }

            beingPreLoadingDeck = false;
            Log.e("onError>>>>>>>>>>>>", "======================004===========");
            Log.e("AikaActivity", "调用了第" + 5536 + "行");

            updateDeckList();
            //disappearProgressCircle();
//            showInputOkAndDisappear();


        }

        @Override
        public void onPreExecute() {
//            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
//                mProgressDialog = StyledProgressDialog.show(DeckPicker.this,
//                        getResources().getString(R.string.import_title),
//                        getResources().getString(R.string.import_importing), false);
//            }

            //disappearProgressCircle();
            // loadingDialog.show();


            /**
             * 如果是第一次启动,不能显示progressDialog
             * 如果不是第一次启动,并且是第一次通过登录界面登录的,也不能显示progressDialog
             */
            String loadingDialogIsShowing = loadingDialog.isShowing() ? "true" : "false";
            String activityIsFinishing = isFinishing() ? "true" : "false";
            //不是第一次启动,并且不是第一次通过登录界面登录//
//            if(!mActivityPaused && !isFirstUse && !isLoginFromWindow && !loadingDialog.isShowing()){
            Log.e("AikaActivity", "mImportAddListener-->onPreExecute-->" + "isShowing: " + loadingDialogIsShowing + "   activityIsFinishing：" + activityIsFinishing + "   Thread.currentThread().getName():" + Thread.currentThread().getName());
            if (!loadingDialog.isShowing() && !isFinishing()) {
                //开始转圈圈;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isImportingDeck = true;
//                        if(progressDialog.getDownLoadTip().equals("导入中...")){
//                            return;
//                        }
                Log.e("improt", "进入了:导入中...");
                progressDialog.show();
                progressDialog.setDownloadTip("导入中...");
//                    }
//                },500);

            }
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
                Log.e("AikaActivity", "调用了第" + 5609 + "行");
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
                mProgressDialog = StyledProgressDialog.show(AikaActivity.this,
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
            mProgressDialog = StyledProgressDialog.show(AikaActivity.this, "",
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
                Themes.showThemedToast(AikaActivity.this, getResources().getString(R.string.export_unsuccessful), true);
            }
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onCancelled() {
        }
    };

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onShowMessageEvent(MessageEvent messageEvent) {
//        String filepath = messageEvent.getMessage();
//        inputingDeckname = filepath.substring(filepath.lastIndexOf("/") + 1);
//        Log.e("filepath", "onShowMessageEvent>>>>>>>>>>>>>>>>>>>>" + filepath);
//        inputingDeckFilepath = filepath;
//        importAdd(filepath);
//    }


    public class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String filepath = intent.getStringExtra("message");
            isFromDownloadDecks = intent.getBooleanExtra("isFromDownloadDecks", false);
            inputingDeckname = filepath.substring(filepath.lastIndexOf("/") + 1);
            Log.e("AikaActivity", "importAdd调用了,第5700行");
            inputingDeckFilepath = filepath;
            importAdd(filepath);
        }
    }

    /////////////////////////////// listener  end ///////////////////////////////

}




