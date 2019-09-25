package com.ichi2yiji.anki;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.dialogs.ActionSheetDialog;
import com.ichi2yiji.anki.dialogs.TagsDialog;
import com.ichi2yiji.anki.multimediacard.IMultimediaEditableNote;
import com.ichi2yiji.anki.multimediacard.activity.MultimediaEditFieldActivity;
import com.ichi2yiji.anki.multimediacard.fields.AudioField;
import com.ichi2yiji.anki.multimediacard.fields.IField;
import com.ichi2yiji.anki.multimediacard.fields.ImageField;
import com.ichi2yiji.anki.multimediacard.fields.TextField;
import com.ichi2yiji.anki.receiver.SdCardReceiver;
import com.ichi2yiji.anki.servicelayer.NoteService;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.AudioRecoderUtils;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.widgets.PopupMenuWithIcons;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.libanki.Card;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Note;
import com.ichi2yiji.libanki.Utils;
import com.ichi2yiji.themes.StyledProgressDialog;
import com.ichi2yiji.themes.Themes;
import com.igexin.sdk.PushManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AikaNoteEditor extends AnkiActivity {
    private TextView typeText;
    private TextView nameText;
    private FieldEditText frontEdit;
    private FieldEditText backEdit;
    private EditText lableEdit;
    private ImageView backToText;
    private TextView saveText;
    private ImageView voiceImg1;
    private ImageView voiceImg2;
    private ImageView iconImg1;
    private ImageView iconImg2;

    private long mCurrentModleId;
    private static JSONArray mAllCardTypes = null;
    private static JSONArray mAllDeckLists = null;
    private Map<String, List<String>> typeItems = null;
    private String currentName;
    private MediaPlayer mp;
    private MediaRecorder mr;
    File f = null;
    AudioRecoderUtils audioRecoderUtils;
    static String pathStr = null;

    public static final String SOURCE_TEXT = "SOURCE_TEXT";
    public static final String TARGET_TEXT = "TARGET_TEXT";
    public static final String EXTRA_CALLER = "CALLER";
    public static final String EXTRA_CARD_ID = "CARD_ID";
    public static final String EXTRA_CONTENTS = "CONTENTS";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_CARD_DECK = "CARD_DECK";

    private static final String ACTION_CREATE_FLASHCARD = "org.openintents.action.CREATE_FLASHCARD";
    private static final String ACTION_CREATE_FLASHCARD_SEND = "android.intent.action.SEND";

    // calling activity
    public static final int CALLER_NOCALLER = 0;

    public static final int CALLER_REVIEWER = 1;
    public static final int CALLER_STUDYOPTIONS = 2;
    public static final int CALLER_DECKPICKER = 3;

    public static final int CALLER_CARDBROWSER_EDIT = 6;
    public static final int CALLER_CARDBROWSER_ADD = 7;

    public static final int CALLER_CARDEDITOR = 8;
    public static final int CALLER_CARDEDITOR_INTENT_ADD = 10;

    public static final int REQUEST_ADD = 0;
    public static final int REQUEST_MULTIMEDIA_EDIT = 2;
    public static final int REQUEST_TEMPLATE_EDIT = 3;

    private boolean mChanged = false;
    private boolean mFieldEdited = false;

    /**
     * Flag which forces the calling activity to rebuild it's definition of current card from scratch
     */
    private boolean mReloadRequired = false;


    /**
     * Broadcast that informs us when the sd card is about to be unmounted
     */
    private BroadcastReceiver mUnmountReceiver = null;

    private LinearLayout mFieldsLayoutContainer;

    private TextView mTagsButton;
    private TextView mCardsButton;
    private Spinner mNoteTypeSpinner;
    private Spinner mNoteDeckSpinner;

    private Note mEditorNote;
    public static Card mCurrentEditedCard;
    private List<String> mSelectedTags;
    private long mCurrentDid;
    private ArrayList<Long> mAllDeckIds;
    private ArrayList<Long> mAllModelIds;
    private Map<Integer, Integer> mModelChangeFieldMap;
    private Map<Integer, Integer> mModelChangeCardMap;

    /* indicates if a new fact is added or a card is edited */
    private boolean mAddNote;

    private boolean mAedictIntent;

    /* indicates which activity called Note Editor */
    private int mCaller;

    private LinkedList<FieldEditText> mEditFields;

    private MaterialDialog mProgressDialog;

    private String[] mSourceText;


    // A bundle that maps field ords to the text content of that field for use in
    // restoring the Activity.
    private Bundle mSavedFields;
    private LinearLayout layout;
    private ScrollView scrollView;
    private String mCurrentDeck;
    String [] ss;
    private TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aika_note_editor);
