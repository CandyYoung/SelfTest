package com.example.selftest.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

/**
 * 异步网络图片加载
 * 
 * @author 海强
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

	private static String BASE_FOLDERR;
	private static HashMap<String, String> localImageMap = new HashMap<String, String>();
	private ImageView imageView;
	private WeakReference<Context> mContext;
	private boolean mNeedRound;

	public ImageLoader(ImageView iv, Context context) {
		this(iv, context, false);
	}

	public ImageLoader(ImageView iv, Context context, boolean needRound) {
		imageView = iv;
		mContext = new WeakReference<Context>(context);
		mNeedRound = needRound;

		BASE_FOLDERR = mContext.get()
				.getDir("imageCache", Context.MODE_PRIVATE).getAbsolutePath();
		if (!new File(BASE_FOLDERR).exists()) {
			Log.i("ImageLoader， ImageLoader", "mkdirs");
			new File(BASE_FOLDERR).mkdirs();
		}
	}

	public long getSize() {
		long size = 0;

		File file = new File(BASE_FOLDERR);
		if (file.exists() && file.isDirectory()) {
			size = getFolderSize(file);
		}

		return size;
	}

	public void ClearCache() {
		File file = new File(BASE_FOLDERR);
		if (file.exists() && file.isDirectory()) {
			deleteFolder(file);
		}
	}
	
	private void deleteFolder(File file){
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

	private long getFolderSize(File file) {
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

	@Override
	protected Bitmap doInBackground(String... params) {
		String urldisplay = params[0];
		String newFilePath = BASE_FOLDERR + "/"
				+ Base64.encodeToString(urldisplay.getBytes(), Base64.DEFAULT)
				+ ".jpg";
		// Log.i("doInBackground， newFilePath", newFilePath);
		Bitmap mIcon = null;
		try {
			File file = new File(newFilePath);
			if (!file.exists() || file.length() <= 0) {

				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon = BitmapFactory.decodeStream(in);

				OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(file));

					// 注意，这里存文件的时候要用Bitmap.compress方法。通用的存文件会不可用。
					mIcon.compress(CompressFormat.JPEG, 100, out);
				} finally {
					if (out != null) {
						out.flush();
						out.close();
					}
					in.close();
				}

				localImageMap.put(urldisplay, newFilePath);

			} else {
				// Log.i("doInBackground， ", "Image exist");
				// Log.i("doInBackground， file.length", "length=" +
				// file.length());
				// TODO: long强转int
				byte[] buffer = new byte[(int) file.length()];
				// while (fis.read(buffer) > 0) {
				//
				// }
				InputStream in = null;
				try {
					in = new BufferedInputStream(new FileInputStream(file));
					in.read(buffer);
				} finally {
					if (in != null) {
						in.close();
					}
				}

				// InputStream in = StreamConvertor.byteToInputStream(buffer);
				mIcon = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
				buffer = null;
				// fis.close();
				// in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (mNeedRound) {
			Bitmap bitmap = getRoundedCornerBitmap(result);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageBitmap(result);
		}

		super.onPostExecute(result);
	}

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
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

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
