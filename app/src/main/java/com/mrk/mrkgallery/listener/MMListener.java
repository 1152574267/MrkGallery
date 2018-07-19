package com.mrk.mrkgallery.listener;

import com.huawei.hiai.vision.visionkit.image.detector.Label;

public interface MMListener {
    void onTaskCompleted(Label label);
}
