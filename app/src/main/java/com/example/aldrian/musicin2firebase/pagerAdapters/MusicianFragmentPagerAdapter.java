package com.example.aldrian.musicin2firebase.pagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.aldrian.musicin2firebase.fragments.CurrentFragment;
import com.example.aldrian.musicin2firebase.fragments.MusicianFindJobFragment;
import com.example.aldrian.musicin2firebase.fragments.InboxFragment;
import com.example.aldrian.musicin2firebase.fragments.ProfileFragment;

/**
 * Created by Tommy on 11/12/17.
 */

public class MusicianFragmentPagerAdapter extends FragmentPagerAdapter {

    public MusicianFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MusicianFindJobFragment();
        } else if (position == 1){
            return new CurrentFragment();
        } else if (position==2) {
            return new InboxFragment();
        } else {
            return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}
