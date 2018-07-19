package com.mrk.mrkgallery.util;

import android.content.Context;
import android.database.Cursor;

import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.model.MediaDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbHelper {
    public static final HashMap<Integer, String> LABEL_CONTENTS = new HashMap<Integer, String>();

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
        LABEL_CONTENTS.put(40, "city overlook");
        LABEL_CONTENTS.put(41, "sunrise sunset");
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

    public static final String[] LABEL_CATEGORYS = {
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

    public static List<PhotoItem> getPhotoList(Context context) {
        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        photoList.clear();

        final String[] imageColumns = MediaDataGenerator.ImageDataGenerator.imageColumns;
        Cursor cursor = context.getContentResolver().query
                (MediaDataGenerator.imageUri,
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

}
