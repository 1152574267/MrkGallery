package com.mrk.mrkgallery.model;

import android.net.Uri;
import android.provider.MediaStore;

public class MediaDataGenerator {
    public static final Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    public static class ImageDataGenerator {
        public static final String[] ImageColumns = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.MIME_TYPE
        };
    }

}
