package com.mrk.mrkgallery.listener;

import com.huawei.hiai.vision.visionkit.image.detector.Label;

/**
 * Created by huarong on 2018/2/26.
 */
public interface MMListener {
    void onTaskCompleted(Label label);
}