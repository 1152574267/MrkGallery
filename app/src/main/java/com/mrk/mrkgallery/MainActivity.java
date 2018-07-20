package com.mrk.mrkgallery;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hiai.vision.common.ConnectionCallback;                //加载连接服务的回调函数
import com.huawei.hiai.vision.common.VisionBase;                        //加载连接服务的静态类
import com.mrk.mrkgallery.adapter.XFragmentPagerAdapter;
import com.mrk.mrkgallery.model.FragmentGenerator;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private static final String TAG = "MainActivity";

//    private static final int REQUEST_IMAGE_TAKE = 100;

    //    private Button btnTake;
//    private ImageView ivImage;
//    private TextView tvLabel;
//    private ProgressDialog dialog;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //private FragmentPagerAdapter fpa;
    private XFragmentPagerAdapter fpa;
//    private Uri fileUri;
//    private Bitmap bmp;
//    private File mediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView();
        initData();

        // 拍照
//        btnTake.setOnClickListener(new Button.OnClickListener() {
//
//            public void onClick(View v) {
//                initDetect();
//
//                // fileUri = getOutputMediaFileUri();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    fileUri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".fileprovider", getOutputMediaFile());
//                } else {
//                    fileUri = Uri.fromFile(getOutputMediaFile());
//                }
//                Log.d(LOG_TAG, "end get uri = " + fileUri);
//
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                startActivityForResult(i, REQUEST_IMAGE_TAKE);
//            }
//        });

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
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_list);
        mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        fpa = new XFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(fpa);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    private void initData() {
        mTabLayout.removeAllTabs();

        for (int i = 0; i < 1; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setIcon(FragmentGenerator.drawableArr[i]).setText(FragmentGenerator.strArr[i]));
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if ((requestCode == REQUEST_IMAGE_TAKE) && resultCode == RESULT_OK) {
//            String imgPath;
//
//            if (requestCode == REQUEST_IMAGE_TAKE) {
//                // imgPath = Environment.getExternalStorageDirectory() + fileUri.getPath();
//                imgPath = mediaFile.getAbsolutePath();
//            }
//            Log.d(TAG, "imgPath = " + imgPath);
//        }
//    }

    /**
     * Create a file Uri for saving an image or video
     */
//    private Uri getOutputMediaFileUri() {
//        Log.d(LOG_TAG, "authority = " + getPackageName() + ".provider");
//        Log.d(LOG_TAG, "getApplicationContext = " + getApplicationContext());
//
//        return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", getOutputMediaFile());
//    }

    /**
     * Create a File for saving an image
     */
//    private File getOutputMediaFile() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LabelDetect");
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(LOG_TAG, "failed to create directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                "IMG_" + timeStamp + ".jpg");
//        Log.d(LOG_TAG, "mediaFile " + mediaFile);
//
//        return mediaFile;
//    }
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

    public void mm() {
       fpa.updateFragment();
    }

}
