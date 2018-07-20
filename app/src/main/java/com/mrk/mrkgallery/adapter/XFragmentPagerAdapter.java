package com.mrk.mrkgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.mrk.mrkgallery.model.FragmentGenerator;
import com.mrk.mrkgallery.view.LabelDetectFragment;
import com.mrk.mrkgallery.view.SceneDetectFragment;

import java.util.List;

public class XFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private FragmentManager fm;

    public XFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        this.fm = fm;
        mFragmentList = FragmentGenerator.getFragmentList();
    }

    public void updateFragment() {

        mFragmentList.clear();
        mFragmentList.add(new SceneDetectFragment());
        //mFragmentList.add(new LabelDetectFragment());

        notifyDataSetChanged();
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

    @Override

    public int getItemPosition(Object object) {

        // TODO Auto-generated method stub

        return PagerAdapter.POSITION_NONE;

    }

}
