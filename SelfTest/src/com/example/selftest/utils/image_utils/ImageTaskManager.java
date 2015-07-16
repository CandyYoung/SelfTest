package com.example.selftest.utils.image_utils;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghq on 2015/7/14 0014.
 */
class ImageTaskManager {

    private static List<TaskBean> mImageTaskCollection;

    public static void addDownLoader(String url, ImageDownloadAsyncTask task, ImageView imageView) {
        if (mImageTaskCollection == null) {
            mImageTaskCollection = new ArrayList<>();
        }

        mImageTaskCollection.add(new TaskBean(imageView, url, task));
    }

    public static ImageDownloadAsyncTask checkDownloader(String url, ImageView imageView) {
        if (mImageTaskCollection != null) {
            for (int i = 0; i < mImageTaskCollection.size(); i++) {
                if (mImageTaskCollection.get(i).getWebPath().equals(url)){
                    return mImageTaskCollection.get(i).getTask();
                }
                if (mImageTaskCollection.get(i).getImageView().equals(imageView)) {
                    mImageTaskCollection.get(i).getTask().cancel(true);
                    mImageTaskCollection.remove(i);
                    continue;
                }
            }
        }
        return null;
    }
}
