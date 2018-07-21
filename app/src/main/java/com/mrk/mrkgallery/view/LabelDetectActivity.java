package com.mrk.mrkgallery.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hiai.vision.image.detector.LabelDetector;
import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.adapter.MRecyclerViewAdapter;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.decoration.MyDecoration;
import com.mrk.mrkgallery.util.DbHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LabelDetectActivity extends AppCompatActivity implements
        MRecyclerViewAdapter.OnItemClickListener, MRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = LabelDetectActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MRecyclerViewAdapter<PhotoItem> mAdapter;
    private LabelDetector labelDetector;

    private String labelType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo);

        Intent intent = getIntent();
        if (intent != null) {
            labelType = intent.getStringExtra("label_type");
        }

        DbHelper.initLabelContents();
        Log.i(TAG, "init LabelDetector");
        // 定义detector实例，将此工程的Context当做入参
        labelDetector = new LabelDetector(this);
        Log.i(TAG, "start to get label");

        mRecyclerView = (RecyclerView) findViewById(R.id.tablist);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        mAdapter = new MRecyclerViewAdapter<PhotoItem>(this, photoList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startAsyncTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        if (null != labelDetector) {
            labelDetector.release();
        }
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "onItemClick: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {
        Toast.makeText(this, "onItemLongClick: " + position, Toast.LENGTH_SHORT).show();
    }

    public void startAsyncTask() {
        Flowable.create(new FlowableOnSubscribe<PhotoItem>() {

            @Override
            public void subscribe(@NonNull FlowableEmitter<PhotoItem> emitter) throws Exception {
                List<PhotoItem> photoItems = DbHelper.getPhotoList(LabelDetectActivity.this);
                Log.d(TAG, "subscribe: " + photoItems.size());

                for (int i = 0; i < photoItems.size(); i++) {
                    emitter.onNext(photoItems.get(i));
                }
            }
        }, BackpressureStrategy.BUFFER)
                //.delay(1, TimeUnit.SECONDS)
                .map(new Function<PhotoItem, PhotoItem>() {

                    @Override
                    public PhotoItem apply(@NonNull PhotoItem photoItem) throws Exception {
                        /********************** 图片分类检测 ***********************/
                        String detectLabel = DbHelper.getDetectLabel(photoItem.getPhotoPath(), labelDetector);
                        photoItem.setPhotoName(detectLabel);
                        /********************** 图片分类检测************************/

                        return photoItem;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhotoItem>() {

                    @Override
                    public void accept(PhotoItem photoItem) throws Exception {
                        String name = photoItem.getPhotoName();
                        Log.d(TAG, "labelType: " + labelType + ", name: " + name);
                        if (!TextUtils.isEmpty(labelType) && name.equals(labelType)) {
                            mAdapter.addItem(photoItem);
                        }
                        // mPhotoView.scrollToPosition(0);
                    }
                });
    }

}
