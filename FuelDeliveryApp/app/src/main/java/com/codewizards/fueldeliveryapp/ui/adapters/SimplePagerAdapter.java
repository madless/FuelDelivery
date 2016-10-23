package com.codewizards.fueldeliveryapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;

import java.util.List;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {
    List<String> tabTitles;
    List<BaseTabFragment> fragments;

    public SimplePagerAdapter(FragmentManager fm, List<BaseTabFragment> fragments, List<String> tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    public void clearAll() {
        fragments.clear();
    }
}
