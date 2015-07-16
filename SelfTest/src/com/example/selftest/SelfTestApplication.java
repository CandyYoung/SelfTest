package com.example.selftest;

import android.app.Application;

import com.example.selftest.utils.image_utils.ImageUtil;

/**
 * Created by zhanghq on 2015/7/16 0016.
 */
public class SelfTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageUtil.init(this);
    }
}
