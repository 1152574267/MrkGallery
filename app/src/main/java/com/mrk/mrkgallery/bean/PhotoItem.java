package com.mrk.mrkgallery.bean;

import java.io.Serializable;

public class PhotoItem implements Serializable {
    private String mPhotoPath;
    private String mPhotoName;
    private int mPhotoResId;
    private String mPhotoCategory;
    private String mPhotoLabel;

    public void setPhotoName(String photoName) {
        mPhotoName = photoName;
    }

    public String getPhotoName() {
        return mPhotoName;
    }

    public void setPhotoPath(String path) {
        mPhotoPath = path;
    }

    public String getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoResId(int photoResId) {
        mPhotoResId = photoResId;
    }

    public int getPhotoResId() {
        return mPhotoResId;
    }

    public void setPhotoCategory(String category) {
        mPhotoCategory = category;
    }

    public String getPhotoCategory() {
        return mPhotoCategory;
    }

    public void setPhotoLabel(String label) {
        mPhotoLabel = label;
    }

    public String getPhotoLabel() {
        return mPhotoLabel;
    }

}
