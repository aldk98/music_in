package com.example.aldrian.musicin2firebase.musician;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.pagerAdapters.MusicianFragmentPagerAdapter;

public class MusicianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musician);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MusicianFragmentPagerAdapter adapter = new MusicianFragmentPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }
}
