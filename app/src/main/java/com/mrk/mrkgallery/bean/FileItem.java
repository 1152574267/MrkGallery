package com.mrk.mrkgallery.bean;

import java.io.Serializable;

public class FileItem implements Serializable {
    private int mIcon;
    private String mName;

    public void setFileName(String name) {
        mName = name;
    }

    public String getFileName() {
        return mName;
    }

    public void setFileIcon(int icon) {
        mIcon = icon;
    }

    public int getFileIcon() {
        return mIcon;
    }
}
