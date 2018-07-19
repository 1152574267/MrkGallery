package com.mrk.mrkgallery.util;

import android.content.Context;
import android.database.Cursor;

import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.model.MediaDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    public List<PhotoItem> getPhotoList(Context context) {
        List<PhotoItem> photoList = new ArrayList<PhotoItem>();
        photoList.clear();

        final String[] imageColumns = MediaDataGenerator.ImageDataGenerator.ImageColumns;
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
