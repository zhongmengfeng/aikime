/****************************************************************************************
 * Copyright (c) 2014 Timothy Rae <perceptualchaos2@gmail.com>                          *
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.compat.CompatHelper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import timber.log.Timber;


public class NavigationDrawerActivity extends AnkiActivity implements Drawer.OnDrawerItemClickListener,
        OnCheckedChangeListener, Drawer.OnDrawerNavigationListener {

    /** Navigation Drawer */
    protected CharSequence mTitle;
    protected Boolean mFragmented = false;
    private Drawer mDrawer;
    private AccountHeader mHeader = null;
    // Other members
    private String mOldColPath;
    // Navigation drawer list item entries
//    protected static final int DRAWER_DECK_PICKER = 0;
//    protected static final int DRAWER_BROWSER = 1;
    protected static final int DRAWER_STATISTICS = 2;
//    protected static final int DRAWER_NIGHT_MODE = 3;
//    protected static final int DRAWER_SETTINGS = 4;
//    protected static final int DRAWER_HELP = 5;
//    protected static final int DRAWER_FEEDBACK = 6;

    //自定义
    protected static final int DRAWER_DECK_PICKER = 0;
    protected static final int DRAWER_BROWSER = 1;
    protected static final int DRAWER_READCARD = 2;
    protected static final int DRAWER_TESTCARD = 3;
    protected static final int DRAWER_PREFERENCE = 4;
    protected static final int DRAWER_PERSONALCENTER = 5;
    protected static final int DRAWER_STATISTIC = 6;
    protected static final int DRAWER_ONLINEHELP = 7;

    // Intent request codes
    public static final int REQUEST_PREFERENCES_UPDATE = 100;
    public static final int REQUEST_BROWSE_CARDS = 101;
    public static final int REQUEST_STATISTICS = 102;

    private int mSelectedItem = DRAWER_DECK_PICKER;
    private long mCurrentCardId = -1L;


    // Navigation drawer initialisation
    protected void initNavigationDrawer(View mainView) {
        initNavigationDrawer(mainView, false);
    }
    protected void initNavigationDrawer(View mainView, boolean fullScreen){
        // Setup toolbar
        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
            } catch (RuntimeException e) {
                Timber.e("Error setting toolbar as support actionbar");
                AnkiDroidApp.sendExceptionReport(e, "Samsung device error using Toolbar");
            }
        }
        // Create the items for the navigation drawer
