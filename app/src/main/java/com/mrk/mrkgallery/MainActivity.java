package com.mrk.mrkgallery;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hiai.vision.common.ConnectionCallback;                //加载连接服务的回调函数
import com.huawei.hiai.vision.common.VisionBase;                        //加载连接服务的静态类
import com.mrk.mrkgallery.adapter.XFragmentPagerAdapter;
import com.mrk.mrkgallery.model.FragmentGenerator;
import com.mrk.mrkgallery.util.TfAIUtil;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter fpa;

    private int moduleIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView();
        initData();

        // 应用VisionBase静态类进行初始化，异步拿到服务的连接
        // To connect HiAi Engine service using VisionBase
        VisionBase.init(MainActivity.this, new ConnectionCallback() {

            /**
             * 可以在这里进行detector类的初始化、标记服务连接状态等
             * */
            @Override
            public void onServiceConnect() {
                //This callback method is called when the connection to the service is successful.
                //Here you can initialize the detector class, mark the service connection status, and more.
                Log.i(TAG, "onServiceConnect ");
            }

            /**
             * 可以选择在这里进行服务的重连，或者对异常进行处理
             * */
            @Override
            public void onServiceDisconnect() {
                //This callback method is called when disconnected from the service.
                //You can choose to reconnect here or to handle exceptions.
                Log.i(TAG, "onServiceDisconnect");
            }
        });
    }

    private void initView() {
        moduleIndex = TfAIUtil.MODULE_SCENE_DETECT;
        Intent i = getIntent();
        if (i != null) {
            moduleIndex = i.getIntExtra("module_index", TfAIUtil.MODULE_SCENE_DETECT);
        }

        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_list);
        mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        fpa = new XFragmentPagerAdapter(getSupportFragmentManager(), moduleIndex);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(fpa);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    private void initData() {
        mTabLayout.removeAllTabs();

//        for (int i = 0; i < 1; i++) {
        mTabLayout.addTab(mTabLayout.newTab().setIcon(FragmentGenerator.drawableArr[moduleIndex]).setText(FragmentGenerator.strArr[moduleIndex]));
//        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        onTabItemSelected(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void onTabItemSelected(int position) {
        Fragment fragment = fpa.getItem(position);
    }

}
