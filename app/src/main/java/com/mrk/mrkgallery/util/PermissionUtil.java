package com.mrk.mrkgallery.util;

import android.Manifest;

public class PermissionUtil {
    public static final int REQUEST_PERMISSIONS_CODE = 100;

    public static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

}