//        PrimaryDrawerItem deckListItem = new PrimaryDrawerItem().withName(R.string.decks)
//                .withIcon(R.drawable.ic_list_black_24dp).withIdentifier(DRAWER_DECK_PICKER).withIconTintingEnabled(true);
//        PrimaryDrawerItem browserItem = new PrimaryDrawerItem().withName(R.string.card_browser)
//                .withIcon(R.drawable.ic_search_black_24dp).withIdentifier(DRAWER_BROWSER).withIconTintingEnabled(true);
//        PrimaryDrawerItem statsItem = new PrimaryDrawerItem().withName(R.string.statistics)
//                .withIcon(R.drawable.ic_equalizer_black_24dp).withIdentifier(DRAWER_STATISTICS).withIconTintingEnabled(true);
//        SecondarySwitchDrawerItem nightModeItem = new SecondarySwitchDrawerItem().withName(R.string.night_mode)
//                .withChecked(AnkiDroidApp.getSharedPrefs(this).getBoolean("invertedColors", false))
//                .withOnCheckedChangeListener(this).withSelectable(false)
//                .withIcon(R.drawable.ic_brightness_3_black_24dp).withIdentifier(DRAWER_NIGHT_MODE).withIconTintingEnabled(true);
//        SecondaryDrawerItem settingsItem = new SecondaryDrawerItem().withName(R.string.settings)
//                .withIcon(R.drawable.ic_settings_black_24dp).withIdentifier(DRAWER_SETTINGS).withIconTintingEnabled(true);
//        SecondaryDrawerItem helpItem = new SecondaryDrawerItem().withName(R.string.help)
//                .withIcon(R.drawable.ic_help_black_24dp).withIdentifier(DRAWER_HELP).withIconTintingEnabled(true);
//        SecondaryDrawerItem feedbackItem = new SecondaryDrawerItem().withName(R.string.send_feedback)
//                .withIcon(R.drawable.ic_feedback_black_24dp).withIdentifier(DRAWER_FEEDBACK).withIconTintingEnabled(true);

        //自定义列表
        PrimaryDrawerItem deckListItem = new PrimaryDrawerItem().withName("牌组列表")
                .withIcon(R.drawable.nav_list).withIdentifier(DRAWER_DECK_PICKER).withIconTintingEnabled(true);
        PrimaryDrawerItem browserItem = new PrimaryDrawerItem().withName("卡片浏览")
                .withIcon(R.drawable.nav_search).withIdentifier(DRAWER_BROWSER).withIconTintingEnabled(true);
        PrimaryDrawerItem readCardItem = new PrimaryDrawerItem().withName("阅读制卡")
                .withIcon(R.drawable.nav_read).withIdentifier(DRAWER_READCARD).withIconTintingEnabled(true);
        PrimaryDrawerItem testCardItem = new PrimaryDrawerItem().withName("模考制卡")
                .withIcon(R.drawable.nav_test).withIdentifier(DRAWER_TESTCARD).withIconTintingEnabled(true);
        PrimaryDrawerItem preferenceItem = new PrimaryDrawerItem().withName("偏好设置")
                .withIcon(R.drawable.nav_setting).withIdentifier(DRAWER_PREFERENCE).withIconTintingEnabled(true);
        PrimaryDrawerItem personalCenterItem = new PrimaryDrawerItem().withName("个人中心")
                .withIcon(R.drawable.nav_person).withIdentifier(DRAWER_PERSONALCENTER).withIconTintingEnabled(true);
        PrimaryDrawerItem statisticItem = new PrimaryDrawerItem().withName("统计分析")
                .withIcon(R.drawable.nav_statistic).withIdentifier(DRAWER_STATISTIC).withIconTintingEnabled(true);
        PrimaryDrawerItem onlineHelpItem = new PrimaryDrawerItem().withName("在线帮助")
                .withIcon(R.drawable.nav_help).withIdentifier(DRAWER_ONLINEHELP).withIconTintingEnabled(true);

        // Create the header if the screen isn't tiny
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//        if (dpHeight > 320 && dpWidth > 320) {
//            mHeader = new AccountHeaderBuilder()
//                    .withActivity(this)
//                    .withHeaderBackground(Themes.getResFromAttr(this, R.attr.navDrawerImage))
//                    .withDividerBelowHeader(false)
//                    .build();
//        }

        //更换头部图片为cbd.jpg
        if (dpHeight > 320 && dpWidth > 320) {
            mHeader = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(getResources().getDrawable(R.drawable.cbd))
                    .withDividerBelowHeader(false)
                    .build();
        }
        // Add the items to the drawer and build it
//        DrawerBuilder builder = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .withAccountHeader(mHeader)
//                .withTranslucentStatusBar(true)
//                .withFullscreen(fullScreen)
//                .addDrawerItems(
//                        deckListItem, browserItem, statsItem,
//                        new DividerDrawerItem(),
//                        nightModeItem, settingsItem, helpItem, feedbackItem
//                )
//                .withOnDrawerNavigationListener(this)
//                .withOnDrawerItemClickListener(this);

