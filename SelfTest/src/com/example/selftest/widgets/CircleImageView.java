package com.example.selftest.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.PaintFlagsDrawFilter;

public class CircleImageView extends ImageView {
	private Paint paint = new Paint();

	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (null != drawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				Bitmap b = toRoundCorner(bitmap);
				paint.reset();
				// 实现了图片的自适应，需要指明imageview的尺寸大小
				canvas.drawBitmap(b,
						new Rect(0, 0, b.getWidth(), b.getHeight()), new Rect(
								0, 0, getWidth(), getHeight()), paint);
			}

		} else {
			super.onDraw(canvas);
		}
	}

	private Bitmap toRoundCorner(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);
		canvas.drawARGB(0, 0, 0, 0);

		int diameter = Math.max(bitmap.getWidth(), bitmap.getHeight());
		final Rect rect = new Rect(0, 0, diameter, diameter);
		// 抗锯齿
		paint.setAntiAlias(true);
		paint.setColor(0xff424242);

		// 抗锯齿
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				diameter / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, null, rect, paint);
		return output;
	}
}
