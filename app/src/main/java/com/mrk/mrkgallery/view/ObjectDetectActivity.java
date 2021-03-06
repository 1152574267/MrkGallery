package com.mrk.mrkgallery.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.adapter.MRecyclerViewAdapter;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.decoration.MyDecoration;
import com.mrk.mrkgallery.tfai.Classifier;
import com.mrk.mrkgallery.tfai.TensorFlowImageClassifier;
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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ObjectDetectActivity extends AppCompatActivity implements
        MRecyclerViewAdapter.OnItemClickListener, MRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = ObjectDetectActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MRecyclerViewAdapter<PhotoItem> mAdapter;
    private Classifier mClassifier;
    private CompositeDisposable mDisposables;
    private DisposableSubscriber<PhotoItem> mSubscriber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo);

        mDisposables = new CompositeDisposable();
        mSubscriber = new DisposableSubscriber<PhotoItem>() {

            @Override
            public void onNext(PhotoItem photoItem) {
                String category = photoItem.getPhotoCategory();
                Log.d(TAG, "onNext - category: " + category);

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

        mClassifier = TensorFlowImageClassifier.create(getAssets(),
                TfAIUtil.MODEL_FILE,
                TfAIUtil.LABEL_FILE,
                TfAIUtil.INPUT_SIZE,
                TfAIUtil.IMAGE_MEAN,
                TfAIUtil.IMAGE_STD,
                TfAIUtil.INPUT_NAME,
                TfAIUtil.OUTPUT_NAME);

        mRecyclerView = (RecyclerView) findViewById(R.id.tablist);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        mAdapter = new MRecyclerViewAdapter<PhotoItem>(this, photoList, TfAIUtil.MODULE_TF_DETECT);
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

        if (null != mClassifier) {
            mClassifier.close();
        }
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
                List<PhotoItem> photoItems = TfAIUtil.getPhotoList(ObjectDetectActivity.this);
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
                        /********************** TF图像识别分类 ***********************/
                        String tfType = TfAIUtil.startImageClassifier(photoItem.getPhotoPath(), mClassifier);
                        photoItem.setPhotoCategory(tfType);
                        /********************** TF图像识别分类 ************************/

                        return photoItem;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);

        mDisposables.add(mSubscriber);
    }

}
