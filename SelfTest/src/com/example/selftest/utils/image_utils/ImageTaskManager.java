package com.example.selftest.utils.image_utils;

import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhanghq on 2015/7/14 0014.
 */
class ImageTaskManager {

    private static List<TaskBean> mImageTaskMap;

    public static void addDownLoader(String url, ImageDownloadAsyncTask task, ImageView imageView) {
        if (mImageTaskMap == null) {
            mImageTaskMap = new ArrayList<>();
        }

        mImageTaskMap.add(new TaskBean(imageView,url,task));
    }

    public static void removeDownLoader(String url) {
        if (mImageTaskMap != null) {
            mImageTaskMap.remove(url);
        }
    }

    public static void cancelDwonloader(String url, ImageView imageView) {
        if (mImageTaskMap != null) {
            for (int i = 0; i < mImageTaskMap.size(); i++) {
                if (mImageTaskMap.get(i).getWebPath().equals(url) || mImageTaskMap.get(i).getImageView().equals(imageView)) {
                    mImageTaskMap.get(i).getTask().cancel(true);
                    mImageTaskMap.remove(i);
                }
            }
        }
    }
}