//        applyKitKatAndLollipopTranslucency();
        ApplyTranslucency.applyKitKatTranslucency(this);
        audioRecoderUtils = new AudioRecoderUtils();
        mEditFields = new LinkedList<>();
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            mCaller = savedInstanceState.getInt("caller");
            mAddNote = savedInstanceState.getBoolean("addFact");
            mCurrentDid = savedInstanceState.getLong("did");
            mSelectedTags = new ArrayList<>(Arrays.asList(savedInstanceState.getStringArray("tags")));
            mSavedFields = savedInstanceState.getBundle("editFields");
        } else {
            mCaller = intent.getIntExtra(EXTRA_CALLER, CALLER_NOCALLER);
            if (mCaller == CALLER_NOCALLER) {
                String action = intent.getAction();
                if (action != null
                        && (ACTION_CREATE_FLASHCARD.equals(action) || ACTION_CREATE_FLASHCARD_SEND.equals(action))) {
                    mCaller = CALLER_CARDEDITOR_INTENT_ADD;
                }
            }
        }
        initUI();
        back();
        startLoadingCollection();
    }

    public void initUI() {
        typeText = (TextView) findViewById(R.id.note_editor_type_text);
        nameText = (TextView) findViewById(R.id.note_editor_name_text);
        //frontEdit = (FieldEditText) findViewById(R.id.note_editor_front);
        //backEdit = (FieldEditText) findViewById(R.id.note_editor_back);
        /*mEditFields.add(frontEdit);
        mEditFields.add(backEdit);*/
        lableEdit = (EditText) findViewById(R.id.note_editor_lable);
        backToText = (ImageView) findViewById(R.id.note_editor_backTo);
        saveText = (TextView) findViewById(R.id.note_editor_save);
        layout = (LinearLayout)findViewById(R.id.note_editor_layout);
        scrollView = (ScrollView)findViewById(R.id.note_edit_scroll);
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.requestFocus();
        titleText = (TextView)findViewById(R.id.note_edit_title);
        //itemListView = (ListView) findViewById(R.id.note_editor_listview);
        //itemListView.setDividerHeight(0);
        //iconImg1 = (ImageView) findViewById(R.id.note_editor_icon1);
        //iconImg2 = (ImageView) findViewById(R.id.note_editor_icon2);
        //voiceImg1 = (ImageView) findViewById(R.id.note_editor_voice1);
        //voiceImg2 = (ImageView) findViewById(R.id.note_editor_voice2);
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (){

                }*/
                saveNote();
            }
        });
        /*voiceImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AikaNoteEditor.this, "播放录音", Toast.LENGTH_SHORT).show();
                if (pathStr != null){
                    audioRecoderUtils.startPlayer(pathStr);
                }else {
                    Toast.makeText(AikaNoteEditor.this, "没有音频文件", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
       /* iconImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });*/


        audioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(AikaNoteEditor.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                pathStr = filePath;
            }
        });

        //Button的touch监听
        /*voiceImg1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        getVedioPermission();//获取录音权限,并录音
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("end voice","end voice");
                        audioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                        Toast.makeText(AikaNoteEditor.this,"录音结束",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });*/
    }

    @Override
    protected void onCollectionLoaded(Collection col) {
        super.onCollectionLoaded(col);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent = getIntent();
        Timber.d("onCollectionLoaded: caller: %d", mCaller);

        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());

        registerExternalStorageListener();

        //View mainView = findViewById(android.R.id.content);

            /*Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }*/

        mFieldsLayoutContainer = (LinearLayout) findViewById(R.id.CardEditorEditFieldsLayout);

        mTagsButton = (TextView) findViewById(R.id.CardEditorTagText);
           /* mCardsButton = (TextView) findViewById(R.id.CardEditorCardsText);
            mCardsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.i("NoteEditor:: Cards button pressed. Opening template editor");
                    showCardTemplateEditor();
                }
            });*/

        Preferences.COMING_FROM_ADD = false;

        mAedictIntent = false;

        switch (mCaller) {
                /*case CALLER_NOCALLER:
                    Timber.e("no caller could be identified, closing");
                    finishWithoutAnimation();
                    return;*/

            case CALLER_REVIEWER:
                titleText.setText("编辑笔记");
                mCurrentEditedCard = AbstractFlashcardViewer.getEditorCard();
                if (mCurrentEditedCard == null) {
                    finishWithoutAnimation();
                    return;
                }
                mEditorNote = mCurrentEditedCard.note();
                mAddNote = false;
                break;

            case CALLER_STUDYOPTIONS:
            case CALLER_DECKPICKER:
                mAddNote = true;
                break;

            case CALLER_CARDBROWSER_EDIT:
                titleText.setText("编辑笔记");
                mCurrentEditedCard = CardBrowserActivity.sCardBrowserCard;
                Log.e("mCurrentEditedCard", mCurrentEditedCard + "//////");
                if (mCurrentEditedCard == null) {
                    finishWithoutAnimation();
                    return;
                }
                mEditorNote = mCurrentEditedCard.note();
                mEditorNote.getCol().getModels().getName();
                Log.e("mEditorNote.getCol()",mEditorNote.getCol().getModels().getName().toString());
                mAddNote = false;
                break;

            case CALLER_CARDBROWSER_ADD:
                titleText.setText("编辑笔记");
                mAddNote = true;
                break;

            case CALLER_CARDEDITOR:
                mAddNote = true;
                break;

            case CALLER_CARDEDITOR_INTENT_ADD:
                fetchIntentInformation(intent);
                if (mSourceText == null) {
                    finishWithoutAnimation();
                    return;
                }
                if (mSourceText[0].equals("Aedict Notepad") && addFromAedict(mSourceText[1])) {
                    finishWithoutAnimation();
                    return;
                }
                mAddNote = true;
                break;
        }
        try {
            //Log.e("mEditorNoteggggg",mEditorNote.getFields()[0]+"ffff"+mEditorNote.getFields()[1]);
            if (mEditorNote != null){
                ss = mEditorNote.getFields();
            }
            mCurrentDid = getCol().getDecks().current().getLong("id");
            currentName = getCol().getDecks().current().getString("name");
            mCurrentDeck = intent.getStringExtra(EXTRA_CARD_DECK);
            if (mCurrentEditedCard != null) {
                JSONObject modle = getCol().getNote(mCurrentEditedCard.getNid()).model();
                typeText.setText(currentName);
                if(mCurrentDeck!=null){
                    nameText.setText(mCurrentDeck);
                }else {
                    nameText.setText(currentName);
                }

                //Log.e("Modle.Files", modle.get("flds").toString());
                List<String> allItems = new ArrayList<>();
                try{
                    JSONArray itemArray = (JSONArray)modle.get("flds");
                    for (int i = 0;i<itemArray.length();i++){
                        allItems.add(((JSONObject)itemArray.get(i)).getString("name"));
                    }
                    for (int j = 0;j<allItems.size();j++){
                        if(ss!=null){
                            addView(j,allItems.get(j),ss[j]);
                        }else{
                            addView(j,allItems.get(j),"");
                        }

                    }
                }catch (JSONException e){
                    Log.e("JSONException",e.getMessage());
                }
                if (modle != null) {
                    typeText.setText(modle.get("name").toString());
                }
            }
            //frontEdit.setText();
            //typeText.setText(type);
            //Log.e("currentName", currentName);
            mCurrentModleId = 99999;
            JSONArray typeList = new JSONArray();
            ArrayList<JSONObject> modles = getCol().getModels().all();
            for (int i = 0; i < modles.size(); i++) {
                JSONObject obj = modles.get(i);
                String name = obj.getString("name");
                String modleId = obj.getString("id");
                JSONArray flds = obj.getJSONArray("flds");
                List<String> fields = new ArrayList<>();
                for (int j = 0; j < flds.length(); j++) {
                    fields.add(((JSONObject) flds.get(j)).getString("name"));
                }
                JSONArray array = new JSONArray();
                array.put(0, name);
                array.put(1, modleId);
                array.put(3, fields);
                typeList.put(array);
            }
            mAllCardTypes = typeList;
            Log.e("mAllCardTypes", mAllCardTypes.toString());
            JSONArray deckList = new JSONArray();
            ArrayList<JSONObject> decks = getCol().getDecks().all();
            for (JSONObject deck : decks) {
                String name = deck.getString("name");
                String deckId = deck.getString("id");
                JSONArray array = new JSONArray();
                array.put(0, name);
                array.put(1, deckId);
                deckList.put(array);
            }
            mAllDeckLists = deckList;
            Log.e("mAllDeckLists", mAllDeckLists.toString());
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        setNoteName();
        setNoteType();


        // Note type Selector
        //mNoteTypeSpinner = (Spinner) findViewById(R.id.note_type_spinner);
        mAllModelIds = new ArrayList<Long>();
        final ArrayList<String> modelNames = new ArrayList<String>();
        ArrayList<JSONObject> models = getCol().getModels().all();
        Collections.sort(models, new JSONNameComparator());
        for (JSONObject m : models) {
            try {
                modelNames.add(m.getString("name"));
                mAllModelIds.add(m.getLong("id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayAdapter<String> noteTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelNames);
        // mNoteTypeSpinner.setAdapter(noteTypeAdapter);
        noteTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Deck Selector
        //TextView deckTextView = (TextView) findViewById(R.id.CardEditorDeckText);
        // If edit mode and more than one card template distinguish between "Deck" and "Card deck"
            /*try {
                if (!mAddNote && mEditorNote.model().getJSONArray("tmpls").length()>1) {
                    //deckTextView.setText(R.string.CardEditorCardDeck);
                }
            } catch (JSONException e1) {
                throw new RuntimeException();
            }*/
        //mNoteDeckSpinner = (Spinner) findViewById(R.id.note_deck_spinner);
        mAllDeckIds = new ArrayList<Long>();
        final ArrayList<String> deckNames = new ArrayList<String>();

        ArrayList<JSONObject> decks = getCol().getDecks().all();
        Collections.sort(decks, new JSONNameComparator());
        for (JSONObject d : decks) {
            try {
                // add current deck and all other non-filtered decks to deck list
                long thisDid = d.getLong("id");
                long currentDid = getCol().getDecks().current().getLong("id");
                if (d.getInt("dyn") == 0 || (!mAddNote && thisDid == currentDid)) {
                    deckNames.add(d.getString("name"));
                    mAllDeckIds.add(thisDid);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayAdapter<String> noteDeckAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deckNames);
        //mNoteDeckSpinner.setAdapter(noteDeckAdapter);
        noteDeckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            /*mNoteDeckSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    // Timber.i("NoteEditor:: onItemSelected() fired on mNoteDeckSpinner with pos = "+Integer.toString(pos));
                    mCurrentDid = mAllDeckIds.get(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do Nothing
                }
            });
*/
        //setDid(mEditorNote);

        //setNote(mEditorNote);

        // Set current note type and deck positions in spinners
            /*int position;
            try {
                position = mAllModelIds.indexOf(mEditorNote.model().getLong("id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }*/
        // set selection without firing selectionChanged event
        // nb: setOnItemSelectedListener needs to occur after this
        //mNoteTypeSpinner.setSelection(position, false);

        if (mAddNote) {
            //mNoteTypeSpinner.setOnItemSelectedListener(new SetNoteTypeListener());
            //setTitle(R.string.cardeditor_title_add_note);
            // set information transferred by intent
            String contents = null;
            if (mSourceText != null) {
                if (mAedictIntent && (mEditFields.size() == 3) && mSourceText[1].contains("[")) {
                    contents = mSourceText[1].replaceFirst("\\[", "\u001f" + mSourceText[0] + "\u001f");
                    contents = contents.substring(0, contents.length() - 1);
                } else if (mEditFields.size() > 0) {
                    mEditFields.get(0).setText(mSourceText[0]);
                    if (mEditFields.size() > 1) {
                        mEditFields.get(1).setText(mSourceText[1]);
                    }
                }
            } else {
                contents = intent.getStringExtra(EXTRA_CONTENTS);
            }
            if (contents != null) {
                setEditFieldTexts(contents);
            }
        } else {
            //mNoteTypeSpinner.setOnItemSelectedListener(new EditNoteTypeListener());
            setTitle(R.string.cardeditor_title_edit_card);
        }


            /*((LinearLayout) findViewById(R.id.CardEditorTagButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.i("NoteEditor:: Tags button pressed... opening tags editor");
                    showTagsDialog();
                }
            });*/

        if (!mAddNote && mCurrentEditedCard != null) {
            Timber.i("NoteEditor:: Edit note activity successfully started with card id %d", mCurrentEditedCard.getId());
        }
    }

    private void setEditFieldTexts(String contents) {
        String[] fields = null;
        int len;
        if (contents == null) {
            len = 0;
        } else {
            fields = Utils.splitFields(contents);
            len = fields.length;
        }
        for (int i = 0; i < mEditFields.size(); i++) {
            if (i < len) {
                mEditFields.get(i).setText(fields[i]);
            } else {
                mEditFields.get(i).setText("");
            }
        }
    }

    private void showTagsDialog() {
        if (mSelectedTags == null) {
            mSelectedTags = new ArrayList<String>();
        }
        ArrayList<String> tags = new ArrayList<String>(getCol().getTags().all());
        ArrayList<String> selTags = new ArrayList<String>(mSelectedTags);
        TagsDialog dialog = com.ichi2yiji.anki.dialogs.TagsDialog.newInstance(TagsDialog.TYPE_ADD_TAG, selTags,
                tags);
        dialog.setTagsDialogListener(new TagsDialog.TagsDialogListener() {
            @Override
            public void onPositive(List<String> selectedTags, int option) {
                mSelectedTags = selectedTags;
                updateTags();
            }
        });
        showDialogFragment(dialog);
    }

    private void updateTags() {
        if (mSelectedTags == null) {
            mSelectedTags = new ArrayList<String>();
        }
        mTagsButton.setText(getResources().getString(R.string.CardEditorTags,
                getCol().getTags().join(getCol().getTags().canonify(mSelectedTags)).trim().replace(" ", ", ")));
    }

    /**
     * finish when sd card is ejected
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
            //registerReceiver(mUnmountReceiver, iFilter);
        }
    }

    private void showCardTemplateEditor() {
        Intent intent = new Intent(this, CardTemplateEditor.class);
        // Pass the model ID
        try {
            intent.putExtra("modelId", getCurrentlySelectedModel().getLong("id"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // Also pass the card ID if not adding new note
        if (!mAddNote) {
            intent.putExtra("noteId", mCurrentEditedCard.note().getId());
        }
        startActivityForResultWithAnimation(intent, REQUEST_TEMPLATE_EDIT, ActivityTransitionAnimation.LEFT);
    }

    private JSONObject getCurrentlySelectedModel() {
        return getCol().getModels().get(mAllModelIds.get(mNoteTypeSpinner.getSelectedItemPosition()));
    }

    private void fetchIntentInformation(Intent intent) {
        Bundle extras = intent.getExtras();
        if (ACTION_CREATE_FLASHCARD.equals(intent.getAction())) {
            // mSourceLanguage = extras.getString(SOURCE_LANGUAGE);
            // mTargetLanguage = extras.getString(TARGET_LANGUAGE);
            mSourceText = new String[2];
            mSourceText[0] = extras.getString(SOURCE_TEXT);
            mSourceText[1] = extras.getString(TARGET_TEXT);
        } else {
            String first;
            String second;
            if (extras.getString(Intent.EXTRA_SUBJECT) != null) {
                first = extras.getString(Intent.EXTRA_SUBJECT);
            } else {
                first = "";
            }
            if (extras.getString(Intent.EXTRA_TEXT) != null) {
                second = extras.getString(Intent.EXTRA_TEXT);
            } else {
                second = "";
            }
            // Some users add cards via SEND intent from clipboard. In this case SUBJECT is empty
            if (first.equals("")) {
                // Assume that if only one field was sent then it should be the front
                first = second;
                second = "";
            }
            Pair<String, String> messages = new Pair<String, String>(first, second);

            mSourceText = new String[2];
            mSourceText[0] = messages.first;
            mSourceText[1] = messages.second;
        }
    }

    private boolean addFromAedict(String extra_text) {
        String category = "";
        String[] notepad_lines = extra_text.split("\n");
        for (int i = 0; i < notepad_lines.length; i++) {
            if (notepad_lines[i].startsWith("[") && notepad_lines[i].endsWith("]")) {
                category = notepad_lines[i].substring(1, notepad_lines[i].length() - 1);
                if (category.equals("default")) {
                    if (notepad_lines.length > i + 1) {
                        String[] entry_lines = notepad_lines[i + 1].split(":");
                        if (entry_lines.length > 1) {
                            mSourceText[0] = entry_lines[1];
                            mSourceText[1] = entry_lines[0];
                            mAedictIntent = true;
                        } else {
                            Themes.showThemedToast(AikaNoteEditor.this,
                                    getResources().getString(R.string.intent_aedict_empty), false);
                            return true;
                        }
                    } else {
                        Themes.showThemedToast(AikaNoteEditor.this, getResources().getString(R.string.intent_aedict_empty),
                                false);
                        return true;
                    }
                    return false;
                }
            }
        }
        Themes.showThemedToast(AikaNoteEditor.this, getResources().getString(R.string.intent_aedict_category), false);
        return true;
    }

    /**
     * 点击笔记名称时，弹出Dialog,显示出所有牌组的名称
     */
    public void setNoteName() {
        final List<String> deckNames = new ArrayList<>();
        final List<String> deckDids = new ArrayList<>();
        for (int i = 0; i < mAllDeckLists.length(); i++) {
            try {
                JSONArray array = (JSONArray) mAllDeckLists.getJSONArray(i);
                deckNames.add(array.getString(0));
                deckDids.add(array.getString(1));
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }

        nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog actionSheetDialog = new ActionSheetDialog(AikaNoteEditor.this)
                        .builder()
                        .setViewPostionAndWidth(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0.95)
                        .setTitle("请选择牌组名称")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .showCancelItem();
                for (int i = 0; i < deckNames.size(); i++) {
                    //弹出菜单list item的添加
                    final String deckname = deckNames.get(i);
                    actionSheetDialog.addSheetItem(deckname, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    //item的点击事件
//                                    deck_name.setText(deckname);//将标题改为此牌组名称
                                    setTextName(deckNames.get(which - 1));
                                    mCurrentDid = Long.valueOf(deckDids.get(which - 1));
                                }
                            });
                }
                actionSheetDialog.show();

            }
        });

    }

    public void setTextName(String nameStr) {
        nameText.setText(nameStr);
    }

    /**
     * 点击笔记类型时，弹出Dialog,显示出所有牌组的类型
     */
    public void setNoteType() {
        final List<String> deckTypes = new ArrayList<>();
        final List<String> deckIds = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Log.e("mAllCardTypes", mAllCardTypes.toString());
        typeItems = new HashMap<>();
        for (int i = 0; i < mAllCardTypes.length(); i++) {
            try {
                JSONArray array = (JSONArray) mAllCardTypes.getJSONArray(i);
                deckTypes.add(array.getString(0));
                deckIds.add(array.getString(1));
                //list.clear();
                list = (ArrayList<String>) array.get(3);
                //typeItems存放key为笔记类型 value为filds
                typeItems.put(array.get(0).toString(), list);
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog actionSheetDialog = new ActionSheetDialog(AikaNoteEditor.this)
                        .builder()
                        .setViewPostionAndWidth(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0.95)
                        .setTitle("请选择卡片类型")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .showCancelItem();
                for (int i = 0; i < deckTypes.size(); i++) {
                    //弹出菜单list item的添加
                    final String deckname = deckTypes.get(i);
                    actionSheetDialog.addSheetItem(deckname, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    //item的点击事件
//                                    deck_name.setText(deckname);//将标题改为此牌组名称
                                    layout.removeAllViews();
                                    setTextType(deckTypes.get(which - 1));
                                    LayoutInflater inflater = LayoutInflater.from(AikaNoteEditor.this);
                                    List<String> strs = typeItems.get(deckTypes.get(which - 1));
                                    Log.e("typeItems.get(deckType",typeItems.get(deckTypes.get(which - 1)).toString());
                                    for (int i = 0;i<strs.size();i++){
                                        addView(i,strs.get(i),"");
                                    }
                                    //itemAdapter = new NoteEditItemAdapter(AikaNoteEditor.this, typeItems.get(deckTypes.get(which - 1)));
                                    //itemListView.setAdapter(itemAdapter);
                                    /*itemAdapter.setOnImgClickListener(new NoteEditItemAdapter.OnImgClickListener() {
                                        @Override
                                        public void click(int position) {
                                            getPhoto();
                                        }
                                    });
                                    itemAdapter.setOnAudieoClickListener(new NoteEditItemAdapter.OnAudieoClickListener() {
                                        @Override
                                        public void start(int position) {
                                            getVedioPermission();//获取录音权限,并录音
                                        }
                                        @Override
                                        public void end(int position) {
                                            audioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                                            Toast.makeText(AikaNoteEditor.this,"录音结束",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/
                                    mCurrentModleId = Long.valueOf(deckIds.get(which - 1));
                                }
                            });
                }
                actionSheetDialog.show();
            }
        });

    }

    public void setTextType(String type) {
        typeText.setText(type);
    }

    private DeckTask.TaskListener mSaveFactHandler = new DeckTask.TaskListener() {
        private boolean mCloseAfter = false;
        private Intent mIntent;


        @Override
        public void onPreExecute() {
            Resources res = getResources();
            mProgressDialog = StyledProgressDialog
                    .show(AikaNoteEditor.this, "", res.getString(R.string.saving_facts), false);
        }


        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
            int count = values[0].getInt();
            if (count > 0) {
                //mChanged = true;
                //mSourceText = null;
                Note oldNote = mEditorNote.clone();
                //setNote();
                // Respect "Remember last input when adding" field option.
                JSONArray flds;
                try {
                    flds = mEditorNote.model().getJSONArray("flds");
                    if (oldNote != null) {
                        for (int fldIdx = 0; fldIdx < flds.length(); fldIdx++) {
                            if (flds.getJSONObject(fldIdx).getBoolean("sticky")) {
                                mEditFields.get(fldIdx).setText(oldNote.getFields()[fldIdx]);
                            }
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException();
                }
                Themes.showThemedToast(AikaNoteEditor.this,
                        getResources().getQuantityString(R.plurals.factadder_cards_added, count, count), true);
            } else {
                Themes.showThemedToast(AikaNoteEditor.this, getResources().getString(R.string.factadder_saving_error), true);
            }
            if (!mAddNote || mCaller == CALLER_CARDEDITOR || mAedictIntent) {
                //mChanged = true;
                mCloseAfter = true;
            } else if (mCaller == CALLER_CARDEDITOR_INTENT_ADD) {
                if (count > 0) {
                    // mChanged = true;
                }
                mCloseAfter = true;
                mIntent = new Intent();
                mIntent.putExtra(EXTRA_ID, getIntent().getStringExtra(EXTRA_ID));
            } else if (!mEditFields.isEmpty()) {
                mEditFields.getFirst().requestFocus();
            }
            if (!mCloseAfter) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    try {
                        mProgressDialog.dismiss();
                    } catch (IllegalArgumentException e) {
                        Timber.e(e, "Note Editor: Error on dismissing progress dialog");
                    }
                }
            }
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result.getBoolean()) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    try {
                        mProgressDialog.dismiss();
                    } catch (IllegalArgumentException e) {
                        Timber.e(e, "Note Editor: Error on dismissing progress dialog");
                    }
                }
                if (mCloseAfter) {
                    if (mIntent != null) {
                        // closeNoteEditor(mIntent);
                    } else {
                        //closeNoteEditor();
                    }
                } else {
                    // Reset check for changes to fields
                    mFieldEdited = false;
                }
            } else {
                // RuntimeException occured on adding note
                // closeNoteEditor(DeckPicker.RESULT_DB_ERROR);
            }
        }


        @Override
        public void onCancelled() {
        }
    };

    private class SetNoteTypeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // If a new column was selected then change the key used to map from mCards to the column TextView
            //Timber.i("NoteEditor:: onItemSelected() fired on mNoteTypeSpinner");
            long oldModelId;
            try {
                oldModelId = getCol().getModels().current().getLong("id");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            long newId = mAllModelIds.get(pos);
            if (oldModelId != newId) {
                JSONObject model = getCol().getModels().get(newId);
                getCol().getModels().setCurrent(model);
                JSONObject cdeck = getCol().getDecks().current();
                try {
                    cdeck.put("mid", newId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                getCol().getDecks().save(cdeck);
                // Update deck
                if (!getCol().getConf().optBoolean("addToCur", true)) {
                    try {
                        mCurrentDid = model.getLong("did");
                        updateDeckPosition();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Reset edit fields
                int size = mEditFields.size();
                String[] oldValues = new String[size];
                for (int i = 0; i < size; i++) {
                    oldValues[i] = mEditFields.get(i).getText().toString();
                }
                setNote();
                resetEditFields(oldValues);
                duplicateCheck();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do Nothing
        }
    }

    private void updateDeckPosition() {
        int position = mAllDeckIds.indexOf(mCurrentDid);
        if (position != -1) {
            mNoteDeckSpinner.setSelection(position, false);
        } else {
            Timber.e("updateDeckPosition() error :: mCurrentDid=%d, position=%d", mCurrentDid, position);
        }
    }

    private void setNote() {
        setNote(null);
    }


    private void setNote(Note note) {
        if (note == null || mAddNote) {
            JSONObject model = getCol().getModels().current();
            mEditorNote = new Note(getCol(), model);
        } else {
            mEditorNote = note;
        }
        if (mSelectedTags == null) {
            mSelectedTags = mEditorNote.getTags();
        }
        updateDeckPosition();
        updateTags();
        updateCards(mEditorNote.model());
        populateEditFields();
    }

    private void resetEditFields(String[] content) {
        for (int i = 0; i < Math.min(content.length, mEditFields.size()); i++) {
            mEditFields.get(i).setText(content[i]);
        }
    }

    private boolean duplicateCheck() {
        boolean isDupe;
        FieldEditText field = mEditFields.get(0);
        // Keep copy of current internal value for this field.
        String oldValue = mEditorNote.getFields()[0];
        // Update the field in the Note so we can run a dupe check on it.
        updateField(field);
        // 1 is empty, 2 is dupe, null is neither.
        Integer dupeCode = mEditorNote.dupeOrEmpty();
        // Change bottom line color of text field
        if (dupeCode != null && dupeCode == 2) {
            field.getBackground().setColorFilter(getResources().getColor(R.color.material_red_500),
                    PorterDuff.Mode.SRC_ATOP);
            isDupe = true;
        } else {
            field.getBackground().clearColorFilter();
            isDupe = false;
        }
        // Put back the old value so we don't interfere with modification detection
        mEditorNote.values()[0] = oldValue;
        return isDupe;
    }

    /**
     * Update the list of card templates for current note type
     */
    private void updateCards(JSONObject model) {
        try {
            JSONArray tmpls = model.getJSONArray("tmpls");
            String cardsList = "";
            // Build comma separated list of card names
            for (int i = 0; i < tmpls.length(); i++) {
                String name = tmpls.getJSONObject(i).optString("name");
                // If more than one card then make currently selected card underlined
                if (!mAddNote && tmpls.length() > 1 && model == mEditorNote.model() &&
                        mCurrentEditedCard.template().optString("name").equals(name)) {
                    name = "<u>" + name + "</u>";
                }
                cardsList += name;
                if (i < tmpls.length() - 1) {
                    cardsList += ", ";
                }
            }
            // Make cards list red if the number of cards is being reduced
            if (!mAddNote && tmpls.length() < mEditorNote.model().getJSONArray("tmpls").length()) {
                cardsList = "<font color='red'>" + cardsList + "</font>";
            }
            mCardsButton.setText(Html.fromHtml(getResources().getString(R.string.CardEditorCards, cardsList)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存笔记
     */
    public void saveNote() {
        String name = nameText.getText().toString();
        String type = typeText.getText().toString();
        if(mCurrentModleId != 99999){
            JSONObject model = getCol().getModels().get(mCurrentModleId);
            mEditorNote = new Note(getCol(), model);
        }else{
            AikaNoteEditor.this.finish();
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            return;
        }
        Log.e("dfsdfgsf",typeItems.toString());
        List<String> strs = typeItems.get(type);
        for(int i = 0;i<strs.size();i++){
            View v = layout.getChildAt(i);
            FieldEditText editText = (FieldEditText) v.findViewById(R.id.note_editor_item_content);
            updateField(i,editText);
        }
        //获取输入框的内容（order）
        /*for (FieldEditText f : mEditFields) {
            updateField(f);
        }*/
        //mEditorNote.setField();
        getCol().getModels().setChanged();
        try {
            mEditorNote.model().put("did", mCurrentDid);
            Log.e("mSelectedTags", mSelectedTags + "/");
          /* mEditorNote.setTagsFromStr(tagsAsString(mSelectedTags));
           JSONArray ja = new JSONArray();
           for (String t : mSelectedTags) {
               ja.put(t);
           }*/
            //getCol().getModels().current().put("tags", ja);
            getCol().getModels().setChanged();
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ADD_FACT, mSaveFactHandler, new DeckTask.TaskData(mEditorNote));

    }

    private boolean updateField(FieldEditText field) {
        String newValue = field.getText().toString().replace(FieldEditText.NEW_LINE, "<br>");
        if (!mEditorNote.values()[field.getOrd()].equals(newValue)) {
            mEditorNote.values()[field.getOrd()] = newValue;
            return true;
        }
        return false;
    }
    private boolean updateField(int position,FieldEditText field) {
        String newValue = field.getText().toString().replace(FieldEditText.NEW_LINE, "<br>");
        if (!mEditorNote.values()[position].equals(newValue)) {
            mEditorNote.values()[position] = newValue;
            return true;
        }
        return false;
    }

    private String tagsAsString(List<String> tags) {
        return TextUtils.join(" ", tags);
    }

    /**
     * 点击图片调用系统的方法，获得本机图片或者拍照上传
     */
    public void getPhoto() {
        requestCameraPermission();
        new AlertDialog.Builder(AikaNoteEditor.this)
                .setTitle("请选择...")
                .setItems(new String[]{"相册", "拍照"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                /* User clicked so do some stuff */
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setData(Uri
                                                .parse("content://media/internal/images/media"));
                                        intent.setAction(Intent.ACTION_PICK);
                                        startActivityForResult(Intent
                                                        .createChooser(intent,
                                                                "Select Picture"),
                                                1);
                                        break;
                                    case 1:
                                        Intent i = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(i, 2);
                                        break;

                                }
                            }
                        }).create().show();
    }

    /**
     * 实现录音功能
     */
    public void getVoice() {
        mp = new MediaPlayer();
        mr = new MediaRecorder();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        // Bundle bundle = data.getExtras();
        //Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        File file = new File("/sdcard/myImage/");
        FileOutputStream bout = null;

        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/myImage/111.jpg";

        try {
            bout = new FileOutputStream(fileName);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bout != null) {
                    bout.flush();
                    bout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(file.getAbsolutePath())) {
                ToastAlone.showShortToast("拍照路径为:" + file.getAbsolutePath());
            } else {
                ToastAlone.showShortToast("拍照路径为空");
            }
        }
    }

    private class EditNoteTypeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // Get the current model
            long noteModelId;
            try {
                noteModelId = mCurrentEditedCard.model().getLong("id");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Get new model
            JSONObject newModel = getCol().getModels().get(mAllModelIds.get(pos));
            // Configure the interface according to whether note type is getting changed or not
            if (mAllModelIds.get(pos) != noteModelId) {
                // Initialize mapping between fields of old model -> new model
                mModelChangeFieldMap = new HashMap<Integer, Integer>();
                for (int i = 0; i < mEditorNote.items().length; i++) {
                    mModelChangeFieldMap.put(i, i);
                }
                // Initialize mapping between cards new model -> old model
                mModelChangeCardMap = new HashMap<Integer, Integer>();
                try {
                    for (int i = 0; i < newModel.getJSONArray("tmpls").length(); i++) {
                        if (i < mEditorNote.cards().size()) {
                            mModelChangeCardMap.put(i, i);
                        } else {
                            mModelChangeCardMap.put(i, null);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Update the field text edits based on the default mapping just assigned
                updateFieldsFromMap(newModel);
                // Don't let the user change any other values at the same time as changing note type
                mSelectedTags = mEditorNote.getTags();
                updateTags();
                ((LinearLayout) findViewById(R.id.CardEditorTagButton)).setEnabled(false);
                //((LinearLayout) findViewById(R.id.CardEditorCardsButton)).setEnabled(false);
                mNoteDeckSpinner.setEnabled(false);
                int position = mAllDeckIds.indexOf(mCurrentEditedCard.getDid());
                if (position != -1) {
                    mNoteDeckSpinner.setSelection(position, false);
                }
            } else {
                populateEditFields();
                updateCards(mCurrentEditedCard.model());
                ((LinearLayout) findViewById(R.id.CardEditorTagButton)).setEnabled(true);
                //((LinearLayout) findViewById(R.id.CardEditorCardsButton)).setEnabled(false);
                mNoteDeckSpinner.setEnabled(true);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do Nothing
        }
    }

    /**
     * Update all the field EditText views based on the currently selected note type and the mModelChangeFieldMap
     */
    private void updateFieldsFromMap(JSONObject newModel) {
        // Get the field map for new model and old fields list
        String[][] oldFields = mEditorNote.items();
        Map<String, Pair<Integer, JSONObject>> fMapNew = getCol().getModels().fieldMap(newModel);
        // Build array of label/values to provide to field EditText views
        String[][] fields = new String[fMapNew.size()][2];
        for (String fname : fMapNew.keySet()) {
            // Field index of new note type
            Integer i = fMapNew.get(fname).first;
            // Add values from old note type if they exist in map, otherwise make the new field empty
            if (mModelChangeFieldMap.containsValue(i)) {
                // Get index of field from old note type given the field index of new note type
                Integer j = getKeyByValue(mModelChangeFieldMap, i);
                // Set the new field label text
                if (allowFieldRemapping()) {
                    // Show the content of old field if remapping is enabled
                    fields[i][0] = String.format(getResources().getString(R.string.field_remapping), fname, oldFields[j][0]);
                } else {
                    fields[i][0] = fname;
                }

                // Set the new field label value
                fields[i][1] = oldFields[j][1];
            } else {
                // No values from old note type exist in the mapping
                fields[i][0] = fname;
                fields[i][1] = "";
            }
        }
        populateEditFields(fields, true);
        updateCards(newModel);
    }

    /**
     * Convenience method for getting the corresponding key given the value in a 1-to-1 map
     *
     * @param map
     * @param value
     * @return
     */
    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void populateEditFields() {
        String[][] fields;
        // If we have a bundle of pre-populated field values, we overwrite the existing values
        // with those ones since we are resuming the activity after it was terminated early.
        if (mSavedFields != null) {
            fields = mEditorNote.items();
            for (String key : mSavedFields.keySet()) {
                int ord = Integer.parseInt(key);
                String text = mSavedFields.getString(key);
                fields[ord][1] = text;
            }
            // Clear the saved values since we've consumed them.
            mSavedFields = null;
        } else {
            fields = mEditorNote.items();
        }
        populateEditFields(fields, false);
    }

    private void populateEditFields(String[][] fields, boolean editModelMode) {
        mFieldsLayoutContainer.removeAllViews();
        mEditFields = new LinkedList<FieldEditText>();

        // Use custom font if selected from preferences
        Typeface mCustomTypeface = null;
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(getBaseContext());
        String customFont = preferences.getString("browserEditorFont", "");
        if (!customFont.equals("")) {
            mCustomTypeface = AnkiFont.getTypeface(this, customFont);
        }

        for (int i = 0; i < fields.length; i++) {
            View editline_view = getLayoutInflater().inflate(R.layout.card_multimedia_editline, null);
            FieldEditText newTextbox = (FieldEditText) editline_view.findViewById(R.id.id_note_editText);

            initFieldEditText(newTextbox, i, fields[i], mCustomTypeface, !editModelMode);

            TextView label = newTextbox.getLabel();
            label.setPadding((int) UIUtils.getDensityAdjustedValue(this, 3.4f), 0, 0, 0);
            mEditFields.add(newTextbox);

            ImageButton mediaButton = (ImageButton) editline_view.findViewById(R.id.id_media_button);
            // Load icons from attributes
            int[] icons = Themes.getResFromAttr(this, new int[]{R.attr.attachFileImage, R.attr.upDownImage});
            // Make the icon change between media icon and switch field icon depending on whether editing note type
            if (editModelMode && allowFieldRemapping()) {
                // Allow remapping if originally more than two fields
                mediaButton.setBackgroundResource(icons[1]);
                setRemapButtonListener(mediaButton, i);
            } else if (editModelMode && !allowFieldRemapping()) {
                mediaButton.setBackgroundResource(0);
            } else {
                // Use media editor button if not changing note type
                mediaButton.setBackgroundResource(icons[0]);
                setMMButtonListener(mediaButton, i);
            }
            mFieldsLayoutContainer.addView(label);
            mFieldsLayoutContainer.addView(editline_view);
        }
    }

    private void initFieldEditText(FieldEditText editText, final int index, String[] values, Typeface customTypeface, boolean enabled) {
        String name = values[0];
        String content = values[1];
        editText.init(index, name, content);
        if (customTypeface != null) {
            editText.setTypeface(customTypeface);
        }
    }

    /**
     * @return whether or not to allow remapping of fields for current model
     */
    private boolean allowFieldRemapping() {
        // Map<String, Pair<Integer, JSONObject>> fMapNew = getCol().getModels().fieldMap(getCurrentlySelectedModel())
        return mEditorNote.items().length > 2;
    }

    private void setRemapButtonListener(ImageButton remapButton, final int newFieldIndex) {
        remapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("NoteEditor:: Remap button pressed for new field %d", newFieldIndex);
                // Show list of fields from the original note which we can map to
                PopupMenu popup = new PopupMenu(AikaNoteEditor.this, v);
                final String[][] items = mEditorNote.items();
                for (int i = 0; i < items.length; i++) {
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, items[i][0]);
                }
                // Add "nothing" at the end of the list
                popup.getMenu().add(Menu.NONE, items.length, Menu.NONE, R.string.nothing);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Get menu item id
                        Integer idx = item.getItemId();
                        Timber.i("NoteEditor:: User chose to remap to old field %d", idx);
                        // Retrieve any existing mappings between newFieldIndex and idx
                        Integer previousMapping = getKeyByValue(mModelChangeFieldMap, newFieldIndex);
                        Integer mappingConflict = mModelChangeFieldMap.get(idx);
                        // Update the mapping depending on any conflicts
                        if (idx == items.length && previousMapping != null) {
                            // Remove the previous mapping if None selected
                            mModelChangeFieldMap.remove(previousMapping);
                        } else if (idx < items.length && mappingConflict != null && previousMapping != null && newFieldIndex != mappingConflict) {
                            // Swap the two mappings if there was a conflict and previous mapping
                            mModelChangeFieldMap.put(previousMapping, mappingConflict);
                            mModelChangeFieldMap.put(idx, newFieldIndex);
                        } else if (idx < items.length && mappingConflict != null) {
                            // Set the conflicting field to None if no previous mapping to swap into it
                            mModelChangeFieldMap.remove(previousMapping);
                            mModelChangeFieldMap.put(idx, newFieldIndex);
                        } else if (idx < items.length) {
                            // Can simply set the new mapping if no conflicts
                            mModelChangeFieldMap.put(idx, newFieldIndex);
                        }
                        // Reload the fields
                        updateFieldsFromMap(getCurrentlySelectedModel());
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void setMMButtonListener(ImageButton mediaButton, final int index) {
        mediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("NoteEditor:: Multimedia button pressed for field %d", index);
                final Collection col = CollectionHelper.getInstance().getCol(AikaNoteEditor.this);
                if (mEditorNote.items()[index][1].length() > 0) {
                    // If the field already exists then we start the field editor, which figures out the type
                    // automatically
                    IMultimediaEditableNote mNote = NoteService.createEmptyNote(mEditorNote.model());
                    NoteService.updateMultimediaNoteFromJsonNote(col, mEditorNote, mNote);
                    IField field = mNote.getField(index);
                    startMultimediaFieldEditor(index, mNote, field);
                } else {
                    // Otherwise we make a popup menu allowing the user to choose between audio/image/text field
                    // TODO: Update the icons for dark material theme, then can set 3rd argument to true
                    PopupMenuWithIcons popup = new PopupMenuWithIcons(AikaNoteEditor.this, v, false);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.popupmenu_multimedia_options, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            IMultimediaEditableNote mNote = NoteService.createEmptyNote(mEditorNote.model());
                            NoteService.updateMultimediaNoteFromJsonNote(col, mEditorNote, mNote);
                            IField field;
                            switch (item.getItemId()) {
                                case R.id.menu_multimedia_audio:
                                    Timber.i("NoteEditor:: Record audio button pressed");
                                    field = new AudioField();
                                    mNote.setField(index, field);
                                    startMultimediaFieldEditor(index, mNote, field);
                                    return true;
                                case R.id.menu_multimedia_photo:
                                    Timber.i("NoteEditor:: Add image button pressed");
                                    field = new ImageField();
                                    mNote.setField(index, field);
                                    startMultimediaFieldEditor(index, mNote, field);
                                    return true;
                                case R.id.menu_multimedia_text:
                                    Timber.i("NoteEditor:: Advanced editor button pressed");
                                    field = new TextField();
                                    field.setText(mEditFields.get(index).getText().toString());
                                    mNote.setField(index, field);
                                    startMultimediaFieldEditor(index, mNote, field);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            }
        });
    }

    private void startMultimediaFieldEditor(final int index, IMultimediaEditableNote mNote, IField field) {
        Intent editCard = new Intent(AikaNoteEditor.this, MultimediaEditFieldActivity.class);
        editCard.putExtra(MultimediaEditFieldActivity.EXTRA_FIELD_INDEX, index);
        editCard.putExtra(MultimediaEditFieldActivity.EXTRA_FIELD, field);
        editCard.putExtra(MultimediaEditFieldActivity.EXTRA_WHOLE_NOTE, mNote);
        startActivityForResultWithoutAnimation(editCard, REQUEST_MULTIMEDIA_EDIT);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
    }

    /**
     * 点击回退键，结束此activity
     */
    public void back() {
        backToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AikaNoteEditor.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }

    public class JSONNameComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject lhs, JSONObject rhs) {
            String[] o1;
            String[] o2;
            try {
                o1 = lhs.getString("name").split("::");
                o2 = rhs.getString("name").split("::");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < Math.min(o1.length, o2.length); i++) {
                int result = o1[i].compareToIgnoreCase(o2[i]);
                if (result != 0) {
                    return result;
                }
            }
            if (o1.length < o2.length) {
                return -1;
            } else if (o1.length > o2.length) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void getVedioPermission() {
        //判断是否有录音权限
        PackageManager pkgManager = getPackageManager();
        boolean recordAudioPermission =
                pkgManager.checkPermission(android.Manifest.permission.RECORD_AUDIO, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !recordAudioPermission) {
            requestAudioPermission();
        } else {
            audioRecoderUtils.startRecord();
            Toast.makeText(AikaNoteEditor.this, "长按录音，录音开始", Toast.LENGTH_SHORT).show();
            PushManager.getInstance().initialize(this.getApplicationContext());
        }
    }

    /**
     * 请求权限
     */
    public static int RECORD_AUDIO = 0;

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_AUDIO);
    }

    public static int CAMERA = 1;

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                CAMERA);
    }

    /**
     * 请求权限的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RECORD_AUDIO) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                audioRecoderUtils.startRecord();
                Toast.makeText(AikaNoteEditor.this, "长按录音，录音开始", Toast.LENGTH_SHORT).show();
                PushManager.getInstance().initialize(this.getApplicationContext());

            } else {
                Toast.makeText(this, "对不起，艾卡记忆的使用需要得到您对以上权限的许可，请您重启应用并选择允许，谢谢！", Toast.LENGTH_LONG).show();
                /*finish();
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);*/
            }
        } else if (requestCode == CAMERA) {
            if ((grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(AikaNoteEditor.this, "获得拍照权限", Toast.LENGTH_SHORT).show();
                PushManager.getInstance().initialize(this.getApplicationContext());

            } else {
                Toast.makeText(this, "对不起，艾卡记忆的使用需要得到您对以上权限的许可，请您重启应用并选择允许，谢谢！", Toast.LENGTH_LONG).show();
                /*finish();
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);*/
            }
        } else {
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    public void addView(int position,String str,String item){
        LayoutInflater inflater = LayoutInflater.from(AikaNoteEditor.this);
        View v = inflater.inflate(R.layout.item_note_editor_layout,null);
        layout.addView(v,position);
        TextView textView = (TextView) v.findViewById(R.id.note_edit_item_name);
        ImageView pictureImg = (ImageView) v.findViewById(R.id.note_editor_icon1);
        ImageView audieoImg = (ImageView) v.findViewById(R.id.note_editor_voice1);
        FieldEditText editText = (FieldEditText) v.findViewById(R.id.note_editor_item_content);
        textView.setText(str);
        editText.setText(item);
        pictureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });
        audieoImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        getVedioPermission();//获取录音权限,并录音
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断录音是否已经开始录音
                        Log.e("event.getEventTime()",event.getEventTime()-event.getDownTime()+"");
                        if(audioRecoderUtils.isStart&&event.getEventTime()-event.getDownTime()>500){
                            audioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                            Toast.makeText(getApplicationContext(),"录音结束",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"您还没开始录音",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
    }
}
