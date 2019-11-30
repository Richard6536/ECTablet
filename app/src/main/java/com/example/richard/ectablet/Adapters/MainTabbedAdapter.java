package com.example.richard.ectablet.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.richard.ectablet.Fragments.d.BatteryFragment;
import com.example.richard.ectablet.Fragments.d.MapFragment;

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
