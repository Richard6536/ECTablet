package com.example.richard.ectablet.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.richard.ectablet.Fragments.BatteryFragment;
import com.example.richard.ectablet.Fragments.MapFragment;

public class MainTabbedAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MainTabbedAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            case 1:
                BatteryFragment batteryFragment = new BatteryFragment();
                return batteryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
