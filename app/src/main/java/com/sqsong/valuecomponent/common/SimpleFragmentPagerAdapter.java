package com.sqsong.valuecomponent.common;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class SimpleFragmentPagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> mFragments;
    private List<String> mTitleList;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<T> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitleList = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList == null || position < 0 || position > mTitleList.size() - 1)
            return super.getPageTitle(position);

        return mTitleList.get(position);
    }
}
