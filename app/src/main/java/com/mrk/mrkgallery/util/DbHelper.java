package com.mrk.mrkgallery.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.model.MediaDataGenerator;
import com.mrk.mrkgallery.tfai.Classifier;
import com.mrk.mrkgallery.tfai.MnistClassifier;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    public static final int MODULE_SCENE_DETECT = 0;
    public static final int MODULE_LABEL_DETECT = 1;
    public static final int MODULE_TF_DETECT = 2;
    public static final int MODULE_MNIST_DETECT = 3;
    public static final int INPUT_SIZE = 224;
    public static final int IMAGE_MEAN = 117;
    public static final float IMAGE_STD = 1;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "output";
    public static final String MODEL_FILE = "file:///android_asset/model/tensorflow_inception_graph.pb";
    public static final String LABEL_FILE = "file:///android_asset/model/imagenet_comp_graph_label_strings.txt";
    public static final String MNIST_MODEL_FILE = "file:///android_asset/model/mnist.pb";

    public static final int mnistResId[] = {
            R.drawable.num_0,
            R.drawable.num_1,
            R.drawable.num_2,
            R.drawable.num_3,
            R.drawable.num_4,
            R.drawable.num_5,
            R.drawable.num_6,
            R.drawable.num_7,
            R.drawable.num_8,
            R.drawable.num_9
    };

    public static List<PhotoItem> getPhotoList(Context context) {
        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        photoList.clear();

        final String[] imageColumns = MediaDataGenerator.ImageDataGenerator.imageColumns;
        Cursor cursor = context.getContentResolver().query(
                MediaDataGenerator.imageUri,
                imageColumns,
                null,
                null,
                imageColumns[3]);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(cursor.getColumnIndex(imageColumns[1]));
                String path = cursor.getString(cursor.getColumnIndex(imageColumns[4]));

                PhotoItem item = new PhotoItem();
                item.setPhotoName(name);
                item.setPhotoPath(path);
                photoList.add(item);
            } while (cursor.moveToNext());
            cursor.close();
            cursor = null;
        }

        return photoList;
    }

    /**
     * 开始图片识别匹配
     */
    public static String startImageClassifier(String imgPath, Classifier classifier) {
        if (TextUtils.isEmpty(imgPath) || (classifier == null)) {
            return "no tf type";
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        Bitmap croppedBitmap = getScaleBitmap(bitmap, INPUT_SIZE);
        final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
        Log.i("startImageClassifier", "startImageClassifier results: " + results);

        StringBuffer str = new StringBuffer();
        for (int i = 0; i < results.size(); i++) {
            str.append(results.get(i).getTitle() + " ");
        }

        return str.toString().trim();
    }

    /**
     * 对图片进行缩放
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, int size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) size) / width;
        float scaleHeight = ((float) size) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 开始手写数字图片识别
     */
    public static String startMnistClassifier(Context context, int imgResId, MnistClassifier classifier) {
        if (classifier == null) {
            return "no mnist type";
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgResId);
//        classifier.getGrayPix_R(bitmap, true);
        int mnistType = classifier.getPredictResult(bitmap);
        return String.valueOf(mnistType);
    }

    public static List<PhotoItem> getMnistPhotoList() {
        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        photoList.clear();

        for (int i = 0; i < mnistResId.length; i++) {
            PhotoItem item = new PhotoItem();
            item.setPhotoName(i + ".png");
            item.setPhotoResId(mnistResId[i]);
            photoList.add(item);
        }

        return photoList;
    }

}
