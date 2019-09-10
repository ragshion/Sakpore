package com.ragshion.sakpore.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.jaeger.library.StatusBarUtil;
import com.ragshion.sakpore.R;
import com.ragshion.sakpore.adapter.MenuAdapter;
import com.ragshion.sakpore.objek.MenuData;
import com.ragshion.sakpore.utilities.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout;
    RecyclerView mRecyclerView;
    List<MenuData> mMenu;
    MenuData mMenuData;
    MenuAdapter menuAdapter;
    GridLayoutManager mGridLayoutManager;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StatusBarUtil.setTranslucent(this, 0);
//        StatusBarUtil.setTransparent(this);

        Toolbar toolbar = findViewById(R.id.footer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        sharedPrefManager = new SharedPrefManager(this);

        createSlider();
        createMenuIcon();

        mRecyclerView = findViewById(R.id.recyclerview_kelas);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        menuAdapter = new MenuAdapter(this, mMenu);
        mRecyclerView.setAdapter(menuAdapter);
        mRecyclerView.setFocusable(false);

        NestedScrollView nestedScrollView = findViewById(R.id.mainScrollview);
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);

    }

    void createSlider(){
        sliderLayout = findViewById(R.id.imageslider);

        HashMap<String,String> url_maps = new HashMap<String, String>();

        url_maps.put(" ", getResources().getString(R.string.pathcarousel)+"carousel/1.jpg");
        url_maps.put("  ", getResources().getString(R.string.pathcarousel)+"carousel/2.jpg");
        url_maps.put("", getResources().getString(R.string.pathcarousel)+"carousel/3.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);

            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
//                    .setOnSliderClickListener(MainActivity.this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }

        // you can change animasi, time page and anythink.. read more on github
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        sliderLayout.startAutoCycle();
    }

    public void createMenuIcon(){
        mMenu = new ArrayList<>();
        mMenuData = new MenuData("Kuliner", R.drawable.ic_kuliner);
        mMenu.add(mMenuData);
        mMenuData = new MenuData("Jejamu", R.drawable.ic_jejamu);
        mMenu.add(mMenuData);
        mMenuData = new MenuData("Kelontong", R.drawable.ic_kelontong);
        mMenu.add(mMenuData);
        mMenuData = new MenuData("Tentang App", R.drawable.ic_info_outline_black_24dp);
        mMenu.add(mMenuData);
        mMenuData = new MenuData("Whatsapp", R.drawable.ic_whatsapp);
        mMenu.add(mMenuData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 12){
            this.recreate();
        }
    }


    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
