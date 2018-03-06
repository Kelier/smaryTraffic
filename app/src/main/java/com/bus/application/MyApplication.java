package com.bus.application;

import android.app.Application;

/**
 * Created by John Yan on 1/31/2017.
 */

public class MyApplication extends Application {
    //保存图片本地路径
    private String cachePath ;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private static final String IMAGE_FILE_TEMPNAME = "tempImage.jpg";
    @Override
    public void onCreate() {
        super.onCreate();
        cachePath = getExternalCacheDir().getAbsolutePath() + "/";
    }

    public String getCachePath() {
        return cachePath;
    }
    public String getUserPhoto() {
        return cachePath + IMAGE_FILE_NAME ;
    }
    public String getTempPhoto() {
        return cachePath + IMAGE_FILE_TEMPNAME ;
    }
}
