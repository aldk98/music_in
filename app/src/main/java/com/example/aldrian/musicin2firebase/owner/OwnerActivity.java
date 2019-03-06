package com.example.aldrian.musicin2firebase.owner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.example.aldrian.musicin2firebase.R;
import com.example.aldrian.musicin2firebase.pagerAdapters.OwnerFragmentPagerAdapter;

public class OwnerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OwnerFragmentPagerAdapter mOwnerFragmentPagerAdapter = new OwnerFragmentPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mOwnerFragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }
}
