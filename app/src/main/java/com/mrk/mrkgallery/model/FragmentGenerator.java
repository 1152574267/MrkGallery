package com.mrk.mrkgallery.model;

import android.support.v4.app.Fragment;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.util.DbHelper;
import com.mrk.mrkgallery.view.LabelListFragment;
import com.mrk.mrkgallery.view.SceneListFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentGenerator {
    public static int[] strArr = new int[]{R.string.tv_image_scene_detect, R.string.tv_image_label_detect, R.string.tv_image_object_detect, R.string.tv_image_mnist_detect};
    public static int[] drawableArr = new int[]{R.drawable.icon_app, R.drawable.img_tab_icon, R.drawable.img_tab_icon, R.drawable.icon_app};

    public static List<Fragment> getFragmentList(int moduleIndex) {
        List<Fragment> fragmentList = new ArrayList<Fragment>();

        if (moduleIndex == DbHelper.MODULE_SCENE_DETECT) {
            fragmentList.add(new SceneListFragment());
        } else if (moduleIndex == DbHelper.MODULE_LABEL_DETECT) {
            fragmentList.add(new LabelListFragment());
        }

        return fragmentList;
    }

}
