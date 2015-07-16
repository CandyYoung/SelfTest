package com.example.selftest.utils.image_utils;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by zhanghq on 2015/7/16 0016.
 */
class TaskBean implements Serializable {

    private ImageView imageView;
    private String webPath;
    private ImageDownloadAsyncTask task;

    TaskBean(ImageView imageView,String webPath,ImageDownloadAsyncTask task){
        this.imageView = imageView;
        this.webPath = webPath;
        this.task = task;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public ImageDownloadAsyncTask getTask() {
        return task;
    }

    public void setTask(ImageDownloadAsyncTask task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskBean)) return false;

        TaskBean taskBean = (TaskBean) o;

        if (imageView != null ? !imageView.equals(taskBean.imageView) : taskBean.imageView != null)
            return false;
        if (webPath != null ? !webPath.equals(taskBean.webPath) : taskBean.webPath != null)
            return false;
        return !(task != null ? !task.equals(taskBean.task) : taskBean.task != null);

    }

    @Override
    public int hashCode() {
        int result = imageView != null ? imageView.hashCode() : 0;
        result = 31 * result + (webPath != null ? webPath.hashCode() : 0);
        result = 31 * result + (task != null ? task.hashCode() : 0);
        return result;
    }
}
