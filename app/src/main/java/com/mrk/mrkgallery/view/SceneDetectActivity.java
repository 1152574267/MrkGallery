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

import com.huawei.hiai.vision.image.detector.SceneDetector;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SceneDetectActivity extends AppCompatActivity implements
        MRecyclerViewAdapter.OnItemClickListener, MRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = SceneDetectActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MRecyclerViewAdapter<PhotoItem> mAdapter;
    private SceneDetector sceneDetector;
    private CompositeDisposable mDisposables;
    private DisposableSubscriber<PhotoItem> mSubscriber;

    private String sceneType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo);

        Intent intent = getIntent();
        if (intent != null) {
            sceneType = intent.getStringExtra("scene_type");
        }

        mDisposables = new CompositeDisposable();
        mSubscriber = new DisposableSubscriber<PhotoItem>() {

            @Override
            public void onNext(PhotoItem photoItem) {
                Log.d(TAG, "onNext");

                String category = photoItem.getPhotoCategory();
                Log.d(TAG, "sceneType: " + sceneType + ", category: " + category);
                if (!TextUtils.isEmpty(sceneType) && category.equals(sceneType)) {
                    mAdapter.addItem(photoItem);
                }
                // mPhotoView.scrollToPosition(0);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        DbHelper.initSceneContents();
        sceneDetector = new SceneDetector(this);

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

        if (null != sceneDetector) {
            sceneDetector.release();
        }
        if (null != mDisposables) {
            mDisposables.clear();
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
        mDisposables.clear();

        Flowable.create(new FlowableOnSubscribe<PhotoItem>() {

            @Override
            public void subscribe(@NonNull FlowableEmitter<PhotoItem> emitter) throws Exception {
                List<PhotoItem> photoItems = DbHelper.getPhotoList(SceneDetectActivity.this);
                Log.d(TAG, "subscribe: " + photoItems.size());

                for (int i = 0; i < photoItems.size(); i++) {
                    emitter.onNext(photoItems.get(i));
                }
            }
        }, BackpressureStrategy.BUFFER)
                .map(new Function<PhotoItem, PhotoItem>() {

                    @Override
                    public PhotoItem apply(@NonNull PhotoItem photoItem) throws Exception {
                        /********************** 场景检测 ***************************/
                        String sceneType = DbHelper.getSceneType(photoItem.getPhotoPath(), sceneDetector);
                        photoItem.setPhotoCategory(sceneType);
                        /********************** 场景检测 ***************************/

                        return photoItem;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);

        mDisposables.add(mSubscriber);
    }

}
