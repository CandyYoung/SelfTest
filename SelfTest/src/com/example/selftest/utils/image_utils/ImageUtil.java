package com.example.selftest.utils.image_utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by zhanghq on 2015/7/15 0015.
 */
public class ImageUtil {

    private static String BASE_FOLDERR;
    private static HashMap<String, String> localImageMap = new HashMap<String, String>();
    private ImageView mImageView;
    private WeakReference<Context> mContext;
    private boolean mNeedRound;

    private ImageUtil(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    public static void init(Context context) {
        BASE_FOLDERR = context
                .getDir("imageCache", Context.MODE_PRIVATE).getAbsolutePath();
        if (!new File(BASE_FOLDERR).exists()) {
            Log.i("ImageDownloadAsyncTask", "mkdirs");
            new File(BASE_FOLDERR).mkdirs();
        }
    }

    public static ImageUtil get(Context context) {
        return new ImageUtil(context);
    }

    public void loadImage(ImageView imageView, String webPath) {
        loadImage(imageView, webPath, false);
    }

    public void loadImage(final ImageView imageView, String webPath, boolean needRound) {
        mImageView = imageView;
        mNeedRound = needRound;

        mImageView.clearAnimation();
        ImageDownloadAsyncTask taskExist = ImageTaskManager.checkDownloader(webPath, imageView);
        if (taskExist != null) {
            taskExist.setLoadCallback(new ImageDownloadAsyncTask.ImageLoadCallback() {
                @Override
                public void loadDone(Bitmap bitmap) {
                    if (mNeedRound) {
                        Bitmap bitmapRound = ImageUtil.get(mContext.get()).getRoundedCornerBitmap(bitmap);
                        imageView.setImageBitmap(bitmapRound);
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
        }

        ImageDownloadAsyncTask taskNew = new ImageDownloadAsyncTask(mImageView, mContext.get(), mNeedRound);
        ImageTaskManager.addDownLoader(webPath, taskNew, imageView);
        taskNew.execute(webPath);
    }


    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        // 画圆或者画圆角矩形
        // canvas.drawCircle(cx, cy, radius, paint);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static long getSize() {
        long size = 0;

        File file = new File(BASE_FOLDERR);
        if (file.exists() && file.isDirectory()) {
            size = getFolderSize(file);
        }

        return size;
    }

    public static void clearCache() {
        File file = new File(BASE_FOLDERR);
        if (file.exists() && file.isDirectory()) {
            deleteFolder(file);
        }
    }

    private static void deleteFolder(File file) {
        if (file.exists() && file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (subFile.isDirectory()) {
                    deleteFolder(subFile);
                } else {
                    subFile.delete();
                }
            }

            file.delete();
        }
    }

    private static long getFolderSize(File file) {
        long size = 0;
        if (file.exists() && file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                if (subFile.isDirectory()) {
                    size += getFolderSize(subFile);
                } else {
                    size += subFile.length();
                }
            }
        }

        return size;
    }
}