//        DividerDrawerItem dividerDrawerItem = new DividerDrawerItem();
//        dividerDrawerItem.getLayoutRes();
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(mHeader)
                .withTranslucentStatusBar(true)
                .withFullscreen(fullScreen)
                .addDrawerItems(
                        deckListItem, new DividerDrawerItem(),  readCardItem,
                        new DividerDrawerItem(), testCardItem, new DividerDrawerItem(),browserItem, new DividerDrawerItem(), preferenceItem,
                        new DividerDrawerItem(), personalCenterItem, new DividerDrawerItem(), statisticItem, new DividerDrawerItem(),
                        onlineHelpItem, new DividerDrawerItem()
                )
                .withOnDrawerNavigationListener(this)
                .withOnDrawerItemClickListener(this);

        mDrawer = builder.build();
    }


    /** Sets selected navigation drawer item */
    protected void selectNavigationItem(int itemId) {
        if (mDrawer != null) {
            mDrawer.setSelection(itemId, false);
            mSelectedItem = itemId;
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }
    }


    /**
     * This function locks the navigation drawer closed in regards to swipes,
     * but continues to allowed it to be opened via it's indicator button. This
     * function in a noop if the drawer hasn't been initialized.
     */
    protected void disableDrawerSwipe() {
        if (mDrawer != null && mDrawer.getDrawerLayout() != null) {
            mDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    /**
     * This function allows swipes to open the navigation drawer. This
     * function in a noop if the drawer hasn't been initialized.
     */
    protected void enableDrawerSwipe() {
        if (mDrawer != null && mDrawer.getDrawerLayout() != null) {
            mDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    /**
     * Enable the workaround that allows the activity to resize when a software keyboard
     * appears. Use this instead of android:windowSoftInputMode="adjustResize" for any
     * activity that extends this class.
     */
    protected void allowResizeForSoftKeyboard() {
        if (mDrawer != null) {
            mDrawer.keyboardSupportEnabled(this, true);
        }
    }


    protected void showBackIcon() {
        if (mDrawer != null) {
            mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(this);
        // Update language
        AnkiDroidApp.setLanguage(preferences.getString(Preferences.LANGUAGE, ""));
        // Restart the activity on preference change
        if (requestCode == REQUEST_PREFERENCES_UPDATE) {
            if (mOldColPath!=null && CollectionHelper.getCurrentAnkiDroidDirectory(this).equals(mOldColPath)) {
                // collection path hasn't been changed so just restart the current activity
                if ((this instanceof Reviewer) && preferences.getBoolean("tts", false)) {
                    // Workaround to kick user back to StudyOptions after opening settings from Reviewer
                    // because onDestroy() of old Activity interferes with TTS in new Activity
                    finishWithoutAnimation();
                } else {
                    restartActivity();
                }
            } else {
                // collection path has changed so kick the user back to the DeckPicker
                CollectionHelper.getInstance().closeCollection(true);
                CompatHelper.getCompat().restartActivityInvalidateBackstack(this);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Get the drawer layout.
     *
     * The drawer layout is the parent layout for activities that use the Navigation Drawer.
     */
    public DrawerLayout getDrawerLayout() {
        if (mDrawer != null) {
            return mDrawer.getDrawerLayout();
        } else {
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            Timber.i("Back key pressed");
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void toOpenDrawer(){
        mDrawer.openDrawer();
    }


    @Override
    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
        if (mSelectedItem == iDrawerItem.getIdentifier()) {
            mDrawer.closeDrawer();
            return true;
        }
//        switch (iDrawerItem.getIdentifier()) {
//            case DRAWER_DECK_PICKER:
//                Intent deckPicker = new Intent(this, DeckPicker.class);
//                deckPicker.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);    // opening DeckPicker should clear back history
//                startActivityWithAnimation(deckPicker, ActivityTransitionAnimation.RIGHT);
//                break;
//            case DRAWER_BROWSER:
//                Intent cardBrowser = new Intent(this, CardBrowser.class);
//                cardBrowser.putExtra("selectedDeck", getCol().getDecks().selected());
//                if (mCurrentCardId >= 0) {
//                    cardBrowser.putExtra("currentCard", mCurrentCardId);
//                }
//                startActivityForResultWithAnimation(cardBrowser, REQUEST_BROWSE_CARDS, ActivityTransitionAnimation.LEFT);
//                break;
//            case DRAWER_STATISTICS:
//                Intent intent = new Intent(this, Statistics.class);
//                intent.putExtra("selectedDeck", getCol().getDecks().selected());
//                startActivityForResultWithAnimation(intent, REQUEST_STATISTICS, ActivityTransitionAnimation.LEFT);
//                break;
//            case DRAWER_SETTINGS:
//                mOldColPath = CollectionHelper.getCurrentAnkiDroidDirectory(this);
//                startActivityForResultWithAnimation(new Intent(this, Preferences.class), REQUEST_PREFERENCES_UPDATE, ActivityTransitionAnimation.FADE);
//                break;
//            case DRAWER_HELP:
//                openUrl(Uri.parse(AnkiDroidApp.getManualUrl()));
//                break;
//            case DRAWER_FEEDBACK:
//                openUrl(Uri.parse(AnkiDroidApp.getFeedbackUrl()));
//                break;
//            default:
//                return false;
//        }

        //自定义
        switch (iDrawerItem.getIdentifier()) {
            case DRAWER_DECK_PICKER:
                Intent deckPicker = new Intent(this, DeckPicker.class);
                deckPicker.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);    // opening DeckPicker should clear back history
                startActivityWithAnimation(deckPicker, ActivityTransitionAnimation.RIGHT);
                break;
//            case DRAWER_BROWSER:
//                Intent cardBrowser = new Intent(this, CardBrowser.class);
//                cardBrowser.putExtra("selectedDeck", getCol().getDecks().selected());
//                if (mCurrentCardId >= 0) {
//                    cardBrowser.putExtra("currentCard", mCurrentCardId);
//                }
//                startActivityForResultWithAnimation(cardBrowser, REQUEST_BROWSE_CARDS, ActivityTransitionAnimation.LEFT);
//                break;
            case DRAWER_BROWSER:
                //卡片浏览页面
                Toast.makeText(this, "Sorry，这个功能还在完善中哦",Toast.LENGTH_SHORT).show();
//                Intent cardBrowser = new Intent(this, CardBrowserActivity.class);
//                cardBrowser.putExtra("selectedDeck", getCol().getDecks().selected());
//                if (mCurrentCardId >= 0) {
//                    cardBrowser.putExtra("currentCard", mCurrentCardId);
//                }
//                startActivityForResultWithAnimation(cardBrowser, REQUEST_BROWSE_CARDS, ActivityTransitionAnimation.LEFT);
                break;
            case DRAWER_READCARD:
                //阅读制卡页面
                Intent intent_readcard = new Intent(NavigationDrawerActivity.this, DeckReader.class);
                intent_readcard.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_readcard);
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case DRAWER_TESTCARD:
                Intent intent_testcard = new Intent(NavigationDrawerActivity.this, DeckTest.class);
                intent_testcard.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_testcard);
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case DRAWER_PREFERENCE:
                Intent intent_preference = new Intent(NavigationDrawerActivity.this, SettingActivity.class);
                intent_preference.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent_preference);
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case DRAWER_PERSONALCENTER:
                //个人中心页面
                Intent intent = new Intent(NavigationDrawerActivity.this, PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                //dx  add
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case DRAWER_STATISTIC:
                //统计分析页面
                Intent intent_statisticsaika = new Intent(NavigationDrawerActivity.this, StatisticsAika.class);
                intent_statisticsaika.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                // dx  add
                startActivity(intent_statisticsaika);

//                Intent intent_statistics = new Intent(this, Statistics.class);
//                intent_statistics.putExtra("selectedDeck", getCol().getDecks().selected());
//                Log.e("NavigationDrawer","getCol().getDecks().selected()>>>>>>" + getCol().getDecks().selected());
//                startActivityForResultWithAnimation(intent_statistics, REQUEST_STATISTICS, ActivityTransitionAnimation.LEFT);

                break;
            case DRAWER_ONLINEHELP:
                Toast.makeText(this, "Sorry，这个功能还在完善中哦",Toast.LENGTH_SHORT).show();
                //暂时跳转至恭喜页面
//                Intent intent_congratulation = new Intent(NavigationDrawerActivity.this, CongratulationActivity.class);
//                intent_congratulation.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent_congratulation);
                break;
            default:
                return false;
        }
        mDrawer.closeDrawer();
        mSelectedItem = iDrawerItem.getIdentifier();
        return true;
    }

    @Override
    public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(this);
        if (preferences.getBoolean("invertedColors", false)) {
            preferences.edit().putBoolean("invertedColors", false).commit();
        } else {
            preferences.edit().putBoolean("invertedColors", true).commit();
        }
        CompatHelper.getCompat().restartActivityInvalidateBackstack(this);
    }

    @Override
    public boolean onNavigationClickListener(View clickedView) {
        finishWithAnimation(ActivityTransitionAnimation.DIALOG_EXIT);
        return true;
    }

    protected void setCurrentCardId(long id) {
        mCurrentCardId = id;
    }

    public boolean isDrawerOpen() {
        if (mDrawer == null) {
            return false;
        }else{
            return mDrawer.isDrawerOpen();
        }
    }
}