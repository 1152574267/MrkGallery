package com.mrk.mrkgallery.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mrk.mrkgallery.MainActivity;
import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.util.DbHelper;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mSceneDetect;
    private RelativeLayout mLableDetect;
    private LinearLayout mObjectDetect;
    private LinearLayout mMnistDetect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_normal_home);

        initView();
    }

    private void initView() {
        mSceneDetect = (LinearLayout) findViewById(R.id.ll_home_scene_detect);
        mLableDetect = (RelativeLayout) findViewById(R.id.rl_home_label_detect);
        mObjectDetect = (LinearLayout) findViewById(R.id.ll_home_object_detect);
        mMnistDetect = (LinearLayout) findViewById(R.id.ll_home_mnist_detect);

        mSceneDetect.setOnClickListener(this);
        mLableDetect.setOnClickListener(this);
        mObjectDetect.setOnClickListener(this);
        mMnistDetect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.ll_home_scene_detect:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("module_index", DbHelper.MODULE_SCENE_DETECT);
                break;
            case R.id.rl_home_label_detect:
                intent.setClass(this, MainActivity.class);
                intent.putExtra("module_index", DbHelper.MODULE_LABEL_DETECT);
                break;
            case R.id.ll_home_object_detect:
                intent.setClass(this, TFDetectActivity.class);
                break;
            case R.id.ll_home_mnist_detect:
                intent.setClass(this, MnistDetectActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
