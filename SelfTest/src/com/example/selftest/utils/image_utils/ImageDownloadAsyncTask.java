package com.example.selftest.utils.image_utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

/**
 * 异步网络图片加载
 *
 * @author 海强
 */
class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private static String BASE_FOLDER;
    private static HashMap<String, String> localImageMap = new HashMap<String, String>();
    private ImageView imageView;
    private WeakReference<Context> mContext;
    private boolean mNeedRound;

    private ImageLoadCallback loadCallback;

    public void setLoadCallback(ImageLoadCallback callback) {
        loadCallback = callback;
    }

    public interface ImageLoadCallback {
        void loadDone(Bitmap bitmap);
    }

    public ImageDownloadAsyncTask(ImageView iv, Context context) {
        this(iv, context, false);
    }

    public ImageDownloadAsyncTask(ImageView iv, Context context, boolean needRound) {
        imageView = iv;
        imageView.clearAnimation();
        mContext = new WeakReference<Context>(context);
        mNeedRound = needRound;

        BASE_FOLDER = mContext.get()
                .getDir("imageCache", Context.MODE_PRIVATE).getAbsolutePath();
        if (!new File(BASE_FOLDER).exists()) {
            Log.i("ImageDownloadAsyncTask", "mkdirs");
            new File(BASE_FOLDER).mkdirs();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String urlDisplay = params[0];
        String newFilePath = BASE_FOLDER + "/"
                + Base64.encodeToString(urlDisplay.getBytes(), Base64.DEFAULT)
                + ".jpg";
        // Log.i("doInBackground， newFilePath", newFilePath);
        Bitmap mIcon = null;
        try {
            File file = new File(newFilePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (!file.exists() || file.length() <= 0) {

                InputStream in = null;
                OutputStream out = null;

                try {
//                    options.inJustDecodeBounds = true;
                    in = new java.net.URL(urlDisplay).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                    out = new BufferedOutputStream(new FileOutputStream(file));

                    // 注意，这里存文件的时候要用Bitmap.compress方法。通用的存文件会不可用。
                    mIcon.compress(CompressFormat.JPEG, 100, out);
                } catch (Exception ex) {
                    Log.d("LoadImageTask", "ExceptionHappened, outHeight=" + options.outHeight + ", width=" + options.outWidth);
                } finally {
                    if (out != null) {
                        out.flush();
                        out.close();
                        out = null;
                    }
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                }

                localImageMap.put(urlDisplay, newFilePath);

            } else {

//                byte[] buffer = new byte[(int) file.length()];

                InputStream in = null;
                try {
//                    options.inJustDecodeBounds = true;
                    in = new BufferedInputStream(new FileInputStream(file));
//                    in.read(buffer);
                    mIcon = BitmapFactory.decodeStream(in, null, options);//.decodeByteArray(buffer, 0, buffer.length);
                } catch (Exception ex) {
                    Log.d("LoadImageTask", "ExceptionHappened, outHeight=" + options.outHeight + ", width=" + options.outWidth);
                } finally {
                    if (in != null) {
                        in.close();
                        in = null;
                    }
//                    buffer = null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (mNeedRound) {
            Bitmap bitmap = ImageUtil.get(mContext.get()).getRoundedCornerBitmap(result);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageBitmap(result);
        }

        if (loadCallback != null) {
            loadCallback.loadDone(result);
        }
//        if (result != null) {
//            result.recycle();
//        }

        super.onPostExecute(result);
    }

}
