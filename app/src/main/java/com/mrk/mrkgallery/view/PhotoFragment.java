package com.mrk.mrkgallery.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.hiai.vision.image.detector.LabelDetector;
import com.huawei.hiai.vision.visionkit.common.Frame;
import com.huawei.hiai.vision.visionkit.image.detector.Label;
import com.huawei.hiai.vision.visionkit.image.detector.LabelContent;    //加载标签检测内容类
import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.adapter.MRecyclerViewAdapter;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.decoration.MyDecoration;
import com.mrk.mrkgallery.listener.MMListener;
import com.mrk.mrkgallery.task.LabelDetectTask;
import com.mrk.mrkgallery.util.DbHelper;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

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

public class PhotoFragment extends Fragment implements MMListener,
        MRecyclerViewAdapter.OnItemClickListener, MRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = PhotoFragment.class.getSimpleName();

    private Context mContext;
    private RecyclerView mPhotoView;
    private MRecyclerViewAdapter<PhotoItem> mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        mAdapter = new MRecyclerViewAdapter<PhotoItem>(mContext, photoList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        mPhotoView = (RecyclerView) view.findViewById(R.id.tablist);
//        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mPhotoView.setHasFixedSize(true);
        mPhotoView.setLayoutManager(layoutManager);
        mPhotoView.addItemDecoration(new MyDecoration(mContext, MyDecoration.HORIZONTAL_LIST));
        mPhotoView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        startAsyncTask();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        startAsyncTask();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mContext = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint isVisibleToUser: " + isVisibleToUser);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(mContext, "onItemClick: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {
        Toast.makeText(mContext, "onItemLongClick: " + position, Toast.LENGTH_SHORT).show();
    }

    public void startAsyncTask() {
        DbHelper.initLabelContents();

        Flowable.create(new FlowableOnSubscribe<PhotoItem>() {

            @Override
            public void subscribe(@NonNull FlowableEmitter<PhotoItem> emitter) throws Exception {
                List<PhotoItem> photoItems = DbHelper.getPhotoList(mContext);
                Log.d(TAG, "subscribe: " + photoItems.size());

                for (int i = 0; i < photoItems.size(); i++) {
                    emitter.onNext(photoItems.get(i));
                }
            }
        }, BackpressureStrategy.BUFFER)
                .flatMap(new Function<PhotoItem, Publisher<PhotoItem>>() {

                    @Override
                    public Publisher<PhotoItem> apply(@NonNull PhotoItem photoItem) throws Exception {
                        Bitmap bmp = BitmapFactory.decodeFile(photoItem.getPhotoPath());

                        Log.i(TAG, "init LabelDetector");
                        // 定义detector实例，将此工程的Context当做入参
                        labelDetector = new LabelDetector(mContext);
                        Log.i(TAG, "start to get label");

                        startTime = System.currentTimeMillis();
                        Label result_label = getLabel(bmp);
                        endTime = System.currentTimeMillis();
                        Log.i(TAG, String.format("labeldetect whole time: %d ms", endTime - startTime));

                        //release engine after detect finished
                        labelDetector.release();

                        if (result_label == null) {
                            photoItem.setPhotoName("not get label");
                        } else {
                            String strLabel = "category: ";
                            int categoryID = result_label.getCategory();
                            if (categoryID < 0 || categoryID >= DbHelper.LABEL_CATEGORYS.length) {
                                strLabel += "Others";
                            } else {
                                strLabel += DbHelper.LABEL_CATEGORYS[categoryID];
                            }
                            strLabel += ", probability: " + String.valueOf(result_label.getCategoryProbability()) + "\n";

                            List<LabelContent> labelContents = result_label.getLabelContent();
                            for (LabelContent labelContent : labelContents) {
                                strLabel += "labelContent: ";
                                int labelContentID = labelContent.getLabelId();
                                String name = DbHelper.LABEL_CONTENTS.get(labelContentID);
                                if (name == null) {
                                    strLabel += "other";
                                } else {
                                    strLabel += name;
                                }
                                strLabel += ", probability: " + String.valueOf(labelContent.getProbability()) + "\n";
                            }
                            photoItem.setPhotoName(strLabel);
                        }

                        return photoItem;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PhotoItem>() {

                    @Override
                    public void accept(PhotoItem photoItem) throws Exception {
                        mAdapter.addItem(photoItem);
                        mPhotoView.scrollToPosition(0);
                    }
                });
    }

    private LabelDetector labelDetector;
    private long startTime;
    private long endTime;

    public Label getLabel(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "bitmap is null ");
            return null;
        }

        // 定义frame
        Frame frame = new Frame();
        // 将需进行图片分类标签图像的bitmap放入frame中
        frame.setBitmap(bitmap);
        Log.d(TAG, "runVisionService " + "start get label");

        /**
         * 调用detect方法得到图片分类标签检测结果
         *
         * null: 表示同步处理
         * 非null: 即回调函数接口对象，表示异步处理，用于异步返回结果, 目前暂不支持异步处理
         * */
        JSONObject jsonObject = labelDetector.detect(frame, null);
        /**
         * 通过convertResult将json字符串转为java类的形式（您也可以自己解析json字符串）
         *
         * label: 标签结果
         * category: 图片类别
         * categoryProbability: 图片类别置信度
         * labelContents: 标签列表
         * labelId: 标签ID
         * probability: 标签置信度，范围：-1及 0~1，-1表示算法未提供置信度
         *
         * 类别ID
         * 0 - 人像, 1 - 美食, 2 - 风景, 3 - 文档, 4 - 节日,
         * 5 - 活动, 6 - 动物, 7 - 运动, 8 - 交通工具, 9 - 家居,
         * 10 - 电器, 11 - 艺术, 12 - 工具, 13 - 服饰, 14 - 配饰,
         * 15 - 玩具, -2 - 其他
         * */
        Label label = labelDetector.convertResult(jsonObject);
        if (null == label) {
            Log.e(TAG, "label is null ");
            return null;
        }

        return label;
    }

    @Override
    public void onTaskCompleted(Label label) {

    }

}
