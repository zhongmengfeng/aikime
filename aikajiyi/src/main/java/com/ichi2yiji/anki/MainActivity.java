package com.ichi2yiji.anki;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.fragment.DeckPickerFragment;
import com.ichi2yiji.anki.fragment.DeckReaderFragment;
import com.ichi2yiji.anki.fragment.DeckTestFragment;
import com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem;
import com.ichi2yiji.anki.toprightandbottomrightmenu.TopRightMenu;
import com.ichi2yiji.anki.util.ApplyTranslucency;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private ImageView topRightMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_main);
        ApplyTranslucency.applyKitKatTranslucency(this);

//        DeckPickerFragment pickerFragment = new DeckPickerFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_main, pickerFragment);
//        fragmentTransaction.commit();

        SegmentedGroup segmented = (SegmentedGroup)findViewById(R.id.segmented);
        segmented.setTintColor(Color.WHITE, Color.parseColor("#007aff"));
        segmented.setOnCheckedChangeListener(this);

        topRightMenu = (ImageView)findViewById(R.id.top_right);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.deck_reader:

                DeckReaderFragment readerFragment = new DeckReaderFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_main, readerFragment);
                ft.commit();

                /*FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.fragment_main, readerFragment);
                ft1.commit();*/

                topRightMenu.setImageResource(R.drawable.question);
                break;
            case R.id.deck_picker:

                DeckPickerFragment pickerFragment = new DeckPickerFragment();

                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.fragment_main, pickerFragment);
                ft1.commit();


                /*FragmentManager fm2 = getFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.fragment_main, pickerFragment);
                ft2.commit();*/

                topRightMenu.setImageResource(R.drawable.top_right);
                topRightMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TopRightMenu mTopRightMenu = new TopRightMenu(MainActivity.this);

                        //添加菜单项
                        List<MenuItem> menuItems = new ArrayList<>();
                        menuItems.add(new MenuItem("筛选牌组"));
                        menuItems.add(new MenuItem("检查数据"));
                        menuItems.add(new MenuItem("检查媒体"));
                        menuItems.add(new MenuItem("新建牌组"));
                        menuItems.add(new MenuItem("添加笔记"));

                        mTopRightMenu
                                .setHeight(450)     //默认高度480
                                .setWidth(190)      //默认宽度wrap_content
                                .showIcon(false)     //显示菜单图标，默认为true
                                .dimBackground(true)        //背景变暗，默认为true
                                .needAnimationStyle(true)   //显示动画，默认为true
                                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                                .addMenuList(menuItems)
                                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                                    @Override
                                    public void onMenuItemClick(int position) {
                                        Toast.makeText(MainActivity.this, "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .showAsDropDown(topRightMenu, -120, 20);


                    }
                });
                break;
            case R.id.deck_test:

                DeckTestFragment testFragment = new DeckTestFragment();

                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.fragment_main, testFragment);
                ft2.commit();


                /*FragmentManager fm3 = getFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                ft3.replace(R.id.fragment_main, testFragment);
                ft3.commit();*/
                topRightMenu.setImageResource(R.drawable.question);
                break;

            default:
        }
    }

}
