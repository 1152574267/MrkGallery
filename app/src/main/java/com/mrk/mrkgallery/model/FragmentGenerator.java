package com.mrk.mrkgallery.model;

import android.support.v4.app.Fragment;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.view.LabelDetectFragment;
import com.mrk.mrkgallery.view.PhotoFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentGenerator {
    public static int[] strArr = new int[]{R.string.tv_image_label_detect, R.string.tv_123};
    public static int[] drawableArr = new int[]{R.drawable.ic_launcher, R.drawable.ic_launcher};

    public static List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new PhotoFragment());
        fragmentList.add(new LabelDetectFragment());

        return fragmentList;
    }

}
