package com.mrk.mrkgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mrk.mrkgallery.model.FragmentGenerator;

import java.util.List;

public class XFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public XFragmentPagerAdapter(FragmentManager fm, int moduleIndex) {
        super(fm);

        mFragmentList = FragmentGenerator.getFragmentList(moduleIndex);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList.get(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
