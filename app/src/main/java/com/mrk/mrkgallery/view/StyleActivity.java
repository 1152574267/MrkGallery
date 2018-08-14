package com.mrk.mrkgallery.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.adapter.StyleRecyclerViewAdapter;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.decoration.MyDecoration;
import com.mrk.mrkgallery.util.TfAIUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class StyleActivity extends AppCompatActivity implements
        StyleRecyclerViewAdapter.OnItemClickListener, StyleRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = StyleActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private StyleRecyclerViewAdapter<PhotoItem> mAdapter;
    private CompositeDisposable mDisposables;
    private DisposableSubscriber<PhotoItem> mSubscriber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        mDisposables = new CompositeDisposable();
        mSubscriber = new DisposableSubscriber<PhotoItem>() {

            @Override
            public void onNext(PhotoItem photoItem) {
                if (mAdapter != null) {
                    mAdapter.addItem(photoItem);
                }
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

        mRecyclerView = (RecyclerView) findViewById(R.id.tablist);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        mAdapter = new StyleRecyclerViewAdapter<PhotoItem>(this, photoList);
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

        if (null != mDisposables) {
            mDisposables.clear();
        }
    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(this, "onItemClick: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {
//        Toast.makeText(this, "onItemLongClick: " + position, Toast.LENGTH_SHORT).show();
    }

    public void startAsyncTask() {
        mDisposables.clear();

        Flowable.create(new FlowableOnSubscribe<PhotoItem>() {

            @Override
            public void subscribe(@NonNull FlowableEmitter<PhotoItem> emitter) throws Exception {
                for (int i = 0; i < TfAIUtil.styleResId.length; i++) {
                    PhotoItem item = new PhotoItem();
                    item.setPhotoResId(TfAIUtil.styleResId[i]);
                    emitter.onNext(item);
                }
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);

        mDisposables.add(mSubscriber);
    }

}
