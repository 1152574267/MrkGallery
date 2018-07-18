package com.mrk.mrkgallery.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.huawei.hiai.vision.image.detector.LabelDetector;     //加载标签检测detector类
import com.huawei.hiai.vision.visionkit.common.Frame;           //加载Frame类
import com.huawei.hiai.vision.visionkit.image.detector.Label;   //加载标签检测结果类
import com.mrk.mrkgallery.listener.MMListener;

import org.json.JSONObject;

public class LabelDetectTask extends AsyncTask<Bitmap, Integer, Label> {
    private static final String LOG_TAG = "label_detect";

    private MMListener listener;
    private LabelDetector labelDetector;
    private long startTime;
    private long endTime;

    public LabelDetectTask(MMListener listener) {
        this.listener = listener;
    }

    @Override
    protected Label doInBackground(Bitmap... bmp) {
        Log.i(LOG_TAG, "init LabelDetector");
        // 定义detector实例，将此工程的Context当做入参
        labelDetector = new LabelDetector((Context) listener);
        Log.i(LOG_TAG, "start to get label");

        startTime = System.currentTimeMillis();
        Label result_label = getLabel(bmp[0]);
        endTime = System.currentTimeMillis();
        Log.i(LOG_TAG, String.format("labeldetect whole time: %d ms", endTime - startTime));

        //release engine after detect finished
        labelDetector.release();

        return result_label;
    }

    @Override
    protected void onPostExecute(Label result) {
        super.onPostExecute(result);

        listener.onTaskCompleted(result);
    }

    public Label getLabel(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(LOG_TAG, "bitmap is null ");
            return null;
        }

        // 定义frame
        Frame frame = new Frame();
        // 将需进行图片分类标签图像的bitmap放入frame中
        frame.setBitmap(bitmap);
        Log.d(LOG_TAG, "runVisionService " + "start get label");

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
            Log.e(LOG_TAG, "label is null ");
            return null;
        }

        return label;
    }

}
