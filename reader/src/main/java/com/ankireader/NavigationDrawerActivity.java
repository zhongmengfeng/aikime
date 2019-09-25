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
 * this program.  If not, see <http://www.gnu.com.chaojiyiji/licenses/>.                           *
 ****************************************************************************************/
package com.ankireader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;



public class NavigationDrawerActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener,
        OnCheckedChangeListener, Drawer.OnDrawerNavigationListener {

    /** Navigation Drawer */
    protected CharSequence mTitle;
    protected Boolean mFragmented = false;
    private Drawer mDrawer;
    private AccountHeader mHeader = null;
    // Other members
    private String mOldColPath;
    // Navigation drawer list item entries
    protected static final int DRAWER_DECK_PICKER = 0;
    protected static final int DRAWER_USER_CENTER = 1;
    protected static final int DRAWER_CARDINFO = 2;
//    protected static final int DRAWER_STATISTICS = 2;
//    protected static final int DRAWER_NIGHT_MODE = 3;
//    protected static final int DRAWER_SETTINGS = 4;
//    protected static final int DRAWER_HELP = 5;
//    protected static final int DRAWER_FEEDBACK = 6;
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
//        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);

//        if (toolbar != null) {
//            try {
//                setSupportActionBar(toolbar);
//            } catch (RuntimeException e) {
//                Timber.e("Error setting toolbar as support actionbar");
//                AnkiDroidApp.sendExceptionReport(e, "Samsung device error using Toolbar");
//            }
//        }
        // Create the items for the navigation drawer
        PrimaryDrawerItem deckListItem = new PrimaryDrawerItem().withName(R.string.decks)
                .withIcon(R.drawable.ic_list_library_tags).withIdentifier(DRAWER_DECK_PICKER).withIconTintingEnabled(true);
        PrimaryDrawerItem userCenter = new PrimaryDrawerItem().withName(R.string.user_center)
                .withIcon(R.drawable.ic_list_library_author).withIdentifier(DRAWER_USER_CENTER).withIconTintingEnabled(true);
        PrimaryDrawerItem cardInfo = new PrimaryDrawerItem().withName(R.string.card_info)
                .withIcon(R.drawable.ic_list_library_books).withIdentifier(DRAWER_CARDINFO).withIconTintingEnabled(true);
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
        // Create the header if the screen isn't tiny
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpHeight > 320 && dpWidth > 320) {
            mHeader = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(getResources().getDrawable(R.drawable.nav_drawer_logo))
                    .withDividerBelowHeader(false)
                    .build();
        }
        // Add the items to the drawer and build it
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(this)
//                .withToolbar(toolbar)
                .withAccountHeader(mHeader)
                .withTranslucentStatusBar(true)
                .withFullscreen(fullScreen)
                .addDrawerItems(
                        deckListItem, userCenter, cardInfo
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
//        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(this);
//        // Update language
//        AnkiDroidApp.setLanguage(preferences.getString(Preferences.LANGUAGE, ""));
//        // Restart the activity on preference change
//        if (requestCode == REQUEST_PREFERENCES_UPDATE) {
//            if (mOldColPath!=null && CollectionHelper.getCurrentAnkiDroidDirectory(this).equals(mOldColPath)) {
//                // collection path hasn't been changed so just restart the current activity
//                if ((this instanceof Reviewer) && preferences.getBoolean("tts", false)) {
//                    // Workaround to kick user back to StudyOptions after opening settings from Reviewer
//                    // because onDestroy() of old Activity interferes with TTS in new Activity
//                    finishWithoutAnimation();
//                } else {
//                    restartActivity();
//                }
//            } else {
//                // collection path has changed so kick the user back to the DeckPicker
//                CollectionHelper.getInstance().closeCollection(true);
//                CompatHelper.getCompat().restartActivityInvalidateBackstack(this);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
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
//            Timber.i("Back key pressed");
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
        switch (iDrawerItem.getIdentifier()) {
            case DRAWER_DECK_PICKER:
//                Intent deckPicker = new Intent(this, DeckPicker.class);
//                deckPicker.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);    // opening DeckPicker should clear back history
//                startActivityWithAnimation(deckPicker, ActivityTransitionAnimation.RIGHT);
                break;
            case DRAWER_USER_CENTER:
                Intent intent = new Intent(NavigationDrawerActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
                break;
            case DRAWER_CARDINFO:
                break;

//            case DRAWER_BROWSER:
////                Intent cardBrowser = new Intent(this, CardBrowser.class);
////                cardBrowser.putExtra("selectedDeck", getCol().getDecks().selected());
////                if (mCurrentCardId >= 0) {
////                    cardBrowser.putExtra("currentCard", mCurrentCardId);
////                }
////                startActivityForResultWithAnimation(cardBrowser, REQUEST_BROWSE_CARDS, ActivityTransitionAnimation.LEFT);
//                break;
//            case DRAWER_STATISTICS:
////                Intent intent = new Intent(this, Statistics.class);
////                intent.putExtra("selectedDeck", getCol().getDecks().selected());
////                startActivityForResultWithAnimation(intent, REQUEST_STATISTICS, ActivityTransitionAnimation.LEFT);
//                break;
//            case DRAWER_SETTINGS:
////                mOldColPath = CollectionHelper.getCurrentAnkiDroidDirectory(this);
////                startActivityForResultWithAnimation(new Intent(this, Preferences.class), REQUEST_PREFERENCES_UPDATE, ActivityTransitionAnimation.FADE);
//                break;
//            case DRAWER_HELP:
////                openUrl(Uri.parse(AnkiDroidApp.getManualUrl()));
//                break;
//            case DRAWER_FEEDBACK:
////                openUrl(Uri.parse(AnkiDroidApp.getFeedbackUrl()));
//                break;
            default:
                return false;
        }
        mDrawer.closeDrawer();
        mSelectedItem = iDrawerItem.getIdentifier();
        return true;
    }

    @Override
    public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
//        SharedPreferences preferences = AnkiDroidApp.getSharedPrefs(this);
//        if (preferences.getBoolean("invertedColors", false)) {
//            preferences.edit().putBoolean("invertedColors", false).commit();
//        } else {
//            preferences.edit().putBoolean("invertedColors", true).commit();
//        }
//        CompatHelper.getCompat().restartActivityInvalidateBackstack(this);
    }

    @Override
    public boolean onNavigationClickListener(View clickedView) {
//        finishWithAnimation(ActivityTransitionAnimation.DIALOG_EXIT);
        return true;
    }

    protected void setCurrentCardId(long id) {
        mCurrentCardId = id;
    }

    public boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen();
    }
}