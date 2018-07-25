package com.mrk.mrkgallery.model;

import android.support.v4.app.Fragment;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.view.LabelListFragment;
import com.mrk.mrkgallery.view.SceneListFragment;
import com.mrk.mrkgallery.view.TFListFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentGenerator {
    public static int[] strArr = new int[]{R.string.tv_image_scene_detect, R.string.tv_image_label_detect, R.string.tv_image_scene_detect};
    public static int[] drawableArr = new int[]{R.drawable.img_tab_icon, R.drawable.img_tab_icon, R.drawable.img_tab_icon};

    public static List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new SceneListFragment());
        fragmentList.add(new LabelListFragment());
        fragmentList.add(new TFListFragment());

        return fragmentList;
    }

}
