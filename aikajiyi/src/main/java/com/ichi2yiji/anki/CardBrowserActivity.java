package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.dialogs.ActionSheetDialog;
import com.ichi2yiji.anki.dialogs.CardBrowserMySearchesDialog;
import com.ichi2yiji.anki.dialogs.CardBrowserOrderDialog;
import com.ichi2yiji.anki.dialogs.TagsDialog;
import com.ichi2yiji.anki.receiver.SdCardReceiver;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.widgets.DeckDropDownAdapter;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.libanki.Card;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Note;
import com.ichi2yiji.libanki.Utils;
import com.ichi2yiji.themes.Themes;
import com.ichi2yiji.upgrade.Upgrade;
import com.ichi2yiji.widget.WidgetStatus;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * 卡片浏览
 * Created by Administrator on 2017/1/4.
 */

public class CardBrowserActivity extends NavigationDrawerActivity implements
        DeckDropDownAdapter.SubtitleListener{
    private ArrayList<HashMap<String, String>> mCards;
    private HashMap<String, String> mDeckNames;
    private ArrayList<JSONObject> mDropDownDecks;
    private SearchView mSearchView;
    private ListView mCardsListView;
    private Spinner mCardsColumn1Spinner;
    private Spinner mCardsColumn2Spinner;
    //private MultiColumnListAdapter mCardsAdapter;
    private String mSearchTerms;
    private String mRestrictOnDeck;

    private MenuItem mSearchItem;
    private MenuItem mSaveSearchItem;
    private MenuItem mMySearchesItem;
    private static int sLastSelectedDeckIndex = -1;

    private int mPositionInCardsList;

    private int mOrder;
    private boolean mOrderAsc;
    private int mColumn1Index;
    private int mColumn2Index;

    private static final int DIALOG_TAGS = 3;

    private static final int BACKGROUND_NORMAL = 0;
    private static final int BACKGROUND_MARKED = 1;
    private static final int BACKGROUND_SUSPENDED = 2;
    private static final int BACKGROUND_MARKED_SUSPENDED = 3;

    private static final int EDIT_CARD = 0;
    private static final int ADD_NOTE = 1;
    private static final int DEFAULT_FONT_SIZE_RATIO = 100;
    // Minimum number of cards to render
    private static final int MIN_CARDS_TO_RENDER = 1000;
    // Should match order of R.array.card_browser_order_labels
    public static final int CARD_ORDER_NONE = 0;
    private static final String[] fSortTypes = new String[] {
            "",
            "noteFld",
            "noteCrt",
            "noteMod",
            "cardMod",
            "cardDue",
            "cardIvl",
            "cardEase",
            "cardReps",
            "cardLapses"};
    // list of available keys in mCards corresponding to the column names in R.array.browser_column2_headings.
    // Note: the last 6 are currently hidden
    private static final String[] COLUMN1_KEYS = {"question", "sfld"};
    private static final String[] COLUMN2_KEYS = {"answer",
            "card",
            "deck",
            "note",
            "question",
            "tags",
            "lapses",
            "reviews",
            "changed",
            "created",
            "due",
            "ease",
            "edited",
            "interval"};
    private long mLastRenderStart = 0;
    private ActionBar mActionBar;
    private DeckDropDownAdapter mDropDownAdapter;
    private Spinner mActionBarSpinner;
    private boolean mReloadRequired = false;

    /**
     * Broadcast that informs us when the sd card is about to be unmounted
     */
    private BroadcastReceiver mUnmountReceiver = null;
    ///////////////////////////////dx  add
    private CardBrowserAdapter cardBrowserAdapter=null;
    public static Card sCardBrowserCard;
    ///////////////////listview长按事件的Dialog中的值
    public static final int CONTEXT_MENU_MARK = 0;
    public static final int CONTEXT_MENU_SUSPEND = 1;
    public static final int CONTEXT_MENU_DELETE = 2;
    public static final int CONTEXT_MENU_DETAILS = 3;
    private static final int MAX_TITLE_LENGTH = 75;
    private SVProgressHUD svProgressHUD;
    ///////////////////////////////dx  add
    /*private MaterialDialog.ListCallback mContextMenuListener = new MaterialDialog.ListCallback() {
        @Override
        public void onSelection(MaterialDialog materialDialog, View view, int which,
                                CharSequence charSequence) {
            if (getCards().size() == 0) {
                // Don't do anything if mCards empty
                searchCards();
                return;
            }
            switch (which) {
                case CardBrowserContextMenu.CONTEXT_MENU_MARK:
                    Card card = getCol().getCard(Long.parseLong(getCards().get(mPositionInCardsList).get("id")));
                    onMark(card);
                    updateCardInList(card, null);
                    return;

                case CardBrowserContextMenu.CONTEXT_MENU_SUSPEND:
                    if (currentCardInUseByReviewer()) {
                        mReloadRequired = true;
                    }
                    DeckTask.launchDeckTask(
                            DeckTask.TASK_TYPE_DISMISS_NOTE,
                            mSuspendCardHandler,
                            new DeckTask.TaskData(getCol().getCard(Long.parseLong(getCards().get(
                                    mPositionInCardsList).get("id"))), 1));
                    return;

                case CardBrowserContextMenu.CONTEXT_MENU_DELETE:
                    Resources res = getResources();
                    new MaterialDialog.Builder(CardBrowserActivity.this)
                            .title(res.getString(R.string.delete_card_title))
                            .iconAttr(R.attr.dialogErrorIcon)
                            .content(res.getString(R.string.delete_card_message, getCards().get(mPositionInCardsList)
                                    .get("sfld")))
                            .positiveText(res.getString(R.string.dialog_positive_delete))
                            .negativeText(res.getString(R.string.dialog_cancel))
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    Card card = getCol().getCard(Long.parseLong(getCards().get(mPositionInCardsList).get("id")));
                                    deleteNote(card);
                                    DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDeleteNoteHandler,
                                            new DeckTask.TaskData(card, 3));
                                }
                            })
                            .build().show();
                    return;

                case CardBrowserContextMenu.CONTEXT_MENU_DETAILS:
                    Long cardId = Long.parseLong(getCards().get(mPositionInCardsList).get("id"));
                    Intent previewer = new Intent(CardBrowserActivity.this, Previewer.class);
                    previewer.putExtra("currentCardId", cardId);
                    startActivityWithoutAnimation(previewer);
            }
        }
    };*/


    private MaterialDialog.ListCallbackSingleChoice mOrderDialogListener =
            new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog materialDialog, View view, int which,
                                           CharSequence charSequence) {
                    if (which != mOrder) {
                        mOrder = which;
                        mOrderAsc = false;
                        try {
                            if (mOrder == 0) {
                                getCol().getConf().put("sortType", fSortTypes[1]);
                                AnkiDroidApp.getSharedPrefs(getBaseContext()).edit()
                                        .putBoolean("cardBrowserNoSorting", true)
                                        .commit();
                            } else {
                                getCol().getConf().put("sortType", fSortTypes[mOrder]);
                                AnkiDroidApp.getSharedPrefs(getBaseContext()).edit()
                                        .putBoolean("cardBrowserNoSorting", false)
                                        .commit();
                            }
                            // default to descending for non-text fields
                            if (fSortTypes[mOrder].equals("noteFld")) {
                                mOrderAsc = true;
                            }
                            getCol().getConf().put("sortBackwards", mOrderAsc);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        searchCards();
                    } else if (which != CARD_ORDER_NONE) {
                        mOrderAsc = !mOrderAsc;
                        try {
                            getCol().getConf().put("sortBackwards", mOrderAsc);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Collections.reverse(mCards);
                        updateList();
                    }
                    return true;
                }
            };

    private CardBrowserMySearchesDialog.MySearchesDialogListener mMySearchesDialogListener =
            new CardBrowserMySearchesDialog.MySearchesDialogListener() {
                @Override
                public void OnSelection(String searchName) {
                    JSONObject savedFiltersObj = getCol().getConf().optJSONObject("savedFilters");
                    if (savedFiltersObj != null) {
                        mSearchTerms = savedFiltersObj.optString(searchName);
                        mSearchView.setQuery(mSearchTerms, false);
                        MenuItemCompat.expandActionView(mSearchItem);
                        searchCards();
                    }
                }

                @Override
                public void OnRemoveSearch(String searchName) {
                    try {
                        JSONObject savedFiltersObj = getCol().getConf().optJSONObject("savedFilters");
                        if (savedFiltersObj != null && savedFiltersObj.has(searchName)) {
                            savedFiltersObj.remove(searchName);
                            getCol().getConf().put("savedFilters", savedFiltersObj);
                            getCol().flush();
                            if (savedFiltersObj.length() == 0) {
                                mMySearchesItem.setVisible(false);
                            }
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void OnSaveSearch(String searchName, String searchTerms) {
                    if (TextUtils.isEmpty(searchName)) {
                        Themes.showThemedToast(CardBrowserActivity.this,
                                getString(R.string.card_browser_list_my_searches_new_search_error_empty_name), true);
                        return;
                    }
                    try {
                        JSONObject savedFiltersObj = getCol().getConf().optJSONObject("savedFilters");
                        boolean should_save = false;
                        if (savedFiltersObj == null) {
                            savedFiltersObj = new JSONObject();
                            savedFiltersObj.put(searchName, searchTerms);
                            should_save = true;
                        } else if (!savedFiltersObj.has(searchName)) {
                            savedFiltersObj.put(searchName, searchTerms);
                            should_save = true;
                        } else {
                            Themes.showThemedToast(CardBrowserActivity.this,
                                    getString(R.string.card_browser_list_my_searches_new_search_error_dup), true);
                        }
                        if (should_save) {
                            getCol().getConf().put("savedFilters", savedFiltersObj);
                            getCol().flush();
                            mSearchView.setQuery("", false);
                            mMySearchesItem.setVisible(true);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            };


    private void onSearch() {
        mSearchTerms = mSearchView.getQuery().toString();
        if (mSearchTerms.length() == 0) {
            mSearchView.setQueryHint(getResources().getString(R.string.downloaddeck_search));
        }
        searchCards();
    }

    private void onMark(Card card) {
        Note note = card.note();
        if (note.hasTag("marked")) {
            note.delTag("marked");
        } else {
            note.addTag("marked");
        }
        note.flush();
    }

    private ImageView searchImg;
    private ImageView card_browser_backImg;
    private RelativeLayout card_browser_backImg_lyt;
    private EditText searchText;
    private ListView listView;
    private ImageView cancleTextImg;
    private TextView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
//        applyKitKatAndLollipopTranslucency();
        ApplyTranslucency.applyKitKatTranslucency(this);
        Timber.d("onCreate()");
        View mainView = getLayoutInflater().inflate(R.layout.activity_cardbrowser, null);
        setContentView(mainView);
//        initNavigationDrawer(mainView);
        startLoadingCollection();

        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("加载中");
        card_browser_backImg = (ImageView) findViewById(R.id.card_browser_backImg);
        card_browser_backImg_lyt = (RelativeLayout) findViewById(R.id.card_browser_backImg_lyt);
        searchImg = (ImageView) findViewById(R.id.card_browser_searchImg);
        searchText = (EditText) findViewById(R.id.card_browser_searchText);
        listView = (ListView) findViewById(R.id.card_browser_listview);
        listView.setDividerHeight(0);
        cancleTextImg = (ImageView) findViewById(R.id.card_browser_cleartext);
        search = (TextView)findViewById(R.id.card_browser_search_text);
        searchCardByCondition();
    }


    // Finish initializing the activity after the collection has been correctly loaded
    @Override
    protected void onCollectionLoaded(Collection col) {
        super.onCollectionLoaded(col);
        Timber.d("onCollectionLoaded()");
        mDeckNames = new HashMap<String, String>();
        for (long did : getCol().getDecks().allIds()) {
            mDeckNames.put(String.valueOf(did), getCol().getDecks().name(did));
        }
        registerExternalStorageListener();

        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());

        // Add drop-down menu to select deck to action bar.
        mDropDownDecks = getCol().getDecks().allSorted();
        mDropDownAdapter = new DeckDropDownAdapter(this, mDropDownDecks);
//        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowTitleEnabled(false);
//        mActionBarSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
//        mActionBarSpinner.setAdapter(mDropDownAdapter);
//        mActionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectDropDownItem(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // do nothing
//            }
//        });
//        mActionBarSpinner.setVisibility(View.VISIBLE);

        try {
            mOrder = CARD_ORDER_NONE;
            String colOrder = getCol().getConf().getString("sortType");
            for (int c = 0; c < fSortTypes.length; ++c) {
                if (fSortTypes[c].equals(colOrder)) {
                    mOrder = c;
                    break;
                }
            }
            if (mOrder == 1 && preferences.getBoolean("cardBrowserNoSorting", false)) {
                mOrder = 0;
            }
            mOrderAsc = Upgrade.upgradeJSONIfNecessary(getCol(), getCol().getConf(), "sortBackwards", false);
            // default to descending for non-text fields
            if (fSortTypes[mOrder].equals("noteFld")) {
                mOrderAsc = !mOrderAsc;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mCards = new ArrayList<>();
//        mCardsListView = (ListView) findViewById(R.id.card_browser_list);
//        // Create a spinner for column1
//        mCardsColumn1Spinner = (Spinner) findViewById(R.id.browser_column1_spinner);
//        ArrayAdapter<CharSequence> column1Adapter = ArrayAdapter.createFromResource(this,
//                R.array.browser_column1_headings, android.R.layout.simple_spinner_item);
//        column1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mCardsColumn1Spinner.setAdapter(column1Adapter);
//        mColumn1Index = AnkiDroidApp.getSharedPrefs(getBaseContext()).getInt("cardBrowserColumn1", 0);
//        mCardsColumn1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                // If a new column was selected then change the key used to map from mCards to the column TextView
//                if (pos != mColumn1Index) {
//                    mColumn1Index = pos;
//                    AnkiDroidApp.getSharedPrefs(AnkiDroidApp.getInstance().getBaseContext()).edit()
//                            .putInt("cardBrowserColumn1", mColumn1Index).commit();
//                    String[] fromMap = mCardsAdapter.getFromMapping();
//                    fromMap[0] = COLUMN1_KEYS[mColumn1Index];
//                    mCardsAdapter.setFromMapping(fromMap);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do Nothing
//            }
//        });
//        // Load default value for column2 selection
//        mColumn2Index = AnkiDroidApp.getSharedPrefs(getBaseContext()).getInt("cardBrowserColumn2", 0);
//        // Setup the column 2 heading as a spinner so that users can easily change the column type
//        mCardsColumn2Spinner = (Spinner) findViewById(R.id.browser_column2_spinner);
//        ArrayAdapter<CharSequence> column2Adapter = ArrayAdapter.createFromResource(this,
//                R.array.browser_column2_headings, android.R.layout.simple_spinner_item);
//        column2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mCardsColumn2Spinner.setAdapter(column2Adapter);
//        // Create a new list adapter with updated column map any time the user changes the column
//        mCardsColumn2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                // If a new column was selected then change the key used to map from mCards to the column TextView
//                if (pos != mColumn2Index) {
//                    mColumn2Index = pos;
//                    AnkiDroidApp.getSharedPrefs(AnkiDroidApp.getInstance().getBaseContext()).edit()
//                            .putInt("cardBrowserColumn2", mColumn2Index).commit();
//                    String[] fromMap = mCardsAdapter.getFromMapping();
//                    fromMap[1] = COLUMN2_KEYS[mColumn2Index];
//                    mCardsAdapter.setFromMapping(fromMap);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do Nothing
//            }
//        });
//        // get the font and font size from the preferences
//        int sflRelativeFontSize = preferences.getInt("relativeCardBrowserFontSize", DEFAULT_FONT_SIZE_RATIO);
//        String sflCustomFont = preferences.getString("browserEditorFont", "");
//        // make a new list adapter mapping the data in mCards to column1 and column2 of R.layout.card_item_browser
//        mCardsAdapter = new MultiColumnListAdapter(
//                this,
//                mCards,
//                R.layout.card_item_browser,
//                new String[] {COLUMN1_KEYS[mColumn1Index], COLUMN2_KEYS[mColumn2Index]},
//                new int[] {R.id.card_sfld, R.id.card_column2},
//                "flags",
//                sflRelativeFontSize,
//                sflCustomFont);
//        // link the adapter to the main mCardsListView
//        mCardsListView.setAdapter(mCardsAdapter);
//        // make the second column load dynamically when scrolling
//        mCardsListView.setOnScrollListener(new RenderOnScroll());
//        // set the spinner index
//        mCardsColumn1Spinner.setSelection(mColumn1Index);
//        mCardsColumn2Spinner.setSelection(mColumn2Index);
//
//
//        mCardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // load up the card selected on the list
//                mPositionInCardsList = position;
//                long cardId = Long.parseLong(mCards.get(mPositionInCardsList).get("id"));
//                sCardBrowserCard = getCol().getCard(cardId);
//                // start note editor using the card we just loaded
//                Intent editCard = new Intent(CardBrowserActivity.this, NoteEditor.class);
//                editCard.putExtra(NoteEditor.EXTRA_CALLER, NoteEditor.CALLER_CARDBROWSER_EDIT);
//                editCard.putExtra(NoteEditor.EXTRA_CARD_ID, sCardBrowserCard.getId());
//                startActivityForResultWithAnimation(editCard, EDIT_CARD, ActivityTransitionAnimation.LEFT);
//            }
//        });
//        mCardsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                mPositionInCardsList = position;
//                HashMap<String, String> card = mCards.get(mPositionInCardsList);
//                int flags = Integer.parseInt(card.get("flags"));
//                String cardName = card.get("sfld");
//                boolean isMarked = (flags == 2 || flags == 3);
//                boolean isSuspended = (flags == 1 || flags == 3);
//                showDialogFragment(CardBrowserContextMenu
//                        .newInstance(cardName, isMarked, isSuspended, mContextMenuListener));
//                return true;
//            }
//        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // initialize mSearchTerms to a default value
        mSearchTerms = "";

//        // set the currently selected deck
//        if (sLastSelectedDeckIndex == -1) {
//            String currentDeckName;
//            try {
//                currentDeckName = getCol().getDecks().current().getString("name");
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            for (int dropDownDeckIdx = 0; dropDownDeckIdx < mDropDownDecks.size(); dropDownDeckIdx++) {
//                JSONObject deck = mDropDownDecks.get(dropDownDeckIdx);
//                String deckName;
//                try {
//                    deckName = deck.getString("name");
//                } catch (JSONException e) {
//                    throw new RuntimeException();
//                }
//                if (deckName.equals(currentDeckName)) {
//                    selectDropDownItem(dropDownDeckIdx + 1);
//                    break;
//                }
//            }
//        } else if (sLastSelectedDeckIndex > 0 && sLastSelectedDeckIndex < mDropDownDecks.size()) {
//            selectDropDownItem(sLastSelectedDeckIndex);
//        }
        selectDropDownItem(2);
//        textView_1.setText(mCards.toString());
        //Log.e("CardBrowserActivity", "mDeckNames1111>>>>>>>"+ mDeckNames.toString());
       // Log.e("CardBrowserActivity", "mDropDownDecks1111>>>>>>>"+ mDropDownDecks.toString());
        //textView_2.setText(mDeckNames.toString());
        //Log.e("mDeckNames.toString()",mDeckNames.toString());

    }


    @Override
    protected void onStop() {
        Timber.d("onStop()");
        // cancel rendering the question and answer, which has shared access to mCards
        DeckTask.cancelTask(DeckTask.TASK_TYPE_SEARCH_CARDS);
        DeckTask.cancelTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA);
        super.onStop();
        if (!isFinishing()) {
            WidgetStatus.update(this);
            UIUtils.saveCollectionInBackground(this);
        }
    }


    @Override
    protected void onDestroy() {
        Timber.d("onDestroy()");
        super.onDestroy();
        if (mUnmountReceiver != null) {
            unregisterReceiver(mUnmountReceiver);
        }
    }


    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            super.onBackPressed();
        } else {
            Timber.i("Back key pressed");
            Intent data = new Intent();
            if (getIntent().hasExtra("selectedDeck")) {
                data.putExtra("originalDeck", getIntent().getLongExtra("selectedDeck", 0L));
            }
            if (mReloadRequired) {
                // Add reload flag to result intent so that schedule reset when returning to note editor
                data.putExtra("reloadRequired", true);
            }
            closeCardBrowser(RESULT_OK, data);
        }
    }

    @Override
    protected void onResume() {
        Timber.d("onResume()");
        super.onResume();
        selectNavigationItem(DRAWER_BROWSER);
 //       Log.e("ResuCardBrowserActivity", ">>>>>>>"+ mCards.toString());
//        Log.e("CardBrowserActivity", ">>>>>>>"+ mDeckNames.toString());
//        Log.e("CardBrowserActivity", ">>>>>>>"+ mDropDownDecks.toString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_browser, menu);
        mSaveSearchItem = menu.findItem(R.id.action_save_search);
        mSaveSearchItem.setVisible(false); //the searchview's query always starts empty.
        mMySearchesItem = menu.findItem(R.id.action_list_my_searches);
        JSONObject savedFiltersObj = getCol().getConf().optJSONObject("savedFilters");
        mMySearchesItem.setVisible(savedFiltersObj != null && savedFiltersObj.length() > 0);
        mSearchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }


            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // SearchView doesn't support empty queries so we always reset the search when collapsing
                mSearchTerms = "";
                mSearchView.setQuery(mSearchTerms, false);
                searchCards();
                return true;
            }
        });
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                mSaveSearchItem.setVisible(!TextUtils.isEmpty(newText));
                return true;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearch();
                mSearchView.clearFocus();
                return true;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Provide SearchView with the previous search terms
                mSearchView.setQuery(mSearchTerms, false);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add_card_from_card_browser:
                Intent intent = new Intent(CardBrowserActivity.this, NoteEditor.class);
                intent.putExtra(NoteEditor.EXTRA_CALLER, NoteEditor.CALLER_CARDBROWSER_ADD);
                startActivityForResultWithAnimation(intent, ADD_NOTE, ActivityTransitionAnimation.LEFT);
                //dx   add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                return true;

            case R.id.action_save_search:
                String searchTerms = mSearchView.getQuery().toString();
                showDialogFragment(CardBrowserMySearchesDialog.newInstance(null, mMySearchesDialogListener,
                        searchTerms, CardBrowserMySearchesDialog.CARD_BROWSER_MY_SEARCHES_TYPE_SAVE));
                return true;

            case R.id.action_list_my_searches:
                JSONObject savedFiltersObj = getCol().getConf().optJSONObject("savedFilters");
                HashMap<String, String> savedFilters = new HashMap<String, String>();
                if (savedFiltersObj != null) {
                    Iterator<String> it = savedFiltersObj.keys();
                    while (it.hasNext()) {
                        String searchName = it.next();
                        savedFilters.put(searchName, savedFiltersObj.optString(searchName));
                    }
                }
                showDialogFragment(CardBrowserMySearchesDialog.newInstance(savedFilters, mMySearchesDialogListener,
                        "", CardBrowserMySearchesDialog.CARD_BROWSER_MY_SEARCHES_TYPE_LIST));
                return true;
            case R.id.action_sort_by_size:
                showDialogFragment(CardBrowserOrderDialog
                        .newInstance(mOrder, mOrderAsc, mOrderDialogListener));
                return true;

            case R.id.action_show_marked:
                mSearchTerms = "tag:marked";
                mSearchView.setQuery("", false);
                mSearchView.setQueryHint(getResources().getString(R.string.card_browser_show_marked));
                searchCards();
                return true;

            case R.id.action_show_suspended:
                mSearchTerms = "is:suspended";
                mSearchView.setQuery("", false);
                mSearchView.setQueryHint(getResources().getString(R.string.card_browser_show_suspended));
                searchCards();
                return true;

            case R.id.action_search_by_tag:
                showDialogFragment(DIALOG_TAGS);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FIXME:
        Timber.d("onActivityResult(requestCode=%d, resultCode=%d)", requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == DeckPicker.RESULT_DB_ERROR) {
            closeCardBrowser(DeckPicker.RESULT_DB_ERROR);
        }

        if (requestCode == EDIT_CARD && resultCode != RESULT_CANCELED) {
            Timber.i("CardBrowser:: CardBrowser: Saving card...");
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_UPDATE_FACT, mUpdateCardHandler,
                    new DeckTask.TaskData(sCardBrowserCard, false));
        } else if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            if (mSearchView != null) {
                mSearchTerms = mSearchView.getQuery().toString();
                searchCards();
            } else {
                Timber.w("Note was added from browser and on return mSearchView == null");
            }

        }

        if (requestCode == EDIT_CARD &&  data!=null && data.hasExtra("reloadRequired")) {
            // if reloadRequired flag was sent from note editor then reload card list
            searchCards();
            // keep track of changes for reviewer
            if (currentCardInUseByReviewer()) {
                mReloadRequired = true;
            }
        }
    }

    private boolean currentCardInUseByReviewer() {
        if (getIntent().hasExtra("currentCard") && getCards().size() > mPositionInCardsList
                && getCards().get(mPositionInCardsList) != null) {
            long reviewerCard = getIntent().getExtras().getLong("currentCard");
            long selectedCard = Long.parseLong(getCards().get(mPositionInCardsList).get("id"));
            if (selectedCard == reviewerCard) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private DialogFragment showDialogFragment(int id) {
        DialogFragment dialogFragment = null;
        String tag = null;
        switch(id) {
            case DIALOG_TAGS:
                TagsDialog dialog = com.ichi2yiji.anki.dialogs.TagsDialog.newInstance(
                        TagsDialog.TYPE_FILTER_BY_TAG, new ArrayList<String>(), new ArrayList<String>(getCol().getTags().all()));
                dialog.setTagsDialogListener(new TagsDialog.TagsDialogListener() {
                    @Override
                    public void onPositive(List<String> selectedTags, int option) {
                        mSearchView.setQuery("", false);
                        String tags = selectedTags.toString();
                        mSearchView.setQueryHint(getResources().getString(R.string.card_browser_tags_shown,
                                tags.substring(1, tags.length() - 1)));
                        StringBuilder sb = new StringBuilder();
                        switch (option) {
                            case 1:
                                sb.append("is:new ");
                                break;
                            case 2:
                                sb.append("is:due ");
                                break;
                            default:
                                // Logging here might be appropriate : )
                                break;
                        }
                        int i = 0;
                        for (String tag : selectedTags) {
                            if (i != 0) {
                                sb.append("or ");
                            } else {
                                sb.append("("); // Only if we really have selected tags
                            }
                            sb.append("tag:").append(tag).append(" ");
                            i++;
                        }
                        if (i > 0) {
                            sb.append(")"); // Only if we added anything to the tag list
                        }
                        mSearchTerms = sb.toString();
                        searchCards();
                    }
                });
                dialogFragment = dialog;
                break;
            default:
                break;
        }


        dialogFragment.show(getSupportFragmentManager(), tag);
        return dialogFragment;
    }


    public void selectDropDownItem(int position) {
//        mActionBarSpinner.setSelection(position);
//        sLastSelectedDeckIndex = position;
        if (position == 0) {
            mRestrictOnDeck = "";
        } else {
            JSONObject deck = mDropDownDecks.get(position - 1);
            String deckName;
            try {
                deckName = deck.getString("name");
            } catch (JSONException e) {
                throw new RuntimeException();
            }
            try {
                getCol().getDecks().select(deck.getLong("id"));
            } catch (JSONException e) {
                Timber.e(e, "Could not get ID from deck");
            }
            mRestrictOnDeck = "deck:\"" + deckName + "\" ";
        }
        searchCards();
    }

    private void searchCards() {
        // cancel the previous search & render tasks if still running
        DeckTask.cancelTask(DeckTask.TASK_TYPE_SEARCH_CARDS);
        DeckTask.cancelTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA);
        String searchText;
        if (mSearchTerms.contains("deck:")) {
            searchText = mSearchTerms;
        } else {
            searchText = mRestrictOnDeck + mSearchTerms;
        }
//        if (colIsOpen() && mCardsAdapter!= null) {
        if (colIsOpen()) {
            // clear the existing card list
            getCards().clear();
//            mCardsAdapter.notifyDataSetChanged();
            // Perform database query to get all card ids / sfld. Shows "filtering cards..." progress message
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_SEARCH_CARDS, mSearchCardsHandler, new DeckTask.TaskData(
                    new Object[] { mDeckNames, searchText, ((mOrder != CARD_ORDER_NONE)) }));
        }
        Log.e("CardBrowse%%%%", "searchCards>>>>>>>"+ mCards.toString());
    }



    private void updateList() {
//        mCardsAdapter.notifyDataSetChanged();
//        mDropDownAdapter.notifyDataSetChanged();
    }

    /**
     * @return text to be used in the subtitle of the drop-down deck selector
     */
    public String getSubtitleText() {
        int count = getCards().size();
        return getResources().getQuantityString(R.plurals.card_browser_subtitle, count, count);
    }


    private int getPosition(ArrayList<HashMap<String, String>> list, long cardId) {
        String cardid = Long.toString(cardId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("id").equals(cardid)) {
                return i;
            }
        }
        return -1;
    }


    private void updateCardInList(Card card, String updatedCardTags) {
        Note note = card.note();
        int pos;
        for (Card c : note.cards()) {
            // get position in the mCards search results HashMap
            pos = getPosition(getCards(), c.getId());
            if (pos < 0 || pos >= getCards().size()) {
                continue;
            }
            // update tags
            if (updatedCardTags != null) {
                getCards().get(pos).put("tags", updatedCardTags);
            }
            // update sfld
            String sfld = note.getSFld();
            getCards().get(pos).put("sfld", sfld);
            // update Q & A etc
            updateSearchItemQA(getCards().get(pos), c);
            // update deck
            String deckName;
            try {
                deckName = getCol().getDecks().get(card.getDid()).getString("name");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            getCards().get(pos).put("deck", deckName);
            // update flags (marked / suspended / etc) which determine color
            String flags = Integer.toString((c.getQueue() == -1 ? 1 : 0) + (note.hasTag("marked") ? 2 : 0));
            getCards().get(pos).put("flags", flags);
        }
        updateList();
    }

    private DeckTask.TaskListener mUpdateCardHandler = new DeckTask.TaskListener() {
        @Override
        public void onPreExecute() {
            showProgressBar();
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
            updateCardInList(values[0].getCard(), values[0].getString());
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            Timber.d("Card Browser - mUpdateCardHandler.onPostExecute()");
            if (!result.getBoolean()) {
                closeCardBrowser(DeckPicker.RESULT_DB_ERROR);
            }
            hideProgressBar();
        }


        @Override
        public void onCancelled() {
        }
    };

    public static void updateSearchItemQA(HashMap<String, String> item, Card c) {
        // render question and answer
        HashMap<String, String> qa = c._getQA(true, true);
        // Render full question / answer if the bafmt (i.e. "browser appearance") setting forced blank result
        if (qa.get("q").equals("") || qa.get("a").equals("")) {
            HashMap<String, String> qaFull = c._getQA(true, false);
            if (qa.get("q").equals("")) {
                qa.put("q", qaFull.get("q"));
            }
            if (qa.get("a").equals("")) {
                qa.put("a", qaFull.get("a"));
            }
        }
        // update the original hash map to include rendered question & answer
        String q = qa.get("q");
        String a = qa.get("a");
        // remove the question from the start of the answer if it exists
        if (a.startsWith(q)) {
            a = a.replaceFirst(Pattern.quote(q), "");
        }
        // put all of the fields in except for those that have already been pulled out straight from the
        // database
        item.put("answer", formatQA(a));
        item.put("card", c.template().optString("name"));
        // item.put("changed",strftime("%Y-%m-%d", localtime(c.getMod())));
        // item.put("created",strftime("%Y-%m-%d", localtime(c.note().getId()/1000)));
        // item.put("due",getDueString(c));
        // item.put("ease","");
        // item.put("edited",strftime("%Y-%m-%d", localtime(c.note().getMod())));
        // item.put("interval","");
        item.put("lapses", Integer.toString(c.getLapses()));
        item.put("note", c.model().optString("name"));
        item.put("question", formatQA(q));
        item.put("reviews", Integer.toString(c.getReps()));
    }


    private static String formatQA(String txt) {
        /* Strips all formatting from the string txt for use in displaying question/answer in browser */
        String s = txt.replace("<br>", " ");
        s = s.replace("<br />", " ");
        s = s.replace("<div>", " ");
        s = s.replace("\n", " ");
        s = s.replaceAll("\\[sound:[^]]+\\]", "");
        s = s.replaceAll("\\[\\[type:[^]]+\\]\\]", "");
        s = Utils.stripHTMLMedia(s);
        s = s.trim();
        return s;
    }


    private void deleteNote(Card card) {
        if (currentCardInUseByReviewer()) {
            mReloadRequired = true;
        }
        ArrayList<Card> cards = card.note().cards();
        int pos;
        for (Card c : cards) {
            pos = getPosition(getCards(), c.getId());
            if (pos >= 0 && pos < getCards().size()) {
                getCards().remove(pos);
            }
        }
        // Delete itself if not deleted
        pos = getPosition(getCards(), card.getId());
        if (pos >= 0 && pos < getCards().size()) {
            getCards().remove(pos);
        }
        updateList();
    }


    private DeckTask.TaskListener mSuspendCardHandler = new DeckTask.TaskListener() {
        @Override
        public void onPreExecute() {
            showProgressBar();
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result.getBoolean() && mCards != null) {
                updateCardInList(getCol().getCard(Long.parseLong(mCards.get(mPositionInCardsList).get("id"))), null);
            } else {
                closeCardBrowser(DeckPicker.RESULT_DB_ERROR);
            }
            hideProgressBar();
        }


        @Override
        public void onCancelled() {
        }
    };

    private DeckTask.TaskListener mDeleteNoteHandler = new DeckTask.TaskListener() {
        @Override
        public void onPreExecute() {
            showProgressBar();
        }

        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            hideProgressBar();
            //删除完数据后，刷新ListView
            cardBrowserAdapter.notifyDataSetChanged();
        }


        @Override
        public void onCancelled() {
        }
    };

    private DeckTask.TaskListener mSearchCardsHandler = new DeckTask.TaskListener() {
        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
            if (mCards != null && values[0]!= null) {
                mCards.clear();
                mCards.addAll(values[0].getCards());
                updateList();
                Log.e("CardBrowserActivity", "mSearchCardsHandler>>>>>>>onProgressUpdate"+ mCards.toString());

            }
        }


        @Override
        public void onPreExecute() {
            showProgressBar();
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result != null && mCards != null) {
                Timber.i("CardBrowser:: Completed doInBackgroundSearchCards Successfuly");
//                updateList();
//                if (!mSearchView.isIconified()) {
//                    showSimpleSnackbar(getSubtitleText(), false);
//                }
                // After the initial searchCards query, start rendering the question and answer in the background
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA, mRenderQAHandler,
                        new DeckTask.TaskData(new Object[]{mCards, 0, Math.min(mCards.size(), MIN_CARDS_TO_RENDER)}));
                Log.e("CardBrowserActivity", "mSearchCardsHandler>>>>>>>onPostExecute>>>"+ mCards.toString());
            }
            hideProgressBar();
        }

        @Override
        public void onCancelled(){
            Timber.d("doInBackgroundSearchCards onCancelled() called");
        }
    };

    private DeckTask.TaskListener mRenderQAHandler = new DeckTask.TaskListener() {
        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
//            mCardsAdapter.notifyDataSetChanged();
//            Log.e("CardBrowserActivity", "mRenderQAHandler>>>>>>>onProgressUpdate"+ mCards.toString());
        }


        @Override
        public void onPreExecute() {
            Timber.d("Starting Q&A background rendering");
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result != null) {
                hideProgressBar();
//                mCardsAdapter.notifyDataSetChanged();
                Log.e("CardBrowserActivity", "mRenderQAHandler>>>>>>>onPostExecute"+ mCards.toString());
                //textView_1.setText(mCards.toString());
                //Log.e("mCards.toString()",mCards.toString());
                Timber.d("Completed doInBackgroundRenderBrowserQA Successfuly");
            } else {
                // Might want to do something more proactive here like show a message box?
                Timber.e("doInBackgroundRenderBrowserQA was not successful... continuing anyway");
            }
            /////////////////////////dx add
            cardBrowserAdapter = new CardBrowserAdapter(CardBrowserActivity.this,mCards);
            listView.setAdapter(cardBrowserAdapter);
            svProgressHUD.dismiss();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,String> map = mCards.get(mPositionInCardsList);
                    map.get("question");
                    ArrayList<String> strs = new ArrayList<String>();
                    long cardId = Long.parseLong(mCards.get(mPositionInCardsList).get("id"));
                    sCardBrowserCard=getCol().getCard(cardId);
                    Intent intent=new Intent(CardBrowserActivity.this,AikaNoteEditor.class);
                    intent.putExtra(AikaNoteEditor.EXTRA_CALLER, AikaNoteEditor.CALLER_CARDBROWSER_EDIT);
                    intent.putExtra(AikaNoteEditor.EXTRA_CARD_ID, sCardBrowserCard.getId());
                    intent.putExtra(AikaNoteEditor.EXTRA_CARD_DECK,map.get("deck"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mPositionInCardsList = position;
                    HashMap<String, String> card = mCards.get(mPositionInCardsList);
                    int flags = Integer.parseInt(card.get("flags"));
                    String cardName = card.get("sfld");
                    boolean isMarked = (flags == 2 || flags == 3);
                    boolean isSuspended = (flags == 1 || flags == 3);
                   /* showDialogFragment(CardBrowserContextMenu
                            .newInstance(cardName, isMarked, isSuspended, mContextMenuListener));*/
                    showDialog(isMarked,isSuspended);
                    return true;
                }

            });
            /////////////////////////dx add

        }
        /////////////////////////////////////dx start
        public void showDialog(boolean isMarked,boolean isSuspended){
            Resources res = getResources();
            String[] entries = new String[4];
            entries[CONTEXT_MENU_DELETE] = res.getString(R.string.card_browser_delete_card);
            entries[CONTEXT_MENU_DETAILS] = res.getString(R.string.card_editor_preview_card);
            entries[CONTEXT_MENU_MARK] = res.getString(
                    isMarked?
                            R.string.card_browser_unmark_card :
                            R.string.card_browser_mark_card);
            entries[CONTEXT_MENU_SUSPEND] = res.getString(
                    isSuspended?
                            R.string.card_browser_unsuspend_card :
                            R.string.card_browser_suspend_card);
            new ActionSheetDialog(CardBrowserActivity.this)
                    .builder()
                    .setViewPostionAndWidth(Gravity.CENTER, 0.75)
                    .setTitle("请选择操作")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem(entries[0], ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Card card = getCol().getCard(Long.parseLong(getCards().get(mPositionInCardsList).get("id")));
                                    onMark(card);
                                    updateCardInList(card, null);
                                }
                            })
                    .addSheetItem(entries[1], ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    if (currentCardInUseByReviewer()) {
                                        mReloadRequired = true;
                                    }
                                    DeckTask.launchDeckTask(
                                            DeckTask.TASK_TYPE_DISMISS_NOTE,
                                            mSuspendCardHandler,
                                            new DeckTask.TaskData(getCol().getCard(Long.parseLong(getCards().get(
                                                    mPositionInCardsList).get("id"))), 1));
                                }
                            })
                    .addSheetItem(entries[2], ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Resources res = getResources();
                                    new MaterialDialog.Builder(CardBrowserActivity.this)
                                            .title(res.getString(R.string.delete_card_title))
                                            .iconAttr(R.attr.dialogErrorIcon)
                                            .content(res.getString(R.string.delete_card_message, getCards().get(mPositionInCardsList)
                                                    .get("sfld")))
                                            .positiveText(res.getString(R.string.dialog_positive_delete))
                                            .negativeText(res.getString(R.string.dialog_cancel))
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onPositive(MaterialDialog dialog) {
                                                    Card card = getCol().getCard(Long.parseLong(getCards().get(mPositionInCardsList).get("id")));
                                                    deleteNote(card);
                                                    DeckTask.launchDeckTask(DeckTask.TASK_TYPE_DISMISS_NOTE, mDeleteNoteHandler,
                                                            new DeckTask.TaskData(card, 3));
                                                }
                                            })
                                            .build().show();
                                }
                            })
                    .addSheetItem(entries[3], ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Long cardId = Long.parseLong(getCards().get(mPositionInCardsList).get("id"));
                                    Intent previewer = new Intent(CardBrowserActivity.this, Previewer.class);
                                    previewer.putExtra("currentCardId", cardId);
                                    startActivity(previewer);
                                    overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                                }
                            })
                    .addSheetItem("取消", ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                }
                            }).show();
        }
        /////////////////////////////////////dx end


        @Override
        public void onCancelled() {
            hideProgressBar();
        }
    };


    private void closeCardBrowser(int result) {
        closeCardBrowser(result, null);
    }

    private void closeCardBrowser(int result, Intent data) {
        setResult(result, data);
        finishWithAnimation(ActivityTransitionAnimation.RIGHT);
    }

    public static void clearSelectedDeck() {
        sLastSelectedDeckIndex = -1;
    }

    /**
     * Render the second column whenever the user stops scrolling
     */
    private final class RenderOnScroll implements AbsListView.OnScrollListener {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // Show the progress bar if scrolling to given position requires rendering of the question / answer
            int lastVisibleItem = firstVisibleItem + visibleItemCount;
            // Don't try to start rendering before scrolling has begun (firstVisibleItem == 0)
            if (firstVisibleItem > 0 && lastVisibleItem > 0 && getCards().size() > lastVisibleItem) {
                String firstAns = getCards().get(firstVisibleItem).get("answer");
                // Note: max value of lastVisibleItem is totalItemCount, so need to subtract 1
                String lastAns = getCards().get(lastVisibleItem - 1).get("answer");
                if (firstAns != null && firstAns.equals("") || lastAns != null && lastAns.equals("")) {
                    showProgressBar();
                    // Also start rendering the items on the screen every 300ms while scrolling
                    long currentTime = SystemClock.elapsedRealtime ();
                    if ((currentTime - mLastRenderStart > 300 || lastVisibleItem >= totalItemCount)) {
                        mLastRenderStart = currentTime;
                        DeckTask.cancelTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA);
                        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA, mRenderQAHandler,
                                new DeckTask.TaskData(new Object[]{getCards(), firstVisibleItem, visibleItemCount}));
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            // TODO: Try change to RecyclerView as currently gets stuck a lot when using scrollbar on right of ListView
            // Start rendering the question & answer every time the user stops scrolling
            if (scrollState == SCROLL_STATE_IDLE) {
                int startIdx = listView.getFirstVisiblePosition();
                int numVisible = listView.getLastVisiblePosition() - startIdx;
                DeckTask.launchDeckTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA, mRenderQAHandler,
                        new DeckTask.TaskData(new Object[]{getCards(), startIdx - 5, 2 * numVisible + 5}));
            }
        }
    }

   /* private final class MultiColumnListAdapter extends BaseAdapter {
        private ArrayList<HashMap<String, String>> mData;
        private final int mResource;
        private String[] mFromKeys;
        private final int[] mToIds;
        private final String mColorFlagKey;
        private float mOriginalTextSize = -1.0f;
        private final int mFontSizeScalePcent;
        private Typeface mCustomTypeface = null;
        private LayoutInflater mInflater;


        public MultiColumnListAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource,
                                      String[] from, int[] to, String colorFlagKey, int fontSizeScalePcent, String customFont) {
            mData = data;
            mResource = resource;
            mFromKeys = from;
            mToIds = to;
            mColorFlagKey = colorFlagKey;
            mFontSizeScalePcent = fontSizeScalePcent;
            if (!customFont.equals("")) {
                mCustomTypeface = AnkiFont.getTypeface(context, customFont);
            }
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the main container view if it doesn't already exist, and call bindView
            View v;
            if (convertView == null) {
                v = mInflater.inflate(mResource, parent, false);
                final int count = mToIds.length;
                final View[] columns = new View[count];
                for (int i = 0; i < count; i++) {
                    columns[i] = v.findViewById(mToIds[i]);
                }
                v.setTag(columns);
            } else {
                v = convertView;
            }
            bindView(position, v);
            return v;
        }


        private void bindView(int position, View v) {
            // Draw the content in the columns
            View[] columns = (View[]) v.getTag();
            final Map<String, String> dataSet = mData.get(position);
            final int colorIdx = getColor(dataSet.get(mColorFlagKey));
            int[] colors = Themes.getColorFromAttr(CardBrowserActivity.this, new int[]{android.R.attr.colorBackground,
                    R.attr.markedColor, R.attr.suspendedColor, R.attr.markedColor});
            for (int i = 0; i < mToIds.length; i++) {
                TextView mCol = (TextView) columns[i];
                // set font for column
                setFont(mCol);
                // set background color for column
                mCol.setBackgroundColor(colors[colorIdx]);
                // set text for column
                mCol.setText(dataSet.get(mFromKeys[i]));
            }
        }


        private void setFont(TextView v) {
            // Set the font and font size for a TextView v
            float currentSize = v.getTextSize();
            if (mOriginalTextSize < 0) {
                mOriginalTextSize = v.getTextSize();
            }
            // do nothing when pref is 100% and apply scaling only once
            if (mFontSizeScalePcent != 100 && Math.abs(mOriginalTextSize - currentSize) < 0.1) {
                v.setTextSize(TypedValue.COMPLEX_UNIT_SP, mOriginalTextSize * (mFontSizeScalePcent / 100.0f));
            }

            if (mCustomTypeface != null) {
                v.setTypeface(mCustomTypeface);
            }
        }


        private int getColor(String flag) {
            int which = BACKGROUND_NORMAL;
            if (flag == null) {
                // use BACKGROUND_NORMAL
            } else if (flag.equals("1")) {
                which = BACKGROUND_SUSPENDED;
            } else if (flag.equals("2")) {
                which = BACKGROUND_MARKED;
            } else if (flag.equals("3")) {
                which = BACKGROUND_MARKED_SUSPENDED;
            }
            return which;
        }


        public void setFromMapping(String[] from) {
            mFromKeys = from;
            notifyDataSetChanged();
        }


        public String[] getFromMapping() {
            return mFromKeys;
        }


        @Override
        public int getCount() {
            return mData.size();
        }


        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }
    }*/


    private ArrayList<HashMap<String, String>> getCards() {
        if (mCards == null) {
            mCards = new ArrayList<>();
        }
        return mCards;
    }


    /**
     * Show/dismiss dialog when sd card is ejected/remounted (collection is saved by SdCardReceiver)
     */
    private void registerExternalStorageListener() {
        if (mUnmountReceiver == null) {
            mUnmountReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(SdCardReceiver.MEDIA_EJECT)) {
                        finishWithoutAnimation();
                    }
                }
            };
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(SdCardReceiver.MEDIA_EJECT);
            registerReceiver(mUnmountReceiver, iFilter);
        }
    }
    /////////////////////////////////////dx start

    /**
     * 根据输入的搜索条件来搜索卡片
     */
    public void searchCardByCondition(){
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>0){
                    cancleTextImg.setVisibility(View.VISIBLE);
                    search.setText("搜索");
                }else {
                    cancleTextImg.setVisibility(View.INVISIBLE);
                    search.setText("取消");
                    search.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户输入的条件
                String condition=searchText.getText().toString();
                searchCard(condition);
                searchText.setText("");
            }
        });
        cancleTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户输入的条件
                String condition=searchText.getText().toString();
                searchCard(condition);
                searchText.setText("");
            }
        });

        card_browser_backImg_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardBrowserActivity.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }
    private void searchCard(String cond) {
        DeckTask.cancelTask(DeckTask.TASK_TYPE_SEARCH_CARDS);
        DeckTask.cancelTask(DeckTask.TASK_TYPE_RENDER_BROWSER_QA);
        String searchText;
        if(cond.contains("deck:")){
            searchText=cond;
        }else{
            searchText = mRestrictOnDeck+cond;
        }
        if (colIsOpen()) {
            // clear the existing card list
            getCards().clear();
//            mCardsAdapter.notifyDataSetChanged();
            // Perform database query to get all card ids / sfld. Shows "filtering cards..." progress message
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_SEARCH_CARDS, mSearchCardsHandler, new DeckTask.TaskData(
                    new Object[] { mDeckNames, searchText, ((mOrder != CARD_ORDER_NONE)) }));
        }
        Log.e("CardBrowserActivity", "searchCards>>>>>>>"+ mCards.toString());
    }
    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatAndLollipopTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setStatusBarTintResource(R.color.aika_theme);//通知栏所需颜色
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initState();
        }
    }

    @TargetApi(19)  //Android4.4
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //Android5.0
    private void initState() {
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    ////////////////////////////////////dx end
}
