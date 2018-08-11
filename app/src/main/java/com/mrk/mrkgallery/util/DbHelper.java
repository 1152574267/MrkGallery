package com.mrk.mrkgallery.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.huawei.hiai.vision.image.detector.LabelDetector;
import com.huawei.hiai.vision.image.detector.SceneDetector;
import com.huawei.hiai.vision.visionkit.common.Frame;                   //加载Frame类
import com.huawei.hiai.vision.visionkit.image.detector.Label;
import com.huawei.hiai.vision.visionkit.image.detector.LabelContent;
import com.huawei.hiai.vision.visionkit.image.detector.Scene;           //加载场景检测结果类
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.model.MediaDataGenerator;
import com.mrk.mrkgallery.tfai.Classifier;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    public static final int MODULE_SCENE_DETECT = 0;
    public static final int MODULE_LABEL_DETECT = 1;
    public static final int MODULE_TF_DETECT = 2;
    public static final int INPUT_SIZE = 224;
    public static final int IMAGE_MEAN = 117;
    public static final float IMAGE_STD = 1;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "output";
    public static final String MODEL_FILE = "file:///android_asset/model/tensorflow_inception_graph.pb";
    public static final String LABEL_FILE = "file:///android_asset/model/imagenet_comp_graph_label_strings.txt";
    public static final String MNIST_MODEL_FILE = "file:///android_asset/model/mnist.pb";

    public static final SparseArray<String> LABEL_CONTENTS = new SparseArray<String>();
    public static final SparseArray<String> SCENE_CONTENTS = new SparseArray<String>();

    public static final String[] LABEL_CATEGORYS = {
            "Others",
            "People",
            "Food",
            "Landscapes",
            "Documents",
            "Festival",
            "Activities",
            "Animal",
            "Sports",
            "Vehicle",
            "Household products",
            "Appliance",
            "Art",
            "Tools",
            "Apparel",
            "Accessories",
            "Toy"
    };

    public static void initLabelContents() {
        LABEL_CONTENTS.clear();

        LABEL_CONTENTS.put(0, "people");
        LABEL_CONTENTS.put(1, "food");
        LABEL_CONTENTS.put(2, "landscapes");
        LABEL_CONTENTS.put(3, "document");
        LABEL_CONTENTS.put(4, "id card");
        LABEL_CONTENTS.put(5, "passport");
        LABEL_CONTENTS.put(6, "debit card");
        LABEL_CONTENTS.put(7, "bicycle");
        LABEL_CONTENTS.put(8, "bus");
        LABEL_CONTENTS.put(9, "ship");
        LABEL_CONTENTS.put(10, "train");
        LABEL_CONTENTS.put(11, "airplane");
        LABEL_CONTENTS.put(12, "automobile");
        LABEL_CONTENTS.put(13, "bird");
        LABEL_CONTENTS.put(14, "cat");
        LABEL_CONTENTS.put(15, "dog");
        LABEL_CONTENTS.put(16, "fish");
        LABEL_CONTENTS.put(18, "wardrobe");
        LABEL_CONTENTS.put(19, "smartphone");
        LABEL_CONTENTS.put(20, "laptop");
        LABEL_CONTENTS.put(24, "bridal veil");
        LABEL_CONTENTS.put(25, "flower");
        LABEL_CONTENTS.put(26, "toy block");
        LABEL_CONTENTS.put(27, "sushi");
        LABEL_CONTENTS.put(28, "barbecue");
        LABEL_CONTENTS.put(29, "banana");
        LABEL_CONTENTS.put(31, "watermelon");
        LABEL_CONTENTS.put(32, "noodle");
        LABEL_CONTENTS.put(34, "piano");
        LABEL_CONTENTS.put(35, "wedding");
        LABEL_CONTENTS.put(36, "playing chess");
        LABEL_CONTENTS.put(37, "basketball");
        LABEL_CONTENTS.put(38, "badminton");
        LABEL_CONTENTS.put(39, "football");
        LABEL_CONTENTS.put(41, "sunrise sunset");
        LABEL_CONTENTS.put(40, "city overlook");
        LABEL_CONTENTS.put(42, "ocean & beach");
        LABEL_CONTENTS.put(43, "bridge");
        LABEL_CONTENTS.put(44, "sky");
        LABEL_CONTENTS.put(45, "grassland");
        LABEL_CONTENTS.put(46, "street");
        LABEL_CONTENTS.put(47, "night");
        LABEL_CONTENTS.put(49, "grove");
        LABEL_CONTENTS.put(50, "lake");
        LABEL_CONTENTS.put(51, "snow");
        LABEL_CONTENTS.put(52, "mountain");
        LABEL_CONTENTS.put(53, "building");
        LABEL_CONTENTS.put(54, "cloud");
        LABEL_CONTENTS.put(55, "waterfall");
        LABEL_CONTENTS.put(56, "fog & haze");
        LABEL_CONTENTS.put(57, "porcelain");
        LABEL_CONTENTS.put(58, "model runway");
        LABEL_CONTENTS.put(59, "rainbow");
        LABEL_CONTENTS.put(60, "candle");
        LABEL_CONTENTS.put(62, "statue of liberty");
        LABEL_CONTENTS.put(63, "ppt");
        LABEL_CONTENTS.put(66, "baby carriage");
        LABEL_CONTENTS.put(67, "group photo");
        LABEL_CONTENTS.put(68, "dine together");
        LABEL_CONTENTS.put(69, "eiffel tower");
        LABEL_CONTENTS.put(70, "dolphin");
        LABEL_CONTENTS.put(71, "giraffe");
        LABEL_CONTENTS.put(72, "penguin");
        LABEL_CONTENTS.put(73, "tiger");
        LABEL_CONTENTS.put(74, "zebra");
        LABEL_CONTENTS.put(76, "lion");
        LABEL_CONTENTS.put(77, "elephant");
        LABEL_CONTENTS.put(78, "leopard");
        LABEL_CONTENTS.put(79, "peafowl");
        LABEL_CONTENTS.put(80, "blackboard");
        LABEL_CONTENTS.put(81, "balloon");
        LABEL_CONTENTS.put(83, "air conditioner");
        LABEL_CONTENTS.put(84, "washing machine");
        LABEL_CONTENTS.put(85, "refrigerator");
        LABEL_CONTENTS.put(86, "camera");
        LABEL_CONTENTS.put(88, "gun");
        LABEL_CONTENTS.put(89, "dress & skirt");
        LABEL_CONTENTS.put(91, "uav");
        LABEL_CONTENTS.put(92, "apple");
        LABEL_CONTENTS.put(93, "dumpling");
        LABEL_CONTENTS.put(94, "coffee");
        LABEL_CONTENTS.put(95, "grape");
        LABEL_CONTENTS.put(96, "hot pot");
        LABEL_CONTENTS.put(97, "diploma");
        LABEL_CONTENTS.put(102, "watch");
        LABEL_CONTENTS.put(103, "glasses");
        LABEL_CONTENTS.put(104, "ferris wheel");
        LABEL_CONTENTS.put(105, "fountain");
        LABEL_CONTENTS.put(106, "pavilion");
        LABEL_CONTENTS.put(107, "fireworks");
        LABEL_CONTENTS.put(108, "business card");
        LABEL_CONTENTS.put(109, "riding");
        LABEL_CONTENTS.put(110, "music show");
        LABEL_CONTENTS.put(111, "sailboat");
        LABEL_CONTENTS.put(112, "giant panda");
        LABEL_CONTENTS.put(113, "birthday cake");
        LABEL_CONTENTS.put(114, "birthday");
        LABEL_CONTENTS.put(115, "christmas");
        LABEL_CONTENTS.put(116, "the great wall");
        LABEL_CONTENTS.put(117, "oriental pearl tower");
        LABEL_CONTENTS.put(118, "guangzhou tower");
        LABEL_CONTENTS.put(120, "tower");
        LABEL_CONTENTS.put(121, "rabbit");
        LABEL_CONTENTS.put(123, "trolley case");
        LABEL_CONTENTS.put(124, "nail");
        LABEL_CONTENTS.put(125, "guitar");
    }

    public static void initSceneContents() {
        SCENE_CONTENTS.clear();

        SCENE_CONTENTS.put(0, "UNKNOWN");
        SCENE_CONTENTS.put(1, "UNSUPPORT");
        SCENE_CONTENTS.put(2, "BEACH");
        SCENE_CONTENTS.put(3, "BLUESKY");
        SCENE_CONTENTS.put(4, "SUNSET");
        SCENE_CONTENTS.put(5, "FOOD");
        SCENE_CONTENTS.put(6, "FLOWER");
        SCENE_CONTENTS.put(7, "GREENPLANT");
        SCENE_CONTENTS.put(8, "SNOW");
        SCENE_CONTENTS.put(9, "NIGHT");
        SCENE_CONTENTS.put(10, "TEXT");
        SCENE_CONTENTS.put(11, "STAGE");
        SCENE_CONTENTS.put(12, "CAT");
        SCENE_CONTENTS.put(13, "DOG");
        SCENE_CONTENTS.put(14, "FIREWORK");
        SCENE_CONTENTS.put(15, "OVERCAST");
        SCENE_CONTENTS.put(16, "FALLEN");
        SCENE_CONTENTS.put(17, "PANDA");
        SCENE_CONTENTS.put(18, "CAR");
        SCENE_CONTENTS.put(19, "OLDBUILDINGS");
        SCENE_CONTENTS.put(20, "BICYCLE");
        SCENE_CONTENTS.put(21, "WATERFALL");
    }

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

    public static String getSceneType(String imgPath, SceneDetector sceneDetector) {
        if (TextUtils.isEmpty(imgPath) || (sceneDetector == null)) {
            return SCENE_CONTENTS.get(0);
        }

        Bitmap bmp = BitmapFactory.decodeFile(imgPath);

        // 构造Frame对象
        Frame frame = new Frame();
        frame.setBitmap(bmp);
        // 进行场景检测
        JSONObject jsonScene = sceneDetector.detect(frame, null);
        // 获取Java类形式的结果
        Scene sc = sceneDetector.convertResult(jsonScene);
        // 获取识别出来的场景类型
        int type = sc.getType();

        return SCENE_CONTENTS.get(type);
    }

    public static String getDetectLabel(String imgPath, LabelDetector labelDetector) {
        if (TextUtils.isEmpty(imgPath) || (labelDetector == null)) {
            return "not get label";
        }

        long startTime = 0, endTime = 0;
        StringBuffer detectLabel = new StringBuffer();

        Bitmap bmp = BitmapFactory.decodeFile(imgPath);

        startTime = System.currentTimeMillis();
//        Label result_label = getLabel(bmp, labelDetector);
        Frame frame = new Frame();
        frame.setBitmap(bmp);
        JSONObject jsonObject = labelDetector.detect(frame, null);
        Label result_label = labelDetector.convertResult(jsonObject);
        endTime = System.currentTimeMillis();
        Log.i("getDetectLabel", String.format("labeldetect time: %d ms", endTime - startTime));

        if (result_label == null) {
            detectLabel.append("not get label");
        } else {
//            detectLabel.append("category: ");
            int categoryID = result_label.getCategory();
            Log.i("getDetectLabel", "categoryID: " + categoryID);
            if (categoryID < 0 || categoryID >= LABEL_CATEGORYS.length) {
                detectLabel.append("Others");
            } else {
                detectLabel.append(LABEL_CATEGORYS[categoryID]);
            }

            List<LabelContent> labelContents = result_label.getLabelContent();
            for (int i = 0; i < labelContents.size(); i++) {
//                detectLabel.append(" labelContent: ");
                detectLabel.append("-");
                int labelContentID = labelContents.get(i).getLabelId();
                Log.i("getDetectLabel", "labelContentID: " + labelContentID);
                if (labelContentID < 0 || labelContentID >= LABEL_CONTENTS.size()) {
                    detectLabel.append("other");
                } else {
                    String name = LABEL_CONTENTS.get(labelContentID);
                    detectLabel.append(name);
                }
            }
        }

        return detectLabel.toString().trim();
    }

    public static Label getLabel(Bitmap bitmap, LabelDetector labelDetector) {
        if (bitmap == null) {
            Log.e("getLabel", "bitmap is null");
            return null;
        }

        // 定义frame
        Frame frame = new Frame();
        // 将需进行图片分类标签图像的bitmap放入frame中
        frame.setBitmap(bitmap);
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
            Log.e("getLabel", "label is null");
            return null;
        }

        return label;
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

}
